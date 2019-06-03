package com.esb.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description:zookeeper线程工具类
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-6.3
 * @version 1.00.00
 * @history:
 */
public class ThreadPoolUtil {
	
	public static final String THREADCOUNT = "threadCount";
	
    private final static int threadCount = Constant.getConst(THREADCOUNT) != null ?  Integer.parseInt(Constant.getConst(THREADCOUNT)) : 4;
    
    private final static ExecutorService threadPool = Executors.newFixedThreadPool(threadCount);

    public static ExecutorService getThreadPool() {
        return threadPool;
    }
}
