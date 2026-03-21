package com.novel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.novel.core.common.constant.DatabaseConsts;
import com.novel.core.common.constant.ErrorCodeEnum;
import com.novel.core.common.resp.RestResp;
import com.novel.dao.entity.BookComment;
import com.novel.dao.entity.UserInfo;
import com.novel.dao.mapper.BookCommentMapper;
import com.novel.dao.mapper.UserInfoMapper;
import com.novel.dto.req.UserCommentReqDto;
import com.novel.dto.resp.BookCommentResDto;
import com.novel.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author LC
 */
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookCommentMapper bookCommentMapper;

    private final UserInfoMapper userInfoMapper;

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
    public RestResp<BookCommentResDto> listNewestComment(Long bookId) {

        QueryWrapper<BookComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(DatabaseConsts.BookCommentTable.COLUMN_BOOK_ID,bookId);

        Long commentTotal = bookCommentMapper.selectCount(queryWrapper);

        queryWrapper.orderByDesc(DatabaseConsts.CommonColumnEnum.CREATE_TIME.getName())
                .last(DatabaseConsts.SqlEnum.LIMIT_5.getSql());

        List<BookComment> bookComments = bookCommentMapper.selectList(queryWrapper);

        List<Long> ids = bookComments.stream().map(BookComment::getUserId).toList();
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.in(DatabaseConsts.CommonColumnEnum.ID.getName(),ids);
        List<UserInfo> userInfos = userInfoMapper.selectList(userInfoQueryWrapper);

        Map<Long,UserInfo> userInfoMap = userInfos.stream().collect(Collectors.toMap(UserInfo::getId, Function.identity()));

        List<BookCommentResDto.CommentInfo> commentInfos =
                bookComments.stream().map(v-> {

                    UserInfo userInfo = userInfoMap.get(v.getUserId());

                    return BookCommentResDto.CommentInfo.builder()
                            .id(v.getId())
                            .commentContent(v.getCommentContent())
                            .commentUser(userInfo.getUsername())
                            .commentUserId(v.getUserId())
                            .commentUserPhoto(userInfo.getUserPhoto())
                            .commentTime(v.getCreateTime())
                            .build();
                }).toList();

        return RestResp.ok(BookCommentResDto.builder()
                .commentTotal(commentTotal)
                .comments(commentInfos)
                .build());
    }

}
