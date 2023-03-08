package com.atguigu.auth;

import com.atguigu.auth.mapper.SysRoleMapper;
import com.atguigu.model.system.SysRole;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author 一只鱼zzz
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMpDemo1 {

  @Autowired
  private SysRoleMapper sysRoleMapper;

  @Test
  public void getAll(){
    List<SysRole> sysRoles = sysRoleMapper.selectList(null);
    sysRoles.forEach(System.out::println);
  }

}
