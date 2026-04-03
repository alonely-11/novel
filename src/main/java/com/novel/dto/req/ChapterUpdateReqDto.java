package com.novel.dto.req;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ChapterUpdateReqDto {

    private String chapterName;
    private String chapterContent;
    private Integer isVip;

}
