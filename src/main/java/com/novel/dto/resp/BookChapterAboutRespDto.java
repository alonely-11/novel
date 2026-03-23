package com.novel.dto.resp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookChapterAboutRespDto {

    private BookChapterRespDto chapterInfo;

    private Long chapterTotal;

    private String contentSummary;

}
