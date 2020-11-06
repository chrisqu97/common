package cn.com.qucl.common.utils;

import cn.com.qucl.common.annotations.ExcelCol;
import cn.com.qucl.common.annotations.ExcelIgnore;
import cn.com.qucl.common.annotations.ExcelTitle;
import cn.com.qucl.common.enums.ExcelCellTypeEnum;
import cn.com.qucl.common.enums.ExcelTypeEnum;
import cn.com.qucl.common.exceptions.DataConvertException;
import cn.com.qucl.common.exceptions.DataOutputException;
import cn.com.qucl.common.pojo.dto.ExcelMultipartFile;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author qucl
 * @date 2018/11/15 18:21
 * 表格工具
 */
@Data
@Slf4j
@SuppressWarnings("unchecked")
public class ExcelUtils {
    private static final String EMPTY = "";
    private static final String DOT = ".";
    private static final String SLASH = "/";

    private static final String FIRE_FOX = "firefox";


    public static ExcelUtils getInstance() {
        return new ExcelUtils();
    }

    /**
     * 导出excel文件
     *
     * @param fileName     文件名(无后缀)
     * @param dir          目录
     * @param type         excel类型
     * @param dataList     数据
     * @param classToExcel 数据类型（使用@ExcelCol和@ExcelTitle和@ExcelIgnore注解对象）
     */
    public void outputExcelForFile(String fileName, String dir, ExcelTypeEnum type, List<Object> dataList, Class<?> classToExcel) {
        if (StringUtils.isBlank(fileName)) {
            throw new IllegalArgumentException("文件名为空");
        }
        if (StringUtils.isBlank(dir)) {
            throw new IllegalArgumentException("目录为空");
        }

        if (ExcelTypeEnum.V2003.equals(type)) {
            HSSFWorkbook workbook = new HSSFWorkbook();
            outExcel(workbook, dataList, classToExcel);
            outForFile(fileName, dir, workbook, type);
        } else if (ExcelTypeEnum.V2007.equals(type) || ExcelTypeEnum.V2010.equals(type)) {
            XSSFWorkbook workbook = new XSSFWorkbook();
            outExcel(workbook, dataList, classToExcel);
            outForFile(fileName, dir, workbook, type);
        } else {
            throw new IllegalArgumentException("无法导出该类型的文件");
        }
    }

    /**
     * 导出excel下载
     *
     * @param fileName     文件名
     * @param request      请求
     * @param response     返回
     * @param type         excel类型
     * @param dataList     数据
     * @param classToExcel 数据类型（使用@ExcelCol和@ExcelTitle和@ExcelIgnore注解对象）
     */
    public void outputExcelWithResponse(String fileName, HttpServletRequest request, HttpServletResponse response, ExcelTypeEnum type, List dataList, Class classToExcel) {
        if (StringUtils.isBlank(fileName)) {
            throw new IllegalArgumentException("文件名为空");
        }

        if (ExcelTypeEnum.V2003.equals(type)) {
            HSSFWorkbook workbook = new HSSFWorkbook();
            outExcel(workbook, dataList, classToExcel);
            outWithResponse(fileName, request, response, workbook, type);
        } else if (ExcelTypeEnum.V2007.equals(type) || ExcelTypeEnum.V2010.equals(type)) {
            XSSFWorkbook workbook = new XSSFWorkbook();
            outExcel(workbook, dataList, classToExcel);
            outWithResponse(fileName, request, response, workbook, type);
        } else {
            throw new IllegalArgumentException("无法导出该类型的文件");
        }
    }

    /**
     * 导出excelMultipartFile
     *
     * @param fileName     文件名
     * @param type         excel类型
     * @param dataList     数据
     * @param classToExcel 数据类型（使用@ExcelCol和@ExcelTitle和@ExcelIgnore注解对象）
     */
    public MultipartFile outputExcelForMultipartFile(String fileName, ExcelTypeEnum type, List<Object> dataList, Class<?> classToExcel) {
        if (StringUtils.isBlank(fileName)) {
            throw new IllegalArgumentException("文件名为空");
        }

        if (ExcelTypeEnum.V2003.equals(type)) {
            HSSFWorkbook workbook = new HSSFWorkbook();
            outExcel(workbook, dataList, classToExcel);
            return outForMultipartFile(fileName, workbook, type);
        } else if (ExcelTypeEnum.V2007.equals(type) || ExcelTypeEnum.V2010.equals(type)) {
            XSSFWorkbook workbook = new XSSFWorkbook();
            outExcel(workbook, dataList, classToExcel);
            return outForMultipartFile(fileName, workbook, type);
        } else {
            throw new IllegalArgumentException("无法导出该类型的文件");
        }
    }

