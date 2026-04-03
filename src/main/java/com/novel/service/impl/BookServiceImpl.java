package com.novel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.novel.core.auth.UserHolder;
import com.novel.core.common.constant.CommonConsts;
import com.novel.core.common.constant.DatabaseConsts;
import com.novel.core.common.constant.ErrorCodeEnum;
import com.novel.core.common.req.PageReqDto;
import com.novel.core.common.resp.PageRespDto;
import com.novel.core.common.resp.RestResp;
import com.novel.dao.entity.*;
import com.novel.dao.mapper.*;
import com.novel.dto.req.ChapterUpdateReqDto;
import com.novel.dto.req.UserCommentReqDto;
import com.novel.dto.resp.*;
import com.novel.manager.*;
import com.novel.manager.dao.UserDaoManager;
import com.novel.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.awt.print.Book;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author LC
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private static final Integer REC_BOOK_COUNT = 4;

    private final BookCommentMapper bookCommentMapper;

    private final UserDaoManager userDaoManager;

    private final BookRankCacheManager bookRankCacheManager;

    private final BookInfoCacheManager bookInfoCacheManager;

    private final BookChapterCacheManager bookChapterCacheManager;

    private final BookContentCacheManager bookContentCacheManager;

    private final BookChapterMapper bookChapterMapper;

    private final UserBookshelfMapper userBookshelfMapper;

    private final BookInfoMapper bookInfoMapper;

    private final BookCategoryCacheManager bookCategoryCacheMapper;
    private final BookContentMapper bookContentMapper;

