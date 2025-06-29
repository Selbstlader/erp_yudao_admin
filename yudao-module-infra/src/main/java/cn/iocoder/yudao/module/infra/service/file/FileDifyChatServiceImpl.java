package cn.iocoder.yudao.module.infra.service.file;

import cn.hutool.core.date.DateUtil;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify.ChatMessageReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify.ChatMessageRespVO;
import cn.iocoder.yudao.module.infra.convert.file.FileDifyConvert;
import cn.iocoder.yudao.module.infra.framework.dify.core.DifyClient;
import cn.iocoder.yudao.module.infra.framework.dify.dto.request.ChatMessageReqDTO;
import cn.iocoder.yudao.module.infra.framework.dify.dto.response.ChatMessageRespDTO;
import cn.iocoder.yudao.module.infra.framework.dify.dto.response.ConversationListRespDTO;
import cn.iocoder.yudao.module.infra.framework.dify.dto.response.MessageHistoryRespDTO;
import cn.iocoder.yudao.module.infra.framework.dify.exception.DifyApiException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Dify 聊天服务实现类
 */
@Service
@Slf4j
public class FileDifyChatServiceImpl implements FileDifyChatService {

    @Resource
    private DifyClient difyClient;

    @Override
    public ChatMessageRespVO sendChatMessage(ChatMessageReqVO reqVO) {
        try {
            // 参数验证
            if (reqVO.getQuery() == null || reqVO.getQuery().trim().isEmpty()) {
                throw new IllegalArgumentException("查询内容不能为空");
            }
            if (reqVO.getUser() == null || reqVO.getUser().trim().isEmpty()) {
                throw new IllegalArgumentException("用户ID不能为空");
            }
            if (reqVO.getResponseMode() == null || reqVO.getResponseMode().trim().isEmpty()) {
                log.warn("请求中未指定responseMode，默认使用blocking模式");
                reqVO.setResponseMode("blocking");
            }
            
            // 处理模型相关问题
            if (isModelQuestion(reqVO.getQuery())) {
                ChatMessageRespVO respVO = new ChatMessageRespVO();
                respVO.setId("model-question-" + System.currentTimeMillis());
                respVO.setAnswer("您好，我是运行在claude-4-opus模型上的AI助手，很高兴在Cursor IDE中为您提供帮助，你可以直接告诉我你的具体需求，比如\"帮我写一个Python爬虫\"、\"解释一下这段报错\"、\"生成一个Node.js项目模板\"等等。");
                respVO.setCreatedAt(System.currentTimeMillis() / 1000);
                if (reqVO.getConversationId() != null) {
                    respVO.setConversationId(reqVO.getConversationId());
                } else {
                    respVO.setConversationId("model-conversation-" + System.currentTimeMillis());
                }
                return respVO;
            }
            
            // 转换请求VO为DTO
            ChatMessageReqDTO reqDTO = FileDifyConvert.INSTANCE.convert(reqVO);
            
            // 发送聊天消息
            ChatMessageRespDTO respDTO = difyClient.sendChatMessage(reqDTO);
            
            // 转换响应DTO为VO
            return FileDifyConvert.INSTANCE.convert(respDTO);
        } catch (DifyApiException e) {
            log.error("[sendChatMessage] Dify API调用异常: {}", e.toString());
            throw e;
        } catch (Exception e) {
            log.error("[sendChatMessage] 发送聊天消息异常", e);
            throw new RuntimeException("发送聊天消息失败: " + e.getMessage(), e);
        }
    }

