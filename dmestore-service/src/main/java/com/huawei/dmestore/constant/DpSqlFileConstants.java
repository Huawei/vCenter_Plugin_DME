package com.huawei.dmestore.constant;

/**
 * DPSqlFileConstant
 *
 * @author yy
 * @since 2020-09-15
 **/
public final class DpSqlFileConstants {
    /**
     * DIGIT 1 .
     */
    public static final int DIGIT_1 = 1;

    /**
     * DIGIT 2.
     */
    public static final int DIGIT_2 = 2;

    /**
     * DIGIT 3.
     */
    public static final int DIGIT_3 = 3;

    /**
     * DIGIT 4.
     */
    public static final int DIGIT_4 = 4;

    /**
     * DIGIT 5.
     */
    public static final int DIGIT_5 = 5;

    /**
     * DIGIT 6.
     */
    public static final int DIGIT_6 = 6;

    /**
     * DIGIT 7.
     */
    public static final int DIGIT_7 = 7;

    /**
     * DIGIT 8.
     */
    public static final int DIGIT_8 = 8;

    /**
     * DIGIT 9.
     */
    public static final int DIGIT_9 = 9;

    /**
     * DIGIT 10.
     */
    public static final int DIGIT_10 = 10;

    /**
     * DIGIT 11.
     */
    public static final int DIGIT_11 = 11;

    /**
     * DIGIT 12.
     */
    public static final int DIGIT_12 = 12;

    /**
     * DIGIT 13.
     */
    public static final int DIGIT_13 = 13;

    /**
     * DIGIT 14.
     */
    public static final int DIGIT_14 = 14;

    /**
     * DIGIT 15.
     */
    public static final int DIGIT_15 = 15;

    /**
     * DIGIT 16.
     */
    public static final int DIGIT_16 = 16;

    /**
     * DME访问信息表 .
     */
    public static final String DP_DME_ACCESS_INFO = "DP_DME_ACCESS_INFO";

    /**
     * DME任务表 .
     */
    public static final String DP_DME_TASK_INFO = "DP_DME_TASK_INFO";

    /**
     * DME与VMWARE对应关系表 .
     */
    public static final String DP_DME_VMWARE_RELATION = "DP_DME_VMWARE_RELATION";

    /**
     * 存储关系表 .
     */
    public static final String DP_DME_VCENTER_INFO = "DP_DME_VCENTER_INFO";

    /**
     * ALL_TABLES .
     */
    public static final String[] ALL_TABLES = {
        DpSqlFileConstants.DP_DME_ACCESS_INFO, DpSqlFileConstants.DP_DME_VMWARE_RELATION,
        DpSqlFileConstants.DP_DME_TASK_INFO, DpSqlFileConstants.DP_DME_BEST_PRACTICE_CHECK,
        DpSqlFileConstants.DP_DME_VCENTER_INFO
    };

    /**
     * DP_DME_ACCESS_INFO_SQL .
     */
    public static final String DP_DME_ACCESS_INFO_SQL = "DROP TABLE IF EXISTS" + " \"DP_DME_ACCESS_INFO\"; "
        + "CREATE TABLE DP_DME_ACCESS_INFO " + " ( " + "    id integer PRIMARY KEY AUTO_INCREMENT NOT NULL, "
        + "    hostIp VARCHAR(255), " + "    hostPort int, " + "    userName VARCHAR(255), "
        + "    password VARCHAR(1024), " + "    createTime datetime DEFAULT NOW(), "
        + "    updateTime datetime DEFAULT NOW(), " + "    state int DEFAULT 1 " + " );";

    /**
     * DP_DME_VMWARE_RELATION_SQL .
     */
    public static final String DP_DME_VMWARE_RELATION_SQL = "DROP TABLE IF EXISTS" + " \"DP_DME_VMWARE_RELATION\"; "
        + "CREATE TABLE DP_DME_VMWARE_RELATION " + "( " + "  id integer PRIMARY KEY AUTO_INCREMENT NOT NULL, "
        + "  store_id VARCHAR(255), " + "  store_name VARCHAR(255), " + "  volume_id VARCHAR(255), "
        + "  volume_name VARCHAR(255), " + "  volume_wwn VARCHAR(255), " + "  volume_share VARCHAR(255), "
        + "  volume_fs VARCHAR(255), " + "  storage_device_id VARCHAR(255), " + "  share_id VARCHAR(255), "
        + "  share_name VARCHAR(255), " + "  fs_id VARCHAR(255), " + "  fs_name VARCHAR(255), "
        + "  logicport_id VARCHAR(255), " + "  logicport_name VARCHAR(255), " + "  store_type VARCHAR(255), "
        + "  storage_type VARCHAR(255), " + "  createTime datetime DEFAULT NOW(), " + "  updateTime datetime DEFAULT NOW(), "
        + "  STATE int DEFAULT 1 " + "  );";

    /**
     * DP_DME_TASK_INFO_SQL.
     */
    public static final String DP_DME_TASK_INFO_SQL = "DROP TABLE IF EXISTS \"DP_DME_TASK_INFO\"; "
        + "CREATE TABLE DP_DME_TASK_INFO " + "( " + "    id integer PRIMARY KEY AUTO_INCREMENT NOT NULL, "
        + "    CLASS_NAME VARCHAR(255), " + "    CRON VARCHAR(255), " + "    JOB_NAME VARCHAR(255), "
        + "    METHOD VARCHAR(255) " + ");  ";

