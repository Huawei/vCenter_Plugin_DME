package com.huawei.dmestore.model;

import java.util.List;

/**
 * @author Administrator
 **/
public class DmeDatasetsQueryResponse {
    private int pageNo;
    private int pageSize;
    private int totalSize;
    private List<DmeDatasetBean> datas;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public List<DmeDatasetBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DmeDatasetBean> datas) {
        this.datas = datas;
    }
}
