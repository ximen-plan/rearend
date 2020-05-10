package com.ximen.plan.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ximen.plan.entity.SysUserRole;
import com.ximen.plan.mapper.SysUserRoleMapper;
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
public class SysUserRoleService extends ServiceImpl<SysUserRoleMapper, SysUserRole> {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    /**
     * 根据用户ID查询
     *
     * @param userId
     */
    public List<SysUserRole> findByUserId(Long userId) {
        return this.sysUserRoleMapper.selectList(new QueryWrapper<SysUserRole>().eq("user_id", userId));
    }


    /**
     * 根据用户ID查询角色ID集合
     */
    public List<Long> findRolesByUserId(Long userId) {
        List<SysUserRole> userRoles = sysUserRoleMapper.selectList(new QueryWrapper<SysUserRole>().eq("user_id", userId));
        return userRoles.stream().map(userRole -> userRole.getRoleId()).collect(Collectors.toList());
    }
}
