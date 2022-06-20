package com.huawei.insa2.comm.cmpp;

/**
 * 定义系统所用到的常量
 * @author linmeilong
 * @version 1.0
 */

import java.io.IOException;

import org.apache.log4j.Logger;

import com.huawei.insa2.util.Resource;

public class CMPPConstant {
	private static Logger logger = Logger.getLogger(CMPPConstant.class);
	public static boolean debug;
	public static String LOGINING;
	public static String LOGIN_ERROR;
	public static String SEND_ERROR;
	public static String CONNECT_TIMEOUT;
	public static String STRUCTURE_ERROR;
	public static String NONLICETSP_ID;
	public static String SP_ERROR;
	public static String VERSION_ERROR;
	public static String OTHER_ERROR;
	public static String HEARTBEAT_ABNORMITY;
	public static String SUBMIT_INPUT_ERROR;
	public static String CONNECT_INPUT_ERROR;
	public static String CANCEL_INPUT_ERROR;
	public static String QUERY_INPUT_ERROR;
	public static String DELIVER_REPINPUT_ERROR;
	public static String ACTIVE_REPINPUT_ERROR;
	public static String SMC_MESSAGE_ERROR;
	public static String INT_SCOPE_ERROR;
	public static String STRING_LENGTH_GREAT;
	public static String STRING_NULL;
	public static String VALUE_ERROR;
	public static String FEE_USERTYPE_ERROR;
	public static String REGISTERED_DELIVERY_ERROR;
	public static String PK_TOTAL_ERROR;
	public static String PK_NUMBER_ERROR;

	// 定义协议中用到Command_ID的值
	public static final int Connect_Command_Id = 1;
	public static final int Connect_Rep_Command_Id = 0x80000001;
	public static final int Terminate_Command_Id = 2;
	public static final int Terminate_Rep_Command_Id = 0x80000002;
	public static final int Submit_Command_Id = 4;
	public static final int Submit_Rep_Command_Id = 0x80000004;
	public static final int Deliver_Command_Id = 5;
	public static final int Deliver_Rep_Command_Id = 0x80000005;
	public static final int Query_Command_Id = 6;
	public static final int Query_Rep_Command_Id = 0x80000006;
	public static final int Cancel_Command_Id = 7;
	public static final int Cancel_Rep_Command_Id = 0x80000007;
	public static final int Active_Test_Command_Id = 8;
	public static final int Active_Test_Rep_Command_Id = 0x80000008;
	
	/** 状态常量-尚未初始化。 */
	public static String COMM_NOT_INIT;

	/** 状态常量-正在建立连接。 */
	public static String COMM_CONNECTING;

	/** 状态常量-正在重新建立连接。 */
	public static String COMM_RECONNECTING;

	/** 状态常量-建立连接成功。 */
	public static String COMM_CONNECTED;

	/** 状态常量－正在心跳。 */
	public static String COMM_HEARTBEATING;

	/** 状态常量－正在接收数据。 */
	public static String COMM_RECEIVEING;

	/** 状态常量－正在关闭连接。 */
	public static String COMM_CLOSEING;

	/** 状态常量－连接已经正常关闭。 */
	public static String COMM_CLOSED;

	/** 运行期错误常量-不认识的地址。(无法得到IP地址，可能DNS查询失败或IP地址格式错误) */
	public static String COMM_UNKNOWN_HOST;

	/** 运行期错误常量-端口号配置错误。(不是整数或范围不在0~65535内)。 */
	public static String COMM_PORT_ERROR;

	/** 运行期错误常量-连接被拒绝。(目的主机存在，但在指定的端口上没有TCP监听)。 */
	public static String COMM_CONNECT_REFUSE;

	/** 运行期错误常量-主机不可达。(地址正确，但路由不通或主机没开)。 */
	public static String COMM_NO_ROUTE_TO_HOST;

	/** 运行期错误常量-接收数据超时。(接收数据时出现错误)。 */
	public static String COMM_RECEIVE_TIMEOUT;

	/** 运行期错误常量-连接被对方关闭。 */
	public static String COMM_CLOSE_BY_PEER;

	/** 运行期错误常量-连接被对方重置。 */
	public static String COMM_RESET_BY_PEER;

	/** 运行期错误常量－连接已经关闭。 */
	public static String COMM_CONNECTION_CLOSED;

	/** 错误类别常量-通信异常。 */
	public static String COMM_COMMUNICATION_ERROR;

