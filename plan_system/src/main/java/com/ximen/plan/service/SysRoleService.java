package com.ximen.plan.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ximen.plan.dto.responsedto.PageResultDTO;
import com.ximen.plan.entity.SysRole;
import com.ximen.plan.entity.SysRolePermission;
import com.ximen.plan.mapper.SysRoleMapper;
import com.ximen.plan.mapper.SysRolePermissionMapper;
import com.ximen.plan.vo.RoleUpdateParamsVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author ZhiShun.Cai
 * @date 2019/12/18 19:06
 * @note
 */
@Service
@Transactional
public class SysRoleService extends ServiceImpl<SysRoleMapper, SysRole> {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    /**
     * 分页查询
     *
     * @param pageNumber
     * @param pageSize
     * @param searchKey
     * @return
     */
    public PageResultDTO<SysRole> page(Integer pageNumber, Integer pageSize, String searchKey) {
        IPage<SysRole> roleIPage = null;
        QueryWrapper<SysRole> queryWrapper = null;
        if (StringUtils.isNotBlank(searchKey)) {
            queryWrapper
                    .like("role_name", searchKey)
                    .like("remark", searchKey);
        }
        roleIPage = sysRoleMapper.selectPage(new Page<SysRole>(pageNumber, pageSize), queryWrapper);
        return new PageResultDTO<>(roleIPage.getTotal(), roleIPage.getRecords());
    }

    /**
     * //根据角色ID集合查询
     *
     * @param roleIds
     * @return
     */
    public List<SysRole> findByIds(List<Long> roleIds) {
        return this.sysRoleMapper.selectBatchIds(roleIds);
    }

    /**
     * 查询所有角色
     *
     * @return
     */
    public List<SysRole> findAll() {
        return this.sysRoleMapper.selectList(null);
    }

    /**
     * 修改
     *
     * @param roleUpdateParamsVO
     * @return
     */
    public void update(RoleUpdateParamsVO roleUpdateParamsVO) {
        //1.删除之前的权限
        this.sysRolePermissionMapper.delete(new QueryWrapper<SysRolePermission>().eq("role_id", roleUpdateParamsVO.getRoleId()));
        //2.添加角色权限关系权限
        addRolePermission(roleUpdateParamsVO.getRoleId(), roleUpdateParamsVO.getPermissionIds());
        //跟新角色信息
        roleUpdateParamsVO.setModifyTime(new Date());
        this.sysRoleMapper.updateById(roleUpdateParamsVO);

    }

    /**
     * @param roleId        角色ID
     * @param permissionIds 权限ID集合
     */
    private void addRolePermission(Long roleId, List<Long> permissionIds) {
        for (Long permissionId : permissionIds) {
            this.sysRolePermissionMapper.insert(new SysRolePermission(roleId, permissionId));
        }
    }
}
