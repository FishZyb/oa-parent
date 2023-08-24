package com.atguigu.auth.service.impl;

import com.atguigu.auth.service.SysMenuService;
import com.atguigu.auth.service.SysUserService;
import com.atguigu.model.system.SysUser;
import com.atguigu.security.custom.CustomUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author 一只鱼zzz
 * @version 1.0
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private SysUserService sysUserService;

  @Autowired
  private SysMenuService sysMenuService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    SysUser sysUser = sysUserService.getUserByUserName(username);
    if(null == sysUser) {
      throw new UsernameNotFoundException("用户名不存在！");
    }

    if(sysUser.getStatus().intValue() == 0) {
      throw new RuntimeException("账号已停用");
    }

    //根据用户id查询用户操作权限的数据集合
    List<String> userPermsList = sysMenuService.findUserPermsByUserId(sysUser.getId());
    //创建一个List集合，用于封装最终的权限数据
    List<SimpleGrantedAuthority> authList = new ArrayList<>();
    //把查询出的list集合，进行遍历
    for(String perms: userPermsList){
      authList.add(new SimpleGrantedAuthority(perms.trim()));
    }

    return new CustomUser(sysUser, authList);
  }
}
