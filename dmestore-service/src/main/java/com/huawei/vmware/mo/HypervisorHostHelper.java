package com.huawei.vmware.mo;

import com.huawei.vmware.util.VmwareContext;
import com.vmware.vim25.CustomFieldStringValue;
import com.vmware.vim25.DynamicProperty;
import com.vmware.vim25.ObjectContent;

import java.util.List;

/**
 * HypervisorHostHelper
 *
 * @author Administrator
 * @since 2020-12-11
 */
public class HypervisorHostHelper {
    /**
     * findVmFromObjectContent
     *
     * @param context                 context
     * @param ocs                     ocs
     * @param name                    name
     * @param instanceNameCustomField instanceNameCustomField
     * @return VirtualMachineMO
     */
    public static VirtualMachineMO findVmFromObjectContent(VmwareContext context, ObjectContent[] ocs, String name,
                                                           String instanceNameCustomField) {
        if (ocs != null && ocs.length > 0) {
            for (ObjectContent oc : ocs) {
                String vmNameInvCenter = null;
                String vmInternalCsName = null;
                List<DynamicProperty> objProps = oc.getPropSet();
                if (objProps != null) {
                    for (DynamicProperty objProp : objProps) {
                        if ("name".equals(objProp.getName())) {
                            vmNameInvCenter = (String) objProp.getVal();
                        } else if (objProp.getName().contains(instanceNameCustomField)) {
                            if (objProp.getVal() != null) {
                                vmInternalCsName = ((CustomFieldStringValue) objProp.getVal()).getValue();
                            }
                        }
                        if ((vmNameInvCenter != null && name.equalsIgnoreCase(vmNameInvCenter)) || (
                            vmInternalCsName != null && name.equalsIgnoreCase(vmInternalCsName))) {
                            VirtualMachineMO vmMo = new VirtualMachineMO(context, oc.getObj());
                            return vmMo;
                        }
                    }
                }
            }
        }
        return null;
    }
}
