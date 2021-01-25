package com.huawei.vmware.mo;

import org.junit.Before;
import org.junit.Test;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName DiskControllerTypeTest.java
 * @Description TODO
 * @createTime 2020年12月02日 15:42:00
 */
public class DiskControllerTypeTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getType() {
        String[] types = {
            "osdefault", ScsiDiskControllerType.LSILOGIC_SAS, "scsi", "ide", ScsiDiskControllerType.VMWARE_PARAVIRTUAL,
            ScsiDiskControllerType.BUSLOGIC
        };
        for (String type : types) {
            DiskControllerType.getType(type);
        }
    }
}