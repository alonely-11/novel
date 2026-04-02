package com.novel.dto.resp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookCategoryRespDto {

    private Long id;
    private String name;

}
