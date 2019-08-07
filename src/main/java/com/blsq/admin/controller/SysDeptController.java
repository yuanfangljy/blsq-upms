package com.blsq.admin.controller;



import com.blsq.admin.common.constant.CommonConstants;
import com.blsq.admin.common.dto.DeptDTO;
import com.blsq.admin.common.util.R;
import com.blsq.admin.common.util.SecurityUtils;
import com.blsq.admin.model.SysDept;
import com.blsq.admin.service.ISysDeptService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

/**
 * <p>
 * 部门管理 前端控制器
 * </p>
 *
 * @author liujiayi
 * @since 2019-04-14
 */
@RestController
@RequestMapping("/sysDept")
@AllArgsConstructor
@Api(value = "dept", description = "部门管理模块")
public class SysDeptController {

    private final ISysDeptService sysDeptService;

    /**
     * 通过ID查询
     *
     * @param sysDept
     * @return SysDept
     */
    @PostMapping("/getById")
    public R getById(@RequestBody SysDept sysDept) {
        if(sysDept==null){
            return R.builder().code(CommonConstants.FAIL).message("对象不能为空").build();
        }
        return new R<>(sysDeptService.getById(sysDept.getDeptId()));
    }


    /**
     * 返回树形菜单集合
     *
     * @return 树形菜单
     */
    @PostMapping(value = "/tree")
    public R getTree() {
        return new R<>(sysDeptService.selectTree());
    }

    /**
     * 返回当前用户树形菜单集合
     *
     * @return 树形菜单
     */
    @PostMapping(value = "/user-tree")
    public R getUserTree() {
        return new R<>(sysDeptService.getUserTree());
    }

    /**
     * 添加
     *
     * @param deptDTO 实体
     * @return success/false
     */
    @PostMapping("/save")
    public R save(@Valid @RequestBody DeptDTO deptDTO) {
        Integer userId = SecurityUtils.getUser().getId();
        deptDTO.setUserId(userId);
        return new R<>(sysDeptService.saveDept(deptDTO));
    }

    /**
     * 删除
     *
     * @param sysDept ID
     * @return success/false
     */
    @PostMapping("/removeById")
    public R removeById(@RequestBody SysDept sysDept) {
        return new R<>(sysDeptService.removeDeptById(sysDept.getDeptId()));
    }

    /**
     * 编辑
     *
     * @param deptDTO 实体
     * @return success/false
     */
    @PostMapping("/update")
    public R update(@Valid @RequestBody DeptDTO deptDTO) {
        deptDTO.setUpdateTime(LocalDateTime.now());
        return new R<>(sysDeptService.updateDeptById(deptDTO));
    }
}

