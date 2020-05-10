package com.ximen.plan.mapper;


import com.ximen.plan.entity.SysUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author ZhiShun.Cai
 * @date 2019/12/18 11:01
 * @note
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SysUserMapperTest {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Test
    public void fun() {
        SysUser sysUser = sysUserMapper.selectById(5);
        System.out.println(sysUser);
    }
}