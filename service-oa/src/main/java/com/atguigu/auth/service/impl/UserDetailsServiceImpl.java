package com.atguigu.auth.service.impl;

import com.atguigu.auth.service.SysUserService;
import com.atguigu.model.system.SysUser;
import com.atguigu.security.custom.CustomUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author 一只鱼zzz
 * @version 1.0
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private SysUserService sysUserService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    SysUser sysUser = sysUserService.getUserByUserName(username);
    if(null == sysUser) {
      throw new UsernameNotFoundException("用户名不存在！");
    }

    if(sysUser.getStatus().intValue() == 0) {
      throw new RuntimeException("账号已停用");
    }
    return new CustomUser(sysUser, Collections.emptyList());
  }
}
