package com.novel.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class UserInfoDto implements Serializable {

    private Long id;

    private Integer status;
}