//    private final UserInfoMapper userInfoMapper;


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

    @Override
    public RestResp<Void> updateComment(Long userId,Long id, String content) {

        QueryWrapper<BookComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.CommonColumnEnum.ID.getName(), id)
                .eq(DatabaseConsts.BookCommentTable.COLUMN_USER_ID,userId);
        BookComment bookComment = new BookComment();
        bookComment.setCommentContent(content);
        bookComment.setUpdateTime(LocalDateTime.now());
        bookCommentMapper.update(bookComment,queryWrapper);
        return RestResp.ok(null);
    }

    @Override
    public RestResp<Void> deleteComment(Long userId, Long id) {

        QueryWrapper<BookComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.CommonColumnEnum.ID.getName(), id)
                .eq(DatabaseConsts.BookCommentTable.COLUMN_USER_ID,userId);
        bookCommentMapper.delete(queryWrapper);

        return RestResp.ok(null);
    }

    @Override
    public RestResp<BookCommentRespDto> listNewestComments(Long bookId) {
        // 查询评论总数
        QueryWrapper<BookComment> commentCountQueryWrapper = new QueryWrapper<>();
        commentCountQueryWrapper.eq(DatabaseConsts.BookCommentTable.COLUMN_BOOK_ID, bookId);
        Long commentTotal = bookCommentMapper.selectCount(commentCountQueryWrapper);
        BookCommentRespDto bookCommentRespDto = BookCommentRespDto.builder()
                .commentTotal(commentTotal).build();
        if (commentTotal > 0) {

            // 查询最新的评论列表
            QueryWrapper<BookComment> commentQueryWrapper = new QueryWrapper<>();
            commentQueryWrapper.eq(DatabaseConsts.BookCommentTable.COLUMN_BOOK_ID, bookId)
                    .orderByDesc(DatabaseConsts.CommonColumnEnum.CREATE_TIME.getName())
                    .last(DatabaseConsts.SqlEnum.LIMIT_5.getSql());
            List<BookComment> bookComments = bookCommentMapper.selectList(commentQueryWrapper);

            // 查询评论用户信息，并设置需要返回的评论用户名
            List<Long> userIds = bookComments.stream().map(BookComment::getUserId).toList();
            List<UserInfo> userInfos = userDaoManager.listUsers(userIds);
            Map<Long, UserInfo> userInfoMap = userInfos.stream()
                    .collect(Collectors.toMap(UserInfo::getId, Function.identity()));
            List<BookCommentRespDto.CommentInfo> commentInfos = bookComments.stream()
                    .map(v -> BookCommentRespDto.CommentInfo.builder()
                            .id(v.getId())
                            .commentUserId(v.getUserId())
                            .commentUser(userInfoMap.get(v.getUserId()).getUsername())
                            .commentUserPhoto(userInfoMap.get(v.getUserId()).getUserPhoto())
                            .commentContent(v.getCommentContent())
                            .commentTime(v.getCreateTime()).build()).toList();
            bookCommentRespDto.setComments(commentInfos);
        } else {
            bookCommentRespDto.setComments(Collections.emptyList());
        }
        return RestResp.ok(bookCommentRespDto);
    }

    @Override
    public RestResp<List<BookRankRespDto>> listVisitRankBooks() {
        return RestResp.ok(bookRankCacheManager.listVisitRankBooks());
    }

    @Override
    public RestResp<List<BookRankRespDto>> listNewestRankBooks() {
        return RestResp.ok(bookRankCacheManager.listNewestRankBookds());
    }

    @Override
    public RestResp<List<BookRankRespDto>> listUpdateRankBooks() {
        return RestResp.ok(bookRankCacheManager.listUpdateRankBooks());
    }

    @Override
    public RestResp<BookInfoRespDto> getBookById(Long bookId) {

        return RestResp.ok(bookInfoCacheManager.getBookInfo(bookId));
    }

    @Override
    public RestResp<BookChapterAboutRespDto> getLastChapterAbout(Long bookId) {

        BookInfoRespDto bookInfo = bookInfoCacheManager.getBookInfo(bookId);

        BookChapterRespDto chapterInfo =bookChapterCacheManager.getChapter(bookInfo.getLastChapterId());

        String content = bookContentCacheManager.getBookContent(bookInfo.getLastChapterId());

        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID, bookId);
        Long chapterTotal = bookChapterMapper.selectCount(queryWrapper);

        return RestResp.ok(BookChapterAboutRespDto.builder()
                .chapterInfo(chapterInfo)
                .chapterTotal(chapterTotal)
                .contentSummary(content.substring(0,30))
                .build());
    }

    @Override
    public RestResp<List<BookInfoRespDto>> listRecBooks(Long bookId) throws NoSuchAlgorithmException {

        //拿到小说信息
        BookInfoRespDto bookInfo = bookInfoCacheManager.getBookInfo(bookId);

        List<Long> lastUpdateIdList = bookInfoCacheManager.getLastUpdateIdList(bookInfo.getCategoryId());

        //检验非空与排除本书
        if(CollectionUtils.isEmpty(lastUpdateIdList)){
            return RestResp.ok(Collections.emptyList());
        }

        List<Long> candidateIdList = lastUpdateIdList.stream().filter(
                id-> !Objects.equals(id, bookId)
        ).toList();

        if (CollectionUtils.isEmpty(candidateIdList)) {
            return RestResp.ok(Collections.emptyList());
        }
        // 确定最小推荐书本数量
        int actuallyRecCount = Math.min(REC_BOOK_COUNT,candidateIdList.size());

        List<BookInfoRespDto> respDtoList = new ArrayList<>();
        Set<Integer> recIdIndexSet = new HashSet<>();
        Random rand= SecureRandom.getInstanceStrong();

        while (respDtoList.size() < actuallyRecCount && recIdIndexSet.size() < candidateIdList.size()) {
            int recIdIndex = rand.nextInt(candidateIdList.size());
            if (!recIdIndexSet.contains(recIdIndex)) {
                recIdIndexSet.add(recIdIndex);
                respDtoList.add(bookInfoCacheManager.getBookInfo(candidateIdList.get(recIdIndex)));
            }
        }

        //返回
        return RestResp.ok(respDtoList);
    }

    @Override
    public RestResp<Integer> getBookshelfStatus(String bookId, Long userId) {

        QueryWrapper<UserBookshelf>  queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.UserBookshelfTable.COLUMN_BOOK_ID, bookId)
                .eq(DatabaseConsts.UserBookshelfTable.COLUMN_USER_ID,userId);

        return RestResp.ok(
                userBookshelfMapper.selectCount(queryWrapper)>0?
                        CommonConsts.YES:CommonConsts.NO);
    }

    @Override
    public RestResp<PageRespDto<UserCommentRespDto>> listComments(Long userId, PageReqDto pageReqDto) {

//        QueryWrapper<BookComment> queryWrapperCount = new QueryWrapper<>();
//        queryWrapperCount.eq(DatabaseConsts.UserBookshelfTable.COLUMN_USER_ID,userId);
//        Long total =  bookCommentMapper.selectCount(queryWrapperCount);
//
//        QueryWrapper<BookComment> queryWrapperBC = new QueryWrapper<>();
//        queryWrapperBC.eq(DatabaseConsts.UserBookshelfTable.COLUMN_USER_ID,userId);
//        List<BookComment> bookCommentList = bookCommentMapper.selectList(queryWrapperBC);

        IPage<BookComment> page = new Page<>();
        page.setCurrent(pageReqDto.getPageNum());
        page.setSize(pageReqDto.getPageSize());
        QueryWrapper<BookComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookCommentTable.COLUMN_USER_ID, userId)
                .orderByDesc(DatabaseConsts.CommonColumnEnum.UPDATE_TIME.getName());
        IPage<BookComment> bookCommentIPage = bookCommentMapper.selectPage(page, queryWrapper);
        List<BookComment> bookCommentList = bookCommentIPage.getRecords();

        List<UserCommentRespDto> userCommentRespDtoList = Collections.emptyList();
        if (!CollectionUtils.isEmpty(bookCommentList)) {
            QueryWrapper<BookInfo> queryWrapperBI = new QueryWrapper<>();
            List<Long> ids = bookCommentList.stream().map(BookComment::getBookId).toList();
            queryWrapperBI.in(DatabaseConsts.CommonColumnEnum.ID.getName(),ids);
            List<BookInfo> bookInfoList = bookInfoMapper.selectList(queryWrapperBI);
            Map<Long,BookInfo> map = bookInfoList.stream().collect(Collectors.toMap(BookInfo::getId,Function.identity()));
            userCommentRespDtoList = bookCommentList.stream().map(v->
                    UserCommentRespDto.builder()
                            .commentContent(v.getCommentContent())
                            .commentBookPic(map.get(v.getBookId())!=null?map.get(v.getBookId()).getPicUrl():null)
                            .commentBook(map.get(v.getBookId())!=null?map.get(v.getBookId()).getBookName():null)
                            .commentTime(v.getCreateTime())
                            .build()
            ).toList();
        }
        return RestResp.ok(PageRespDto.of(
                pageReqDto.getPageNum(),
                pageReqDto.getPageSize(),
                page.getTotal(),
                userCommentRespDtoList));
    }

    @Override
    public RestResp<Void> addVisitCount(Long bookId) {

        bookInfoMapper.addVisitCount(bookId);

        bookInfoCacheManager.evictBookInfo(bookId);

        return RestResp.ok();
    }

    @Override
    public RestResp<Long> getPreChapterId(Long chapterId) {

        BookChapterRespDto chapter = bookChapterCacheManager.getChapter(chapterId);

        Long bookId = chapter.getBookId();
        Integer chapterNum = chapter.getChapterNum();

        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID, bookId)
                .lt(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM, chapterNum)
                .orderByDesc(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM)
                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());

        return RestResp.ok(
                Optional.ofNullable(bookChapterMapper.selectOne(queryWrapper))
                        .map(BookChapter::getId)
                        .orElse(null)
        );
    }

    @Override
    public RestResp<Long> getNextChapterId(Long chapterId) {

        BookChapterRespDto chapter = bookChapterCacheManager.getChapter(chapterId);

        Long bookId = chapter.getBookId();
        Integer chapterNum = chapter.getChapterNum();

        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID, bookId)
                .gt(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM, chapterNum)
                .orderByAsc(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM)
                .last(DatabaseConsts.SqlEnum.LIMIT_1.getSql());

        return RestResp.ok(
                Optional.ofNullable(bookChapterMapper.selectOne(queryWrapper))
                        .map(BookChapter::getId)
                        .orElse(null)
        );
    }

    @Override
    public RestResp<List<BookChapterRespDto>> listChapters(Long bookId) {

        QueryWrapper<BookChapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookChapterTable.COLUMN_BOOK_ID, bookId)
                .orderByAsc(DatabaseConsts.BookChapterTable.COLUMN_CHAPTER_NUM);

        return RestResp.ok(bookChapterMapper.selectList(queryWrapper).stream()
                .map(v -> BookChapterRespDto.builder()
                        .id(v.getId())
                        .chapterName(v.getChapterName())
                        .isVip(v.getIsVip())
                        .build()).toList());

