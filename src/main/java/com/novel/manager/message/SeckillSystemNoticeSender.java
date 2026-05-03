package com.novel.manager.message;

import com.novel.core.common.constant.MessageSenderTypeConsts;
import org.springframework.stereotype.Component;

@Component(value = MessageSenderTypeConsts.SECKILL_SYS_NOTICE_SENDER)
public class SeckillSystemNoticeSender extends AbstractSysNoticeSender {

    @Override
    protected String getTitleTemplate() {
        return "秒杀即将开始";
    }

    @Override
    protected String getContentTemplate() {
        return "{}秒杀,{}即将,不要错过哦！点击{}前往。";
    }

}
