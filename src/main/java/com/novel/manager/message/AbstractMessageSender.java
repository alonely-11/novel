package com.novel.manager.message;

/**
 * 抽象的消息发送器
 */
public abstract class AbstractMessageSender implements MessageSender {

    private static final String PLACEHOLDER = "{}";

    /**
     * 定义消息发送的模板，子类不能修改此模板
     * @param toUserId
     * @param args
     */
    @Override
    public final void sendMessage(Long toUserId, Object... args) {
        //1.获取消息标题模板
        String titleTemplate = getTitleTemplate();
        //2.获取消息内容模板
        String contentTemplate = getContentTemplate();
        //3.解析消息模板
        String title = resolveTitle(titleTemplate,args);
        //4.解析消息内容
        String content = resolveContent(contentTemplate,args);
        //5.发送消息
        sendMessage(toUserId,title,content);
    }

    protected abstract void sendMessage(Long toUserId, String messageTitle, String messageContent);

    protected abstract String getTitleTemplate();

    protected abstract String getContentTemplate();

    /**
     * 解析消息标题模板
     * @param titleTemplate 消息内容模板
     * @param arguments 用来解析的参数列表
     * @return 解析后的消息标题
     */
    protected String resolveTitle(String titleTemplate,Object... arguments) {
        return titleTemplate;
    }

    protected String resolveContent(String contentTemplate,Object... args) {
        if(args.length>0){
            StringBuilder formattedContent = new StringBuilder(contentTemplate);
            for(Object arg : args){
                int start = formattedContent.indexOf(PLACEHOLDER);
                formattedContent.replace(start, start + PLACEHOLDER.length(), String.valueOf(arg));
            }
            return formattedContent.toString();
        }
        return contentTemplate;
    }

}
