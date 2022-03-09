package com.coisini.model;

import lombok.Data;
import lombok.ToString;
import java.util.List;

/**
 * @Description 分页结果封装
 * @author coisini
 * @date Mar 9, 2022
 * @version 2.0
 */
@Data
@ToString
public class PageResult<T> {

	/**
	 * 数据条数
	 */
    private Long total;

	/**
	 * 数据
	 */
    private List<T> rows;

    public PageResult(Long total, List<T> rows) {
        super();
        this.total = total;
        this.rows = rows;
    }

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

}
