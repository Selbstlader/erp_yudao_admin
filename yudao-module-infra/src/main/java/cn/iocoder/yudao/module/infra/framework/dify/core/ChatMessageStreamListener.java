package cn.iocoder.yudao.module.infra.framework.dify.core;

import cn.iocoder.yudao.module.infra.framework.dify.dto.response.ChatMessageRespDTO;

/**
 * Dify 聊天消息流监听器
 */
public interface ChatMessageStreamListener {

    /**
     * 接收到消息片段
     *
     * @param messageId 消息ID
     * @param conversationId 会话ID
     * @param content 消息内容
     */
    void onMessage(String messageId, String conversationId, String content);

    /**
     * 接收到错误
     *
     * @param error 错误信息
     */
    void onError(String error);

    /**
     * 流结束
     *
     * @param messageResp 完整的消息响应
     */
    void onComplete(ChatMessageRespDTO messageResp);
} 