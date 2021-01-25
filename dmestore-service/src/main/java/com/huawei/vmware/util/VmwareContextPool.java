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

package com.huawei.vmware.util;


import com.huawei.dmestore.utils.StringUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * VmwareContextPool
 *
 * @author Administrator 
 * @since 2020-12-10
 */
public class VmwareContextPool {
    private static final Logger logger = LoggerFactory.getLogger(VmwareContextPool.class);

    private static final Duration DEFAULT_CHECK_INTERVAL = Duration.millis(10000L);
    private static final int DEFAULT_IDLE_QUEUE_LENGTH = 4;

    private final ConcurrentMap<String, Queue<VmwareContext>> pool;
    private int maxIdleQueueLength = DEFAULT_IDLE_QUEUE_LENGTH;
    private Duration idleCheckInterval = DEFAULT_CHECK_INTERVAL;

    private Timer timer = new Timer();

    /**
     * VmwareContextPool
     */
    public VmwareContextPool() {
        this(DEFAULT_IDLE_QUEUE_LENGTH, DEFAULT_CHECK_INTERVAL);
    }

    /**
     * VmwareContextPool
     *
     * @param maxIdleQueueLength maxIdleQueueLength
     * @param idleCheckInterval idleCheckInterval
     */
    public VmwareContextPool(int maxIdleQueueLength, Duration idleCheckInterval) {
        pool = new ConcurrentHashMap<>();

        this.maxIdleQueueLength = maxIdleQueueLength;
        this.idleCheckInterval = idleCheckInterval;

        timer.scheduleAtFixedRate(getTimerTask(), this.idleCheckInterval.getMillis(),
            this.idleCheckInterval.getMillis());
    }

    /**
     * getContext
     *
     * @param vcenterAddress vcenterAddress
     * @param vcenterUserName vcenterUserName
     * @return VmwareContext
     */
    public VmwareContext getContext(final String vcenterAddress, final String vcenterUserName) {
        final String poolKey = composePoolKey(vcenterAddress, vcenterUserName).intern();
        if (StringUtil.isNullOrEmpty(poolKey)) {
            return null;
        }
        synchronized (poolKey) {
            final Queue<VmwareContext> ctxList = pool.get(poolKey);
            if (ctxList != null && !ctxList.isEmpty()) {
                final VmwareContext context = ctxList.remove();
                if (context != null) {
                    context.setPoolInfo(this, poolKey);
                }
                if (logger.isTraceEnabled()) {
                    logger.trace("Return an VmwareContext from the idle pool: " + poolKey
                        + ". current pool size: " + ctxList.size()
                        + ", outstanding count: " + VmwareContext.getOutstandingContextCount());
                }
                return context;
            }
            return null;
        }
    }

    /**
     * registerContext
     *
     * @param context context
     */
    public void registerContext(final VmwareContext context) {
        assert context.getPool() == this;
        assert context.getPoolKey() != null;

        final String poolKey = context.getPoolKey().intern();
        synchronized (poolKey) {
            Queue<VmwareContext> ctxQueue = pool.get(poolKey);

            if (ctxQueue == null) {
                ctxQueue = new ConcurrentLinkedQueue<>();
                pool.put(poolKey, ctxQueue);
            }

            if (ctxQueue.size() >= maxIdleQueueLength) {
                final VmwareContext oldestContext = ctxQueue.remove();
                if (oldestContext != null) {
                    try {
                        oldestContext.close();
                    } catch (Throwable t) {
                        logger.error("Unexpected exception caught while trying to purge oldest VmwareContext", t);
                    }
                }
            }
            context.clearStockObjects();
            ctxQueue.add(context);

            if (logger.isTraceEnabled()) {
                logger.trace(
                    "Recycle VmwareContext into idle pool: " + context.getPoolKey()
                        + ", current idle pool size: " + ctxQueue.size()
                        + ", outstanding count: "
                        + VmwareContext.getOutstandingContextCount());
            }
        }
    }

    /**
     * unregisterContext
     *
     * @param context context
     */
    public void unregisterContext(final VmwareContext context) {
        assert context != null;
        final String poolKey = context.getPoolKey().intern();
        final Queue<VmwareContext> ctxList = pool.get(poolKey);
        synchronized (poolKey) {
            if (!StringUtil.isNullOrEmpty(poolKey) && ctxList != null && ctxList.contains(context)) {
                ctxList.remove(context);
            }
        }
    }

    private TimerTask getTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    doKeepAlive();
                } catch (Throwable e) {
                    logger.error("Unexpected exception", e);
                }
            }
        };
    }

    private void doKeepAlive() {
        final List<VmwareContext> closableCtxList = new ArrayList<>();
        for (final Queue<VmwareContext> ctxQueue : pool.values()) {
            for (Iterator<VmwareContext> iterator = ctxQueue.iterator(); iterator.hasNext(); ) {
                final VmwareContext context = iterator.next();
                if (context == null) {
                    iterator.remove();
                    continue;
                }
                try {
                    context.idleCheck();
                } catch (Throwable e) {
                    logger.warn("Exception caught during VmwareContext idle check, close and discard the context", e);
                    closableCtxList.add(context);
                    iterator.remove();
                }
            }
        }
        for (final VmwareContext context : closableCtxList) {
            context.close();
        }
    }

    /**
     * composePoolKey
     *
     * @param vcenterAddress  vcenterAddress
     * @param vcenterUserName vcenterUserName
     * @return String
     */
    public static String composePoolKey(final String vcenterAddress, final String vcenterUserName) {
        assert vcenterUserName != null;
        assert vcenterAddress != null;
        return vcenterUserName + "@" + vcenterAddress;
    }

    /**
     * closeAll
     */
    public void closeAll() {
        for (final Queue<VmwareContext> ctxQueue : pool.values()) {
            for (Iterator<VmwareContext> iterator = ctxQueue.iterator(); iterator.hasNext(); ) {
                final VmwareContext context = iterator.next();
                if (context == null) {
                    iterator.remove();
                    continue;
                }
                context.close();
            }
        }
    }
}
