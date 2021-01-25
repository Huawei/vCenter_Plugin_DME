package com.huawei.vmware.util;

import com.huawei.vmware.autosdk.SessionHelper;

/**
 * SessionHelperFactorY
 *
 * @author lianq
 * @ClassName: SessionHelperFactory
 * @since 2020-12-09
 */
public class SessionHelperFactory {
    private static SessionHelperFactory sessionHelperFactory;

    private SessionHelperFactory() {
    }

    /**
     * getInstance
     *
     * @return SessionHelperFactory
     */
    public static SessionHelperFactory getInstance() {
        if (sessionHelperFactory == null) {
            synchronized (SessionHelperFactory.class) {
                if (sessionHelperFactory == null) {
                    sessionHelperFactory = new SessionHelperFactory();
                }
            }
        }
        return sessionHelperFactory;
    }

    /**
     * build
     *
     * @return SessionHelper
     * @throws Exception Exception
     */
    public SessionHelper build() throws Exception {
        return new SessionHelper();
    }
}
