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
        	//�������6000�˿ڼ����ͻ��������TCP����  
            ServerSocket server = new ServerSocket(6000);  
            Socket socket = null;  
            boolean flag = true;  
            while(flag){  
                //�ȴ��ͻ��˵����ӣ����û�л�ȡ����  
            	socket = server.accept();  
                String info = (""+socket.getRemoteSocketAddress()).substring(1);
                System.out.println("�ͻ���"+ info +"���ӳɹ���"); 
                
                // �����¼�б�
                //MainFrame.socketList.add(socket);
                System.out.println(socket.toString()+" �Ѽ����б�");
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
