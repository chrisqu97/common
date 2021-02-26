package cn.com.qucl.common.util.excel;

/**
 * @author qucl
 * @date 2018/11/15 19:48
 */
public enum ExcelCellTypeEnum {
    /**
     * 数字
     */
    NUMERIC(0),
    /**
     * 字符串
     */
    STRING(1),
    /**
     * 公式
     */
    FORMULA(2),
    /**
     * 空
     */
    BLANK(3),
    /**
     * 布尔型
     */
    BOOLEAN(4),
    /**
     * 错误
     */
    ERROR(5),
    /**
     * 日期类型
     */
    DATE(6);

    ExcelCellTypeEnum(Integer value) {
        this.value = value;
    }

    private Integer value;

    public Integer getValue() {
        return this.value;
    }
}
