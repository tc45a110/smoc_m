/**
 * @desc
 *
 */
package com.base.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.base.common.vo.ChannelMO;

public class ChannelMOUtil {
	protected static final Logger logger = LoggerFactory.getLogger(ChannelMOUtil.class);
	/**
	 * 封装上行短信对象，根据tpUdhi区分长短信
	 * @param tpUdhi
	 * @param messageFormat
	 * @param phoneNumber
	 * @param channelMOSRCID
	 * @param messageByteArray
	 * @param channelID
	 * @return
	 */
	public static ChannelMO getMO(int tpUdhi,int messageFormat,String phoneNumber,String channelMOSRCID,byte[] messageByteArray,String channelID,String channelSRCID) {
		String messageContent="";
		try {

			String encoding = "UTF-16BE";
			if (messageFormat == 0 || messageFormat == 1) {
				encoding = "US-ASCII";
			}else if (messageFormat == 15) {
				encoding = "GBK";
			}

			//代表长短信 通过字节进行判断
			if(tpUdhi== 1 || (messageByteArray[0] == 5 && messageByteArray[1] == 0 && messageByteArray[2] == 3 )){
				byte len = messageByteArray[0];
				//增加对7字头的区分
				byte[] header = null;
				if(len==6){
					header = new byte[7];
				}else{
					header = new byte[6];
				}
				byte[] body = new byte[messageByteArray.length - header.length];
				System.arraycopy(messageByteArray, 0, header, 0, header.length);
				System.arraycopy(messageByteArray, header.length, body, 0, body.length);
				messageContent = new String(body, encoding);
				int messageTotal,messageNumber,messageBatchNumber;
				if(header.length == 6){
					messageTotal = header[4];
					messageNumber = header[5];
					messageBatchNumber = header[3];
				}else {
					messageTotal = header[5];
					messageNumber = header[6];
					byte[] array = new byte[]{ header[3], header[4],(byte)0,(byte)0};
					messageBatchNumber = byte2int(array);
				}
				return new ChannelMO(channelMOSRCID, phoneNumber, messageTotal, messageNumber, messageBatchNumber, messageContent, channelID, channelSRCID);
			}

			messageContent = new String(messageByteArray, encoding);
			return getMO(phoneNumber, channelMOSRCID, messageContent, channelID, channelSRCID);


		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * 封装上行短信对象
	 * @param phoneNumber
	 * @param channelMOSRCID
	 * @param messageContent
	 * @param channelID
	 * @return
	 */
	public static ChannelMO getMO(String phoneNumber,String channelMOSRCID,String messageContent,String channelID,String channelSRCID) {
		ChannelMO channelMO = new ChannelMO(channelMOSRCID, phoneNumber, messageContent, channelID, channelSRCID);
		return channelMO;
	}

	/**
	 * 根据字节数组转int
	 * @param res
	 * @return
	 */
	private static int byte2int(byte[] res) {
		int targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00)
				| ((res[2] << 24) >>> 8) | (res[3] << 24);
		return targets;
	}

}


