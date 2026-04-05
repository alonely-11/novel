package com.novel.dto.resp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class BookChapterRespDto implements Serializable {

    private Long id;
    private Long bookId;
    private Integer chapterNum;
    private String chapterName;
    private Integer chapterWordCount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime chapterUpdateTime;
    private Integer isVip;

}
