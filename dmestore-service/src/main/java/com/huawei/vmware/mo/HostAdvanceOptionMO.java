package com.huawei.vmware.mo;

import com.huawei.vmware.util.VmwareContext;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.OptionValue;

import java.util.List;

/**
 * HostAdvanceOptionMO
 *
 * @author Administrator
 * @since 2020-12-11
 */
public class HostAdvanceOptionMO extends BaseMO {
    /**
     * HostAdvanceOptionMO
     *
     * @param context context
     * @param morFirewallSystem morFirewallSystem
     */
    public HostAdvanceOptionMO(VmwareContext context, ManagedObjectReference morFirewallSystem) {
        super(context, morFirewallSystem);
    }

    /**
     * queryOptions
     *
     * @param name name
     * @return List
     * @throws Exception Exception
     */
    public List<OptionValue> queryOptions(String name) throws Exception {
        return context.getService().queryOptions(mor, name);
    }

    /**
     * updateOptions
     *
     * @param optionValues optionValues
     * @throws Exception Exception
     */
    public void updateOptions(List<OptionValue> optionValues) throws Exception {
        context.getService().updateOptions(mor, optionValues);
    }
}
