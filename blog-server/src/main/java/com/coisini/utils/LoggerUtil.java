package com.coisini.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description 日志工具
 * @author coisini
 * @date Jan 21, 2022
 * @version 1.0
 */
public class LoggerUtil {

    /**
     * 日志工具bean
     * @param clazz
     * @return
     */
    public static Logger loggerFactory(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

}