	/** 错误类别常量-连接异常。 */
	public static String COMM_CONNECT_ERROR;

	/** 错误类别常量-发送数据异常。 */
	public static String COMM_SEND_ERROR;

	/** 错误类别常量-接收数据异常。 */
	public static String COMM_RECEIVE_ERROR;

	/** 错误类别常量-关闭连接异常。 */
	public static String COMM_CLOSE_ERROR;
	
	private static Resource resource;
	
	static{
		try {
			resource = new Resource(new CMPPConstant().getClass(), "resource");
		} catch (IOException e) {
			e.printStackTrace();
		}
		initConstant();
	}

	/**
	 * 系统初始化时调用，读取常量资源字符串。
	 */
	public synchronized static void initConstant() {
		logger.info("常量资源初始化!");
		if (LOGINING == null) {
			LOGINING = resource.get("smproxy/logining");
			LOGIN_ERROR = resource.get("smproxy/login-error");
			SEND_ERROR = resource.get("smproxy/send-error");
			CONNECT_TIMEOUT = resource.get("smproxy/connect-timeout");
			STRUCTURE_ERROR = resource.get("smproxy/structure-error");
			NONLICETSP_ID = resource.get("smproxy/nonlicetsp-id");
			SP_ERROR = resource.get("smproxy/sp-error");
			VERSION_ERROR = resource.get("smproxy/version-error");
			OTHER_ERROR = resource.get("smproxy/other-error");
			HEARTBEAT_ABNORMITY = resource.get("smproxy/heartbeat-abnormity");
			SUBMIT_INPUT_ERROR = resource.get("smproxy/submit-input-error");
			CONNECT_INPUT_ERROR = resource.get("smproxy/connect-input-error");
			CANCEL_INPUT_ERROR = resource.get("smproxy/cancel-input-error");
			QUERY_INPUT_ERROR = resource.get("smproxy/query-input-error");
			DELIVER_REPINPUT_ERROR = resource
					.get("smproxy/deliver-repinput-error");
			ACTIVE_REPINPUT_ERROR = resource
					.get("smproxy/active-repinput-error");
			SMC_MESSAGE_ERROR = resource.get("smproxy/smc-message-error");
			INT_SCOPE_ERROR = resource.get("smproxy/int-scope-error");
			STRING_LENGTH_GREAT = resource.get("smproxy/string-length-great");
			STRING_NULL = resource.get("smproxy/string-null");
			VALUE_ERROR = resource.get("smproxy/value-error");
			FEE_USERTYPE_ERROR = resource.get("smproxy/fee-usertype-error");
			REGISTERED_DELIVERY_ERROR = resource
					.get("smproxy/registered-delivery-erro");
			PK_TOTAL_ERROR = resource.get("smproxy/pk-total-error");
			PK_NUMBER_ERROR = resource.get("smproxy/pk-number-error");
			
		}
		
		COMM_NOT_INIT = resource.get("comm/not-init");
		COMM_CONNECTING = resource.get("comm/connecting");
		COMM_RECONNECTING = resource.get("comm/reconnecting");
		COMM_CONNECTED = resource.get("comm/connected");
		COMM_HEARTBEATING = resource.get("comm/heartbeating");
		COMM_RECEIVEING = resource.get("comm/receiveing");
		COMM_CLOSEING = resource.get("comm/closeing");
		COMM_CLOSED = resource.get("comm/closed");
		COMM_UNKNOWN_HOST = resource.get("comm/unknown-host");
		COMM_PORT_ERROR = resource.get("comm/port-error");
		COMM_CONNECT_REFUSE = resource.get("comm/connect-refused");
		COMM_NO_ROUTE_TO_HOST = resource.get("comm/no-route");
		COMM_RECEIVE_TIMEOUT = resource.get("comm/receive-timeout");
		COMM_CLOSE_BY_PEER = resource.get("comm/close-by-peer");
		COMM_RESET_BY_PEER = resource.get("comm/reset-by-peer");
		COMM_CONNECTION_CLOSED = resource.get("comm/connection-closed");
		COMM_COMMUNICATION_ERROR = resource.get("comm/communication-error");
		COMM_CONNECT_ERROR = resource.get("comm/connect-error");
		COMM_SEND_ERROR = resource.get("comm/send-error");
		COMM_RECEIVE_ERROR = resource.get("comm/receive-error");
		COMM_CLOSE_ERROR = resource.get("comm/close-error");
	}

}