package com.novel.controller.front;

import com.novel.core.common.constant.ApiRouterConsts;
import com.novel.core.common.resp.RestResp;
import com.novel.dto.resp.NewsInfoRespDto;
import com.novel.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ApiRouterConsts.API_FRONT_NEWS_URL_PREFIX)
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping("{id}")
    public RestResp<NewsInfoRespDto> getNews(@PathVariable Long id){
        return newsService.getNews(id);
    }

    @GetMapping("latest_list")
    public RestResp<List<NewsInfoRespDto>> listLatestNews(){
        return newsService.listLatestNews();
    }

}
