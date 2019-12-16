package com.evsoft.common.utils;

import java.util.Map;

/**
 * Created by TWD on 2018/7/3.
 */
public class PageQuery {

    /**
     * 返回json 字符串属性
     * */
    public static String PAGE = "page";

    private Integer page;
    private Integer pageSize;

    public PageQuery(Map<String, Object> params){
        try{
            int pages = Integer.parseInt(params.get("pageNum").toString());
            int pageSizes = Integer.parseInt(params.get("limit").toString());
            this.page = pages;
            this.pageSize = pageSizes;
        }catch(Exception e){
        }
    }

    public Integer getPage() {
        if(null==page){
            return 1;
        }
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        if(null==pageSize){
            return 15;
        }
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
