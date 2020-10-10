package cn.com.qucl.common.enums;

/**
 * @author qucl
 * @date 2018/11/15 18:47
 * 表格文件后缀
 */
public enum ExcelTypeEnum {
    /**
     * 03版
     */
    V2003("xls"),
    /**
     * 07版
     */
    V2007("xlsx"),
    /**
     * 10版
     */
    V2010("xlsx");

    ExcelTypeEnum(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }
}
