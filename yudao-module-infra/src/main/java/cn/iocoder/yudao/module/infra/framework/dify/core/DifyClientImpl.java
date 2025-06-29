package cn.iocoder.yudao.module.infra.framework.dify.core;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.iocoder.yudao.module.infra.framework.dify.config.DifyProperties;
import cn.iocoder.yudao.module.infra.framework.dify.dto.request.DifyCreateDatasetRequest;
import cn.iocoder.yudao.module.infra.framework.dify.dto.response.DifyCreateDatasetResponse;
import cn.iocoder.yudao.module.infra.framework.dify.dto.request.DocumentFileReqDTO;
import cn.iocoder.yudao.module.infra.framework.dify.dto.request.DocumentTextReqDTO;
import cn.iocoder.yudao.module.infra.framework.dify.dto.request.MetadataFieldReqDTO;
import cn.iocoder.yudao.module.infra.framework.dify.dto.request.DocumentMetadataReqDTO;
import cn.iocoder.yudao.module.infra.framework.dify.dto.request.RetrieveReqDTO;
import cn.iocoder.yudao.module.infra.framework.dify.dto.request.ChatMessageReqDTO;
import cn.iocoder.yudao.module.infra.framework.dify.dto.response.*;
import cn.iocoder.yudao.module.infra.framework.dify.exception.DifyApiException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dify 客户端实现类
 */
@Slf4j
@Component
public class DifyClientImpl implements DifyClient {

    @Resource
    private DifyProperties difyProperties;

