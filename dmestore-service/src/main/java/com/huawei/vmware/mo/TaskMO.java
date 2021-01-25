package com.huawei.vmware.mo;

import com.huawei.vmware.util.VmwareContext;
import com.vmware.vim25.LocalizedMethodFault;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.TaskInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TaskMO
 *
 * @author Administrator
 * @since 2020-12-11
 */
public class TaskMO extends BaseMO {
    private static final Logger logger = LoggerFactory.getLogger(TaskMO.class);

    /**
     * TaskMO
     *
     * @param context context
     * @param morTask morTask
     */
    public TaskMO(VmwareContext context, ManagedObjectReference morTask) {
        super(context, morTask);
    }

    /**
     * getTaskFailureInfo
     *
     * @param context context
     * @param morTask morTask
     * @return String
     */
    public static String getTaskFailureInfo(VmwareContext context, ManagedObjectReference morTask) {
        StringBuffer sb = new StringBuffer();
        try {
            TaskInfo info = context.getVimClient().getDynamicProperty(morTask, "info");
            if (info != null) {
                LocalizedMethodFault fault = info.getError();
                if (fault != null) {
                    sb.append(fault.getLocalizedMessage()).append(" ");
                    if (fault.getFault() != null) {
                        sb.append(fault.getFault().getClass().getName());
                    }
                }
            }
        } catch (Exception e) {
            logger.info("[ignored]" + "error retrieving failure info for task : " + e.getLocalizedMessage());
        }
        return sb.toString();
    }
}
