package cn.com.qucl.common.pojo;

import java.util.List;

/**
 * @author qucl
 * @date 2020/9/30 15:26
 * 分页结果展示
 */
public class PageInfo<E> {
    /**
     * 页码
     */
    private Integer pageNum;
    /**
     * 分页大小
     */
    private Integer pageSize;
    /**
     * 总数
     */
    private Long total;

    /**
     * 列表数据
     */
    private List<E> list;

    public static <E> PageInfo<E> page(Integer pageNum, Integer pageSize, Long total, List<E> list) {
        PageInfo<E> pageResult = new PageInfo<>();
        pageResult.pageNum = pageNum;
        pageResult.pageSize = pageSize;
        pageResult.total = total;
        pageResult.list = list;
        return pageResult;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<E> getList() {
        return list;
    }

    public void setList(List<E> list) {
        this.list = list;
    }
}
