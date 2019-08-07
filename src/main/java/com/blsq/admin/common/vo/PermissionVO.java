package com.blsq.admin.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 类描述：用户权限树
 * </p>
 *
 * @author liujiayi
 * @version：1.0
 * @since 2018/12/21 10:18
 */
@Data
public class PermissionVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer menuId;
    private String menuName;
    private String permission;
    private String path;
    private String url;
    private String method;
    private Integer parentId;
    private String icon;
    private String component;
    private Integer sort;
    private Integer type;
    private List<PermissionVO> data;
}
