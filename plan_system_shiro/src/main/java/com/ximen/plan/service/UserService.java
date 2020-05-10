package com.ximen.plan.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ximen.plan.entity.SysUser;
import com.ximen.plan.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ZhiShun.Cai
 * @date 2020/1/1 14:15
 * @note
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    /**
     * 根据名字查询用户
     *
     * @param username
     * @return
     */
    public SysUser findByName(String username) {
        return sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("username", username));
    }
}
