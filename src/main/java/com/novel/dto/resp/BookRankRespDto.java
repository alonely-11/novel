package com.novel.dto.resp;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookRankRespDto {

    private Long id;
    private Long categoryId;
    private String categoryName;
    private String picUrl;
    private String bookName;
    private String authorName;
    private String bookDesc;
    private Integer wordCount;
    private String lastChapterName;
    private LocalDateTime lastChapterUpdateTime;

}
