package com.ximen.plan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ximen.plan.entity.SysRolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author damoncai
 * @since 2019-12-19
 */
public interface SysRolePermissionMapper extends BaseMapper<SysRolePermission> {

    /**
     * 根据角色ID集合查询没有子权限ID集合
     */
    List<SysRolePermission> findPermissionIdsByRoleIdExcludeExistChild(@Param("roleId") Long roleId);
}
