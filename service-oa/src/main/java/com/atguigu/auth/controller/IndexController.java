package com.atguigu.auth.controller;

import com.atguigu.auth.service.SysMenuService;
import com.atguigu.auth.service.SysUserService;
import com.atguigu.common.jwt.JwtHelper;
import com.atguigu.common.result.Result;
import com.atguigu.common.utils.MD5;
import com.atguigu.model.system.SysUser;
import com.atguigu.vo.system.LoginVo;
import com.atguigu.vo.system.RouterVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.jsonwebtoken.Jwt;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 一只鱼zzz
 * @version 1.0
 * 后台登录登出
 */
@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {

  @Autowired
  private SysUserService sysUserService;

  @Autowired
  private SysMenuService sysMenuService;

  //login
  @PostMapping("login")
  public Result login(@RequestBody LoginVo loginVo){
//    Map<String, Object> map = new HashMap<>();
//    map.put("token","admin");
//    return Result.ok(map);

    //1、获取输入的用户名和密码
    //2、根据用户名查询数据库
    String username = loginVo.getUsername();
    LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(SysUser::getUsername,username);
    SysUser sysUser = sysUserService.getOne(wrapper);

    //3、用户信息是否存在
    if(sysUser==null){
      throw new RuntimeException("用户不存在");
    }

    //4、判断密码
    //数据库中的MD5加密的密码
    String password_db = sysUser.getPassword();
    //获取输入的密码
    String password_input = MD5.encrypt(loginVo.getPassword());
    if(!password_db.equals(password_input)){
      throw new RuntimeException("密码错误");
    }

    //5、判断用户是否被禁用 1 可用   0 禁用
    if(sysUser.getStatus().intValue()==0){
      throw new RuntimeException("用户被禁用");
    }

    //6、使用jwt根据用户id和用户名称生成token字符串
    String token = JwtHelper.createToken(sysUser.getId(),sysUser.getUsername());

    //7、返回
    Map<String,Object> map = new HashMap<>();
    map.put("token",token);
    return Result.ok(map);

  }

  //info(获取用户信息)
  @GetMapping("info")
  public Result info(HttpServletRequest request){

//    Map<String, Object> map = new HashMap<>();
//    map.put("roles","[admin]");
//    map.put("name","admin");
//    map.put("avatar","https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
//    return Result.ok(map);

    //1、从请求头获取用户信息（获取请求头token字符串）
    String token = request.getHeader("token");

    //2、从token字符串获取用户id或用户名称
    Long userId = JwtHelper.getUserId(token);

    //3、根据用户id查询数据库，把用户信息获取出来
    SysUser sysUser = sysUserService.getById(userId);

    //4、根据用户id获取用户可以操作的菜单列表
    //查询数据库动态构建路由结构进行显示
    List<RouterVo> routerList = sysMenuService.findUserMenuListByUserId(userId);

    //5、根据用户id获取用户可以操作的按钮列表
    List<String> permsList = sysMenuService.findUserPermsByUserId(userId);

    //6、返回相应的数据
    Map<String, Object> map = new HashMap<>();
    map.put("roles","[admin]");
    map.put("name",sysUser.getName());
    map.put("avatar","https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");

    //返回用户可以操作的菜单
    map.put("routers",routerList);
    //返回用户可以操作的按钮
    map.put("buttons",permsList);

    return Result.ok(map);
  }

  /**
   * 退出
   * @return
   */
  @PostMapping("logout")
  public Result logout(){
    return Result.ok();
  }

}
