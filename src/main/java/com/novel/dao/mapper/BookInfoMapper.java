package com.novel.dao.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.novel.dao.entity.BookInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.novel.dto.req.BookSearchReqDto;

/**
 * <p>
 * 小说信息 Mapper 接口
 * </p>
 *
 * @author ${author}
 * @date 2026/03/11
 */
public interface BookInfoMapper extends BaseMapper<BookInfo> {

    void addVisitCount(Long bookId);

    IPage<BookInfo> searchBooks(IPage<BookInfo> page,BookSearchReqDto condition);
}
