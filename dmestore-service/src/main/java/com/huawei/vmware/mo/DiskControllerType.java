package com.huawei.vmware.mo;

/**
 * DiskControllerType
 *
 * @author ghca
 * @since 2020-12-11
 **/
public enum DiskControllerType {
    /**
     * ide
     **/
    ide,
    /**
     * scsi
     **/
    scsi,
    /**
     * osdefault
     **/
    osdefault,
    /**
     * lsilogic
     **/
    lsilogic,
    /**
     * lsisas1068
     **/
    lsisas1068,
    /**
     * buslogic
     **/
    buslogic,
    /**
     * pvscsi
     **/
    pvscsi,
    /**
     * none
     **/
    none;

    /**
     * getType
     *
     * @param diskController diskController
     * @return DiskControllerType
     */
    public static DiskControllerType getType(String diskController) {
        if (diskController == null || "osdefault".equalsIgnoreCase(diskController)) {
            return DiskControllerType.osdefault;
        } else if ("vim.vm.device.VirtualLsiLogicSASController"
            .equalsIgnoreCase(diskController)
            || "VirtualLsiLogicSASController".equalsIgnoreCase(diskController)
            || diskController.equalsIgnoreCase(ScsiDiskControllerType.LSILOGIC_SAS)) {
            return DiskControllerType.lsisas1068;
        } else if ("vim.vm.device.VirtualLsiLogicController".equalsIgnoreCase(diskController)
            || "VirtualLsiLogicController".equalsIgnoreCase(diskController)
            || diskController.equalsIgnoreCase(ScsiDiskControllerType.LSILOGIC_PARALLEL)
            || "scsi".equalsIgnoreCase(diskController)) {
            return DiskControllerType.lsilogic;
        } else if ("vim.vm.device.VirtualIDEController".equalsIgnoreCase(diskController)
            || "VirtualIDEController".equalsIgnoreCase(diskController)
            || "ide".equalsIgnoreCase(diskController)) {
            return DiskControllerType.ide;
        } else if ("vim.vm.device.ParaVirtualSCSIController".equalsIgnoreCase(diskController)
            || "ParaVirtualSCSIController".equalsIgnoreCase(diskController)
            || diskController.equalsIgnoreCase(ScsiDiskControllerType.VMWARE_PARAVIRTUAL)) {
            return DiskControllerType.pvscsi;
        } else if ("vim.vm.device.VirtualBusLogicController".equalsIgnoreCase(diskController)
            || "VirtualBusLogicController".equalsIgnoreCase(diskController)
            || diskController.equalsIgnoreCase(ScsiDiskControllerType.BUSLOGIC)) {
            return DiskControllerType.buslogic;
        } else {
            return DiskControllerType.none;
        }
    }
}
