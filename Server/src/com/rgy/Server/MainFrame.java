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
	// �������
	private int xOld = 0;
	private int yOld = 0;
	
//	public static Map<Socket, ObjectInputStream> map = new HashMap<>();
	public static ArrayList<Socket> socketList = new ArrayList<>();
	public static Map<String, ObjectInputStream> map = new HashMap<>();
    public static JComboBox<String> cb_ip = new JComboBox<>();
    public static JLabel label_show = new JLabel();
    private JButton btn_task = new JButton("�鿴");
    
    private JTable tb_tasklist;
    public static DefaultTableModel dtm;
    
    private ServerThread myThread = null;
    
    String old = "rgy";
    
//    private Timer timer;

	public MainFrame(String str) {
		super(str);
		this.setBounds(400, 200, 600, 400);
		// ��ӹ��ܴ���
		createInterface();// ��ƽ���  
        registerEvent();// ��������ע���¼� 
        //
//        timer = new Timer();
//        timer.schedule(new CheckSocketTask(), 1000, 5000);
        //
        new LoginThread().run();// ����ͻ��˵�¼
	}

	public void createInterface() {  
        // ȥ�����ڵ�װ��  
        this.setUndecorated(true);
        // �ı䴰���ϵ�ͼ��,�����Լ���ͼ��  
        Toolkit kit = Toolkit.getDefaultToolkit();  
        Image img = kit.getImage("img/TT.jpg");  
        setIconImage(img);  
        // ��ʹ�ò���  
        this.setLayout(null);  

        JLabel label_ip = new JLabel("IP :");  
        label_ip.setBounds(30, 30, 30, 30);
        label_ip.setFont(new Font("Microsoft YaHei", 0, 20)); 
        this.add(label_ip);  
        // ��ַ������
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
        String colsName[] = {"������", "PID", "�Ự��", "�Ự#", "�ڴ�ʹ��"}; 
        tb_tasklist = new JTable(0, 5);//����������
        dtm = (DefaultTableModel)tb_tasklist.getModel();
        dtm.setColumnIdentifiers(colsName);//���ñ�ͷ����
        //���ñ�����ݾ���
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
		
		// �鿴����
		btn_task.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String str_ip = (String)cb_ip.getSelectedItem();
				if(str_ip != null){
					old = str_ip;
				}
				btn_task.setEnabled(false);
				System.out.println(str_ip + "���鿴,"+"old��Ϊ��"+old);
				if(str_ip == null){
					btn_task.setEnabled(true);
				}
				
				ObjectInputStream oint = map.get(str_ip);
				System.out.println("---�л�ip:"+ str_ip +"-----------------");
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
				System.out.println("����cb_ip��������");
				String str_ip = (String)cb_ip.getSelectedItem();
				System.out.println("�������е�ǰֵ : "+str_ip+", old : "+ old);
				if(!old.equals(str_ip)){ // ע������������λ�ã�str_ip����Ϊ�գ����ܷ�ǰ��
					btn_task.setEnabled(true);
				}else{
					btn_task.setEnabled(false);
				}
			}
		});
	}
	
	/**
	 * ���socket����������������Ѿ���ʧ����½���
	 * @author Administrator
	 *
	 */
//	class CheckSocketTask extends TimerTask {
//		@Override
//		public void run() {
//			for(int i=0;i<socketList.size();i++){
//				Socket socket = socketList.get(i);
//				try{
//					socket.sendUrgentData(0xFF);// Զ�˷�����״̬���ͻ����쳣���ѹرգ�
//				}catch(IOException ex){
//					socketList.remove(i);
//					System.out.println("ɾ��һ��Ͽ����ӵ�socket:"+socket.getRemoteSocketAddress());
//					// ���ÿͻ��˴ӵ�¼�б���ɾ��
//					String info = (""+socket.getRemoteSocketAddress()).substring(1);
//					MainFrame.map.remove(info);
//					System.out.println("��map��ɾ����"+info);
//					MainFrame.cb_ip.removeItem(info);
//					System.out.println("��������һ���У�"+MainFrame.cb_ip.getItemCount()+"��");
//					System.out.println("����������ɾ����"+info);
//					MainFrame.label_show.setText("Login : " + MainFrame.map.size());
//				}
//			}
//		}
//	}

	public static void main(String args[]) {
		new MainFrame("task");
	}
}
