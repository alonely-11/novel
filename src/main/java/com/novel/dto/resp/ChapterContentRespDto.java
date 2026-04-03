package com.novel.dto.resp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChapterContentRespDto {

    private String chapterName;
    private String chapterContent;
    private Integer isVip;

}
