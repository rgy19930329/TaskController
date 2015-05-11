package com.rgy.Server;

import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Vector;

import com.rgy.entity.TaskInfo;

public class ServerThread extends Thread {

	private boolean flag = true;
	private ObjectInputStream oint = null;
	private String info = null;

	public ServerThread(String info, ObjectInputStream oint) {
		this.info = info;
		this.oint = oint;
	}

	public void run() {
		try {
			ArrayList<TaskInfo> list = null;

			while (flag) {
				// 接收从客户端发送过来的数据
				list = (ArrayList<TaskInfo>) oint.readObject();
				System.out.println(info + "数据接收中……");
				// 显示
				if (list != null) {
					// 显示
					MainFrame.dtm.setRowCount(0);
					for (int i = 0; i < list.size(); i++) {
						Vector<String> rowinfo = list.get(i).getInfoVector();
						MainFrame.dtm.addRow(rowinfo);
					}
				}else{
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 将该客户端从登录列表中删除
			MainFrame.map.remove(info);
			System.out.println("已从map中删除："+info);
			MainFrame.label_show.setText("Login : " + MainFrame.map.size());
			//
			MainFrame.cb_ip.removeItem(info);// 该方法会触发cb_ip的addActionListener监听
			System.out.println("已从下拉框中删除："+info);
			System.out.println("下拉框中一共还有："+MainFrame.cb_ip.getItemCount()+"项");
				
		}
	}

	public void setStop() {
		this.flag = false;
	}

}
