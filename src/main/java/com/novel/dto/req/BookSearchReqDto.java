package com.novel.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class BookSearchReqDto {

    private String keyword;

    private Integer workDirection;

    private Integer categoryId;

    private Integer isVip;

    private Integer bookStatus;

    private Integer wordCountMin;

    private Integer wordCountMax;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateTimeMin;

    private Integer pageNum;

    private Integer pageSize;

    private String sort;

    private String order;

}
