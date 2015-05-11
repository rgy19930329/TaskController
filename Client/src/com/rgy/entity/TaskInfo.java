package com.rgy.entity;

import java.io.Serializable;
import java.util.Vector;

public class TaskInfo implements Serializable{
	
	private String pname;//������
	private String pid;//����id
	private String sessionName;//�Ự��
	private String sessionFlag;//�Ự��־���Ự#��
	private String memUsg;//�ڴ�ʹ�����
	
	public TaskInfo(){}
	
	public TaskInfo(String result[]){
		this.setPname(result[0]);
		this.setPid(result[1]);
		this.setSessionName(result[2]);
		this.setSessionFlag(result[3]);
		this.setMemUsg(result[4]);
	}
	
	public void setPname(String pname){
		this.pname = pname;
	}
	
	public String getPname(){
		return pname;
	}
	
	public void setPid(String pid){
		this.pid = pid;
	}
	
	public String getPid(){
		return pid;
	}
	
	public void setSessionName(String sessionName){
		this.sessionName = sessionName;
	}
	
	public String getSessionName(){
		return sessionName;
	}
	
	public void setSessionFlag(String sessionFlag){
		this.sessionFlag = sessionFlag;
	}
	
	public String getSessionFlag(){
		return sessionFlag;
	}
	
	public void setMemUsg(String memUsg){
		this.memUsg = memUsg;
	}
	
	public String getMemUsg(){
		return memUsg;
	}
	
	public Vector<String> getInfoVector(){
		Vector<String> rowinfo = new Vector<String>();
		rowinfo.add(pname);
		rowinfo.add(pid);
		rowinfo.add(sessionName);
		rowinfo.add(sessionFlag);
		rowinfo.add(memUsg);
		return rowinfo;
	}
}












