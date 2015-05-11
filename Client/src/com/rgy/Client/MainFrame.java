package com.rgy.Client;

import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.rgy.entity.TaskInfo;
import com.rgy.utils.FileUtils;

public class MainFrame extends JFrame {
	// �������
	private int xOld = 0;
	private int yOld = 0;
	
	private JTable tb_tasklist;
    private DefaultTableModel dtm;
    
    private Timer timer;
    
    private ArrayList<TaskInfo> taskList;

	private JButton btn_small = new JButton("��С��");
	
	public static JLabel label_addr = new JLabel("����ip : ");

	public MainFrame() {
		this.setBounds(400, 200, 600, 400);
		//
		createInterface();// ��ƽ���
		registerEvent();// ��������ע���¼�
		timer = new Timer();
        timer.schedule(new TaskUpdate(), 1000, 1000);
        //
        new LinkThread().run();
	}

	private void createInterface() {
		// ȥ�����ڵ�װ��
		this.setUndecorated(true);
		// ��ʹ�ò���  
        this.setLayout(null);
        //
        String colsName[] = {"������", "PID", "�Ự��", "�Ự#", "�ڴ�ʹ��"}; 
        tb_tasklist = new JTable(0, 5);//����������
        dtm = (DefaultTableModel)tb_tasklist.getModel();
        dtm.setColumnIdentifiers(colsName);//���ñ�ͷ����
        //���ñ�����ݾ���
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();   
        r.setHorizontalAlignment(JLabel.CENTER);   
        tb_tasklist.setDefaultRenderer(Object.class, r);
        
        JScrollPane scroll = new JScrollPane(tb_tasklist);
        scroll.setBounds(30, 30, 550, 300);
        this.add(scroll); 
        //
        taskList = FileUtils.getTaskList();
        for(int i=0;i<taskList.size();i++){
        	Vector<String> rowinfo = taskList.get(i).getInfoVector();
        	dtm.addRow(rowinfo);//��̬������
        }
		//
        btn_small.setBounds(30, 350, 105, 30);
        btn_small.setFont(new Font("Microsoft YaHei", 0, 16)); 
		this.add(btn_small);
		//
		label_addr.setBounds(350, 350, 220, 30);
		label_addr.setFont(new Font("Microsoft YaHei", 0, 18)); 
		this.add(label_addr);
		//
		this.setVisible(true);
	}
	
	private void registerEvent() {
		// �϶�����
		this.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				int xOnScreen = e.getXOnScreen();
				int yOnScreen = e.getYOnScreen();
				int xx = xOnScreen - xOld;
				int yy = yOnScreen - yOld;
				MainFrame.this.setLocation(xx, yy);
			}
		});
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				xOld = e.getX();
				yOld = e.getY();
			}
		});

		// ������ιرճ���
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1
						&& e.getClickCount() == 2) {
					System.exit(0);
				}
			}
		});
		
		// ��С��
		btn_small.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainFrame.this.setState(Frame.ICONIFIED);
			}
		});
	}
	
	class TaskUpdate extends TimerTask {
		@Override
		public void run() {
			ArrayList<TaskInfo> lastTaskList = FileUtils.getTaskList();
			dtm.setRowCount(0);
			for(int i=0;i<lastTaskList.size();i++){
	        	Vector<String> rowinfo = lastTaskList.get(i).getInfoVector();
	        	dtm.addRow(rowinfo);
	        }
		}
	}

	public static void main(String[] args) {
		new MainFrame();
	}
}
