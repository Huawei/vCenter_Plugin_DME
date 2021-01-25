package com.huawei.vmware.mo;

import com.huawei.vmware.util.VmwareContext;
import com.vmware.vim25.CustomFieldDef;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.PrivilegePolicyDef;

import java.util.List;

/**
 * CustomFieldsManagerMO
 *
 * @author Administrator
 * @since 2020-12-11
 */
public class CustomFieldsManagerMO extends BaseMO {
    /**
     * CustomFieldsManagerMO
     *
     * @param context context
     * @param mor mor
     */
    public CustomFieldsManagerMO(VmwareContext context, ManagedObjectReference mor) {
        super(context, mor);
    }

    /**
     * addCustomerFieldDef
     *
     * @param fieldName fieldName
     * @param morType morType
     * @param fieldDefPolicy fieldDefPolicy
     * @param fieldPolicy fieldPolicy
     * @return CustomFieldDef
     * @throws Exception Exception
     */
    public CustomFieldDef addCustomerFieldDef(String fieldName, String morType, PrivilegePolicyDef fieldDefPolicy,
        PrivilegePolicyDef fieldPolicy) throws Exception {
        return context.getService().addCustomFieldDef(getMor(), fieldName, morType, fieldDefPolicy, fieldPolicy);
    }

    /**
     * setField
     *
     * @param morEntity morEntity
     * @param key key
     * @param value value
     * @throws Exception Exception
     */
    public void setField(ManagedObjectReference morEntity, int key, String value) throws Exception {
        context.getService().setField(getMor(), morEntity, key, value);
    }

    /**
     * getFields
     *
     * @return List
     * @throws Exception Exception
     */
    public List<CustomFieldDef> getFields() throws Exception {
        return context.getVimClient().getDynamicProperty(getMor(), "field");
    }

    @Override
    public int getCustomFieldKey(String morType, String fieldName) throws Exception {
        List<CustomFieldDef> fields = getFields();
        if (fields != null) {
            for (CustomFieldDef field : fields) {
                if (field.getName().equals(fieldName) && field.getManagedObjectType().equals(morType)) {
                    return field.getKey();
                }
            }
        }
        return 0;
    }
}
