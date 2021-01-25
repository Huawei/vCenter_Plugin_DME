package com.huawei.vmware.util;

import com.vmware.vise.usersession.ServerInfo;
import com.vmware.vise.usersession.UserSessionService;
import com.vmware.vise.vim.data.VimObjectReferenceService;

import java.util.ArrayList;
import java.util.List;

/**
 * VmwarePluginContextFactory
 *
 * @author Administrator
 * @since 2020-12-10
 */
public class VmwarePluginContextFactory {
    private static VmwareContextPool pool;

    private static final int TIMEOUT = 1200000;

    static {
        System.setProperty("axis.socketSecureFactory", "org.apache.axis.components.net.SunFakeTrustSocketFactory");
        pool = new VmwareContextPool();
    }

    private VmwarePluginContextFactory() {
    }

    private static VmwareContext create(UserSessionService userSessionService,
        VimObjectReferenceService vimObjectReferenceService, String serverguid) throws Exception {
        VmwareContext context = null;
        ServerInfo[] serverInfoList = userSessionService.getUserSession().serversInfo;
        for (ServerInfo serverInfo : serverInfoList) {
            VmwareClient vimClient = new VmwareClient(serverInfo.serviceGuid);
            vimClient.setVcenterSessionTimeout(TIMEOUT);
            vimClient.connect(serverInfo);
            if (serverguid.equalsIgnoreCase(serverInfo.serviceGuid)) {
                context = new VmwareContext(vimClient, serverInfo.serviceGuid);
            }
        }
        return context;
    }

    /**
     * getServerContext
     *
     * @param userSessionService userSessionService
     * @param vimObjectReferenceService vimObjectReferenceService
     * @param serverguid serverguid
     * @return VmwareContext
     * @throws Exception Exception
     */
    public static VmwareContext getServerContext(UserSessionService userSessionService,
        VimObjectReferenceService vimObjectReferenceService, String serverguid) throws Exception {
        VmwareContext context = pool.getContext(serverguid, "");
        if (context == null) {
            context = create(userSessionService, vimObjectReferenceService, serverguid);
        }
        if (context != null) {
            context.setPoolInfo(pool, VmwareContextPool.composePoolKey(serverguid, ""));
            pool.registerContext(context);
        }
        return context;
    }

    /**
     * getAllContext
     *
     * @param userSessionService userSessionService
     * @param vimObjectReferenceService vimObjectReferenceService
     * @return VmwareContext[]
     * @throws Exception Exception
     */
    public static VmwareContext[] getAllContext(UserSessionService userSessionService,
        VimObjectReferenceService vimObjectReferenceService) throws Exception {
        ServerInfo[] serverInfoList = userSessionService.getUserSession().serversInfo;
        List<VmwareContext> vmwareContexts = new ArrayList<>();
        for (ServerInfo serverInfo : serverInfoList) {
            vmwareContexts.add(getServerContext(userSessionService, vimObjectReferenceService, serverInfo.serviceGuid));
        }
        return vmwareContexts.toArray(new VmwareContext[0]);
    }

    /**
     * closeAll
     */
    public static void closeAll() {
        pool.closeAll();
    }
}
