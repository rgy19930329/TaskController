package com.rgy.Client;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import com.rgy.entity.TaskInfo;
import com.rgy.utils.FileUtils;

public class LinkThread implements Runnable {

	@Override
	public void run() {
		try {
			//客户端请求与服务器在6000端口建立TCP连接   
	        Socket socket = new Socket("127.0.0.1", 6000); 
	        socket.setSoTimeout(10000);
	        ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());
	        MainFrame.label_addr.setText("本机ip : "+(""+socket.getLocalSocketAddress()).substring(1));
	        
	        boolean flag = true;  
	        while(flag){   
	            //发送数据到服务端    
	        	ArrayList<TaskInfo> list = FileUtils.getTaskList();
		        oout.writeObject(list);
		        oout.flush();
		        
	            Thread.sleep(1000);
	        }  
	        if(socket != null){  
	        	socket.close(); 
	        }  
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
}
