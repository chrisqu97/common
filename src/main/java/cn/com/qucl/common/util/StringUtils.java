package cn.com.qucl.common.util;


import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    private final static String[] ENCODE_TYPE_LIST = new String[]{"GB2312", "ISO-8859-1", "UTF-8", "GBK"};

    /**
     * 获取字符串的编码
     */
    public static List<String> getEncoding(String str) {
        List<String> list = new ArrayList<>();
        try {
            for (String encode : ENCODE_TYPE_LIST) {
                if (str.equals(new String(str.getBytes(encode), encode))) {
                    list.add(encode);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return list;
    }


    /**
     * 中文
     */
    public static Boolean isCN(String str) {
        String regEx = "^[\\u4e00-\\u9fa5]+$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 全字母
     */
    public static Boolean isEN(String str) {
        String regEx = "^[A-Za-z]+$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 是否包含特殊字符
     */
    public static Boolean includeSpecialCharacters(String str) {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;,\\\\.<>/?！￥…（）—【】‘；：”“’。，、？]|\\n|\\r|\\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }


    /**
     * 将字符串中的中文转化为拼音,其他字符不变
     *
     * @param chinese 汉字串
     * @return 汉语拼音全拼
     */
    public static String getPingYin(String chinese) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        char[] input = chinese.trim().toCharArray();
        StringBuilder output = new StringBuilder();

        try {
            for (char item : input) {
                if (Character.toString(item).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(item, format);
                    output.append(temp[0]);
                } else {
                    output.append(Character.toString(item));
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            log.error(e.getMessage(), e);
        }
        return output.toString();
    }

    /**
     * 获取汉字串拼音首字母，英文字符不变
     * 标点符号全都清除
     *
     * @param chinese 汉字串
     * @return 汉语拼音首字母
     */
    public static String getFirstSpell(String chinese) {
        StringBuilder stringBuilder = new StringBuilder();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (char item : arr) {
            if (item > 128) {
                try {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(item, defaultFormat);
                    if (temp != null) {
                        stringBuilder.append(temp[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    log.error(e.getMessage(), e);
                }
            } else {
                stringBuilder.append(item);
            }
        }
        return stringBuilder.toString().replaceAll("\\W", "").trim();
    }

    /**
     * 获取汉字串拼音，英文字符不变
     * 中文符号会被清除
     *
     * @param chinese 汉字串
     * @return 汉语拼音
     */
    public static String getFullSpell(String chinese) {
        StringBuilder stringBuilder = new StringBuilder();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (char item : arr) {
            if (item > 128) {
                try {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(item, defaultFormat);
                    if (temp != null) {
                        stringBuilder.append(temp[0]);
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    log.error(e.getMessage(), e);
                }
            } else {
                stringBuilder.append(item);
            }
        }
        return stringBuilder.toString();
    }
}
