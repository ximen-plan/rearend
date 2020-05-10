package com.ximen.plan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ximen.plan.entity.SysPermission;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author damoncai
 * @since 2019-12-19
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    /**
     * 查询所有
     *
     * @return
     */
    List<SysPermission> findAll();
}
