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
import cn.iocoder.yudao.module.infra.framework.dify.dto.response.*;
import cn.iocoder.yudao.module.infra.framework.dify.exception.DifyApiException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
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
            HttpRequest request = HttpUtil.createPost(difyProperties.getBaseUrl() + "/v1/datasets/" + datasetId + "/document/create-by-file")
                    .header("Authorization", "Bearer " + difyProperties.getApiKey())
                    .form("file", file);
            
            // 添加data表单参数
            if (form.containsKey("data")) {
                request.form("data", form.get("data"), "text/plain");
            }
            
            // 设置超时
            request.timeout(difyProperties.getConnectTimeout());
            request.setReadTimeout(difyProperties.getReadTimeout());
            
            HttpResponse response = request.execute();
            String responseBody = response.body();
            
            // 处理响应
            if (response.isOk()) {
                JSONObject jsonObject = JSONUtil.parseObj(responseBody);
                // 检查是否包含data字段
                if (jsonObject.containsKey("data")) {
                    return JSONUtil.toBean(jsonObject.getJSONObject("data").toString(), DocumentRespDTO.class);
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
            HttpRequest request = HttpUtil.createPost(difyProperties.getBaseUrl() + "/v1/datasets/" + datasetId + "/documents/" + documentId + "/update-by-file")
                    .header("Authorization", "Bearer " + difyProperties.getApiKey())
                    .form("file", file);
            
            // 添加data表单参数
            if (form.containsKey("data")) {
                request.form("data", form.get("data"), "text/plain");
            }
            
            // 设置超时
            request.timeout(difyProperties.getConnectTimeout());
            request.setReadTimeout(difyProperties.getReadTimeout());
            
            HttpResponse response = request.execute();
            String responseBody = response.body();
            
            // 处理响应
            if (response.isOk()) {
                JSONObject jsonObject = JSONUtil.parseObj(responseBody);
                // 检查是否包含data字段
                if (jsonObject.containsKey("data")) {
                    return JSONUtil.toBean(jsonObject.getJSONObject("data").toString(), DocumentRespDTO.class);
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
        return doGet("/v1/datasets/" + datasetId + "/metadata", null, MetadataListRespDTO.class);
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
        // 检查API密钥格式，若是dataset-开头的数据集级别密钥，给出明确错误提示
        if (StrUtil.startWithIgnoreCase(difyProperties.getApiKey(), "dataset-")) {
            throw new DifyApiException("无法创建知识库：API密钥格式错误，当前使用的是数据集级别API密钥(dataset-*)，创建知识库需要使用应用程序级别API密钥(app-*)");
        }
        
        try {
            // 打印请求详情
            log.debug("[createDataset][请求参数: {}]", JSONUtil.toJsonStr(request));
            
            String endpoint = "/datasets";
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
            request.header("Authorization", "Bearer " + difyProperties.getApiKey());
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
            log.debug("[doPost][开始请求 endpoint: {}, apiKey: {}, body: {}]", 
                      endpoint, 
                      StrUtil.hide(difyProperties.getApiKey(), 3, difyProperties.getApiKey().length() - 3),
                      JSONUtil.toJsonStr(body));
            
            HttpRequest request = HttpUtil.createPost(difyProperties.getBaseUrl() + endpoint);
            request.header("Authorization", "Bearer " + difyProperties.getApiKey());
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
            request.header("Authorization", "Bearer " + difyProperties.getApiKey());
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
            request.header("Authorization", "Bearer " + difyProperties.getApiKey());
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
                
                JSONObject jsonObject = JSONUtil.parseObj(responseBody);
                // 检查是否包含data字段
                if (jsonObject.containsKey("data")) {
                    return JSONUtil.toBean(jsonObject.getJSONObject("data").toString(), responseType);
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
} 