package com.novel.dto.resp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HomeFriendLinkRespDto {

    private String linkName;
    private String linkUrl;

}
