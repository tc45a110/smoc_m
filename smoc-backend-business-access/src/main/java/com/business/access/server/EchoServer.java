/**
 * @desc
 * 
 */
package com.business.access.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import com.base.common.constant.DynamicConstant;

public class EchoServer {
	
    private ServerSocket serverSocketShutdown;
    private boolean isShutdown=false; //服务器是否已经关闭
    private AccessServer accessServer;
    
    public EchoServer(AccessServer accessServer) {
		super();
		this.accessServer = accessServer;
	}

	private Thread shutdownThread=new Thread(){
        //负责关闭服务器的线程
        public void run(){
            while(!isShutdown){
                Socket socketForShutdown=null;
                try{
                    socketForShutdown=serverSocketShutdown.accept();
                    BufferedReader br=new BufferedReader(
                            new InputStreamReader(socketForShutdown.getInputStream())
                    );
                    String command=br.readLine();
                    if (command.equals("shutdown")){
                        socketForShutdown.getOutputStream().write("服务正在停止\r\n".getBytes());
                        isShutdown=true;       
                        accessServer.stop();
                    }
                    else {
                        socketForShutdown.getOutputStream().write("错误的命令\r\n".getBytes());
                        socketForShutdown.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public void startup() throws IOException {
        serverSocketShutdown=new ServerSocket(DynamicConstant.SHUTDOWN_PORT);
        shutdownThread.start();
    }
    
}
