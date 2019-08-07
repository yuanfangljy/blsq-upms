package com.blsq.admin.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 部门关系表
 * </p>
 *
 * @author liujiayi
 * @since 2019-04-14
 */
public class SysDeptRelation extends Model<SysDeptRelation> {

    private static final long serialVersionUID = 1L;

    /**
     * 祖先节点
     */
    private Integer ancestor;

    /**
     * 后代节点
     */
    private Integer descendant;


    public Integer getAncestor() {
        return ancestor;
    }

    public void setAncestor(Integer ancestor) {
        this.ancestor = ancestor;
    }

    public Integer getDescendant() {
        return descendant;
    }

    public void setDescendant(Integer descendant) {
        this.descendant = descendant;
    }

    @Override
    protected Serializable pkVal() {
        return this.ancestor;
    }

    @Override
    public String toString() {
        return "SysDeptRelation{" +
        "ancestor=" + ancestor +
        ", descendant=" + descendant +
        "}";
    }
}
