package com.novel.service;

import com.novel.core.common.resp.RestResp;
import com.novel.dto.req.UserCommentReqDto;
import com.novel.dto.resp.BookCommentResDto;

public interface BookService {

    RestResp<Void> saveComment(UserCommentReqDto dto);

    RestResp<Void> updateComment(Long userId,Long id, String content);

    RestResp<Void> deleteComment(Long userId, Long id);

    RestResp<BookCommentResDto> listNewestComment(Long bookId);
}
