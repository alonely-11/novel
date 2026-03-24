package com.novel.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class AuthorInfoDto implements Serializable {

    private Long id;

    private String penName;

    private Integer status;

}
