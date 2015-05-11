package com.rgy.Server;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class LoginThread implements Runnable {

	@Override
	public void run() {
		try {
        	//服务端在6000端口监听客户端请求的TCP连接  
            ServerSocket server = new ServerSocket(6000);  
            Socket socket = null;  
            boolean flag = true;  
            while(flag){  
                //等待客户端的连接，如果没有获取连接  
            	socket = server.accept();  
                String info = (""+socket.getRemoteSocketAddress()).substring(1);
                System.out.println("客户端"+ info +"连接成功！"); 
                
                // 加入登录列表
                //MainFrame.socketList.add(socket);
                System.out.println(socket.toString()+" 已加入列表");
                MainFrame.map.put(info, new ObjectInputStream(socket.getInputStream()));
                MainFrame.cb_ip.addItem(info);
                MainFrame.label_show.setText("Login : " + MainFrame.map.size());
            }  
            server.close(); 
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
}