    private Sheet createSheet(ExcelTitle excelTitle, Workbook workbook) {
        Sheet sheet;
        if (excelTitle != null && StringUtils.isNotBlank(excelTitle.name())) {
            sheet = workbook.createSheet(excelTitle.name());
        } else {
            sheet = workbook.createSheet();
        }
        return sheet;
    }

    private void outExcel(Workbook workbook, List<Object> dataList, Class<?> classToExcel) {
        ExcelTitle excelTitle = (ExcelTitle) classToExcel.getAnnotation(ExcelTitle.class);
        Sheet sheet = createSheet(excelTitle, workbook);
        if (workbook instanceof HSSFWorkbook) {
            createXls(sheet.getSheetName(), (HSSFSheet) sheet, dataList, classToExcel);
        } else {
            createXlsx(sheet.getSheetName(), (XSSFSheet) sheet, dataList, classToExcel);
        }
    }


    /**
     * 导出为文件
     *
     * @param fileName 文件名
     * @param dir      目录
     * @param workbook 工作簿
     * @param type     excel类型
     */
    private void outForFile(String fileName, String dir, Workbook workbook, ExcelTypeEnum type) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(dir + fileName + DOT + type.getValue());
            workbook.write(fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            throw new DataOutputException("文件保存失败：" + e.getMessage());
        }
    }

    /**
     * 导出为MultipartFile
     *
     * @param fileName 文件名
     * @param workbook 工作簿
     * @param type     excel类型
     */
    private MultipartFile outForMultipartFile(String fileName, Workbook workbook, ExcelTypeEnum type) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            byte[] array = out.toByteArray();
            ByteArrayInputStream in = new ByteArrayInputStream(array);
            byte[] bytes = new byte[in.available()];
            int read = in.read(bytes);
            log.info("read:" + read);
            in.close();
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {
                    bytes[i] += 256;
                }
            }
            return new ExcelMultipartFile(fileName, bytes, type);
        } catch (Exception e) {
            throw new DataConvertException("转MultipartFile失败");
        }
    }


    /**
     * 导出下载
     *
     * @param fileName 文件名
     * @param request  请求
     * @param response 返回
     * @param workbook 工作簿
     * @param type     excel类型
     */
    private void outWithResponse(String fileName, HttpServletRequest request, HttpServletResponse response, Workbook workbook, ExcelTypeEnum type) {
        try {
            String header = request.getHeader("USER-AGENT");
            //处理中文名乱码的问题
            if (StringUtils.containsIgnoreCase(header, FIRE_FOX)) {
                fileName = new String(fileName.getBytes(), StandardCharsets.ISO_8859_1);
            } else {
                fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
            }
            String headStr = "attachment; filename=\"" + fileName + DOT + type.getValue() + "\"";
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("APPLICATION/OCTET-STREAM");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", headStr);
            workbook.write(outputStream);
            outputStream.flush();
        } catch (Exception e) {
            throw new DataOutputException("文件导出失败：" + e.getMessage());
        }
    }

    private void createXls(String titleName, HSSFSheet sheet, List<Object> dataList, Class<?> classToExcel) {
        Map<String, String> colHeaders = getAnnotationColHeaders(classToExcel);
        Map<String, Method> methodMap = getMethodMap(classToExcel);
        //创建标题行
        //合并标题行
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, colHeaders.size() - 1));
        HSSFRow row = sheet.createRow(0);
        HSSFCell title = row.createCell(0);
        title.setCellValue(titleName);
        //遍历创建列标题行
        HSSFRow colHeader = sheet.createRow(1);
        int i = 0;
        for (String value : colHeaders.values()) {
            HSSFCell cell = colHeader.createCell(i);
            cell.setCellValue(value);
            i++;
        }
        //遍历创建数据行
        if (CollectionUtils.isNotEmpty(dataList)) {
            int j = 2;
            for (Object object : dataList) {
                HSSFRow dataRow = sheet.createRow(j);
                int k = 0;
                for (String fieldName : colHeaders.keySet()) {
                    HSSFCell cell = dataRow.createCell(k);
                    cell.setCellValue(getObjectValue(object, fieldName, methodMap));
                    k++;
                }
                j++;
            }
        }
    }

    private void createXlsx(String titleName, XSSFSheet sheet, List<Object> dataList, Class<?> classToExcel) {
        Map<String, String> colHeaders = getAnnotationColHeaders(classToExcel);
        Map<String, Method> methodMap = getMethodMap(classToExcel);
        //创建标题行
        //合并标题行
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, colHeaders.size() - 1));
        XSSFRow row = sheet.createRow(0);
        XSSFCell title = row.createCell(0);
        title.setCellValue(titleName);
        //遍历创建列标题行
        XSSFRow colHeader = sheet.createRow(1);
        int i = 0;
        for (String value : colHeaders.values()) {
            XSSFCell cell = colHeader.createCell(i);
            cell.setCellValue(value);
            i++;
        }
        //遍历创建数据行
        if (CollectionUtils.isNotEmpty(dataList)) {
            int j = 2;
            for (Object object : dataList) {
                XSSFRow dataRow = sheet.createRow(j);
                int k = 0;
                for (String fieldName : colHeaders.keySet()) {
                    XSSFCell cell = dataRow.createCell(k);
                    cell.setCellValue(getObjectValue(object, fieldName, methodMap));
                    k++;
                }
                j++;
            }
        }
    }

    /**
     * 获取注解的列标题
     */
    private Map<String, String> getAnnotationColHeaders(Class<?> classToExcel) {
        Field[] fields = classToExcel.getDeclaredFields();
        Map<String, String> colHeaders = new LinkedHashMap<>();
        for (Field field : fields) {
            ExcelIgnore ignore = field.getAnnotation(ExcelIgnore.class);
            if (ignore == null) {
                ExcelCol fieldAnnotation = field.getAnnotation(ExcelCol.class);
                if (fieldAnnotation != null && StringUtils.isNotBlank(fieldAnnotation.name())) {
                    colHeaders.put(field.getName(), fieldAnnotation.name());
                } else {
                    colHeaders.put(field.getName(), field.getName());
                }
            }
        }
        return colHeaders;
    }

    /**
     * 读取对象的属性值
     *
     * @param object    对象
     * @param fieldName 字段名称
     * @param methodMap get方法
     * @return list
     */
    private String getObjectValue(Object object, String fieldName, Map<String, Method> methodMap) {
        try {
            Method method = methodMap.get("get" + fieldName);
            if (method != null) {
                //使用get方法访问属性
                Object invoke = method.invoke(object);
                if (invoke != null) {
                    return invoke.toString();
                }
                return EMPTY;
            }
        } catch (Exception e) {
            throw new DataConvertException("获取对象属性数据失败：" + e.getMessage());
        }
        return EMPTY;
    }

    private Map<String, Method> getMethodMap(Class<?> classToExcel) {
        //获取get所有方法
        Method[] methods = classToExcel.getDeclaredMethods();
        Map<String, Method> getMethodMap = new HashMap<>(methods.length);
        for (Method method : methods) {
            if (method.getName().startsWith("get")) {
                getMethodMap.put(method.getName().toLowerCase(), method);
            }
        }
        return getMethodMap;
    }

    /**
     * 读取excel数据同时转换为类
     *
     * @param inputStream  输入流
     * @param sheetNum     工作簿 (0-n)
     * @param colHeader    列标题所在行 (1-n)
     * @param startRowNum  开始行（1-n）
     * @param excelToClass 需要转换的类(所有字段都是string类型 @ExcelCol注解列标题映射字段)
     *                     注意数字类型为11.0 格式 转整数类型时需要处理小数点
     * @return List
     * @throws IOException io异常
     */
    public <T> List<T> readExcelForObject(InputStream inputStream, Integer sheetNum, Integer colHeader, Integer startRowNum, ExcelTypeEnum type, Class<T> excelToClass) throws IOException {
        return readExcelForObject(inputStream, sheetNum, colHeader, startRowNum, getColNum(excelToClass), type, excelToClass);
    }

    /**
     * 获取列数
     *
     * @param excelToClass 需要转换的类
     */
    private int getColNum(Class<?> excelToClass) {
        int count = 0;
        Field[] fields = excelToClass.getDeclaredFields();
        for (Field field : fields) {
            ExcelIgnore ignore = field.getAnnotation(ExcelIgnore.class);
            if (ignore == null) {
                count++;
            }
        }
        return count;
    }

    /**
     * 读取excel数据同时转换为类
     *
     * @param inputStream  输入流
     * @param sheetNum     工作簿 (0-n)
     * @param colHeader    列标题所在行 (1-n)
     * @param startRowNum  开始行（1-n）
     * @param endColNum    结束列 (1-n)
     * @param excelToClass 需要转换的类(所有字段都是string类型 @ExcelCol注解列标题映射字段)
     *                     注意数字类型为11.0 格式 转整数类型时需要处理小数点
     * @return List
     * @throws IOException io异常
     */
    public <T> List<T> readExcelForObject(InputStream inputStream, Integer sheetNum, Integer colHeader, Integer startRowNum, Integer endColNum, ExcelTypeEnum type, Class<T> excelToClass) throws IOException {
        if (ExcelTypeEnum.V2003.equals(type)) {
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputStream);
            return readExcel(hssfWorkbook, sheetNum, colHeader, startRowNum, endColNum, excelToClass);
        } else if (ExcelTypeEnum.V2007.equals(type) || ExcelTypeEnum.V2010.equals(type)) {
            XSSFWorkbook xssfWorkbook = new XSSFWorkbook(inputStream);
            return readExcel(xssfWorkbook, sheetNum, colHeader, startRowNum, endColNum, excelToClass);
        } else {
            throw new IllegalArgumentException("无法读取该文件");
        }
    }


    private <T> List<T> readExcel(Workbook workbook, Integer sheetNum, Integer colHeader, Integer startRowNum, Integer endColNum, Class<T> excelToClass) {
        // read the sheet
        Sheet sheet = workbook.getSheetAt(sheetNum);
        return readSheet(sheet, colHeader, startRowNum, endColNum, excelToClass);
    }

    private Map<String, String> getColHeaders(Row row, Integer endColNum) {
        // read the cell
        if (row.getLastCellNum() < endColNum) {
            endColNum = (int) row.getLastCellNum();
        }
        Map<String, String> headers = new HashMap<>(endColNum);
        for (int i = 0; i < endColNum; i++) {
            headers.put(getCellValue(row.getCell(i)), i + "");
        }
        return headers;
    }

    private <T> List<T> readSheet(Sheet sheet, Integer colHeader, Integer startRowNum, Integer endColNum, Class<T> excelToClass) {
        List<T> list = new ArrayList<>();
        if (sheet != null) {
            // read the row
            Row headerRow = sheet.getRow(colHeader - 1);
            Map<String, String> colHeaders = getColHeaders(headerRow, endColNum);
            int rows = sheet.getLastRowNum();
            for (int i = startRowNum - 1; i <= rows; i++) {
                Row row = sheet.getRow(i);
                try {
                    T object = readCell(colHeaders, endColNum, row, excelToClass);
                    if (object != null) {
                        list.add(object);
                    }
                } catch (Exception e) {
                    throw new DataConvertException("转换excel数据失败：" + e.getMessage());
                }
            }
        }
        return list;
    }

    private <T> T readCell(Map<String, String> colHeaders, Integer endColNum, Row row, Class<T> excelToClass) throws IllegalAccessException, InstantiationException {
        Object[] objects = new Object[endColNum];
        //空行计数
        int count = 0;
        for (int i = 0; i < endColNum; i++) {
            objects[i] = getCellValue(row.getCell(i));
            if (StringUtils.isBlank(objects[i] + "")) {
                count++;
            }
        }
        if (count != endColNum) {
            return createNewInstance(colHeaders, excelToClass, objects);
        }
        return null;
    }

    private <T> T createNewInstance(Map<String, String> colHeaders, Class<T> excelToClass, Object[] objects) throws InstantiationException, IllegalAccessException {
        Object object = excelToClass.newInstance();
        Field[] fields = excelToClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            ExcelCol annotation = field.getAnnotation(ExcelCol.class);
            String position;
            if (annotation != null) {
                position = colHeaders.get(annotation.name());
            } else {
                position = colHeaders.get(field.getName());
            }
            setObjectFieldValue(objects, object, field, position);
        }
        return (T) object;
    }

    private void setObjectFieldValue(Object[] objects, Object object, Field field, String position) throws IllegalAccessException {
        if (StringUtils.isNotBlank(position)) {
            if (field.getType() == String.class) {
                field.set(object, objects[Integer.parseInt(position)]);
            }
        }
    }

    private String getCellValue(Cell cell) {
        if (cell != null) {
            if (cell.getCellType() == ExcelCellTypeEnum.NUMERIC.getValue()) {
                return String.valueOf(cell.getNumericCellValue());
            }
            if (cell.getCellType() == ExcelCellTypeEnum.STRING.getValue()) {
                return cell.getStringCellValue();
            }
            if (cell.getCellType() == ExcelCellTypeEnum.FORMULA.getValue()) {
                return cell.getCellFormula();
            }
            if (cell.getCellType() == ExcelCellTypeEnum.BLANK.getValue()) {
                return EMPTY;
            }
            if (cell.getCellType() == ExcelCellTypeEnum.BOOLEAN.getValue()) {
                return String.valueOf(cell.getBooleanCellValue());
            } else {
                return cell.getStringCellValue();
            }
        }
        return null;
    }

    private String getSuffix(String path) {
        if (StringUtils.isBlank(path)) {
            return EMPTY;
        }
        int index = path.lastIndexOf(DOT);
        if (index == -1) {
            return EMPTY;
        }
        return path.substring(index + 1);
    }

    public String getFileName(String path) {
        if (StringUtils.isBlank(path)) {
            return EMPTY;
        }
        return path.substring(path.lastIndexOf(SLASH) + 1, path.lastIndexOf(DOT));
    }
}