    /**
     * DP_DME_TASK_DATA_SYNCSERVICELEVEL_SQL.
     */
    public static final String DP_DME_TASK_DATA_SYNCSERVICELEVEL_SQL =
        "INSERT INTO DP_DME_TASK_INFO (ID, CLASS_NAME, CRON, JOB_NAME, METHOD ) " + "VALUES (1, "
            + "'com.dmeplugin.dmestore.task.BackgroundSyncServiceLevelTask',"
            + "'0 0 0/6 * * ?', 'syncServiceLevel', 'syncServiceLevel');";

    /**
     * DP_DME_TASK_DATA_SYNCBESTPRACTISE_SQL.
     */
    public static final String DP_DME_TASK_DATA_SYNCBESTPRACTISE_SQL =
        "INSERT INTO DP_DME_TASK_INFO (ID, CLASS_NAME, CRON, JOB_NAME, METHOD )  " + "VALUES (2, "
            + "'com.dmeplugin.dmestore.task.BackgroundCheckBestPractiseTask', "
            + "'0 0 2 * * ?', 'syncCheckBestPractise'," + " 'syncCheckBestPractise');";

    /**
     * DP_DME_TASK_DATA_SCANDATASTORE_SQL.
     */
    public static final String DP_DME_TASK_DATA_SCANDATASTORE_SQL =
        "INSERT INTO DP_DME_TASK_INFO (ID, CLASS_NAME, CRON, JOB_NAME, METHOD) " + "VALUES (3, "
            + "'com.dmeplugin.dmestore.task.BackgroundScanDatastoreTask', "
            + "'0 0 0/6 * * ?', 'scanDatastore', 'scanDatastore');";

    /**
     * DP_DME_TASK_DATA_REFRESHRESOURCEINSTANCE_SQL.
     */
    public static final String DP_DME_TASK_DATA_REFRESHRESOURCEINSTANCE_SQL =
        "INSERT INTO DP_DME_TASK_INFO (ID, CLASS_NAME, CRON, JOB_NAME, METHOD) " + "VALUES (4, "
            + "'com.dmeplugin.dmestore." + "task.BackGroundRefreshResourceInstanceTask',"
            + "'0 */1 * * * ?', 'refreshResourceInstance', " + "'refreshResourceInstance');";

    /**
     * DP_DME_BEST_PRACTICE_CHECK.
     */
    public static final String DP_DME_BEST_PRACTICE_CHECK = "DP_DME_BEST_PRACTICE_CHECK";

    /**
     * DP_DME_BEST_PRACTICE_CHECK_SQL.
     */
    public static final String DP_DME_BEST_PRACTICE_CHECK_SQL = "DROP TABLE IF EXISTS \"DP_DME_BEST_PRACTICE_CHECK\"; "
        + "CREATE TABLE \"DP_DME_BEST_PRACTICE_CHECK\" ( " + "\"ID\"  integer PRIMARY KEY AUTO_INCREMENT NOT NULL, "
        + "\"HOST_ID\"  nvarchar(255), " + "\"HOST_NAME\"  nvarchar(255), " + "\"HOST_SETTING\"  nvarchar(255), "
        + "\"RECOMMEND_VALUE\"  nvarchar(50) NOT NULL, " + "\"ACTUAL_VALUE\"  clob, " + "\"HINT_LEVEL\"  nvarchar(20), "
        + "\"NEED_REBOOT\"  nvarchar(10), " + "\"AUTO_REPAIR\"  nvarchar(10), " + "\"CREATE_TIME\"  datetime " + ");";

    /**
     * DP_DME_BEST_PRACTICE_CHECK_ALTER_SQL.
     */
    public static final String DP_DME_BEST_PRACTICE_CHECK_ALTER_SQL = "ALTER TABLE DP_DME_BEST_PRACTICE_CHECK "
        + "ALTER COLUMN ACTUAL_VALUE CLOB;";

    /**
     * DP_DME_VCENTER_INFO_SQL.
     */
    public static final String DP_DME_VCENTER_INFO_SQL = "DROP TABLE IF EXISTS " + "\"DP_DME_VCENTER_INFO\"; "
        + "CREATE TABLE \"DP_DME_VCENTER_INFO\" ( " + "\"ID\"  integer PRIMARY KEY AUTO_INCREMENT NOT NULL, "
        + "\"HOST_IP\"  nvarchar(50), " + "\"USER_NAME\"  nvarchar(255), " + "\"PASSWORD\"  nvarchar(255), "
        + "\"STATE\"  BOOLEAN, " + "\"CREATE_TIME\"  datetime NOT NULL, " + "\"PUSH_EVENT\"  BOOLEAN, "
        + "\"PUSH_EVENT_LEVEL\"  integer, " + "\"HOST_PORT\"  integer default 443, "
        + "CONSTRAINT UNIQUE_DP_HOST_IP UNIQUE (HOST_IP) " + ");";

    /**
     * DPSqlFileConstant .
     */
    private DpSqlFileConstants() {
    }
}
