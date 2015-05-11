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
				// ���մӿͻ��˷��͹���������
				list = (ArrayList<TaskInfo>) oint.readObject();
				System.out.println(info + "���ݽ����С���");
				// ��ʾ
				if (list != null) {
					// ��ʾ
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
			// ���ÿͻ��˴ӵ�¼�б���ɾ��
			MainFrame.map.remove(info);
			System.out.println("�Ѵ�map��ɾ����"+info);
			MainFrame.label_show.setText("Login : " + MainFrame.map.size());
			//
			MainFrame.cb_ip.removeItem(info);// �÷����ᴥ��cb_ip��addActionListener����
			System.out.println("�Ѵ���������ɾ����"+info);
			System.out.println("��������һ�����У�"+MainFrame.cb_ip.getItemCount()+"��");
				
		}
	}

	public void setStop() {
		this.flag = false;
	}

}
