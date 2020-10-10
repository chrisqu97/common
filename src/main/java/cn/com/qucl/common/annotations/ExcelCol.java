package cn.com.qucl.common.annotations;



import cn.com.qucl.common.enums.ExcelCellTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author qucl
 * @date 2019/1/3 16:22
 * excel列注解对应字段属性
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelCol {
    String name() default "";

    ExcelCellTypeEnum type() default ExcelCellTypeEnum.STRING;
}
