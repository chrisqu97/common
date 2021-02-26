package cn.com.qucl.common.util.excel;

import cn.com.qucl.common.util.excel.ExcelTypeEnum;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @author qucl
 * @date 2019/5/21 19:03
 * 数据流转换为MultipartFile
 */
public class ExcelMultipartFile implements MultipartFile {

    private String name;
    private final byte[] bytes;
    private ExcelTypeEnum excelTypeEnum;

    public ExcelMultipartFile(String name, byte[] bytes, ExcelTypeEnum excelTypeEnum) {
        this.name = name;
        this.bytes = bytes;
        this.excelTypeEnum = excelTypeEnum;
    }

    @Override
    public String getName() {
        return name + "." + excelTypeEnum.getValue();
    }

    @Override
    public String getOriginalFilename() {
        return name + "." + excelTypeEnum.getValue();
    }

    @Override
    public String getContentType() {
        return this.excelTypeEnum.getValue();
    }

    @Override
    public boolean isEmpty() {
        return bytes == null || bytes.length == 0;
    }

    @Override
    public long getSize() {
        return bytes.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return bytes;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public void transferTo(File file) throws IOException, IllegalStateException {
        new FileOutputStream(file).write(bytes);
    }
}