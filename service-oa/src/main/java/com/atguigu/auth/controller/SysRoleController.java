package com.atguigu.auth.controller;

import com.atguigu.auth.service.SysRoleService;
import com.atguigu.auth.service.SysUserService;
import com.atguigu.auth.service.impl.SysRoleServiceImpl;
import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysRole;
import com.atguigu.vo.system.AssginRoleVo;
import com.atguigu.vo.system.SysRoleQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author 一只鱼zzz
 * @version 1.0
 */

@Api(tags = "角色管理接口")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

  @Autowired
  private SysRoleServiceImpl sysRoleService;

  @Autowired
  private SysUserService service;

  //1、查询所有角色 和 当前用户所属角色
  @ApiOperation("根据用户获取角色数据")
  @GetMapping("/toAssign/{userId}")
  public Result toAssign(@PathVariable Long userId){
    Map<String,Object> map = sysRoleService.findRoleDataByUserId(userId);
    return Result.ok(map);
  }

  //2、为用户分配角色
  @ApiOperation("根据用户分配角色")
  @PostMapping("/doAssign")
  public Result doAssign(@RequestBody AssginRoleVo assginRoleVo){
    sysRoleService.doAssign(assginRoleVo);
    return Result.ok();
  }

  //查询所有的角色
  @ApiOperation("查询所有的角色")
  @GetMapping("findAll")
  public Result findAll() {
    //调用service中的方法进行实现
    List<SysRole> list = sysRoleService.list();
    return Result.ok(list);
  }

  //条件分页查询
  //page:当前页 limit：每页显示的记录数
  //sysRoleQueryVo 条件的对象
  @ApiOperation("条件分页查询")
  @PreAuthorize("hasAuthority('bnt.sysRole.list')")
  @GetMapping("{page}/{limit}")
  public Result pageQueryRole(@PathVariable Long page,
                              @PathVariable Long limit, SysRoleQueryVo sysRoleQueryVo){
    //调用service中的方法进行实现
    //1、创建Page对象，传递分页相关参数
    Page<SysRole> pageParam = new Page<>(page,limit);
    //2、封装条件，判断条件是否为空，不为空进行封装
    LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
    String roleName = sysRoleQueryVo.getRoleName();
    if(!StringUtils.isEmpty(roleName)){
      //封装
      queryWrapper.like(SysRole::getRoleName,roleName);
    }
    //3、调用方法实现
    IPage<SysRole> rolePage = sysRoleService.page(pageParam, queryWrapper);
    return Result.ok(rolePage);
  }

  @ApiOperation("添加角色")
  @PreAuthorize("hasAuthority('bnt.sysRole.add')")
  @PostMapping("save")
  public Result save(@RequestBody SysRole role){
    //调用service方法
    boolean save = sysRoleService.save(role);
    if(save){
      return Result.ok();
    }else{
      return Result.fail();
    }
  }

  //根据id查询
  @ApiOperation("根据id查询")
  @PreAuthorize("hasAuthority('bnt.sysRole.list')")
  @GetMapping("get/{id}")
  public Result get(@PathVariable Long id){
    SysRole sysRole = sysRoleService.getById(id);
    return Result.ok(sysRole);
  }

  //修改角色
  @ApiOperation("修改角色")
  @PreAuthorize("hasAuthority('bnt.sysRole.update')")
  @PutMapping("update")
  public Result update(@RequestBody SysRole role){
    boolean updateById = sysRoleService.updateById(role);
    if(updateById){
      return Result.ok();
    }else{
      return Result.fail();
    }
  }

  //根据id删除角色
  @ApiOperation("删除角色")
  @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
  @DeleteMapping("remove/{id}")
  public Result delete(@PathVariable Long id){
    boolean remove = sysRoleService.removeById(id);
    if(remove){
      return Result.ok();
    }else{
      return Result.fail();
    }
  }

  @ApiOperation(value = "根据id列表删除")
  @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
  @DeleteMapping("batchRemove")
  public Result batchRemove(@RequestBody List<Long> idList) {
    sysRoleService.removeByIds(idList);
    return Result.ok();
  }

}
