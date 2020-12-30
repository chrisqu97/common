package cn.com.qucl.common.utils;

import cn.com.qucl.common.exceptions.DownloadException;
import cn.com.qucl.common.exceptions.UploadFileException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;

/**
 * @author qucl
 * @date 2018/11/29 14:58
 * http工具
 */
@Slf4j
public class HttpUtils {

    private final RestTemplate restTemplate;

    public HttpUtils(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    /**
     * get/queryParam
     *
     * @param url     地址
     * @param params  参数
     * @param headers 请求头
     * @param type    返回类型
     */
    public <T> T getWithQueryParam(String url, Map<String, Object> params, HttpHeaders headers, Class<T> type) {
        url = addQueryParams(url, params);
        ResponseEntity<T> exchange = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(null, headers), type);
        return exchange.getBody();
    }

    /**
     * form
     *
     * @param url     地址
     * @param params  参数
     * @param method  请求方式
     * @param headers 请求头
     * @param type    返回类型
     */
    private <T> T requestWithForm(String url, Map<String, Object> params, HttpMethod method, HttpHeaders headers, Class<T> type) {
        if (headers == null) {
            headers = new HttpHeaders();
        }
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> form = initForm(params);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(form, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, method, requestEntity, String.class);
        return responseEntity.getBody() != null ? JSON.parseObject(responseEntity.getBody(), type) : null;
    }

    /**
     * get/form
     *
     * @param url     地址
     * @param params  参数
     * @param headers 请求头
     * @param type    返回类型
     */
    public <T> T getWithForm(String url, Map<String, Object> params, HttpHeaders headers, Class<T> type) {
        return requestWithForm(url, params, HttpMethod.GET, headers, type);
    }

