package com.novel.dto.req;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthorRegisterReqDto {

    private Long userId;
    @NotBlank(message = "笔名不能为空")
    private String penName;
    @NotBlank (message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确！")
    private String telPhone;
    @NotBlank(message = "QQ或微信账号不能为空！")
    private String chatAccount;
    @NotBlank(message = "电子邮箱不能为空！")
    @Email(message = "邮箱格式不正确！")
    private String email;
    @NotNull(message = "作品方向不能为空！")
    @Min(0)
    @Max(1)
    private Integer workDirection;

}
