// Decompiled by DJ v2.9.9.60 Copyright 2000 Atanas Neshkov  Date: 2005-7-16 19:21:48
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   SSListener.java

package com.huawei.insa2.comm.sgip;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

// Referenced classes of package com.huawei.insa2.comm.sgip:
//            SSEventListener
/**
 * 用于接收上行和状态报告
 */
public class SSListener extends Thread
{
	private static final Logger logger = Logger.getLogger(SSListener.class);

    public SSListener(String ip, int port, SSEventListener lis)
    {
        this.ip = ip;
        this.port = port;
        listener = lis;
        if(beginListen()){
            start();
        }

    }
    
    //接收上行链接
    public void run()
    {
        do
            try
            {
                Socket incoming = serversocket.accept();  
                logger.info("上行链接:"+incoming);
                listener.onConnect(incoming);
            }
            catch(Exception ex)
            {
            	logger.error(ex);
            }

        while(true);
    }

    public boolean beginListen()
    {
        try
        {
            logger.info("监听上行端口启动开始 ip="+ip+",port="+port);
            serversocket = new ServerSocket();
            serversocket.bind(new InetSocketAddress(ip, port));
            logger.info("监听上行端口启动成功 ip="+ip+",port="+port);
            return true;
        }
        catch(Exception ex)
        {
        	logger.error("监听上行端口启动失败 ip="+ip+",port="+port,ex);
        }
        return false;
    }

    public synchronized void stopListen()
    {

            try
            {
                if(serversocket != null)
                {
                    serversocket.close();
                    serversocket = null;
                }
            }
            catch(Exception ioexception) {
            	logger.error(ioexception);
            }
    }

    private ServerSocket serversocket;
    private SSEventListener listener;
    private String ip;
    private int port;

}