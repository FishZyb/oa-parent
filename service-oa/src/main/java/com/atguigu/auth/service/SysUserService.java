package com.atguigu.auth.service;


import com.atguigu.model.system.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author zyb
 * @since 2023-03-09
 */
public interface SysUserService extends IService<SysUser> {

  /**
   * 更新状态
   * @param id
   * @param status
   */
    void updateStatus(Long id, Integer status);

  /**
   * 根据用户名进行查询
   * @param username
   */
  SysUser getUserByUserName(String username);


}
