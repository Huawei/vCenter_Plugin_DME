package com.huawei.vmware.util;

import com.huawei.vmware.autosdk.SessionHelper;
import com.huawei.vmware.autosdk.TaggingWorkflow;

/**
 * TaggingWorkflowFactory
 *
 * @author lianq
 * @ClassName: TaggingWorkflowFactory
 * @since 2020-12-10
 */
public class TaggingWorkflowFactory {
    private static TaggingWorkflowFactory taggingWorkflowFactory;

    private TaggingWorkflowFactory() {
    }

    /**
     * getInstance
     *
     * @return TaggingWorkflowFactory
     */
    public static TaggingWorkflowFactory getInstance() {
        if (taggingWorkflowFactory == null) {
            synchronized (TaggingWorkflowFactory.class) {
                if (taggingWorkflowFactory == null) {
                    taggingWorkflowFactory = new TaggingWorkflowFactory();
                }
            }
        }
        return taggingWorkflowFactory;
    }

    /**
     * build
     *
     * @param sessionHelper sessionHelper
     * @return TaggingWorkflow
     */
    public TaggingWorkflow build(SessionHelper sessionHelper) {
        return new TaggingWorkflow(sessionHelper);
    }
}
