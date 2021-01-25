package com.huawei.vmware;

import com.huawei.vmware.util.VmwareContext;

import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vise.usersession.UserSessionService;
import com.vmware.vise.vim.data.VimObjectReferenceService;

/**
 * VCConnectionHelper
 *
 * @author Administrator
 * @since 2020-12-10
 */
public abstract class VcConnectionHelpers {
    private UserSessionService userSessionService;
    private VimObjectReferenceService vimObjectReferenceService;
    private String serverurl;
    private int serverport;
    private String username;
    private String password;
    private final String regex = ":";
    private final int index2 = 2;
    private final int index3 = 3;
    private final int index4 = 4;

    public UserSessionService getUserSessionService() {
        return userSessionService;
    }

    public void setUserSessionService(UserSessionService userSessionService) {
        this.userSessionService = userSessionService;
    }

    public VimObjectReferenceService getVimObjectReferenceService() {
        return vimObjectReferenceService;
    }

    public void setVimObjectReferenceService(VimObjectReferenceService vimObjectReferenceService) {
        this.vimObjectReferenceService = vimObjectReferenceService;
    }

    public String getServerurl() {
        return serverurl;
    }

    public void setServerurl(String serverurl) {
        this.serverurl = serverurl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getServerport() {
        return serverport;
    }

    public void setServerport(int serverport) {
        this.serverport = serverport;
    }

    /**
     * 获取单个context，主要用于前端传入了objectid的情况，类似详情，修改这种，需要将object转换为mor传入到helper使用
     *
     * @param serverguid serverguid
     * @return VmwareContext
     * @throws Exception Exception
     */
    public abstract VmwareContext getServerContext(String serverguid) throws Exception;

    /**
     * 获取多个context，主要用于获取所有的主机，datastore这种
     *
     * @return VmwareContext[]
     * @throws Exception Exception
     */
    public abstract VmwareContext[] getAllContext() throws Exception;

    /**
     * objectId2Mor
     *
     * @param objectid objectid
     * @return ManagedObjectReference
     */
    public ManagedObjectReference objectId2Mor(String objectid) {
        String[] objectarry = objectid.split(regex);
        String type = objectarry[index2];
        String value = objectarry[index3];
        ManagedObjectReference mor = new ManagedObjectReference();
        mor.setType(type);
        mor.setValue(value);
        return mor;
    }

    /**
     * objectId2Serverguid
     *
     * @param objectId objectId
     * @return String
     */
    public String objectId2Serverguid(String objectId) {
        String[] objectarry = objectId.split(regex);
        String serverguid = objectarry[index4];
        return serverguid;
    }

    /**
     * mor2ObjectId
     *
     * @param mor mor
     * @param serverguid serverguid
     * @return String
     */
    public String mor2ObjectId(ManagedObjectReference mor, String serverguid) {
        String type = mor.getType();
        String value = mor.getValue();
        String objectid = "urn:vmomi:" + type + regex + value + regex + serverguid;
        objectid = objectid.replace("/", "%252F");
        return objectid;
    }
}
