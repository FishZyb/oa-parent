package com.atguigu.auth.controller;

import com.atguigu.auth.service.SysRoleService;
import com.atguigu.auth.service.impl.SysRoleServiceImpl;
import com.atguigu.model.system.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 一只鱼zzz
 * @version 1.0
 */

@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

  //注入Service
  @Autowired
  private SysRoleServiceImpl sysRoleService;

  //查询所有的角色
  @GetMapping("findAll")
  public List<SysRole> findAll(){
    //调用service中的方法进行实现
    List<SysRole> list = sysRoleService.list();
    return list;
  }

}
