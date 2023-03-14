package com.atguigu.auth.service;

import com.atguigu.model.system.SysMenu;
import com.atguigu.vo.system.AssginMenuVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author zyb
 * @since 2023-03-10
 */
public interface SysMenuService extends IService<SysMenu> {

  /**
   * 菜单列表接口
   * @return
   */
  List<SysMenu> findNodes();

  /**
   * 删除菜单
   * @param id
   */
  void removeMenuById(Long id);

  /**
   * 查询所有菜单 和 角色分配的菜单
   * @param roleId
   * @return
   */
  List<SysMenu> findMenuByRoleId(Long roleId);

  /**
   *  为角色分配菜单
   * @param assginMenuVo
   */
  void doAssign(AssginMenuVo assginMenuVo);
}
