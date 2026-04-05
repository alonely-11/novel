package com.novel.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookInfoRespDto {

    private Long id;

    private Long categoryId;

    private String categoryName;

    private String picUrl;

    private String bookName;

    private Long authorId;

    private String authorName;

    private String bookDesc;

    private Integer bookStatus;

    private Long visitCount;

    private Integer wordCount;

    private Integer commentCount;

    private Long firstChapterId;

    private Long lastChapterId;

    private String lastChapterName;

    @JsonFormat(pattern = "yyy-MM-dd HH:mm")
    private LocalDateTime updateTime;

}
