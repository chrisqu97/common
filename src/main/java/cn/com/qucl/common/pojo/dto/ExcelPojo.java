package cn.com.qucl.common.pojo.dto;

import cn.com.qucl.common.annotations.ExcelCol;
import cn.com.qucl.common.annotations.ExcelIgnore;
import cn.com.qucl.common.annotations.ExcelTitle;
import cn.com.qucl.common.enums.ExcelCellTypeEnum;
import lombok.Data;

import java.util.Date;

/**
 * @author qucl
 * @date 2019/1/10 15:58
 */
@Data
@ExcelTitle(name = "test")
public class ExcelPojo {
    @ExcelIgnore
    private String id;
    @ExcelCol(name = "名字")
    private String name;
    @ExcelCol(name = "年龄")
    private String age;
    @ExcelCol(name = "生日",type = ExcelCellTypeEnum.DATE)
    private Date birthday;
}
