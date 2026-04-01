package com.novel.service;

import com.novel.core.common.req.PageReqDto;
import com.novel.core.common.resp.PageRespDto;
import com.novel.core.common.resp.RestResp;
import com.novel.dto.req.UserCommentReqDto;
import com.novel.dto.resp.*;

import java.security.NoSuchAlgorithmException;
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

    RestResp<List<BookInfoRespDto>> listRecBooks(Long bookId) throws NoSuchAlgorithmException;

    RestResp<Integer> getBookshelfStatus(String bookId, Long userId);

    RestResp<PageRespDto<UserCommentRespDto>> listComments(Long userId, PageReqDto pageReqDto);

    RestResp<Void> addVisitCount(Long bookId);
}
