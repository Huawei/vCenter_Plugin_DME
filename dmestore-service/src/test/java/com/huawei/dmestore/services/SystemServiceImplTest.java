package com.huawei.dmestore.services;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.huawei.dmestore.dao.SystemDao;
import org.junit.Test;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * SystemServiceImpl Tester.
 *
 * @author wangxiangyong
 * @version 1.0
 * @since <pre>十一月 11, 2020</pre>
 */
public class SystemServiceImplTest {
    @Mock
    private SystemDao systemDao;
    @InjectMocks
    private SystemServiceImpl systemService = new SystemServiceImpl();

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Method: initDb()
     */
    @Test
    public void testInitDb() throws Exception {
        doNothing().when(systemDao).checkExistAndCreateTable(anyString(), anyString());
        doNothing().when(systemDao).initData(anyString());
        systemService.initDb();
    }

    /**
     * Method: isTableExists(String tableName)
     */
    @Test
    public void testIsTableExists() throws Exception {
        when(systemDao.checkTable(anyString())).thenReturn(true);
        systemService.isTableExists("DP_DME_ACCESS_INFO");
    }

    /**
     * Method: isColumnExists(String tableName, String columnName)
     */
    @Test
    public void testIsColumnExists() throws Exception {
        when(systemDao.isColumnExists(anyString(), anyString())).thenReturn(true);
        boolean b = systemService.isColumnExists("DP_DME_ACCESS_INFO", "hostIp");
        assert b;
    }

    /**
     * Method: cleanData()
     */
    @Test
    public void testCleanData() throws Exception {
        doNothing().when(systemDao).cleanAllData();
        systemService.cleanData();
        assert true;
    }

} 
