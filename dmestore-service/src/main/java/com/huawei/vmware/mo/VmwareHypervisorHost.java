// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
//

package com.huawei.vmware.mo;

import com.huawei.vmware.util.VmwareContext;
import com.vmware.vim25.ClusterDasConfigInfo;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.ObjectContent;

import java.util.List;


/**
 * Interface to consolidate ESX(i) hosts and HA/FT clusters into ,
 * common interface used by CloudStack Hypervisor resources.
 *
 * @author .
 * @since 2020-12-11
 */
public interface VmwareHypervisorHost {
    /**
     * getContext.
     *
     * @return VmwareContext .
     */
    VmwareContext getContext();

    /**
     * getMor.
     *
     * @return ManagedObjectReference .
     */
    ManagedObjectReference getMor();

    /**
     * getHyperHostName.
     *
     * @return String .
     * @throws Exception .
     */
    String getHyperHostName() throws Exception;

    /**
     * getDasConfig.
     *
     * @return ClusterDasConfigInfo .
     * @throws Exception .
     */
    ClusterDasConfigInfo getDasConfig() throws Exception;

    /**
     * isHaEnabled.
     *
     * @return boolean .
     * @throws Exception .
     */
    boolean isHaEnabled() throws Exception;

    /**
     * setRestartPriorityForVm.
     *
     * @param vmMo     .
     * @param priority .
     * @throws Exception .
     */
    void setRestartPriorityForVm(VirtualMachineMO vmMo, String priority)
        throws Exception;


    /**
     * getHyperHostDatacenter.
     *
     * @return ManagedObjectReference .
     * @throws Exception .
     */
    ManagedObjectReference getHyperHostDatacenter() throws Exception;

    /**
     * getHyperHostOwnerResourcePool.
     *
     * @return ManagedObjectReference .
     * @throws Exception .
     */
    ManagedObjectReference getHyperHostOwnerResourcePool() throws Exception;

    /**
     * getHyperHostCluster.
     *
     * @return ManagedObjectReference.
     * @throws Exception .
     */
    ManagedObjectReference getHyperHostCluster() throws Exception;

    /**
     * listVmsOnHyperHost.
     *
     * @param name .
     * @return List<VirtualMachineMO>.
     * @throws Exception .
     */
    List<VirtualMachineMO> listVmsOnHyperHost(String name) throws Exception;

    /**
     * findVmOnHyperHost.
     *
     * @param name .
     * @return VirtualMachineMO.
     * @throws Exception .
     */
    VirtualMachineMO findVmOnHyperHost(String name) throws Exception;

    /**
     * getVmPropertiesOnHyperHost.
     *
     * @param propertyPaths .
     * @return ObjectContent[] .
     * @throws Exception .
     */
    ObjectContent[] getVmPropertiesOnHyperHost(String[] propertyPaths)
        throws Exception;

    /**
     * getDatastorePropertiesOnHyperHost.
     *
     * @param propertyPaths .
     * @return ObjectContent[] .
     * @throws Exception .
     */
    ObjectContent[] getDatastorePropertiesOnHyperHost(String[] propertyPaths)
        throws Exception;



    /**
     * unmountDatastore.
     *
     * @param esxServiceConsolePort .
     * @return VmwareHypervisorHostNetworkSummary.
     * @throws Exception .
     */
    VmwareHypervisorHostNetworkSummary getHyperHostNetworkSummary(
        String esxServiceConsolePort) throws Exception;

    /**
     * getRecommendedDiskController.
     *
     * @param guestOsId .
     * @return String .
     * @throws Exception .
     */
    String getRecommendedDiskController(String guestOsId) throws Exception;
}
