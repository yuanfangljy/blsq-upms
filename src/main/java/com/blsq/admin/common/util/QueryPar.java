package com.blsq.admin.common.util;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Map;

/**
 * <p>
 * 类描述：分页相关参数:自定义查询条件
 * </p>
 *
 * @author liujiayi
 * @version：1.0
 * @since 2018/12/27 10:05
 */
public class QueryPar<T>  extends Page<T> {

    private static final String SIZE = "pageSize";//每页显示数
    private static final String CURRENT = "currentPage";//当前页码
    private static final String ORDER_BY_FIELD = "orderByField";
    private static final String IS_ASC = "isAsc";
    private Map<Object, Object> conditions;

    public QueryPar(Map<Object,Object> params){
        this.setSize(Integer.parseInt(params.getOrDefault(SIZE, 10).toString()));
        this.setCurrent(Integer.parseInt(params.getOrDefault(CURRENT, 1).toString()));
    }


}