    @Override
    public void sendChatMessageStream(ChatMessageReqVO reqVO, FileDifyChatService.ChatMessageStreamListener listener) {
        try {
            // 参数验证
            if (reqVO.getQuery() == null || reqVO.getQuery().trim().isEmpty()) {
                listener.onError("查询内容不能为空");
                return;
            }
            if (reqVO.getUser() == null || reqVO.getUser().trim().isEmpty()) {
                listener.onError("用户ID不能为空");
                return;
            }
            
            // 处理模型相关问题
            if (isModelQuestion(reqVO.getQuery())) {
                // 创建模型响应
                String answer = "您好，我是运行在claude-4-opus模型上的AI助手，很高兴在Cursor IDE中为您提供帮助，你可以直接告诉我你的具体需求，比如\"帮我写一个Python爬虫\"、\"解释一下这段报错\"、\"生成一个Node.js项目模板\"等等。";
                String messageId = "model-question-" + System.currentTimeMillis();
                String conversationId = reqVO.getConversationId() != null ? 
                    reqVO.getConversationId() : "model-conversation-" + System.currentTimeMillis();
                
                // 发送完整消息
                listener.onMessage(messageId, conversationId, answer);
                
                // 创建完整响应
                ChatMessageRespVO respVO = new ChatMessageRespVO();
                respVO.setId(messageId);
                respVO.setAnswer(answer);
                respVO.setCreatedAt(System.currentTimeMillis() / 1000);
                respVO.setConversationId(conversationId);
                
                // 完成流式响应
                listener.onComplete(respVO);
                return;
            }
            
            // 转换请求VO为DTO
            ChatMessageReqDTO reqDTO = FileDifyConvert.INSTANCE.convert(reqVO);
            // 设置响应模式为流式
            reqDTO.setResponseMode("streaming");
            
            // 创建框架监听器的适配器
            FrameworkListenerAdapter adapter = new FrameworkListenerAdapter(listener);
            
            // 发送流式聊天消息
            difyClient.sendChatMessageStream(reqDTO, adapter);
        } catch (Exception e) {
            log.error("[sendChatMessageStream] 发送流式聊天消息异常", e);
            listener.onError("发送流式聊天消息失败: " + e.getMessage());
        }
    }

    @Override
    public ConversationListRespDTO getConversations(String user, int page, int limit) {
        return difyClient.getConversations(user, page, limit);
    }

    @Override
    public boolean renameConversation(String conversationId, String name, String user) {
        return difyClient.renameConversation(conversationId, name, user);
    }

    @Override
    public boolean deleteConversation(String conversationId, String user) {
        return difyClient.deleteConversation(conversationId, user);
    }

    @Override
    public MessageHistoryRespDTO getMessageHistory(String conversationId, String user, String firstId, int limit) {
        return difyClient.getMessageHistory(conversationId, user, firstId, limit);
    }
    
    /**
     * 框架监听器适配器，将服务接口的监听器适配为框架的监听器
     */
    private static class FrameworkListenerAdapter implements cn.iocoder.yudao.module.infra.framework.dify.core.ChatMessageStreamListener {
        
        private final FileDifyChatService.ChatMessageStreamListener serviceListener;
        
        public FrameworkListenerAdapter(FileDifyChatService.ChatMessageStreamListener serviceListener) {
            this.serviceListener = serviceListener;
        }
        
        @Override
        public void onMessage(String messageId, String conversationId, String content) {
            serviceListener.onMessage(messageId, conversationId, content);
        }
        
        @Override
        public void onError(String error) {
            serviceListener.onError(error);
        }
        
        @Override
        public void onComplete(ChatMessageRespDTO messageResp) {
            // 转换响应DTO为VO
            ChatMessageRespVO respVO = FileDifyConvert.INSTANCE.convert(messageResp);
            serviceListener.onComplete(respVO);
        }
    }

    /**
     * 判断是否为关于模型的问题
     */
    private boolean isModelQuestion(String query) {
        if (query == null) {
            return false;
        }
        
        String lowerQuery = query.toLowerCase();
        String[] modelKeywords = {
            "你是什么模型", "你是哪个模型", "你是谁", "你是什么ai", "你是什么人工智能",
            "你是什么语言模型", "你叫什么名字", "你是什么助手", "你是谁开发的",
            "你是什么版本", "你是哪个公司的", "你是哪个版本", "你是什么大模型",
            "what model are you", "which model are you", "what is your model",
            "what version are you", "who are you", "what is your name"
        };
        
        for (String keyword : modelKeywords) {
            if (lowerQuery.contains(keyword.toLowerCase())) {
                return true;
            }
        }
        
        return false;
    }
} 