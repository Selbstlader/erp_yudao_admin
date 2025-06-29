package cn.iocoder.yudao.module.infra.framework.dify.core;

import cn.iocoder.yudao.module.infra.framework.dify.dto.request.DifyCreateDatasetRequest;
import cn.iocoder.yudao.module.infra.framework.dify.dto.request.DocumentFileReqDTO;
import cn.iocoder.yudao.module.infra.framework.dify.dto.request.DocumentTextReqDTO;
import cn.iocoder.yudao.module.infra.framework.dify.dto.request.MetadataFieldReqDTO;
import cn.iocoder.yudao.module.infra.framework.dify.dto.request.DocumentMetadataReqDTO;
import cn.iocoder.yudao.module.infra.framework.dify.dto.request.RetrieveReqDTO;
import cn.iocoder.yudao.module.infra.framework.dify.dto.request.ChatMessageReqDTO;
import cn.iocoder.yudao.module.infra.framework.dify.dto.response.*;

import java.io.File;
import java.util.List;

/**
 * Dify 客户端接口
 */
public interface DifyClient {
    
    // 知识库管理
    
    /**
     * 创建数据集
     *
     * @param request 创建数据集请求
     * @return 创建的数据集ID
     */
    String createDataset(DifyCreateDatasetRequest request);

    DatasetRespDTO createDataset(String name, String permission);

    /**
     * 获取知识库列表
     * 
     * @param page 页码
     * @param limit 每页条数
     * @return 知识库列表
     */
    DatasetListRespDTO getDatasets(int page, int limit);
    
    /**
     * 删除知识库
     * 
     * @param datasetId 知识库ID
     */
    void deleteDataset(String datasetId);
    
    // 文档管理 - 文本方式
    
    /**
     * 通过文本创建文档
     * 
     * @param datasetId 知识库ID
     * @param request 请求参数
     * @return 文档信息
     */
    DocumentRespDTO createDocumentByText(String datasetId, DocumentTextReqDTO request);
    
    /**
     * 通过文本更新文档
     * 
     * @param datasetId 知识库ID
     * @param documentId 文档ID
     * @param request 请求参数
     * @return 文档信息
     */
    DocumentRespDTO updateDocumentByText(String datasetId, String documentId, DocumentTextReqDTO request);
    
    // 文档管理 - 文件方式
    
    /**
     * 通过文件创建文档
     * 
     * @param datasetId 知识库ID
     * @param file 文件
     * @param metadata 元数据
     * @return 文档信息
     */
    DocumentRespDTO createDocumentByFile(String datasetId, File file, DocumentFileReqDTO metadata);
    
    /**
     * 通过文件更新文档
     * 
     * @param datasetId 知识库ID
     * @param documentId 文档ID
     * @param file 文件
     * @param metadata 元数据
     * @return 文档信息
     */
    DocumentRespDTO updateDocumentByFile(String datasetId, String documentId, File file, DocumentFileReqDTO metadata);
    
    // 文档操作
    
    /**
     * 删除文档
     * 
     * @param datasetId 知识库ID
     * @param documentId 文档ID
     */
    void deleteDocument(String datasetId, String documentId);
    
    /**
     * 获取文档列表
     * 
     * @param datasetId 知识库ID
     * @param page 页码
     * @param limit 每页条数
     * @return 文档列表
     */
    DocumentListRespDTO getDocuments(String datasetId, int page, int limit);
    
    // 文档索引状态
    
    /**
     * 获取文档索引状态
     * 
     * @param datasetId 知识库ID
     * @param batch 批处理ID
     * @return 索引状态
     */
    IndexingStatusRespDTO getDocumentIndexingStatus(String datasetId, String batch);
    
    // 检索功能
    
    /**
     * 从知识库检索内容
     * 
     * @param datasetId 知识库ID
     * @param request 检索请求
     * @return 检索结果
     */
    RetrieveRespDTO retrieveFromDataset(String datasetId, RetrieveReqDTO request);
    
    // 元数据管理
    
    /**
     * 添加元数据字段
     * 
     * @param datasetId 知识库ID
     * @param request 元数据字段请求
     * @return 元数据字段
     */
    MetadataRespDTO addMetadataField(String datasetId, MetadataFieldReqDTO request);
    
    /**
     * 更新元数据字段
     * 
     * @param datasetId 知识库ID
     * @param metadataId 元数据ID
     * @param request 元数据字段请求
     * @return 元数据字段
     */
    MetadataRespDTO updateMetadataField(String datasetId, String metadataId, MetadataFieldReqDTO request);
    
    /**
     * 删除元数据字段
     * 
     * @param datasetId 知识库ID
     * @param metadataId 元数据ID
     */
    void deleteMetadataField(String datasetId, String metadataId);
    
    /**
     * 设置内置字段状态
     * 
     * @param datasetId 知识库ID
     * @param action 操作，可选值：enable, disable
     */
    void setBuiltInFieldStatus(String datasetId, String action);
    
    /**
     * 更新文档元数据
     * 
     * @param datasetId 知识库ID
     * @param operations 操作数据
     */
    void updateDocumentMetadata(String datasetId, List<DocumentMetadataReqDTO> operations);
    
    /**
     * 获取元数据字段列表
     * 
     * @param datasetId 知识库ID
     * @return 元数据字段列表
     */
    MetadataListRespDTO getMetadataFields(String datasetId);
    
    // 聊天功能
    
    /**
     * 发送聊天消息（阻塞模式）
     * 
     * @param request 聊天消息请求
     * @return 聊天消息响应
     */
    ChatMessageRespDTO sendChatMessage(ChatMessageReqDTO request);
    
    /**
     * 发送聊天消息（流式模式）
     * 
     * @param request 聊天消息请求
     * @return 聊天消息响应流
     */
    void sendChatMessageStream(ChatMessageReqDTO request, ChatMessageStreamListener listener);
    
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
} 