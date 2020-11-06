package cn.com.qucl.common.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author qucl
 * @date 2020/8/6 9:29
 * 关键字检查工具
 */
public class KeywordCheckUtils {
    private static final String IS_END = "1";
    private static final String IS_NOT_END = "0";

    /**
     * 初始化关键字字典
     * 树结构
     * 根节点为关键字的第一个字符
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Map initKeywordMap(Set<String> keywordSet) {
        Map keyWordMap = new HashMap<>(keywordSet.size());
        Map nowKeyWordMap;
        //当前字符是否为最后一个字符 以及下一个字符的信息
        Map<String, String> keyInfoMap;
        for (String keyword : keywordSet) {
            nowKeyWordMap = keyWordMap;
            for (int i = 0; i < keyword.length(); i++) {
                char key = keyword.charAt(i);
                Object tmp = nowKeyWordMap.get(key);
                if (tmp != null) {
                    nowKeyWordMap = (Map<Object, Object>) tmp;
                } else {
                    keyInfoMap = new HashMap<>(20);
                    //存在下一个字符
                    keyInfoMap.put("isEnd", IS_NOT_END);
                    nowKeyWordMap.put(key, keyInfoMap);
                    nowKeyWordMap = keyInfoMap;
                }
                if (i == keyword.length() - 1) {
                    //最后一个字符
                    nowKeyWordMap.put("isEnd", IS_END);
                }
            }
        }
        return keyWordMap;
    }


    /**
     * 匹配第一个关键字
     *
     * @param content    文本内容
     * @param keywordMap 关键字字典
     * @return 关键字
     */
    @SuppressWarnings({"rawtypes"})
    public static String checkFirstKeyWord(String content, Map keywordMap) {
        if (StringUtils.isBlank(content)) {
            return "";
        }
        if (CollectionUtils.isEmpty(keywordMap)) {
            return "";
        }
        Object tmp;
        Map nowKeywordMap;
        int beginIndex;
        int endIndex;
        for (int i = 0; i < content.length(); i++) {
            char key = content.charAt(i);
            tmp = keywordMap.get(key);
            if (tmp != null) {
                beginIndex = i;
                nowKeywordMap = (Map) tmp;
                endIndex = checkFirstKeyWord(content, nowKeywordMap, i);
                if (endIndex - beginIndex > 0) {
                    return content.substring(beginIndex, endIndex);
                }
            }
        }
        return "";
    }

    @SuppressWarnings({"rawtypes"})
    private static int checkFirstKeyWord(String content, Map keywordMap, int index) {
        if (keywordMap == null) {
            return 0;
        } else {
            //检查下一个字符
            index++;
            if (IS_END.equals(keywordMap.get("isEnd"))) {
                return index;
            }
            if (index > content.length()) {
                return 0;
            }
            char key = content.charAt(index);
            return checkFirstKeyWord(content, (Map) keywordMap.get(key), index);
        }
    }

    public static void main(String[] args) {
        Set<String> keywords = new HashSet<>(3);
//        keywords.add("abc");
        keywords.add("jjj");
        Map map = initKeywordMap(keywords);
        System.out.println(map);
        String content = "abcjjj";
        String s = checkFirstKeyWord(content, map);
        System.out.println(s);
    }

}