    @Override
    public DatasetRespDTO createDataset(String name, String permission) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("permission", permission);
        return doPost("/v1/datasets", params, DatasetRespDTO.class);
    }

    @Override
    public DatasetListRespDTO getDatasets(int page, int limit) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("limit", limit);
        return doGet("/v1/datasets", params, DatasetListRespDTO.class);
    }

    @Override
    public void deleteDataset(String datasetId) {
        doDelete("/v1/datasets/" + datasetId, null);
    }

    @Override
    public DocumentRespDTO createDocumentByText(String datasetId, DocumentTextReqDTO request) {
        return doPost("/v1/datasets/" + datasetId + "/document/create_by_text", 
                JSONUtil.toBean(JSONUtil.toJsonStr(request), Map.class), 
                DocumentRespDTO.class);
    }

    @Override
    public DocumentRespDTO updateDocumentByText(String datasetId, String documentId, DocumentTextReqDTO request) {
        return doPost("/v1/datasets/" + datasetId + "/documents/" + documentId + "/update_by_text", 
                JSONUtil.toBean(JSONUtil.toJsonStr(request), Map.class), 
                DocumentRespDTO.class);
    }

    @Override
    public DocumentRespDTO createDocumentByFile(String datasetId, File file, DocumentFileReqDTO metadata) {
        Map<String, Object> form = new HashMap<>();
        if (metadata != null) {
            // 转换metadata为form参数
            // 这里需要将整个metadata对象作为data字段提交
            form.put("data", JSONUtil.toJsonStr(metadata));
        }
        
        // 模拟form表单提交文件
        try {
            String url = difyProperties.getBaseUrl() + "/v1/datasets/" + datasetId + "/document/create-by-file";
            
            log.debug("[createDocumentByFile][准备上传文件，URL: {}, 文件名: {}, 大小: {}]", 
                     url, file.getName(), file.length());
            
            // 使用HttpUtil来创建请求对象
            HttpRequest request = HttpUtil.createPost(url)
                    .header("Authorization", "Bearer " + difyProperties.getApiKey());
            
            // 设置超时
            request.timeout(difyProperties.getConnectTimeout());
            request.setReadTimeout(difyProperties.getReadTimeout());
            
            // 将所有表单字段一次性添加，而不是分两次调用
            HashMap<String, Object> formMap = new HashMap<>();
            if (form.containsKey("data")) {
                String metadataJson = form.get("data").toString();
                log.debug("[createDocumentByFile][data参数: {}]", metadataJson);
                formMap.put("data", metadataJson);
            }
            formMap.put("file", file);
            
            // 一次性添加所有表单数据
            request.form(formMap);
            
            HttpResponse response = request.execute();
            String responseBody = response.body();
            
            // 处理响应
            if (response.isOk()) {
                JSONObject jsonObject = JSONUtil.parseObj(responseBody);
                // 检查是否包含data字段
                if (jsonObject.containsKey("document")) {
                    return JSONUtil.toBean(jsonObject.getJSONObject("document").toString(), DocumentRespDTO.class);
                }
                return JSONUtil.toBean(responseBody, DocumentRespDTO.class);
            } else {
                handleErrorResponse(response.getStatus(), responseBody);
                return null; // 这行代码不会执行，因为handleErrorResponse会抛出异常
            }
        } catch (Exception e) {
            if (e instanceof DifyApiException) {
                throw (DifyApiException) e;
            }
            log.error("创建文件文档失败", e);
            throw new DifyApiException("创建文件文档失败: " + e.getMessage());
        }
    }

    @Override
    public DocumentRespDTO updateDocumentByFile(String datasetId, String documentId, File file, DocumentFileReqDTO metadata) {
        Map<String, Object> form = new HashMap<>();
        if (metadata != null) {
            // 转换metadata为form参数
            // 这里需要将整个metadata对象作为data字段提交
            form.put("data", JSONUtil.toJsonStr(metadata));
        }
        
        // 模拟form表单提交文件
        try {
            String url = difyProperties.getBaseUrl() + "/v1/datasets/" + datasetId + "/documents/" + documentId + "/update-by-file";
            
            log.debug("[updateDocumentByFile][准备上传文件，URL: {}, 文件名: {}, 大小: {}]", 
                     url, file.getName(), file.length());
            
            // 使用HttpUtil来创建请求对象
            HttpRequest request = HttpUtil.createPost(url)
                    .header("Authorization", "Bearer " + difyProperties.getApiKey());
            
            // 设置超时
            request.timeout(difyProperties.getConnectTimeout());
            request.setReadTimeout(difyProperties.getReadTimeout());
            
            // 将所有表单字段一次性添加，而不是分两次调用
            HashMap<String, Object> formMap = new HashMap<>();
            if (form.containsKey("data")) {
                String metadataJson = form.get("data").toString();
                log.debug("[updateDocumentByFile][data参数: {}]", metadataJson);
                formMap.put("data", metadataJson);
            }
            formMap.put("file", file);
            
            // 一次性添加所有表单数据
            request.form(formMap);
            
            HttpResponse response = request.execute();
            String responseBody = response.body();
            
            // 处理响应
            if (response.isOk()) {
                JSONObject jsonObject = JSONUtil.parseObj(responseBody);
                // 检查是否包含document字段
                if (jsonObject.containsKey("document")) {
                    return JSONUtil.toBean(jsonObject.getJSONObject("document").toString(), DocumentRespDTO.class);
                }
                return JSONUtil.toBean(responseBody, DocumentRespDTO.class);
            } else {
                handleErrorResponse(response.getStatus(), responseBody);
                return null; // 这行代码不会执行，因为handleErrorResponse会抛出异常
            }
        } catch (Exception e) {
            if (e instanceof DifyApiException) {
                throw (DifyApiException) e;
            }
            log.error("更新文件文档失败", e);
            throw new DifyApiException("更新文件文档失败: " + e.getMessage());
        }
    }

    @Override
    public void deleteDocument(String datasetId, String documentId) {
        doDelete("/v1/datasets/" + datasetId + "/documents/" + documentId, null);
    }

    @Override
    public DocumentListRespDTO getDocuments(String datasetId, int page, int limit) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("limit", limit);
        return doGet("/v1/datasets/" + datasetId + "/documents", params, DocumentListRespDTO.class);
    }

    @Override
    public IndexingStatusRespDTO getDocumentIndexingStatus(String datasetId, String batch) {
        return doGet("/v1/datasets/" + datasetId + "/documents/" + batch + "/indexing-status", null, IndexingStatusRespDTO.class);
    }

    @Override
    public RetrieveRespDTO retrieveFromDataset(String datasetId, RetrieveReqDTO request) {
        return doPost("/v1/datasets/" + datasetId + "/retrieve", 
                JSONUtil.toBean(JSONUtil.toJsonStr(request), Map.class), 
                RetrieveRespDTO.class);
    }

    @Override
    public MetadataRespDTO addMetadataField(String datasetId, MetadataFieldReqDTO request) {
        return doPost("/v1/datasets/" + datasetId + "/metadata", 
                JSONUtil.toBean(JSONUtil.toJsonStr(request), Map.class), 
                MetadataRespDTO.class);
    }

    @Override
    public MetadataRespDTO updateMetadataField(String datasetId, String metadataId, MetadataFieldReqDTO request) {
        return doPatch("/v1/datasets/" + datasetId + "/metadata/" + metadataId, 
                JSONUtil.toBean(JSONUtil.toJsonStr(request), Map.class), 
                MetadataRespDTO.class);
    }

    @Override
    public void deleteMetadataField(String datasetId, String metadataId) {
        doDelete("/v1/datasets/" + datasetId + "/metadata/" + metadataId, null);
    }

    @Override
    public void setBuiltInFieldStatus(String datasetId, String action) {
        doDelete("/v1/datasets/" + datasetId + "/metadata/built-in/" + action, null);
    }

    @Override
    public void updateDocumentMetadata(String datasetId, List<DocumentMetadataReqDTO> operations) {
        doPost("/v1/datasets/" + datasetId + "/documents/metadata", 
                Collections.singletonMap("operations", operations), 
                Void.class);
    }

    @Override
    public MetadataListRespDTO getMetadataFields(String datasetId) {
        return doGet("/v1/datasets/" + datasetId + "/metadata-fields", null, MetadataListRespDTO.class);
    }

    /**
     * 创建数据集
     * 
     * @param request 创建数据集请求
     * @return 创建的数据集ID
     */
    @Override
    public String createDataset(DifyCreateDatasetRequest request) {
        log.info("[createDataset][开始创建数据集({})][indexingTechnique: {}]", request.getName(), request.getIndexingTechnique());
        
        try {
            // 打印请求详情
            log.debug("[createDataset][请求参数: {}]", JSONUtil.toJsonStr(request));
            
            String endpoint = "/v1/datasets";
            Map<String, Object> params = new HashMap<>();
            params.put("name", request.getName());
            params.put("permission", request.getPermission());
            
            // 添加索引技术参数
            if (StrUtil.isNotEmpty(request.getIndexingTechnique())) {
                params.put("indexing_technique", request.getIndexingTechnique());
            }
            
            DifyCreateDatasetResponse response = doPost(endpoint, params, DifyCreateDatasetResponse.class);
            String datasetId = response.getId();
            log.info("[createDataset][创建数据集成功][datasetId: {}]", datasetId);
            
            return datasetId;
        } catch (Exception e) {
            log.error("[createDataset][创建数据集({})失败]", request.getName(), e);
            throw new DifyApiException(String.format("创建数据集(%s)失败: %s", request.getName(), e.getMessage()), e);
        }
    }

    /**
     * 获取适合当前操作的API密钥
     * 聊天相关API使用chatApiKey，数据集和文档相关API使用apiKey
     *
     * @param endpoint API端点路径
     * @return 适合当前操作的API密钥
     */
    private String getApiKeyForEndpoint(String endpoint) {
        // 聊天相关API使用chatApiKey
        if (endpoint.startsWith("/v1/chat") || 
            endpoint.startsWith("/v1/conversation") || 
            endpoint.startsWith("/v1/messages")) {
            return getChatApiKey();
        }
        // 其他API使用apiKey
        return difyProperties.getApiKey();
    }

    /**
     * 执行GET请求
     * 
     * @param endpoint 接口路径
     * @param params 请求参数
     * @param responseType 响应类型
     * @return 响应对象
     */
    private <T> T doGet(String endpoint, Map<String, Object> params, Class<T> responseType) {
        try {
            HttpRequest request = HttpUtil.createGet(difyProperties.getBaseUrl() + endpoint);
            request.header("Authorization", "Bearer " + getApiKeyForEndpoint(endpoint));
            request.header("Content-Type", "application/json");
            
            // 设置请求参数
            if (CollUtil.isNotEmpty(params)) {
                request.form(params);
            }
            
            // 设置超时
            request.timeout(difyProperties.getConnectTimeout());
            request.setReadTimeout(difyProperties.getReadTimeout());
            
            HttpResponse response = request.execute();
            return handleResponse(response, responseType);
        } catch (Exception e) {
            if (e instanceof DifyApiException) {
                throw (DifyApiException) e;
            }
            log.error("GET请求失败: {}", endpoint, e);
            throw new DifyApiException("GET请求失败: " + e.getMessage());
        }
    }

    /**
     * 执行POST请求
     * 
     * @param endpoint 接口路径
     * @param body 请求体
     * @param responseType 响应类型
     * @return 响应对象
     */
    private <T> T doPost(String endpoint, Object body, Class<T> responseType) {
        try {
            String apiKey = getApiKeyForEndpoint(endpoint);
            log.debug("[doPost][开始请求 endpoint: {}, apiKey: {}, body: {}]", 
                      endpoint, 
                      StrUtil.hide(apiKey, 3, apiKey.length() - 3),
                      JSONUtil.toJsonStr(body));
            
            HttpRequest request = HttpUtil.createPost(difyProperties.getBaseUrl() + endpoint);
            request.header("Authorization", "Bearer " + apiKey);
            request.header("Content-Type", "application/json");
            
            // 设置请求体
            if (body != null) {
                String jsonBody = JSONUtil.toJsonStr(body);
                request.body(jsonBody);
            }
            
            // 设置超时
            request.timeout(difyProperties.getConnectTimeout());
            request.setReadTimeout(difyProperties.getReadTimeout());
            
            HttpResponse response = request.execute();
            log.debug("[doPost][请求完成 endpoint: {}, status: {}]", endpoint, response.getStatus());
            
            return handleResponse(response, responseType);
        } catch (Exception e) {
            if (e instanceof DifyApiException) {
                throw (DifyApiException) e;
            }
            log.error("[doPost][请求失败: endpoint: {}, body: {}, error: {}]", 
                      endpoint, 
                      JSONUtil.toJsonStr(body), 
                      e.getMessage());
            throw new DifyApiException("POST请求失败: " + e.getMessage());
        }
    }

    /**
     * 执行PATCH请求
     * 
     * @param endpoint 接口路径
     * @param body 请求体
     * @param responseType 响应类型
     * @return 响应对象
     */
    private <T> T doPatch(String endpoint, Object body, Class<T> responseType) {
        try {
            HttpRequest request = HttpUtil.createRequest(Method.PATCH, difyProperties.getBaseUrl() + endpoint);
            request.header("Authorization", "Bearer " + getApiKeyForEndpoint(endpoint));
            request.header("Content-Type", "application/json");
            
            // 设置请求体
            if (body != null) {
                String jsonBody = JSONUtil.toJsonStr(body);
                request.body(jsonBody);
            }
            
            // 设置超时
            request.timeout(difyProperties.getConnectTimeout());
            request.setReadTimeout(difyProperties.getReadTimeout());
            
            HttpResponse response = request.execute();
            return handleResponse(response, responseType);
        } catch (Exception e) {
            if (e instanceof DifyApiException) {
                throw (DifyApiException) e;
            }
            log.error("PATCH请求失败: {}", endpoint, e);
            throw new DifyApiException("PATCH请求失败: " + e.getMessage());
        }
    }

    /**
     * 执行DELETE请求
     * 
     * @param endpoint 接口路径
     * @param body 请求体
     */
    private void doDelete(String endpoint, Object body) {
        try {
            HttpRequest request = HttpUtil.createRequest(Method.DELETE, difyProperties.getBaseUrl() + endpoint);
            request.header("Authorization", "Bearer " + getApiKeyForEndpoint(endpoint));
            request.header("Content-Type", "application/json");
            
            // 设置请求体
            if (body != null) {
                String jsonBody = JSONUtil.toJsonStr(body);
                request.body(jsonBody);
            }
            
            // 设置超时
            request.timeout(difyProperties.getConnectTimeout());
            request.setReadTimeout(difyProperties.getReadTimeout());
            
            HttpResponse response = request.execute();
            
            // 处理响应
            if (!response.isOk()) {
                handleErrorResponse(response.getStatus(), response.body());
            }
        } catch (Exception e) {
            if (e instanceof DifyApiException) {
                throw (DifyApiException) e;
            }
            log.error("DELETE请求失败: {}", endpoint, e);
            throw new DifyApiException("DELETE请求失败: " + e.getMessage());
        }
    }

    /**
     * 处理HTTP响应
     * 
     * @param response HTTP响应
     * @param responseType 响应类型
     * @return 响应对象
     */
    private <T> T handleResponse(HttpResponse response, Class<T> responseType) {
        try {
            String responseBody = response.body();
            
            // 处理成功响应
            if (response.isOk()) {
                if (responseType == Void.class) {
                    return null;
                }
                
                // 为了调试，记录原始响应
                log.debug("[handleResponse][原始响应: {}]", responseBody);
                
                JSONObject jsonObject = JSONUtil.parseObj(responseBody);
                
                // 特殊处理DocumentRespDTO类型
                if (responseType == DocumentRespDTO.class) {
                    // 检查是否包含document字段（文件上传API）
                    if (jsonObject.containsKey("document")) {
                        DocumentRespDTO result = JSONUtil.toBean(jsonObject.getJSONObject("document").toString(), DocumentRespDTO.class);
                        // 如果包含batch字段，设置到结果中
                        if (jsonObject.containsKey("batch")) {
                            result.setBatch(jsonObject.getStr("batch"));
                        }
                        return (T) result;
                    }
                }
                
                // 检查是否包含data字段
                if (jsonObject.containsKey("data")) {
                    // 判断data字段是对象还是数组
                    Object dataObject = jsonObject.get("data");
                    if (dataObject instanceof cn.hutool.json.JSONArray) {
                        // data是数组，直接使用整个响应进行转换
                        return JSONUtil.toBean(responseBody, responseType);
                    } else if (dataObject instanceof cn.hutool.json.JSONObject) {
                        // data是对象，提取data对象进行转换
                        return JSONUtil.toBean(jsonObject.getJSONObject("data").toString(), responseType);
                    } else {
                        // 其他情况，尝试整个响应转换
                        log.warn("[handleResponse][未识别的data类型: {}，尝试直接转换整个响应]", dataObject.getClass().getName());
                        return JSONUtil.toBean(responseBody, responseType);
                    }
                }
                return JSONUtil.toBean(responseBody, responseType);
            } else {
                // 处理错误响应
                handleErrorResponse(response.getStatus(), responseBody);
                return null; // 这行代码不会执行，因为handleErrorResponse会抛出异常
            }
        } catch (Exception e) {
            if (e instanceof DifyApiException) {
                throw (DifyApiException) e;
            }
            log.error("处理响应失败", e);
            throw new DifyApiException("处理响应失败: " + e.getMessage());
        } finally {
            IoUtil.close(response);
        }
    }

    /**
     * 处理错误响应
     * 
     * @param statusCode HTTP状态码
     * @param responseBody 响应体
     */
    private void handleErrorResponse(int statusCode, String responseBody) {
        try {
            // 先检查响应是否为JSON格式
            if (responseBody.trim().startsWith("{") && responseBody.trim().endsWith("}")) {
                // 是JSON格式，进行正常解析
                JSONObject jsonObject = JSONUtil.parseObj(responseBody, new JSONConfig().setIgnoreNullValue(false));
                String code = jsonObject.getStr("code", "unknown");
                String message = jsonObject.getStr("message", "未知错误");
                
                // 增强特定错误消息的提示
                if ("Missing required parameter in the JSON body".equals(message)) {
                    message = "请求缺少必需参数，请确保包含 'query'、'user' 和 'responseMode' 参数。前端请求时需要明确设置 responseMode: 'blocking' 或 'streaming'";
                } else if (message.contains("Access token is invalid")) {
                    message = "Dify API访问令牌无效，请检查配置中的API密钥是否正确设置";
                } else if (message.contains("rate limit")) {
                    message = "已达到Dify API请求频率限制，请稍后再试";
                }
                
                throw new DifyApiException(statusCode, code, message);
            } else {
                // 非JSON格式响应，如HTML页面
                String errorMessage = statusCode + " - " + (responseBody.length() > 200 ? 
                        responseBody.substring(0, 200) + "..." : responseBody);
                log.error("[handleErrorResponse] 收到非JSON格式错误响应: {}", errorMessage);
                throw new DifyApiException(statusCode, "non_json_response", 
                        "接收到非JSON格式响应: " + statusCode);
            }
        } catch (Exception e) {
            if (e instanceof DifyApiException) {
                throw (DifyApiException) e;
            }
            log.error("解析错误响应失败: {} - {}", statusCode, responseBody, e);
            throw new DifyApiException(statusCode, "parse_error", "解析错误响应失败: " + e.getMessage());
        }
    }

    // 聊天相关API实现

    @Override
    public ChatMessageRespDTO sendChatMessage(ChatMessageReqDTO request) {
        Map<String, Object> params = buildChatMessageParams(request);
        return doPostWithChatApiKey("/v1/chat-messages", params, ChatMessageRespDTO.class);
    }

    @Override
    public void sendChatMessageStream(ChatMessageReqDTO request, ChatMessageStreamListener listener) {
        String url = difyProperties.getBaseUrl() + "/v1/chat-messages";
        Map<String, Object> params = buildChatMessageParams(request);
        params.put("response_mode", "streaming");

        try {
            HttpRequest httpRequest = HttpRequest.post(url)
                    .header("Authorization", "Bearer " + getChatApiKey())
                    .header("Content-Type", "application/json")
                    .header("Accept", "text/event-stream")
                    .body(JSONUtil.toJsonStr(params))
                    .timeout(difyProperties.getReadTimeout());

            HttpResponse response = httpRequest.execute();
            if (response.isOk()) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.bodyStream()))) {
                    String line;
                    StringBuilder fullContent = new StringBuilder();
                    String messageId = null;
                    String conversationId = null;

                    while ((line = reader.readLine()) != null) {
                        if (line.isEmpty()) {
                            continue;
                        }

                        if (line.startsWith("data: ")) {
                            String jsonData = line.substring(6);
                            if ("[DONE]".equals(jsonData)) {
                                // 流结束
                                ChatMessageRespDTO respDTO = new ChatMessageRespDTO();
                                respDTO.setId(messageId);
                                respDTO.setConversationId(conversationId);
                                respDTO.setAnswer(fullContent.toString());
                                listener.onComplete(respDTO);
                                break;
                            }

                            try {
                                JSONObject data = JSONUtil.parseObj(jsonData);
                                String event = data.getStr("event");
                                if ("message".equals(event)) {
                                    JSONObject message = data.getJSONObject("message");
                                    if (messageId == null) {
                                        messageId = message.getStr("id");
                                    }
                                    if (conversationId == null) {
                                        conversationId = message.getStr("conversation_id");
                                    }
                                    String content = message.getStr("answer");
                                    fullContent.append(content);
                                    listener.onMessage(messageId, conversationId, content);
                                }
                            } catch (Exception e) {
                                log.error("[sendChatMessageStream] 解析流数据异常", e);
                                listener.onError("解析流数据异常: " + e.getMessage());
                            }
                        }
                    }
                }
            } else {
                String errorMsg = "请求失败，状态码: " + response.getStatus();
                log.error("[sendChatMessageStream] {}", errorMsg);
                listener.onError(errorMsg);
            }
        } catch (Exception e) {
            log.error("[sendChatMessageStream] 发送流式聊天消息异常", e);
            listener.onError("发送流式聊天消息异常: " + e.getMessage());
            throw new DifyApiException("发送流式聊天消息失败", e);
        }
    }

    @Override
    public ConversationListRespDTO getConversations(String user, int page, int limit) {
        Map<String, Object> params = new HashMap<>();
        params.put("user", user);
        params.put("page", page);
        params.put("limit", limit);
        return doGetWithChatApiKey("/v1/conversations", params, ConversationListRespDTO.class);
    }

    @Override
    public boolean renameConversation(String conversationId, String name, String user) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("user", user);

        try {
            doPatchWithChatApiKey("/v1/conversations/" + conversationId, params, Object.class);
            return true;
        } catch (Exception e) {
            log.error("[renameConversation] 重命名会话异常", e);
            return false;
        }
    }

    @Override
    public boolean deleteConversation(String conversationId, String user) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("user", user);
            doDeleteWithChatApiKey("/v1/conversations/" + conversationId, params);
            return true;
        } catch (Exception e) {
            log.error("[deleteConversation] 删除会话异常", e);
            return false;
        }
    }

    @Override
    public MessageHistoryRespDTO getMessageHistory(String conversationId, String user, String firstId, int limit) {
        Map<String, Object> params = new HashMap<>();
        // Keep using conversation_id for the Dify API, which expects snake_case
        params.put("conversation_id", conversationId);
        params.put("user", user);
        params.put("limit", limit);
        
        if (StrUtil.isNotBlank(firstId)) {
            params.put("first_id", firstId);
        }
        
        return doGetWithChatApiKey("/v1/messages", params, MessageHistoryRespDTO.class);
    }

    /**
     * 构建聊天消息请求参数
     */
    private Map<String, Object> buildChatMessageParams(ChatMessageReqDTO request) {
        Map<String, Object> params = new HashMap<>();
        params.put("query", request.getQuery());
        params.put("user", request.getUser());
        params.put("response_mode", request.getResponseMode());
        
        // 确保inputs字段始终存在，即使为空也发送一个空对象
        if (request.getInputs() != null && !request.getInputs().isEmpty()) {
            params.put("inputs", request.getInputs());
        } else {
            params.put("inputs", new HashMap<>());
        }
        
        if (StrUtil.isNotBlank(request.getConversationId())) {
            params.put("conversation_id", request.getConversationId());
        }
        
        if (request.getEnableSearch() != null) {
            params.put("enable_search", request.getEnableSearch());
        }
        
        if (StrUtil.isNotBlank(request.getSearchKeywords())) {
            params.put("search_keywords", request.getSearchKeywords());
        }
        
        if (request.getReturnReferences() != null) {
            params.put("return_references", request.getReturnReferences());
        }
        
        return params;
    }

    /**
     * 获取聊天API密钥
     * 如果chatApiKey为空，则回退使用apiKey
     *
     * @return 聊天API密钥
     */
    private String getChatApiKey() {
        if (StrUtil.isNotBlank(difyProperties.getChatApiKey())) {
            return difyProperties.getChatApiKey();
        }
        // 回退使用apiKey
        return difyProperties.getApiKey();
    }

    /**
     * 使用聊天API密钥进行GET请求
     */
    private <T> T doGetWithChatApiKey(String endpoint, Map<String, Object> params, Class<T> responseType) {
        String url = difyProperties.getBaseUrl() + endpoint;
        
        // 构建查询参数
        if (params != null && !params.isEmpty()) {
            StringBuilder queryString = new StringBuilder();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (queryString.length() > 0) {
                    queryString.append("&");
                } else {
                    queryString.append("?");
                }
                queryString.append(entry.getKey()).append("=").append(entry.getValue());
            }
            url += queryString.toString();
        }
        
        try {
            HttpRequest request = HttpUtil.createGet(url);
            request.header("Authorization", "Bearer " + getChatApiKey());
            request.header("Content-Type", "application/json");
            
            // 设置超时
            request.timeout(difyProperties.getConnectTimeout());
            request.setReadTimeout(difyProperties.getReadTimeout());
            
            HttpResponse response = request.execute();
            return handleResponse(response, responseType);
        } catch (Exception e) {
            if (e instanceof DifyApiException) {
                throw (DifyApiException) e;
            }
            log.error("GET请求异常: {}", url, e);
            throw new DifyApiException("GET请求异常: " + e.getMessage());
        }
    }

    /**
     * 使用聊天API密钥进行POST请求
     */
    private <T> T doPostWithChatApiKey(String endpoint, Object body, Class<T> responseType) {
        String url = difyProperties.getBaseUrl() + endpoint;
        
        try {
            HttpRequest request = HttpUtil.createPost(url);
            request.header("Authorization", "Bearer " + getChatApiKey());
            request.header("Content-Type", "application/json");
            
            // 设置超时
            request.timeout(difyProperties.getConnectTimeout());
            request.setReadTimeout(difyProperties.getReadTimeout());
            
            // 设置请求体
            if (body != null) {
                if (body instanceof String) {
                    request.body((String) body);
                } else {
                    request.body(JSONUtil.toJsonStr(body));
                }
            }
            
            HttpResponse response = request.execute();
            return handleResponse(response, responseType);
        } catch (Exception e) {
            if (e instanceof DifyApiException) {
                throw (DifyApiException) e;
            }
            log.error("POST请求异常: {}", url, e);
            throw new DifyApiException("POST请求异常: " + e.getMessage());
        }
    }

    /**
     * 使用聊天API密钥进行PATCH请求
     */
    private <T> T doPatchWithChatApiKey(String endpoint, Object body, Class<T> responseType) {
        String url = difyProperties.getBaseUrl() + endpoint;
        
        try {
            HttpRequest request = HttpUtil.createRequest(Method.PATCH, url);
            request.header("Authorization", "Bearer " + getChatApiKey());
            request.header("Content-Type", "application/json");
            
            // 设置超时
            request.timeout(difyProperties.getConnectTimeout());
            request.setReadTimeout(difyProperties.getReadTimeout());
            
            // 设置请求体
            if (body != null) {
                if (body instanceof String) {
                    request.body((String) body);
                } else {
                    request.body(JSONUtil.toJsonStr(body));
                }
            }
            
            HttpResponse response = request.execute();
            return handleResponse(response, responseType);
        } catch (Exception e) {
            if (e instanceof DifyApiException) {
                throw (DifyApiException) e;
            }
            log.error("PATCH请求异常: {}", url, e);
            throw new DifyApiException("PATCH请求异常: " + e.getMessage());
        }
    }

    /**
     * 使用聊天API密钥进行DELETE请求
     */
    private void doDeleteWithChatApiKey(String endpoint, Object body) {
        String url = difyProperties.getBaseUrl() + endpoint;
        
        try {
            HttpRequest request = HttpUtil.createRequest(Method.DELETE, url);
            request.header("Authorization", "Bearer " + getChatApiKey());
            request.header("Content-Type", "application/json");
            
            // 设置超时
            request.timeout(difyProperties.getConnectTimeout());
            request.setReadTimeout(difyProperties.getReadTimeout());
            
            // 设置请求体
            if (body != null) {
                if (body instanceof String) {
                    request.body((String) body);
                } else {
                    request.body(JSONUtil.toJsonStr(body));
                }
            }
            
            HttpResponse response = request.execute();
            if (!response.isOk()) {
                handleErrorResponse(response.getStatus(), response.body());
            }
        } catch (Exception e) {
            if (e instanceof DifyApiException) {
                throw (DifyApiException) e;
            }
            log.error("DELETE请求异常: {}", url, e);
            throw new DifyApiException("DELETE请求异常: " + e.getMessage());
        }
    }
} 