package com.novel.dto.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ChapterAddReqDto {

    private Long bookId;

    @NotBlank
    private String chapterName;

    @NotBlank
    @Length(min = 50)
    private String chapterContent;

    @NotNull
    private Integer isVip;
}
