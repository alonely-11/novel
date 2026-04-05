package com.novel.dto.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
public class BookAddReqDto {

    @NotNull
    private Integer workDirection;

    @NotNull
    private Long categoryId;

    @NotBlank
    private String categoryName;

    @NotBlank
    private String picUrl;

    @NotBlank
    private String bookName;

    @NotBlank
    private String bookDesc;

    @NotNull
    private Integer isVip;

}
