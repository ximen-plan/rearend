package com.ximen.plan.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ximen.plan.entity.SysRolePermission;
import com.ximen.plan.mapper.SysRolePermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
public class SysRolePermissionService extends ServiceImpl<SysRolePermissionMapper, SysRolePermission> {

    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    /**
     * 根据角色查询权角色权限
     *
     * @param roleIds
     * @return
     */
    public List<SysRolePermission> findByRoleIds(List<Long> roleIds) {
        return sysRolePermissionMapper.selectList(new QueryWrapper<SysRolePermission>().in("role_id", roleIds));
    }

    /**
     * 根据角色ID集合查询没有子权限ID集合
     */
    public List<Long> findPermissionIdsByRoleIdExcludeExistChild(Long roleId) {
        List<SysRolePermission> rolePermissions = sysRolePermissionMapper.findPermissionIdsByRoleIdExcludeExistChild(roleId);
        return rolePermissions.stream().map(rolePermission -> rolePermission.getPermissionId()).collect(Collectors.toList());
    }
}
