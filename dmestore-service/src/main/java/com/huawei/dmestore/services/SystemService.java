package com.huawei.dmestore.services;

/**
 * SystemService
 *
 * @author yy
 * @since 2020-09-15
 **/
public interface SystemService {
    /**
     * init table structures and data
     */
    void initDb();

    /**
     * check if table exists in DB
     *
     * @param tableName tableName
     * @return boolean
     */
    boolean isTableExists(String tableName);

    /**
     * check if column exists in the DB table
     *
     * @param tableName tableName
     * @param columnName columnName
     * @return boolean
     */
    boolean isColumnExists(String tableName, String columnName);

    /**
     * delete data from all tables
     */
    void cleanData();
}
