//Licensed to the Apache Software Foundation (ASF) under one
//or more contributor license agreements.  See the NOTICE file
//distributed with this work for additional information
//regarding copyright ownership.  The ASF licenses this file
//to you under the Apache License, Version 2.0 (the
//"License"); you may not use this file except in compliance
//with the License.  You may obtain a copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing,
//software distributed under the License is distributed on an
//"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
//KIND, either express or implied.  See the License for the
//specific language governing permissions and limitations
//under the License.

package com.huawei.vmware.mo;

/**
 * ScsiDiskControllerType
 *
 * @author Administrator
 * @since 2020-12-11
 */
public class ScsiDiskControllerType {
    /**
     * LSILOGIC_PARALLEL
     */
    public static final String LSILOGIC_PARALLEL = "lsilogic";
    /**
     * LSILOGIC_SAS
     */
    public static final String LSILOGIC_SAS = "lsisas1068";
    /**
     * BUSLOGIC
     */
    public static final String BUSLOGIC = "buslogic";
    /**
     * VMWARE_PARAVIRTUAL
     */
    public static final String VMWARE_PARAVIRTUAL = "pvscsi";

    private ScsiDiskControllerType() {
    }
}
