package com.ximen.plan.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ximen.plan.entity.SysPermission;
import com.ximen.plan.entity.SysRolePermission;
import com.ximen.plan.entity.SysUserRole;
import com.ximen.plan.entity.router.RouterMeta;
import com.ximen.plan.entity.router.VueRouter;
import com.ximen.plan.mapper.SysPermissionMapper;
import com.ximen.plan.mapper.SysRolePermissionMapper;
import com.ximen.plan.mapper.SysUserRoleMapper;
import com.ximen.plan.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author damoncai
 * @since 2019-12-19
 */
@Service
@Transactional
public class SysPermissionService extends ServiceImpl<SysPermissionMapper, SysPermission> {

    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;


    /**
     * 根据id集合查询
     *
     * @param permissionIds
     */
    public List<SysPermission> findByIds(List<Long> permissionIds) {
        return this.sysPermissionMapper.selectBatchIds(permissionIds);
    }


    /**
     * 权限列表
     */
    public JSONArray pageList(Integer type) {
        List<SysPermission> sysPermissions = null;
        QueryWrapper<SysPermission> queryWrapper = null;
        if (type != null) {
            queryWrapper = new QueryWrapper<SysPermission>().eq("type", type);
        }
        sysPermissions = sysPermissionMapper.selectList(queryWrapper);
        JSONArray obj = CommonUtils.listToTree(JSONArray.parseArray(JSON.toJSONString(sysPermissions)), "id", "parentId", "children");
        return obj;
    }


    /**
     * 生成路由
     *
     * @param userId
     * @return
     */
    public Object getUserRouters(Long userId) {
        List<VueRouter<SysPermission>> routes = new ArrayList<>();
        Set<Long> permissionIds = null;
        //1.根据用户ID查询角色
        List<SysUserRole> userRoles = sysUserRoleMapper.selectList(new QueryWrapper<SysUserRole>().eq("user_id", userId));
        //2.根据角色ID查询权限ID集合
        List<SysRolePermission> rolePermissions = sysRolePermissionMapper.selectList(new QueryWrapper<SysRolePermission>().in("role_id", userRoles.stream().map(userRole -> userRole.getRoleId()).collect(Collectors.toList())));
        permissionIds = rolePermissions.stream().map(rolePermission -> rolePermission.getPermissionId()).collect(Collectors.toSet());
        //3.查询权限列表
        List<SysPermission> permissions = sysPermissionMapper.selectList(new QueryWrapper<SysPermission>().in("id", permissionIds).eq("type", "0"));
        permissions.forEach(permission -> {
            VueRouter<SysPermission> route = new VueRouter<>();
            route.setId(permission.getId().toString());
            route.setParentId(permission.getParentId().toString());
            route.setIcon(permission.getIcon());
            route.setPath(permission.getPath());
            route.setComponent(permission.getComponent());
            route.setName(permission.getPermissionName());
            route.setMeta(new RouterMeta(permission.getPermissionName(), permission.getIcon(), true, null));
            routes.add(route);
        });

//        JSONArray obj = CommonUtils.listToTree(JSONArray.parseArray(JSON.toJSONString(routes)), "id", "parentId", "children");
//        return obj;
        return buildVueRouter(routes);
    }

    /**
     * 构造前端路由
     *
     * @param routes routes
     * @param <T>    T
     * @return ArrayList<VueRouter < T>>
     */
    public static <T> List<VueRouter<T>> buildVueRouter(List<VueRouter<T>> routes) {
        if (routes == null) {
            return null;
        }
        List<VueRouter<T>> topRoutes = new ArrayList<>();
        VueRouter<T> router = new VueRouter<>();
        routes.forEach(route -> {
            String parentId = route.getParentId();
            if (parentId == null || "0".equals(parentId)) {
                topRoutes.add(route);
                return;
            }
            for (VueRouter<T> parent : routes) {
                String id = parent.getId();
                if (id != null && id.equals(parentId)) {
                    if (parent.getChildren() == null)
                        parent.initChildren();
                    parent.getChildren().add(route);
                    parent.setHasChildren(true);
                    route.setHasParent(true);
                    parent.setHasParent(true);
                    return;
                }
            }
        });
        return topRoutes;
    }

    /**
     * 添加菜单/按钮
     *
     * @param sysPermission
     */
    public void add(SysPermission sysPermission) {
        if (sysPermission.getParentId() == null) {
            sysPermission.setParentId(0L);
        }
        if ("1".equals(sysPermission.getType())) {
            sysPermission.setPath(null);
            sysPermission.setIcon(null);
            sysPermission.setComponent(null);
        }
        sysPermission.setCreateTime(new Date());
        sysPermissionMapper.insert(sysPermission);
    }

    /**
     * 删除节点
     *
     * @param id
     * @return
     */
    public Boolean del(Long id) {
        //1.判断是否存在节点
        Boolean flag = checkIsExistChildren(id);
        if (flag) {
            return false;
        }
        //2.删除节点
        sysPermissionMapper.deleteById(id);
        //3.删除节点对应的角色关系
        sysRolePermissionMapper.delete(new QueryWrapper<SysRolePermission>().eq("permission_id", id));
        return true;
    }

    /**
     * 根据ID判断是否有子节点
     *
     * @return
     */
    public Boolean checkIsExistChildren(Long id) {
        List<SysPermission> permissions = sysPermissionMapper.selectList(new QueryWrapper<SysPermission>().eq("parent_id", id));
        return CollectionUtils.isEmpty(permissions) ? false : true;
    }

    /**
     * 菜单和按钮权限
     */
    public Object menuAndButton() {
        List<SysPermission> sysPermissions = sysPermissionMapper.selectList(new QueryWrapper<SysPermission>().in("type", Arrays.asList(0, 1)));
        JSONArray obj = CommonUtils.listToTree(JSONArray.parseArray(JSON.toJSONString(sysPermissions)), "id", "parentId", "children");
        return obj;
    }
}
