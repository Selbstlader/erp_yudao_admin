package cn.iocoder.yudao.module.infra.service.file;

import cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify.ChatMessageReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify.ChatMessageRespVO;
import cn.iocoder.yudao.module.infra.framework.dify.dto.response.ConversationListRespDTO;
import cn.iocoder.yudao.module.infra.framework.dify.dto.response.MessageHistoryRespDTO;

/**
 * Dify 聊天服务接口
 */
public interface FileDifyChatService {

    /**
     * 发送聊天消息（阻塞模式）
     *
     * @param reqVO 聊天消息请求
     * @return 聊天消息响应
     */
    ChatMessageRespVO sendChatMessage(ChatMessageReqVO reqVO);

    /**
     * 发送聊天消息（流式模式）
     *
     * @param reqVO 聊天消息请求
     * @param listener 流式消息监听器
     */
    void sendChatMessageStream(ChatMessageReqVO reqVO, ChatMessageStreamListener listener);

    /**
     * 获取会话列表
     *
     * @param user 用户ID
     * @param page 页码
     * @param limit 每页条数
     * @return 会话列表
     */
    ConversationListRespDTO getConversations(String user, int page, int limit);

    /**
     * 重命名会话
     *
     * @param conversationId 会话ID
     * @param name 新名称
     * @param user 用户ID
     * @return 是否成功
     */
    boolean renameConversation(String conversationId, String name, String user);

    /**
     * 删除会话
     *
     * @param conversationId 会话ID
     * @param user 用户ID
     * @return 是否成功
     */
    boolean deleteConversation(String conversationId, String user);

    /**
     * 获取会话消息历史
     *
     * @param conversationId 会话ID
     * @param user 用户ID
     * @param firstId 起始消息ID
     * @param limit 消息数量限制
     * @return 消息历史
     */
    MessageHistoryRespDTO getMessageHistory(String conversationId, String user, String firstId, int limit);

    /**
     * 聊天消息流监听器接口
     */
    interface ChatMessageStreamListener {

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
        void onComplete(ChatMessageRespVO messageResp);
    }
} 