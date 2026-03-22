package com.novel.dto.resp;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NewsInfoRespDto {

    private Long id;

    private Long categoryId;

    private String categoryName;

    private String sourceName;

    private String title;

    private LocalDateTime updateTime;

    private String content;

}
