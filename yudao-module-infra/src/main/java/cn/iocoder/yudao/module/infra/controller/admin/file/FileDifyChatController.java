package cn.iocoder.yudao.module.infra.controller.admin.file;

import cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify.ChatMessageReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.dify.ChatMessageRespVO;
import cn.iocoder.yudao.module.infra.framework.dify.dto.response.ConversationListRespDTO;
import cn.iocoder.yudao.module.infra.framework.dify.dto.response.MessageHistoryRespDTO;
import cn.iocoder.yudao.module.infra.framework.dify.exception.DifyApiException;
import cn.iocoder.yudao.module.infra.service.file.FileDifyChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static cn.iocoder.yudao.framework.common.exception.enums.GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.error;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

/**
 * 管理后台 - Dify聊天
 */
@Tag(name = "管理后台 - Dify聊天")
@RestController
@RequestMapping("/infra/dify-chat")
@Validated
@Slf4j
public class FileDifyChatController {

    @Resource
    private FileDifyChatService fileDifyChatService;

    @PostMapping("/message")
    @Operation(summary = "发送聊天消息")
    @PreAuthorize("@ss.hasPermission('infra:dify-chat:send')")
    public CommonResult<ChatMessageRespVO> sendChatMessage(@Valid @RequestBody ChatMessageReqVO reqVO) {
        try {
            // 如果是流式响应，使用SSE
            if ("streaming".equals(reqVO.getResponseMode())) {
                return success(null); // 流式响应会通过SSE发送，这里返回空
            }
            
            // 确保responseMode参数存在
            if (reqVO.getResponseMode() == null || reqVO.getResponseMode().isEmpty()) {
                reqVO.setResponseMode("blocking"); // 默认设置为blocking模式
            }
            
            // 阻塞式响应
            ChatMessageRespVO respVO = fileDifyChatService.sendChatMessage(reqVO);
            return success(respVO);
        } catch (DifyApiException e) {
            // 处理Dify API异常
            log.error("[sendChatMessage] Dify API调用异常: {}", e.toString());
            return error(INTERNAL_SERVER_ERROR.getCode(), "AI服务调用失败: " + e.getMessage());
        } catch (Exception e) {
            // 处理其他异常
            log.error("[sendChatMessage] 发送聊天消息异常", e);
            return error(INTERNAL_SERVER_ERROR.getCode(), "发送消息失败: " + e.getMessage());
        }
    }

    @GetMapping("/message/stream")
    @Operation(summary = "发送流式聊天消息")
    @PreAuthorize("@ss.hasPermission('infra:dify-chat:send')")
    public SseEmitter sendChatMessageStream(@Valid ChatMessageReqVO reqVO) {
        // 创建SSE发射器，超时时间设置为30分钟
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L);
        
        try {
            // 参数验证
            if (reqVO.getQuery() == null || reqVO.getQuery().trim().isEmpty()) {
                emitter.send(SseEmitter.event()
                        .name("error")
                        .data("查询内容不能为空"));
                emitter.complete();
                return emitter;
            }
            
            if (reqVO.getUser() == null || reqVO.getUser().trim().isEmpty()) {
                emitter.send(SseEmitter.event()
                        .name("error")
                        .data("用户ID不能为空"));
                emitter.complete();
                return emitter;
            }
            
            // 设置响应模式为流式
            reqVO.setResponseMode("streaming");
            
            // 创建流式监听器
            FileDifyChatService.ChatMessageStreamListener listener = new FileDifyChatService.ChatMessageStreamListener() {
                @Override
                public void onMessage(String messageId, String conversationId, String content) {
                    try {
                        // 发送消息片段
                        emitter.send(SseEmitter.event()
                                .id(messageId)
                                .name("message")
                                .data(content));
                    } catch (Exception e) {
                        log.error("[sendChatMessageStream] 发送消息片段异常", e);
                        emitter.completeWithError(e);
                    }
                }

                @Override
                public void onError(String error) {
                    try {
                        // 发送错误信息
                        emitter.send(SseEmitter.event()
                                .name("error")
                                .data(error));
                        emitter.complete();
                    } catch (Exception e) {
                        log.error("[sendChatMessageStream] 发送错误信息异常", e);
                        emitter.completeWithError(e);
                    }
                }

                @Override
                public void onComplete(ChatMessageRespVO messageResp) {
                    try {
                        // 发送完整消息
                        emitter.send(SseEmitter.event()
                                .name("complete")
                                .data(messageResp));
                        emitter.complete();
                    } catch (Exception e) {
                        log.error("[sendChatMessageStream] 发送完整消息异常", e);
                        emitter.completeWithError(e);
                    }
                }
            };
            
            // 设置完成回调
            emitter.onCompletion(() -> {
                log.debug("[sendChatMessageStream] SSE完成");
            });
            
            // 设置超时回调
            emitter.onTimeout(() -> {
                log.warn("[sendChatMessageStream] SSE超时");
                try {
                    emitter.send(SseEmitter.event()
                            .name("error")
                            .data("连接超时，请重试"));
                    emitter.complete();
                } catch (Exception e) {
                    log.error("[sendChatMessageStream] 发送超时信息异常", e);
                }
            });
            
            // 设置错误回调
            emitter.onError(throwable -> {
                log.error("[sendChatMessageStream] SSE错误", throwable);
            });
            
            // 异步发送消息
            fileDifyChatService.sendChatMessageStream(reqVO, listener);
        } catch (Exception e) {
            log.error("[sendChatMessageStream] 处理流式聊天消息异常", e);
            try {
                emitter.send(SseEmitter.event()
                        .name("error")
                        .data("处理消息失败: " + e.getMessage()));
                emitter.complete();
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        }
        
        return emitter;
    }

    @GetMapping("/conversations")
    @Operation(summary = "获取会话列表")
    @PreAuthorize("@ss.hasPermission('infra:dify-chat:query')")
    public CommonResult<ConversationListRespDTO> getConversations(
            @RequestParam("user") String user,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        ConversationListRespDTO result = fileDifyChatService.getConversations(user, page, limit);
        return success(result);
    }

    @PutMapping("/conversations/{conversationId}/rename")
    @Operation(summary = "重命名会话")
    @Parameter(name = "conversationId", description = "会话编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('infra:dify-chat:update')")
    public CommonResult<Boolean> renameConversation(
            @PathVariable("conversationId") String conversationId,
            @RequestParam("name") String name,
            @RequestParam("user") String user) {
        boolean result = fileDifyChatService.renameConversation(conversationId, name, user);
        return success(result);
    }

    @DeleteMapping("/conversations/{conversationId}")
    @Operation(summary = "删除会话")
    @Parameter(name = "conversationId", description = "会话编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('infra:dify-chat:delete')")
    public CommonResult<Boolean> deleteConversation(
            @PathVariable("conversationId") String conversationId,
            @RequestParam("user") String user) {
        boolean result = fileDifyChatService.deleteConversation(conversationId, user);
        return success(result);
    }

    @GetMapping("/messages")
    @Operation(summary = "获取会话消息历史")
    @PreAuthorize("@ss.hasPermission('infra:dify-chat:query')")
    public CommonResult<MessageHistoryRespDTO> getMessageHistory(
            @RequestParam("conversationId") String conversationId,
            @RequestParam("user") String user,
            @RequestParam(value = "first_id", required = false) String firstId,
            @RequestParam(value = "limit", defaultValue = "20") int limit) {
        MessageHistoryRespDTO result = fileDifyChatService.getMessageHistory(conversationId, user, firstId, limit);
        return success(result);
    }
} 