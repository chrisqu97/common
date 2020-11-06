package cn.com.qucl.common.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;

/**
 * @author qucl
 * @date 2018/11/29 14:58
 * http工具
 */
public class HttpUtils {

    private final RestTemplate restTemplate;

    public HttpUtils(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * post/json
     *
     * @param url     地址
     * @param body    请求报文
     * @param headers 请求头
     * @param type    返回类型
     */
    public <T> T postForObjectWithJson(String url, String body, HttpHeaders headers, Class<T> type) {
        if (headers == null) {
            headers = new HttpHeaders();
        }
        headers.setContentType(MediaType.APPLICATION_JSON);
        return restTemplate.postForObject(url, new HttpEntity<>(body, headers), type);
    }

    /**
     * post/form
     *
     * @param url     地址
     * @param params  请求参数
     * @param headers 请求头
     * @param type    返回类型
     */
    public <T> T postForObjectWithForm(String url, Map<String, String> params, Map<String, String> headers, Class<T> type) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        addHeaders(httpHeaders, headers);
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        if (CollectionUtils.isNotEmpty(params)) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                form.add(entry.getKey(), entry.getValue());
            }
        }
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(form, httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        return responseEntity.getBody() != null ? JSON.parseObject(responseEntity.getBody(), type) : null;
    }

    /**
     * post/xml
     *
     * @param url     地址
     * @param xml     xml
     * @param headers 请求头
     * @param type    返回类型
     */
    public <T> T postForObjectWithXml(String url, String xml, HttpHeaders headers, Class<T> type) {
        if (headers == null) {
            headers = new HttpHeaders();
        }
        headers.setContentType(MediaType.TEXT_XML);
        HttpEntity<String> requestEntity = new HttpEntity<>(xml, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        return responseEntity.getBody() != null ? JSON.parseObject(responseEntity.getBody(), type) : null;
    }


    /**
     * 添加请求头
     *
     * @param httpHeaders 请求头
     * @param headers     请求头参数
     */
    public void addHeaders(HttpHeaders httpHeaders, Map<String, String> headers) {
        if (CollectionUtils.isNotEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpHeaders.add(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     * url添加参数
     *
     * @param url    地址
     * @param params 请求参数
     */
    public String addUrlParams(String url, Map<String, String> params) {
        StringBuilder stringBuilder = new StringBuilder(url);
        if (CollectionUtils.isNotEmpty(params)) {
            stringBuilder.append("?");
            Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                stringBuilder.append(entry.getKey()).append("=").append(entry.getValue());
                if (iterator.hasNext()) {
                    stringBuilder.append("&");
                }
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 获取ip
     *
     * @param request 请求
     */
    public String getIP(HttpServletRequest request) {
        for (String header : HEADERS_TO_TRY) {
            String ip = request.getHeader(header);
            if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }

    /**
     * 尝试获取ip的header
     */
    private static final String[] HEADERS_TO_TRY = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR",
            "X-Real-IP"
    };

}