//        List<BookChapter> bookChapterList = bookChapterMapper.selectList(queryWrapper);
//
//        List<BookChapterRespDto> chapters = bookChapterList.stream().map(v->
//                bookChapterCacheManager.getChapter(v.getId())
//                ).toList();
//
//        return RestResp.ok(chapters);
    }

    @Override
    public RestResp<List<BookCategoryRespDto>> listCategory(Integer workDirection) {
        return RestResp.ok(bookCategoryCacheMapper.listCategory(workDirection));
    }

    @Override
    public RestResp<BookChapterContentRespDto> getChapterContentAbout(Long chapterId) {

        log.debug("userId:{}", UserHolder.getUserId());

        BookChapterRespDto chapter = bookChapterCacheManager.getChapter(chapterId);

        String content = bookContentCacheManager.getBookContent(chapterId);

        BookInfoRespDto bookInfo = bookInfoCacheManager.getBookInfo(chapter.getBookId());

        return RestResp.ok(
                BookChapterContentRespDto.builder()
                        .bookInfo(bookInfo)
                        .chapterInfo(chapter)
                        .bookContent(content)
                        .build()
        );
    }

    @Override
    public RestResp<ChapterContentRespDto> getBookChapter(Long chapterId) {

        BookChapterRespDto chapter =bookChapterCacheManager.getChapter(chapterId);
        String chapterName = chapter.getChapterName();
        String content = bookContentCacheManager.getBookContent(chapterId);
        Integer isVip = chapter.getIsVip();

        return RestResp.ok(ChapterContentRespDto.builder()
                .chapterName(chapterName)
                .chapterContent(content)
                .isVip(isVip)
                .build());
    }

    @Override
    public RestResp<Void> updateBookChapter(Long chapterId, ChapterUpdateReqDto dto) {

        BookChapter chapter = bookChapterMapper.selectById(chapterId);
        chapter.setChapterName(dto.getChapterName());
        chapter.setIsVip(dto.getIsVip());
        bookChapterMapper.updateById(chapter);
        QueryWrapper<BookContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookContentTable.COLUMN_CHAPTER_ID, chapterId);
        BookContent bookContent = bookContentMapper.selectOne(queryWrapper);
        bookContent.setContent(dto.getChapterContent());
        bookContentMapper.updateById(bookContent);
        return RestResp.ok();
    }

