package com.novel.controller.author;

import com.novel.core.common.constant.ApiRouterConsts;
import com.novel.core.common.resp.RestResp;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiRouterConsts.API_AUTHOR_AI_URL_PREFIX)
@RequiredArgsConstructor
public class AuthorAiController {

    private final ChatClient chatClient;

    /**
     * AI扩写
     * @param text
     * @param ratio
     * @return
     */
    @PostMapping("/expand")
    public RestResp<String> expandText(String text,Double ratio){
        String prompt = "请将以下文本扩写为原长度的" + ratio/100 + "倍：" + text;
        return RestResp.ok(chatClient.prompt()
                .user(prompt)
                .call()
                .content());
    }

    /**
     * AI缩写
     */
    @Operation(summary = "AI缩写接口")
    @PostMapping("/condense")
    public RestResp<String> condenseText(@RequestParam("text") String text, @RequestParam("ratio") Integer ratio) {
        String prompt = "请将以下文本缩写为原长度的" + 100/ratio + "分之一：" + text;
        return RestResp.ok(chatClient.prompt()
                .user(prompt)
                .call()
                .content());
    }

    /**
     * AI续写
     */
    @Operation(summary = "AI续写接口")
    @PostMapping("/continue")
    public RestResp<String> continueText(@RequestParam("text") String text, @RequestParam("length") Integer length) {
        String prompt = "请续写以下文本，续写长度约为" + length + "字：" + text;
        return RestResp.ok(chatClient.prompt()
                .user(prompt)
                .call()
                .content());
    }

    /**
     * AI润色
     */
    @Operation(summary = "AI润色接口")
    @PostMapping("/polish")
    public RestResp<String> polishText(@RequestParam("text") String text) {
        String prompt = "请润色优化以下文本，保持原意：" + text;
        return RestResp.ok(chatClient.prompt()
                .user(prompt)
                .call()
                .content());
    }

}
