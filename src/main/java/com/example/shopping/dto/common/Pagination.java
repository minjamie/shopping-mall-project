package com.example.shopping.dto.common;

import lombok.Data;

@Data
public class Pagination {
    private int totalPage;
    private long totalCount;
    private int currentPage;
    private boolean isLastPage;

    public Pagination(int totalPage, long totalCount, int currentPage, boolean isLastPage) {
        this.totalPage = totalPage;
        this.totalCount = totalCount;
        this.currentPage = currentPage;
        this.isLastPage = isLastPage;
    }
}
