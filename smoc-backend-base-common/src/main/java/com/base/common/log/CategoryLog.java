/**
 * @desc
 * 定义base包通用性日志
 */
package com.base.common.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CategoryLog{
	public static final Logger commonLogger = LoggerFactory.getLogger("common");
	public static final Logger messageLogger = LoggerFactory.getLogger("message");
	public static final Logger connectionLogger = LoggerFactory.getLogger("connection");
	public static final Logger authLogger = LoggerFactory.getLogger("auth");
	public static final Logger proxyLogger = LoggerFactory.getLogger("proxy");
	public static final Logger accessLogger = LoggerFactory.getLogger("access");
	public static final Logger taskLogger = LoggerFactory.getLogger("task");
}