    /**
     * post/json
     *
     * @param url     地址
     * @param body    请求报文
     * @param headers 请求头
     * @param type    返回类型
     */
    public <T> T postWithJson(String url, String body, HttpHeaders headers, Class<T> type) {
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
    public <T> T postWithForm(String url, Map<String, Object> params, HttpHeaders headers, Class<T> type) {
        return requestWithForm(url, params, HttpMethod.POST, headers, type);
    }

    /**
     * post/xml
     *
     * @param url     地址
     * @param xml     xml
     * @param headers 请求头
     * @param type    返回类型
     */
    public <T> T postWithXml(String url, String xml, HttpHeaders headers, Class<T> type) {
        if (headers == null) {
            headers = new HttpHeaders();
        }
        headers.setContentType(MediaType.TEXT_XML);
        HttpEntity<String> requestEntity = new HttpEntity<>(xml, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        return responseEntity.getBody() != null ? JSON.parseObject(responseEntity.getBody(), type) : null;
    }

    /**
     * 上传文件
     *
     * @param url     地址
     * @param form    表单
     * @param method  请求方式
     * @param headers 请求头
     * @param type    返回类型
     */
    private <T> T uploadFile(String url, MultiValueMap<String, Object> form, HttpMethod method, HttpHeaders headers, Class<T> type) {
        try {
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            ResponseEntity<String> exchange = restTemplate.exchange(url, method, new HttpEntity<>(form, headers), String.class);
            return exchange.getBody() != null ? JSON.parseObject(exchange.getBody(), type) : null;
        } catch (UploadFileException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UploadFileException(e.getMessage());
        }
    }

    /**
     * 上传文件
     *
     * @param url     地址
     * @param file    文件
     * @param formKey file的表单key 默认为 file
     * @param params  参数
     * @param method  请求方式
     * @param headers 请求头
     * @param type    返回类型
     */
    public <T> T uploadFile(String url, File file, String formKey, Map<String, Object> params, HttpMethod method, HttpHeaders headers, Class<T> type) {
        MultiValueMap<String, Object> form;
        FileSystemResource resource = new FileSystemResource(file);
        if (CollectionUtils.isNotEmpty(params)) {
            form = new LinkedMultiValueMap<>(params.size() + 1);
            params.forEach((key, value) -> form.add(key, String.valueOf(value)));
        } else {
            form = new LinkedMultiValueMap<>(1);
        }
        if (StringUtils.isBlank(formKey)) {
            form.add("file", resource);
        } else {
            form.add(formKey, resource);
        }
        return uploadFile(url, form, method, headers, type);
    }

    /**
     * 上传文件
     *
     * @param url     地址
     * @param formKey file的表单key 默认为 file
     * @param file    上传的文件
     * @param params  参数
     * @param method  请求方式
     * @param headers 请求头
     * @param type    返回类型
     */
    public <T> T uploadFile(String url, MultipartFile file, String formKey, Map<String, Object> params, HttpMethod method, HttpHeaders headers, Class<T> type) {
        File tempFile = null;
        try {
            String fileName = file.getOriginalFilename();
            if (StringUtils.isBlank(fileName)) {
                throw new UploadFileException("上传的文件名为空");
            }
            String prefix = fileName.substring(fileName.lastIndexOf("."));
            String filename = fileName.replace(prefix, "");
            tempFile = File.createTempFile(filename, prefix);
            file.transferTo(tempFile);
            return uploadFile(url, tempFile, formKey, params, method, headers, type);
        } catch (UploadFileException e) {
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UploadFileException(e.getMessage());
        } finally {
            if (tempFile != null) {
                if (tempFile.delete()) {
                    log.info("临时文件已删除");
                }
            }
        }
    }


    /**
     * 下载文件/输出到游览器
     *
     * @param url      地址
     * @param fileName 文件名
     * @param params   参数
     * @param headers  请求头
     */
    public void download(String url, String fileName, Map<String, Object> params, HttpHeaders headers, HttpMethod method, HttpServletRequest request, HttpServletResponse response) {
        OutputStream outputStream = null;
        try {
            response.setHeader("x-frame-options", "AllowAll");
            response.addHeader("Access-Control-Allow-Origin", "*");
            response.setContentType("application/octet-stream;charset=UTF-8");
            //在浏览器中弹出窗口,给文件名编码,防止中文乱码,区分火狐浏览器和非火狐浏览器
            if (request.getHeader("USER-AGENT").toLowerCase().contains("firefox")) {
                //如果是火狐浏览器,则使用下面的方式为excel文件编码
                response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("GB2312"), StandardCharsets.ISO_8859_1.name()));
            } else {
                response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
            }
            //设置客户端不缓存
            //addHeader增加头文件里没有的属性
            response.addHeader("Pragma", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            outputStream = response.getOutputStream();
            download(url, outputStream, params, headers, method);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new DownloadException(e.getMessage());
        } finally {
            close(outputStream);
        }
    }

    /**
     * 下载文件/输出到文件系统
     *
     * @param url      地址
     * @param fileName 文件名
     * @param filePath 输出路径
     * @param params   参数
     * @param headers  请求头
     * @param method   请求方式
     */
    public void download(String url, String fileName, String filePath, Map<String, Object> params, HttpHeaders headers, HttpMethod method) {
        OutputStream outputStream = null;
        File file = null;
        try {
            file = new File(filePath + fileName);
            if (file.exists()) {
                throw new DownloadException("文件已存在");
            } else {
                if (file.createNewFile()) {
                    log.info("文件创建成功");
                }
            }
            outputStream = new FileOutputStream(file);
            download(url, outputStream, params, headers, method);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            if (file != null && file.exists() && file.delete()) {
                log.info("clean file success");
            }
            throw new DownloadException(e.getMessage());
        } finally {
            close(outputStream);
        }
    }

    /**
     * 下载文件/输出
     *
     * @param url          地址
     * @param outputStream 输出
     * @param params       参数
     * @param headers      请求头
     * @param method       请求方式
     */
    public void download(String url, OutputStream outputStream, Map<String, Object> params, HttpHeaders headers, HttpMethod method) {
        InputStream inputStream = null;
        try {
            MultiValueMap<String, Object> form = initForm(params);
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(form, headers);
            ResponseEntity<byte[]> exchange = restTemplate.exchange(url, method, requestEntity, byte[].class);
            byte[] result = exchange.getBody();
            if (result != null) {
                inputStream = new ByteArrayInputStream(result);
                int len;
                byte[] buf = new byte[1024];
                while ((len = inputStream.read(buf, 0, 1024)) != -1) {
                    outputStream.write(buf, 0, len);
                }
                outputStream.flush();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new DownloadException(e.getMessage());
        } finally {
            close(inputStream);
        }
    }

    /**
     * 关闭
     */
    private void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                log.error("请求关闭异常", e);
            }
        }
    }

    /**
     * url添加参数
     *
     * @param url    地址
     * @param params 请求参数
     */
    private String addQueryParams(String url, Map<String, Object> params) {
        StringBuilder stringBuilder = new StringBuilder(url);
        if (CollectionUtils.isNotEmpty(params)) {
            stringBuilder.append("?");
            Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                stringBuilder.append(entry.getKey()).append("=").append(entry.getValue());
                if (iterator.hasNext()) {
                    stringBuilder.append("&");
                }
            }
        }
        return stringBuilder.toString();
    }

    /**
     * @return form
     */
    private MultiValueMap<String, Object> initForm(Map<String, Object> params) {
        MultiValueMap<String, Object> form;
        if (CollectionUtils.isNotEmpty(params)) {
            form = new LinkedMultiValueMap<>(params.size());
            MultiValueMap<String, Object> finalForm = form;
            params.forEach((key, value) -> finalForm.add(key, String.valueOf(value)));
        } else {
            form = new LinkedMultiValueMap<>(0);
        }
        return form;
    }

    /**
     * 获取源端请求ip
     *
     * @param request 请求
     */
    public String getIp(HttpServletRequest request) {
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
