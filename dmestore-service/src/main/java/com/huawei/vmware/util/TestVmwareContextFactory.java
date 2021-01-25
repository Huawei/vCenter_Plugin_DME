//Licensed to the Apache Software Foundation (ASF) under one
//or more contributor license agreements.  See the NOTICE file
//distributed with this work for additional information
//regarding copyright ownership.  The ASF licenses this file
//to you under the Apache License, Version 2.0 (the
//"License"); you may not use this file except in compliance
//with the License.  You may obtain copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing,
//software distributed under the License is distributed on an
//"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
//KIND, either express or implied.  See the License for the
//specific language governing permissions and limitations
//under the License.

package com.huawei.vmware.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TestVmwareContextFactory
 *
 * @author Administrator
 * @since 2020-12-10
 */
public class TestVmwareContextFactory {
    private static final Logger S_LOGGER = LoggerFactory.getLogger(TestVmwareContextFactory.class);

    private static volatile int seq = 1;
    private static VmwareContextPool pool;

    private static final int TIMEOUT = 1200000;

    private TestVmwareContextFactory() {
    }

    static {
        // skip certificate check
        System.setProperty("axis.socketSecureFactory", "org.apache.axis.components.net.SunFakeTrustSocketFactory");
        pool = new VmwareContextPool();
    }

    /**
     * create
     *
     * @param vcenterAddress  vcenterAddress
     * @param vcenterPort     vcenterPort
     * @param vcenterUserName vcenterUserName
     * @param vcenterPassword vcenterPassword
     * @return VmwareContext
     * @throws Exception Exception
     */
    public static VmwareContext create(String vcenterAddress, int vcenterPort, String vcenterUserName,
                                       String vcenterPassword) throws Exception {
        assert vcenterAddress != null;
        assert vcenterUserName != null;
        assert vcenterPassword != null;

        String serviceUrl = "https://" + vcenterAddress + ":" + vcenterPort + "/sdk/vimService";

        if (S_LOGGER.isDebugEnabled()) {
            S_LOGGER.debug(
                "initialize VmwareContext. url: " + serviceUrl + ", username: " + vcenterUserName + ", password: "
                    + vcenterPassword);
        }

        VmwareClient vimClient = new VmwareClient(vcenterAddress + "-" + seq++);
        vimClient.setVcenterSessionTimeout(TIMEOUT);
        vimClient.connect(serviceUrl, vcenterUserName, vcenterPassword);

        VmwareContext context =
            new VmwareContext(vimClient, vimClient.getServiceContent().getAbout().getInstanceUuid());
        return context;
    }

    /**
     * getContext
     *
     * @param vcenterAddress  vcenterAddress
     * @param vcenterPort     vcenterPort
     * @param vcenterUserName vcenterUserName
     * @param vcenterPassword vcenterPassword
     * @return VmwareContext
     * @throws Exception Exception
     */
    public static VmwareContext getContext(String vcenterAddress, int vcenterPort, String vcenterUserName,
                                           String vcenterPassword) throws Exception {
        VmwareContext context = pool.getContext(vcenterAddress, vcenterUserName);
        if (context == null) {
            context = create(vcenterAddress, vcenterPort, vcenterUserName, vcenterPassword);
        }

        if (context != null) {
            context.setPoolInfo(pool, VmwareContextPool.composePoolKey(vcenterAddress, vcenterUserName));
            pool.registerContext(context);
        }

        return context;
    }
}
