package com.blsq.admin.common.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.blsq.admin.common.constant.CommonConstants;
import lombok.Data;

@Data
public class CommonPageResponse extends R{

    public CommonPageResponse() {
    }

    public CommonPageResponse(R resp, IPage page) {
        super.setCode(resp.getCode());
        super.setMessage(resp.getMessage());
        super.setData(page.getRecords());
        this.setTotal(page.getTotal());
        this.setSize(page.getSize());
        this.setPages(page.getPages());
        this.setCurrent(page.getCurrent());
    }

    private long total;

    private long size;

    private long current;

    private long pages;

    public static CommonPageResponse makeFailrueResp(int code, String msg){
        CommonPageResponse resp = new CommonPageResponse();
        resp.setCode(code);
        resp.setMessage(msg);
        return resp;
    }

    public static CommonPageResponse makeSuccessResp(){
        CommonPageResponse resp = new CommonPageResponse();
        resp.setCode(CommonConstants.SUCCESS);
        resp.setMessage("成功");
        return resp;
    }

    public static CommonPageResponse makeSuccessResp(IPage page){
        CommonPageResponse resp = new CommonPageResponse();
        resp.setCode(CommonConstants.SUCCESS);
        resp.setMessage("成功");
        resp.setData(page.getRecords());
        resp.setTotal(page.getTotal());
        resp.setSize(page.getSize());
        resp.setPages(page.getPages());
        resp.setCurrent(page.getCurrent());
        return resp;
    }
}
