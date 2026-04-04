package com.novel.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Builder
@Data
public class ChapterUpdateReqDto {

    @NotBlank
    private String chapterName;
    @NotBlank
    @Length(min = 50)
    private String chapterContent;
    @NotNull
    private Integer isVip;

}
