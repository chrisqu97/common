package cn.com.qucl.common.constant.enums;

/**
 * @author qucl
 * @date 2020/11/6 10:24
 * 逻辑状态
 */
public enum StateEnum {
    /**
     * 有效
     */
    VALID(1),
    /**
     * 无效
     */
    INVALID(0);

    private final int value;

    StateEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
