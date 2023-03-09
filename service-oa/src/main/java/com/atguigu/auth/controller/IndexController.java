package com.atguigu.auth.controller;

import com.atguigu.common.result.Result;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 一只鱼zzz
 * @version 1.0
 */
@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {

  //login
  @PostMapping("login")
  public Result login(){
    Map<String, Object> map = new HashMap<>();
    map.put("token","admin");
    return Result.ok(map);
  }

  //info(获取用户信息)
  @GetMapping("info")
  public Result info(){
    Map<String, Object> map = new HashMap<>();
    map.put("roles","[admin]");
    map.put("name","admin");
    map.put("avatar","https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
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
