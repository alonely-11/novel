package com.novel.dto.resp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookChapterContentRespDto {

    private BookInfoRespDto bookInfo;
    private BookChapterRespDto chapterInfo;
    private String bookContent;

}
