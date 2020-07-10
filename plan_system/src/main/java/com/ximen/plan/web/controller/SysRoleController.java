package com.ximen.plan.web.controller;

import com.ximen.plan.constants.StatusCode;
import com.ximen.plan.dto.responsedto.PageResultDTO;
import com.ximen.plan.dto.responsedto.ResultDTO;
import com.ximen.plan.entity.SysRole;
import com.ximen.plan.service.SysRoleService;
import com.ximen.plan.vo.RoleUpdateParamsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ZhiShun.Cai
 * @date 2019/12/18 19:03
 * @note
 */
@RestController
@RequestMapping("sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @GetMapping("page/{pageNumber}/{pageSize}")
    public ResultDTO page(@PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize, String searchKey) {
        PageResultDTO<SysRole> resultDTO = sysRoleService.page(pageNumber, pageSize, searchKey);
        return new ResultDTO(StatusCode.SUCCESS, resultDTO);
    }

    /**
     * 查询所有角色
     *
     * @return
     */
    @GetMapping("findAllRoles")
    public ResultDTO findAll() {
        return new ResultDTO(StatusCode.SUCCESS, sysRoleService.findAll());
    }

    /**
     * 更新
     */
    @PutMapping("update")
    public ResultDTO update(@RequestBody RoleUpdateParamsVO roleUpdateParamsVO) {
        sysRoleService.update(roleUpdateParamsVO);
        return new ResultDTO(StatusCode.SUCCESS);
    }
}
