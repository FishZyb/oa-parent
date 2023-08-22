package com.atguigu.auth.service.impl;

import com.atguigu.auth.mapper.SysRoleMapper;
import com.atguigu.auth.mapper.SysUserRoleMapper;
import com.atguigu.auth.service.SysRoleService;
import com.atguigu.auth.service.SysUserRoleService;
import com.atguigu.model.system.SysRole;
import com.atguigu.model.system.SysUserRole;
import com.atguigu.vo.system.AssginRoleVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 一只鱼zzz
 * @version 1.0
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService  {

  @Autowired
  private SysUserRoleMapper sysUserRoleMapper;

  /**
   * 查询所有的角色和当前用户所属的角色
   * @param userId
   * @return
   */
  @Override
  public Map<String, Object> findRoleDataByUserId(Long userId) {
    //1、查询所有的角色（调用该serv的mapper接口，直接用mapper的list方法进行查询）
    List<SysRole> allRolesList = this.list();

    //2、根据userId查询sys_user_role表，查询userId对应的所有的roleId
    LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SysUserRole::getUserId,userId);
    List<SysUserRole> exitUserRoleList = sysUserRoleMapper.selectList(wrapper);
//    List<SysUserRole> existUserRoleList = sysUserRoleMapper.selectList(
//      new LambdaQueryWrapper<SysUserRole>()
//      .eq(SysUserRole::getUserId, userId)
//      .select(SysUserRole::getRoleId));

    //从查询出来的userId对应的roleList集合中，获取所有的roleId，存放到一个集合中
//    List<Long> list = new ArrayList<>();
//    for(SysUserRole sysUserRole : exitUserRoleList){
//      Long roleId = sysUserRole.getRoleId();
//      list.add(roleId);
//    }
    List<Long> exitRoleIdList = exitUserRoleList.stream()
      .map(c->c.getRoleId()).collect(Collectors.toList());

    //3、根据查询到的所有角色的id，找到对应的角色信息
    //根据角色id到所有的角色的list集合进行比较
    List<SysRole> assginRoleList = new ArrayList<>();
    for (SysRole role : allRolesList) {
      //已分配
      if(exitRoleIdList.contains(role.getId())) {
        assginRoleList.add(role);
      }
    }

    //4、把得到的两部分数据封装到map集合中，返回
    Map<String, Object> roleMap = new HashMap<>();
    roleMap.put("assginRoleList", assginRoleList);
    roleMap.put("allRolesList", allRolesList);
    return roleMap;
  }

  /**
   * 为用户分配角色
   * @param assginRoleVo
   */
  @Transactional
  @Override
  public void doAssign(AssginRoleVo assginRoleVo) {
    //把用户之前分配角色的数据删除（根据userid删除）
    sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>()
      .eq(SysUserRole::getUserId, assginRoleVo.getUserId()));

    //重新进行分配
    for(Long roleId : assginRoleVo.getRoleIdList()) {
      if(StringUtils.isEmpty(roleId)) continue;
      SysUserRole userRole = new SysUserRole();
      userRole.setUserId(assginRoleVo.getUserId());
      userRole.setRoleId(roleId);
      sysUserRoleMapper.insert(userRole);
    }
  }
}
