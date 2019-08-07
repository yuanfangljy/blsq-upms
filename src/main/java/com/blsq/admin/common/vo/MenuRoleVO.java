package com.blsq.admin.common.vo;

import com.blsq.admin.model.SysRole;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 类描述：所有菜单以及相关角色信息类
 * </p>
 *
 * @author liujiayi
 * @version：1.0
 * @since 2019/2/20 11:01
 */
@Data
public class MenuRoleVO implements Serializable{

    private static final long serialVersionUID = 1L;
    /**
     * 菜单ID
     */
    private Integer menuId;
    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 菜单权限标识
     */
    private String permission;
    /**
     * 请求url
     */
    private String url;
    /**
     * 一个路径
     */
    private String path;
    /**
     * VUE页面
     */
    private String component;

    /**
     * 角色集合
     */
    private List<SysRoleVO> roleList;
}
