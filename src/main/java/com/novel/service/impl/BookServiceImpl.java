package com.novel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.novel.core.common.constant.DatabaseConsts;
import com.novel.core.common.constant.ErrorCodeEnum;
import com.novel.core.common.resp.RestResp;
import com.novel.dao.entity.BookComment;
import com.novel.dao.mapper.BookCommentMapper;
import com.novel.dto.req.UserCommentReqDto;
import com.novel.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookCommentMapper bookCommentMapper;

    @Override
    public RestResp<Void> saveComment(UserCommentReqDto dto) {

        QueryWrapper<BookComment> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq(DatabaseConsts.BookCommentTable.COLUMN_USER_ID,dto.getUserId())
                .eq(DatabaseConsts.BookCommentTable.COLUMN_BOOK_ID,dto.getBookId());

        if (bookCommentMapper.selectCount(queryWrapper) > 0){
            return RestResp.fail(ErrorCodeEnum.USER_COMMENTED);
        }

        BookComment bookComment = new BookComment();

        bookComment.setUserId(dto.getUserId());
        bookComment.setBookId(dto.getBookId());
        bookComment.setCommentContent(dto.getCommentContent());
        bookComment.setCreateTime(LocalDateTime.now());
        bookComment.setUpdateTime(LocalDateTime.now());

        bookCommentMapper.insert(bookComment);

        return RestResp.ok(null);
    }

}
