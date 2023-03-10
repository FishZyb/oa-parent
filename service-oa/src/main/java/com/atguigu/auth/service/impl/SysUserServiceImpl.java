package com.atguigu.auth.service.impl;


import com.atguigu.auth.mapper.SysUserMapper;
import com.atguigu.auth.service.SysUserService;
import com.atguigu.model.system.SysUser;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author zyb
 * @since 2023-03-09
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

  @Transactional
  @Override
  public void updateStatus(Long id, Integer status) {
    SysUser sysUser = this.getById(id);
    if(status.intValue() == 1) {
      sysUser.setStatus(status);
    } else {
      sysUser.setStatus(0);
    }
    this.updateById(sysUser);
  }
}
