package com.atguigu.auth.service;

import com.atguigu.model.system.SysRole;
import com.atguigu.vo.system.AssginRoleVo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 * @author 一只鱼zzz
 * @version 1.0
 */
@Mapper
public interface SysRoleService extends IService<SysRole> {

      //1、查询所有角色 和 当前用户所属角色
      Map<String, Object> findRoleDataByUserId(Long userId);

      //2、为用户分配角色
      void doAssign(AssginRoleVo assginRoleVo);
}
