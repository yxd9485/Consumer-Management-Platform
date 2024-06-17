package com.dbt.platform.mn.dto;

import lombok.Data;

import java.util.List;

/**
 * @author shuDa
 * @date 2021/12/17
 **/
@Data
public class ResultPage {
    private List records;
    private int total;
    private int size;
    private int current;
    private boolean optimizeCountSql;
    private boolean hitCount;
    private boolean searchCount;
    private int pages;
}
