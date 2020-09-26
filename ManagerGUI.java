package client;


import java.lang.Runnable;
import java.rmi.RemoteException;

import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.HeadlessException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.ListModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ManagerGUI {
	private Client client;
	JFrame frame;
	JList<String> Userlist;
		/**
	 * Create the application.
	 */
	public ManagerGUI(Client client) {
		this.client = client;
		initialize();
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		Thread userListUpdate = new Thread(new userListListener());
		if(client.buildConnection()){
			userListUpdate.start();
		}
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 639);
		frame.getContentPane().setLayout(null);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				try{
					client.remoteInterface.removeAllInfo();	
					if(client.whiteBoard != null){
						client.whiteBoard.frame.dispose();
						client.remoteInterface.closeManagerBoard();
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
		        frame.dispose();
		        client.disconnect();
		    }
		});
		JButton btnCreateWhiteboard = new JButton("Create WhiteBoard");
		btnCreateWhiteboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					if(client.whiteBoard == null){
						client.remoteInterface.createWhiteBoard();
						client.whiteBoard = new DrawingBoard();
						client.whiteBoard.frame.setTitle("Manager WhiteBoard");
						client.whiteBoard.getpanel().setRMI(client.remoteInterface);
						client.whiteBoard.frame.setVisible(true);
						client.whiteBoard.frame.addWindowListener(new java.awt.event.WindowAdapter() {
						    @Override
						    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
								try{
									client.remoteInterface.closeManagerBoard();
								} catch (RemoteException e) {
									e.printStackTrace();
								} 
								client.whiteBoard.getpanel().clear();
						        client.whiteBoard.frame.dispose();
						        client.whiteBoard = null;
						    }
						});
					} else{
						JOptionPane.showMessageDialog(frame, "WhiteBoard is already created.");
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnCreateWhiteboard.setBounds(120, 0, 190, 35);
		frame.getContentPane().add(btnCreateWhiteboard);
		
		JLabel lblUserList = new JLabel("User List");
		lblUserList.setBounds(174, 50, 81, 21);
		frame.getContentPane().add(lblUserList);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(120, 86, 190, 482);
		frame.getContentPane().add(scrollPane);
		
		Userlist = new JList();
		scrollPane.setViewportView(Userlist);
		
		JButton btnKick = new JButton("Kick");
		// Select user listener
				Userlist.addMouseListener(new MouseAdapter(){
		            public void mousePressed(MouseEvent arg0) {	
		            	try {
		            		if(Userlist.isSelectionEmpty()){
		            			client.remoteInterface.setKickUsername("");
		            		}else{
		            			String selectedName = Userlist.getSelectedValue();
		            			client.remoteInterface.setKickUsername(selectedName);		
		            		}
		            	} catch (RemoteException e) {
		            		e.printStackTrace();
		            	}
		            }
		        });
		btnKick.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					
					String reply = client.remoteInterface.KickUser();
					if (reply == "NOTINTHELIST") {
						JOptionPane.showMessageDialog(frame, "Selected user is not connected");
					}else if(reply == "NOTSELECTED"){
						JOptionPane.showMessageDialog(frame, "No user selected");
					}else if(reply.equals(client.username)){
						JOptionPane.showMessageDialog(frame, "Cannot kick yourself");				
					}else{
						client.remoteInterface.RemoveUser(reply);
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		});
		btnKick.setBounds(285, 46, 93, 29);
		frame.getContentPane().add(btnKick);
	}

	public void uploadInfo() throws RemoteException {
		// TODO Auto-generated method stub
		client.remoteInterface.uploadUserInfo(client.username);
	}
	class userListListener implements Runnable{		
		public void run() {
			try {
				while(true) {
					Thread.sleep(500);
					if(client.buildConnection()){
						try {
							//if a client requires to connect
							if(client.remoteInterface.getRequire()==1) {
								int choice=JOptionPane.showConfirmDialog(frame, "Are you allowed this user to connect?", "Warning", JOptionPane.YES_NO_OPTION);
								//if the manager allow the connection
								if(choice == JOptionPane.YES_OPTION){
									client.remoteInterface.setAllow(1);
									client.remoteInterface.setRequire(0);
								}else {
									client.remoteInterface.setAllow(0);
									client.remoteInterface.setRequire(0);	
								}
							}
						} catch (HeadlessException e) {
							e.printStackTrace();
						} catch (RemoteException e) {
							e.printStackTrace();
						}
						String[] namelist = client.getUserNameList();					
						ListModel<String> UserList = Userlist.getModel();
						if(UserList.getSize() != namelist.length) {
							Userlist.setListData(namelist);	
						}
					}
				}
			}catch(NullPointerException e) {
				System.out.println("NullPointerException caught");
				e.printStackTrace();
			} catch (InterruptedException e) {
				System.out.println("InterruptedException caught");
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}				
}
