package com.rgy.Server;

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class MainFrame extends JFrame {
	// 添加属性
	private int xOld = 0;
	private int yOld = 0;
	
//	public static Map<Socket, ObjectInputStream> map = new HashMap<>();
	public static ArrayList<Socket> socketList = new ArrayList<>();
	public static Map<String, ObjectInputStream> map = new HashMap<>();
    public static JComboBox<String> cb_ip = new JComboBox<>();
    public static JLabel label_show = new JLabel();
    private JButton btn_task = new JButton("查看");
    
    private JTable tb_tasklist;
    public static DefaultTableModel dtm;
    
    private ServerThread myThread = null;
    
    String old = "rgy";
    
//    private Timer timer;

	public MainFrame(String str) {
		super(str);
		this.setBounds(400, 200, 600, 400);
		// 添加功能代码
		createInterface();// 设计界面  
        registerEvent();// 管理所有注册事件 
        //
//        timer = new Timer();
//        timer.schedule(new CheckSocketTask(), 1000, 5000);
        //
        new LoginThread().run();// 处理客户端登录
	}

	public void createInterface() {  
        // 去掉窗口的装饰  
        this.setUndecorated(true);
        // 改变窗体上的图标,换成自己的图标  
        Toolkit kit = Toolkit.getDefaultToolkit();  
        Image img = kit.getImage("img/TT.jpg");  
        setIconImage(img);  
        // 不使用布局  
        this.setLayout(null);  

        JLabel label_ip = new JLabel("IP :");  
        label_ip.setBounds(30, 30, 30, 30);
        label_ip.setFont(new Font("Microsoft YaHei", 0, 20)); 
        this.add(label_ip);  
        // 地址下拉框
        cb_ip.setBounds(70, 30, 200, 30);
        cb_ip.setFont(new Font("Microsoft YaHei", 0, 16)); 
        this.add(cb_ip);  
        //
        btn_task.setBounds(280, 30, 80, 30);
        btn_task.setFont(new Font("Microsoft YaHei", 0, 16)); 
        this.add(btn_task);  
        //
        label_show.setBounds(450, 30, 98, 30);
        label_show.setFont(new Font("Microsoft YaHei", 0, 22)); 
        this.add(label_show);  
        label_show.setText("Login : " + map.size());
        //
        String colsName[] = {"进程名", "PID", "会话名", "会话#", "内存使用"}; 
        tb_tasklist = new JTable(0, 5);//创建虚拟表格
        dtm = (DefaultTableModel)tb_tasklist.getModel();
        dtm.setColumnIdentifiers(colsName);//设置表头内容
        //设置表格内容居中
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();   
        r.setHorizontalAlignment(JLabel.CENTER);   
        tb_tasklist.setDefaultRenderer(Object.class, r);
        
        JScrollPane scroll = new JScrollPane(tb_tasklist);
        scroll.setBounds(30, 75, 550, 300);
        this.add(scroll); 
        //
        this.setVisible(true);  
    }  
	
	private void registerEvent() {
		// 拖动窗口
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

		// 点击两次关闭程序
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1
						&& e.getClickCount() == 2) {
					System.exit(0);
				}
			}
		});
		
		// 查看进程
		btn_task.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String str_ip = (String)cb_ip.getSelectedItem();
				if(str_ip != null){
					old = str_ip;
				}
				btn_task.setEnabled(false);
				System.out.println(str_ip + "被查看,"+"old变为："+old);
				if(str_ip == null){
					btn_task.setEnabled(true);
				}
				
				ObjectInputStream oint = map.get(str_ip);
				System.out.println("---切换ip:"+ str_ip +"-----------------");
				if(oint != null){
					if(myThread !=null){
						myThread.setStop();
					}
					myThread = new ServerThread(str_ip, oint);
					myThread.start();
				}
			}
		});
		
		cb_ip.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("触发cb_ip监听……");
				String str_ip = (String)cb_ip.getSelectedItem();
				System.out.println("下拉框中当前值 : "+str_ip+", old : "+ old);
				if(!old.equals(str_ip)){ // 注意两个变量的位置，str_ip可能为空，不能放前面
					btn_task.setEnabled(true);
				}else{
					btn_task.setEnabled(false);
				}
			}
		});
	}
	
	/**
	 * 检查socket连接情况，若连接已经消失则更新界面
	 * @author Administrator
	 *
	 */
//	class CheckSocketTask extends TimerTask {
//		@Override
//		public void run() {
//			for(int i=0;i<socketList.size();i++){
//				Socket socket = socketList.get(i);
//				try{
//					socket.sendUrgentData(0xFF);// 远端非连接状态（客户端异常或已关闭）
//				}catch(IOException ex){
//					socketList.remove(i);
//					System.out.println("删除一项断开连接的socket:"+socket.getRemoteSocketAddress());
//					// 将该客户端从登录列表中删除
//					String info = (""+socket.getRemoteSocketAddress()).substring(1);
//					MainFrame.map.remove(info);
//					System.out.println("从map中删除："+info);
//					MainFrame.cb_ip.removeItem(info);
//					System.out.println("下拉框中一共有："+MainFrame.cb_ip.getItemCount()+"项");
//					System.out.println("从下拉框中删除："+info);
//					MainFrame.label_show.setText("Login : " + MainFrame.map.size());
//				}
//			}
//		}
//	}

	public static void main(String args[]) {
		new MainFrame("task");
	}
}