//    @Override
//    public RestResp<BookCommentRespDto> listNewestComment(Long bookId) {
//
//        QueryWrapper<BookComment> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq(DatabaseConsts.BookCommentTable.COLUMN_BOOK_ID,bookId);
//
//
//        Long commentTotal = bookCommentMapper.selectCount(queryWrapper);
//        if (commentTotal > 0) {
//
//            queryWrapper.orderByDesc(DatabaseConsts.CommonColumnEnum.CREATE_TIME.getName())
//                    .last(DatabaseConsts.SqlEnum.LIMIT_5.getSql());
//
//            List<BookComment> bookComments = bookCommentMapper.selectList(queryWrapper);
//
//            List<Long> ids = bookComments.stream().map(BookComment::getUserId).toList();
//            QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
//            userInfoQueryWrapper.in(DatabaseConsts.CommonColumnEnum.ID.getName(), ids);
//            List<UserInfo> userInfos = userInfoMapper.selectList(userInfoQueryWrapper);
//
//            Map<Long, UserInfo> userInfoMap = userInfos.stream().collect(Collectors.toMap(UserInfo::getId, Function.identity()));
//
//            List<BookCommentRespDto.CommentInfo> commentInfos =
//                    bookComments.stream().map(v -> {
//
//                        UserInfo userInfo = userInfoMap.get(v.getUserId());
//
//                        return BookCommentRespDto.CommentInfo.builder()
//                                .id(v.getId())
//                                .commentContent(v.getCommentContent())
//                                .commentUser(userInfo.getUsername())
//                                .commentUserId(v.getUserId())
//                                .commentUserPhoto(userInfo.getUserPhoto())
//                                .commentTime(v.getCreateTime())
//                                .build();
//                    }).toList();
//
//            return RestResp.ok(BookCommentRespDto.builder()
//                    .commentTotal(commentTotal)
//                    .comments(commentInfos)
//                    .build());
//        }else return RestResp.ok(BookCommentRespDto.builder()
//                .commentTotal(commentTotal)
//                .comments(Collections.emptyList())
//                .build());
//    }

}
