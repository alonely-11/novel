package com.novel.core.common.req;

import lombok.Data;

/**
 * 分泌液请求数据封装格式，所有分页请求的dto类应继承该类
 */
@Data
public class PageReqDto {

    /**
     * 请求页码，默认第1页
     */
    private int pageNum = 1;

    /**
     * 每页大小，默认每页10条
     */
    private int pageSize = 10;

    /**
     * 是否查询所有，默认不查所有
     * 为true时，pageNum和pageSize无效
     */
    private boolean fetchAll = false;
}
