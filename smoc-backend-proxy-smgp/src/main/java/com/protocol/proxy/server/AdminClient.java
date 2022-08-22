/**
 * @desc
 * 
 */
package com.protocol.proxy.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import com.base.common.constant.DynamicConstant;

public class AdminClient {
	 public static void main(String[] args){
	        Socket socket=null;
	        try{
	            socket=new Socket("localhost",DynamicConstant.SHUTDOWN_PORT);
	            //发送关闭命令
	            OutputStream socketOut=socket.getOutputStream();
	            //Scanner scanner=new Scanner(System.in);
	            //String order=scanner.next();
	            socketOut.write("shutdown\r\n".getBytes());
	            //接收服务器反馈
	            BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            String msg=null;
	            while ((msg=br.readLine())!=null){
	                System.out.println(msg);
	            }
	            System.exit(0);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }finally {
	            try{
	                if (socket!=null)
	                    socket.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
}


