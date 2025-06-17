import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.UUID;

public class DifyApiTest {
    
    private static final String API_BASE_URL = "https://api.dify.ai";
    private static final String API_KEY = "dataset-QQ8Y3Cxvss4RvQXvU7Gem4sb";  // 使用配置文件中的API Key
    
    public static void main(String[] args) {
        testCreateDataset();
        System.out.println("\n");
        listDatasets();
    }
    
    /**
     * 测试创建知识库接口
     */
    private static void testCreateDataset() {
        System.out.println("=== 测试创建知识库接口 ===");
        
        // 生成一个随机的知识库名称，避免冲突
        String datasetName = "Test Dataset " + UUID.randomUUID().toString().substring(0, 8);
        
        try {
            String endpoint = "/v1/datasets";
            String requestBody = "{\"name\": \"" + datasetName + "\", \"permission\": \"only_me\"}";
            
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + endpoint))
                    .header("Authorization", "Bearer " + API_KEY)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            
            System.out.println("请求URL: " + API_BASE_URL + endpoint);
            System.out.println("请求头:");
            System.out.println("  Authorization: Bearer " + API_KEY);
            System.out.println("  Content-Type: application/json");
            System.out.println("请求体: " + requestBody);
            
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            
            System.out.println("响应状态码: " + response.statusCode());
            System.out.println("响应体: " + response.body());
            
            // 预期结果：使用dataset-开头的API密钥，我们原来假设应该会失败，但实际似乎可以成功
            if (response.statusCode() != 200) {
                System.out.println("测试结果: 创建知识库失败，错误响应: " + response.body());
            } else {
                System.out.println("测试结果: 成功创建知识库 - 这表明 dataset- 开头的API密钥实际上可以创建知识库");
            }
            
        } catch (IOException | InterruptedException e) {
            System.err.println("测试创建知识库失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 测试列出知识库接口
     */
    private static void listDatasets() {
        System.out.println("=== 测试列出知识库接口 ===");
        
        try {
            String endpoint = "/v1/datasets?page=1&limit=10";
            
            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_BASE_URL + endpoint))
                    .header("Authorization", "Bearer " + API_KEY)
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();
            
            System.out.println("请求URL: " + API_BASE_URL + endpoint);
            System.out.println("请求头:");
            System.out.println("  Authorization: Bearer " + API_KEY);
            System.out.println("  Content-Type: application/json");
            
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            
            System.out.println("响应状态码: " + response.statusCode());
            System.out.println("响应体: " + response.body());
            
            if (response.statusCode() == 200) {
                System.out.println("测试结果: 成功获取知识库列表");
            } else {
                System.out.println("测试结果: 获取知识库列表失败");
            }
            
        } catch (IOException | InterruptedException e) {
            System.err.println("测试列出知识库失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 