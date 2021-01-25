// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package com.huawei.vmware.mo;

import com.huawei.vmware.util.VmwareContext;
import com.vmware.vim25.CustomFieldDef;
import com.vmware.vim25.ManagedObjectReference;

/**
 * BaseMO
 *
 * @author Administrator
 * @since 2020-12-11
 */
public class BaseMO {
    protected VmwareContext context;
    protected ManagedObjectReference mor;

    private String name;

    /**
     * BaseMO
     *
     * @param context context
     * @param mor     mor
     */
    public BaseMO(VmwareContext context, ManagedObjectReference mor) {
        assert context != null;

        this.context = context;
        this.mor = mor;
    }

    /**
     * BaseMO
     *
     * @param context  context
     * @param morType  morType
     * @param morValue morValue
     */
    public BaseMO(VmwareContext context, String morType, String morValue) {
        assert context != null;
        assert morType != null;
        assert morValue != null;

        this.context = context;
        mor = new ManagedObjectReference();
        mor.setType(morType);
        mor.setValue(morValue);
    }

    public VmwareContext getContext() {
        return context;
    }

    /**
     * getMor
     *
     * @return ManagedObjectReference
     */
    public ManagedObjectReference getMor() {
        assert mor != null;
        return mor;
    }

    /**
     * getParentMor
     *
     * @return ManagedObjectReference
     * @throws Exception Exception
     */
    public ManagedObjectReference getParentMor() throws Exception {
        return (ManagedObjectReference) context.getVimClient().getDynamicProperty(mor, "parent");
    }

    /**
     * getName
     *
     * @return String
     * @throws Exception Exception
     */
    public String getName() throws Exception {
        if (name == null) {
            name = (String) context.getVimClient().getDynamicProperty(mor, "name");
        }

        return name;
    }

    /**
     * setCustomFieldValue
     *
     * @param fieldName fieldName
     * @param value value
     * @throws Exception Exception
     */
    public void setCustomFieldValue(String fieldName, String value) throws Exception {
        CustomFieldsManagerMO cfmMo = new CustomFieldsManagerMO(context,
            context.getServiceContent().getCustomFieldsManager());
        int key = getCustomFieldKey(fieldName);
        if (key == 0) {
            try {
                CustomFieldDef field = cfmMo.addCustomerFieldDef(fieldName, getMor().getType(), null, null);
                key = field.getKey();
            } catch (Exception e) {
                key = getCustomFieldKey(fieldName);
            }
        }

        if (key == 0) {
            throw new Exception("Unable to setup custom field facility");
        }

        cfmMo.setField(getMor(), key, value);
    }

    /**
     * getCustomFieldKey
     *
     * @param fieldName fieldName
     * @return int
     * @throws Exception Exception
     */
    public int getCustomFieldKey(String fieldName) throws Exception {
        return getCustomFieldKey(getMor().getType(), fieldName);
    }

    /**
     * getCustomFieldKey
     *
     * @param morType morType
     * @param fieldName fieldName
     * @return int
     * @throws Exception Exception
     */
    public int getCustomFieldKey(String morType, String fieldName) throws Exception {
        assert morType != null;

        CustomFieldsManagerMO cfmMo = new CustomFieldsManagerMO(context,
            context.getServiceContent().getCustomFieldsManager());

        return cfmMo.getCustomFieldKey(morType, fieldName);
    }
}
