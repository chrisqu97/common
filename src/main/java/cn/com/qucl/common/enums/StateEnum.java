package cn.com.qucl.common.enums;

/**
 * @author qucl
 * @date 2020/9/5 16:20
 * 有效状态
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
