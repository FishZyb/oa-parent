package com.atguigu.auth.service.impl;


import com.atguigu.auth.mapper.SysUserMapper;
import com.atguigu.auth.service.SysUserService;
import com.atguigu.model.system.SysUser;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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

  /**
   * 根据UserId更新用户状态
   * @param id
   * @param status
   */
  @Transactional
  @Override
  public void updateStatus(Long id, Integer status) {
    //1、根据userId查询用户对象
    SysUser sysUser = this.getById(id);
    //2、设置修改状态值
    if(status.intValue() == 1) {
      sysUser.setStatus(status);
    } else {
      sysUser.setStatus(0);
    }
    //3、调用方法进行修改
    this.updateById(sysUser);
  }

  @Override
  public SysUser getUserByUserName(String username) {
    LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SysUser::getUsername,username);
    SysUser sysUser = baseMapper.selectOne(wrapper);
    return sysUser;
  }

}
