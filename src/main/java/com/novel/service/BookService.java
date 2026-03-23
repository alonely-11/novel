package com.novel.service;

import com.novel.core.common.resp.RestResp;
import com.novel.dto.req.UserCommentReqDto;
import com.novel.dto.resp.BookChapterAboutRespDto;
import com.novel.dto.resp.BookCommentRespDto;
import com.novel.dto.resp.BookInfoRespDto;
import com.novel.dto.resp.BookRankRespDto;

import java.util.List;

public interface BookService {

    RestResp<Void> saveComment(UserCommentReqDto dto);

    RestResp<Void> updateComment(Long userId,Long id, String content);

    RestResp<Void> deleteComment(Long userId, Long id);

    RestResp<BookCommentRespDto> listNewestComments(Long bookId);

    RestResp<List<BookRankRespDto>> listVisitRankBooks();

    RestResp<List<BookRankRespDto>> listNewestRankBooks();

    RestResp<List<BookRankRespDto>> listUpdateRankBooks();

    RestResp<BookInfoRespDto> getBookById(Long bookId);

    RestResp<BookChapterAboutRespDto> getLastChapterAbout(Long bookId);
}
