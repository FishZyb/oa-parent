package com.atguigu.auth.controller;


import com.atguigu.auth.service.SysUserService;
import com.atguigu.common.result.Result;
import com.atguigu.model.system.SysUser;
import com.atguigu.vo.system.SysRoleQueryVo;
import com.atguigu.vo.system.SysUserQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author zyb
 * @since 2023-03-09
 */

@Api(tags = "用户管理接口")
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {

  @Autowired
  private SysUserService service;

  @ApiOperation(value = "更新状态")
  @GetMapping("updateStatus/{id}/{status}")
  public Result updateStatus(@PathVariable Long id,@PathVariable Integer status){
    service.updateStatus(id,status);
    return Result.ok();
  }

  //用户条件分页查询
  @ApiOperation("用户条件分页查询")
  @GetMapping("{page}/{limit}")
  public Result index(@PathVariable Long page,
                      @PathVariable Long limit,
                      SysUserQueryVo sysUserQueryVo){
    //创建Page对象
    Page<SysUser> userPage = new Page<>(page,limit);
    //封装条件，判断条件值不为空
    LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
    //获取条件值
    String username = sysUserQueryVo.getKeyword();
    String createTimeBegin = sysUserQueryVo.getCreateTimeBegin();
    String createTimeEnd = sysUserQueryVo.getCreateTimeEnd();
    //判断条件值不为空
    if(!StringUtils.isEmpty(username)){
      wrapper.like(SysUser::getUsername,username);
    }if(!StringUtils.isEmpty(createTimeBegin)){
      //大于等于开始时间
      wrapper.ge(SysUser::getCreateTime,createTimeBegin);
    }if(!StringUtils.isEmpty(createTimeEnd)){
      //小于等于结束时间
      wrapper.le(SysUser::getCreateTime,createTimeEnd);
    }

    //调用mp的方法实现条件分页查询
    IPage<SysUser>pageModel = service.page(userPage, wrapper);
    return Result.ok(pageModel);

  }

  @ApiOperation(value = "获取用户")
  @GetMapping("get/{id}")
  public Result get(@PathVariable Long id) {
    SysUser user = service.getById(id);
    return Result.ok(user);
  }

  @ApiOperation(value = "保存用户")
  @PostMapping("save")
  public Result save(@RequestBody SysUser user) {
    service.save(user);
    return Result.ok();
  }

  @ApiOperation(value = "更新用户")
  @PutMapping("update")
  public Result updateById(@RequestBody SysUser user) {
    service.updateById(user);
    return Result.ok();
  }

  @ApiOperation(value = "删除用户")
  @DeleteMapping("remove/{id}")
  public Result remove(@PathVariable Long id) {
    service.removeById(id);
    return Result.ok();
  }

}

