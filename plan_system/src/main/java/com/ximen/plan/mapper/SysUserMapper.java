package com.ximen.plan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ximen.plan.entity.SysUser;
import org.apache.ibatis.annotations.Param;

public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    SysUser findById(@Param("id") Integer id);

    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    SysUser getUserInfo(@Param("userId") int userId);
}
