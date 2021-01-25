//
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
//

package com.huawei.vmware.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.net.ssl.SSLContext;

/**
 * SSLUtils
 *
 * @author Administrator
 * @since 2020-12-09
 */
public class SslUtil {
    /**
     * logger
     */
    public static final Logger logger = LoggerFactory.getLogger(SslUtil.class);

    private SslUtil() {
    }

    /**
     * getSupportedProtocols
     *
     * @param protocols protocols
     * @return String[]
     */
    public static String[] getSupportedProtocols(String[] protocols) {
        Set<String> set = new HashSet<String>();
        for (String string : protocols) {
            if ("SSLv3".equals(string) || "SSLv2Hello".equals(string)) {
                continue;
            }
            set.add(string);
        }
        return set.toArray(new String[set.size()]);
    }

    /**
     * getSupportedCiphers
     *
     * @return String[]
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     */
    public static String[] getSupportedCiphers() throws NoSuchAlgorithmException {
        String[] availableCiphers = getSslContext().getSocketFactory().getSupportedCipherSuites();
        Arrays.sort(availableCiphers);
        return availableCiphers;
    }

    /**
     * getSslContext
     *
     * @return SSLContext
     * @throws NoSuchAlgorithmException NoSuchAlgorithmException
     */
    public static SSLContext getSslContext() throws NoSuchAlgorithmException {
        return SSLContext.getInstance("TLSv1.2");
    }
}
