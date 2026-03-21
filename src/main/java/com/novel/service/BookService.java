package com.novel.service;

import com.novel.core.common.resp.RestResp;
import com.novel.dto.req.UserCommentReqDto;

public interface BookService {

    RestResp<Void> saveComment(UserCommentReqDto dto);

    RestResp<Void> updateComment(Long userId,Long id, String content);
}
