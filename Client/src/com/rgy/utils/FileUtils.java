package com.rgy.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.rgy.entity.TaskInfo;

public class FileUtils {
	
	public static String getTask() {
		BufferedReader br = null;
		try {
			Process p = Runtime.getRuntime().exec("tasklist /FO CSV");
			br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = null;
			StringBuilder sb=new StringBuilder();
			int i = 0;
			while ((line = br.readLine()) != null) {
				if(i > 0){
					sb.append(line+"\n");
				}
				i++;
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public static ArrayList<TaskInfo> getTaskList() {
		ArrayList<TaskInfo> list = new ArrayList<>();
		BufferedReader br = null;
		try {
			Process p = Runtime.getRuntime().exec("tasklist /FO CSV");
			br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = null;
			int i = 0;
			while ((line = br.readLine()) != null) {
				if(i > 0){
					line = line.substring(1, line.length()-1);
					String result[] = line.split("\",\"");
					TaskInfo task = new TaskInfo(result);
					list.add(task);
				}
				i++;
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public static void main(String args[]){
		String result = getTask();
		System.out.println(result);
		
	}
}
