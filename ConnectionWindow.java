package client;

import java.awt.EventQueue;
import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.JTextField;






import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;

public class ConnectionWindow {
	private ClientGUI cgui;
	private ManagerGUI mgui;
	private JFrame frame;
	private JTextField Host;
	private JTextField Port;
	private JTextField Username;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConnectionWindow window = new ConnectionWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ConnectionWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnConnect = new JButton("CONNECT");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String host = Host.getText().trim().toString();
				String port = Port.getText().trim().toString();
				String username = Username.getText().trim().toString();
				if(isValid(host,port,username)){
					try{
						Client client = new Client(host,port,username);
						cgui = new ClientGUI(client);
					
						boolean isConnected = client.buildConnection();
						if(isConnected){
							if (client.remoteInterface.getUserInfo().contains(username)){
								JOptionPane.showMessageDialog(null, "Username already existed");
							}else{
								int clientNum = client.remoteInterface.getUserInfo().size();
								if(clientNum == 0){
									JOptionPane.showMessageDialog(null, "Connection succeed.");
									cgui = null;
									mgui = new ManagerGUI(client);
									mgui.frame.setVisible(true);
									mgui.uploadInfo();
									frame.dispose();
								}else{
									cgui.client.remoteInterface.AllowClient();
									JOptionPane.showMessageDialog(null, "Please waiting for authorization");
									new Thread(new Runnable() {
										public void run() {
											try {
												while(true){
													if (cgui.client.remoteInterface.getRequire()==0){
														int allowed = cgui.client.remoteInterface.getAllow();
														// if allowed:
														if (allowed==1) {
															// Transmit the username to RMI		
															cgui.uploadInfo();
															cgui.client.remoteInterface.setAllow(0);
															// Create a listener for user list
															cgui.createUserListListener();
															cgui.frame.setVisible(true);
															// Dispose this frame
															frame.dispose();
															break;
														} else if(allowed==0) {
															JOptionPane.showMessageDialog(null, "Connection failed. You are refused by the manager");
															break;
														} else{
															JOptionPane.showMessageDialog(null, "Connection failed due to RemoteException. ");
															break;
														}
													}
												}
											} catch (HeadlessException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											} catch (RemoteException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
										}
									}).start();
								}
							}
						}else{
							JOptionPane.showMessageDialog(frame, "Fail to build connection");
						}
					}catch(Exception e){
						e.printStackTrace();
						JOptionPane.showMessageDialog(frame, "Unable to connect to RMI");
					}
				}
			}
		});
		btnConnect.setBounds(0, 215, 428, 29);
		frame.getContentPane().add(btnConnect);
		
		JLabel lblHostName = new JLabel("Host Name");
		lblHostName.setBounds(15, 50, 81, 21);
		frame.getContentPane().add(lblHostName);
		
		JLabel lblPortNumber = new JLabel("Port Number");
		lblPortNumber.setBounds(15, 96, 99, 21);
		frame.getContentPane().add(lblPortNumber);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(15, 147, 81, 21);
		frame.getContentPane().add(lblUsername);
		
		Host = new JTextField();
		Host.setBounds(151, 47, 237, 27);
		frame.getContentPane().add(Host);
		Host.setColumns(10);
		
		Port = new JTextField();
		Port.setBounds(151, 93, 237, 27);
		frame.getContentPane().add(Port);
		Port.setColumns(10);
		
		Username = new JTextField();
		Username.setBounds(151, 144, 237, 27);
		frame.getContentPane().add(Username);
		Username.setColumns(10);
	}
	private boolean isValid(String host, String port, String name){
		if(host.equals("")){
			JOptionPane.showMessageDialog(frame, "Empty Host Name");
			return false;
		}else if(port.equals("")){
			JOptionPane.showMessageDialog(frame, "Empty Port Number");
			return false;
		}else if(name.equals("")){
			JOptionPane.showMessageDialog(frame, "Empty Username");
			return false;
		}else{
			return true;
		}
	}
}
