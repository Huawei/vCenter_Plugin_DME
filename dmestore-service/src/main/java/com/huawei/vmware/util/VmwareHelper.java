package com.huawei.vmware.util;

import com.huawei.vmware.mo.DiskControllerType;
import com.vmware.vim25.GuestOsDescriptor;

/**
 * VmwareHelper
 *
 * @author Administrator
 * @since 2020-12-10
 */
public class VmwareHelper {
    /**
     * MAX_SCSI_CONTROLLER_COUNT
     */
    public static final int MAX_SCSI_CONTROLLER_COUNT = 4;
    /**
     * MAX_ALLOWED_DEVICES_SCSI_CONTROLLER
     */
    public static final int MAX_ALLOWED_DEVICES_SCSI_CONTROLLER = 16;

    /**
     * MAX_SUPPORTED_DEVICES_SCSI_CONTROLLER
     */
    public static final int MAX_SUPPORTED_DEVICES_SCSI_CONTROLLER = MAX_ALLOWED_DEVICES_SCSI_CONTROLLER - 1;
    private static final int BASENUM = 7;

    private VmwareHelper() { }

    /**
     * isReservedScsiDeviceNumber
     *
     * @param deviceNumber deviceNumber
     * @return boolean
     */
    public static boolean isReservedScsiDeviceNumber(int deviceNumber) {
        return (deviceNumber % VmwareHelper.MAX_ALLOWED_DEVICES_SCSI_CONTROLLER) == BASENUM;
    }

    /**
     * getRecommendedDiskControllerFromDescriptor
     *
     * @param guestOsDescriptor guestOsDescriptor
     * @return String
     * @throws Exception Exception
     */
    public static String getRecommendedDiskControllerFromDescriptor(GuestOsDescriptor guestOsDescriptor)
        throws Exception {
        String recommendedController = guestOsDescriptor.getRecommendedDiskController();
        if (DiskControllerType.getType(recommendedController) == DiskControllerType.pvscsi) {
            recommendedController = DiskControllerType.lsilogic.toString();
        }
        return recommendedController;
    }
}
