package cn.com.qucl.common.util;

import cn.com.qucl.common.exception.DataConvertException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author qucl
 * @date 2018/11/28 17:00
 */
@Slf4j
public class XmlUtils {
    private static final String XML_HEADER = "<?xml version='1.1' encoding='UTF-8'?>\n";
    public static final String EMPTY = "";

    public static <T> T readXml(String path, Class<T> xmlToClass) {
        File dataXml = new File(path);
        XStream xStream = new XStream(new DomDriver());
        //配置安全 允许该类型转换
        XStream.setupDefaultSecurity(xStream);
        xStream.allowTypes(new Class[]{xmlToClass});
        //开启注解
        xStream.autodetectAnnotations(true);
        //获取注解配置
        xStream.processAnnotations(xmlToClass);
        //忽略未知属性
        xStream.ignoreUnknownElements();
        return xmlToClass.cast(xStream.fromXML(dataXml));
    }

    /**
     * 读取xml文件
     *
     * @param path 文件路径
     */
    public static String readXml(String path) {
        File dataXml = new File(path);
        long fileLength = dataXml.length();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(dataXml);
            byte[] content = new byte[(int) fileLength];
            int read = inputStream.read(content);
            log.info("read:" + read);
            return new String(content, StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return EMPTY;
        } finally {
            closeStream(inputStream);
        }
    }

    public static String toXml(Object object) {
        XStream xStream = new XStream();
        //开启自动注解
        xStream.autodetectAnnotations(true);
        //调用toXML 将对象转成字符串
        String s = xStream.toXML(object);
        return XML_HEADER + s;
    }

    private static void closeStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 替换元素的值
     *
     * @param inputStream 输入流
     * @param map         替换集合
     */
    public static String replaceElementValue(InputStream inputStream, Map<String, String> map) {
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(inputStream);
            replaceElementValue(map, document);
            return document.asXML();
        } catch (DocumentException e) {
            log.error(e.getMessage(), e);
        } finally {
            closeStream(inputStream);
        }
        return null;
    }

    private static void replaceElementValue(Map<String, String> map, Document document) {
        if (CollectionUtils.isNotEmpty(map)) {
            Element rootElement = document.getRootElement();
            Iterator it = rootElement.elementIterator();
            while (it.hasNext()) {
                Element child = (Element) it.next();
                traverseElement(child, map);
            }
        }
    }

    /**
     * 替换元素的值
     *
     * @param xml 输入流
     * @param map 替换集合
     */
    public static String replaceElementValue(String xml, Map<String, String> map) {
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
            replaceElementValue(map, document);
            return document.asXML();
        } catch (DocumentException e) {
            log.error(e.getMessage(), e);
            throw new DataConvertException("替换xml标签内值失败：" + e.getMessage());
        }
    }

    /**
     * 遍历元素
     *
     * @param element 元素
     * @param map     替换集合
     */
    @SuppressWarnings("unchecked")
    private static void traverseElement(Element element, Map<String, String> map) {
        replaceValue(element, map);
        List<Attribute> attributes = element.attributes();
        for (Attribute attr : attributes) {
            replaceValue(attr, map);
        }
        Iterator itt = element.elementIterator();
        while (itt.hasNext()) {
            Element childOfChild = (Element) itt.next();
            traverseElement(childOfChild, map);
        }
    }

    private static void replaceValue(Attribute attribute, Map<String, String> map) {
        String s = map.get(attribute.getName());
        if (StringUtils.isNotBlank(s)) {
            attribute.setValue(s);
        }
    }

    private static void replaceValue(Element element, Map<String, String> map) {
        String s = map.get(element.getName());
        if (StringUtils.isNotBlank(s)) {
            element.setText(s);
        }
    }

}

