package client;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.rmi.RemoteException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.ListModel;

import java.awt.HeadlessException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ClientGUI {
	Client client;
	JFrame frame;
	JList<String> Userlist;
	/**
	 * Launch the application.
	 */
	
	/**
	 * Create the application.
	 */
	public ClientGUI(Client client) {
		initialize();
		this.client = client;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 448, 635);
		frame.getContentPane().setLayout(null);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				try{
					client.remoteInterface.RemoveUser(client.username);	
					System.exit(0);
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
					JOptionPane.showConfirmDialog(frame, "Server Connection Lost.");
				}
				if(client.whiteBoard != null){
					client.whiteBoard.frame.dispose();
				}
		        frame.dispose();
		        client.disconnect();
		    }
		});
		JButton btnJoinWhiteboard = new JButton("Join WhiteBoard");
		btnJoinWhiteboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try{
					byte[] serverboard;
					try {
						serverboard = client.remoteInterface.getbyte();
					} catch (RemoteException e) {
						serverboard = null;
						JOptionPane.showMessageDialog(frame, "1");
						e.printStackTrace();
					}
					if(serverboard == null){
						JOptionPane.showMessageDialog(frame, "Manager have not created a board.");
					} else if(client.whiteBoard == null){
						client.whiteBoard = new DrawingBoard();
						client.whiteBoard.frame.setTitle("Client WhiteBoard");
						client.whiteBoard.getpanel().setRMI(client.remoteInterface);
						client.whiteBoard.frame.setVisible(true);
						check.start();
						client.whiteBoard.frame.addWindowListener(new java.awt.event.WindowAdapter() {
						    @Override
						    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
								client.whiteBoard.frame.dispose();
								client.whiteBoard = null;
						    }
						});
						byte[] clientboard = client.whiteBoard.getpanel().tobyte();
					} else{
						JOptionPane.showMessageDialog(frame, "You have already joined a WhiteBoard");
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
				} 
			}
		});
		btnJoinWhiteboard.setBounds(129, 5, 167, 29);
		frame.getContentPane().add(btnJoinWhiteboard);
		
		JLabel lblUserList = new JLabel("User List");
		lblUserList.setBounds(168, 49, 81, 21);
		frame.getContentPane().add(lblUserList);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(129, 84, 167, 458);
		frame.getContentPane().add(scrollPane);
		
		Userlist = new JList();
		scrollPane.setViewportView(Userlist);
	}
	public void uploadInfo() throws RemoteException {
		// TODO Auto-generated method stub
		try{
			client.remoteInterface.uploadUserInfo(client.username);
		} catch (NullPointerException e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(frame, "Server connection lost");
		}
	}

	public void createUserListListener() {
		Thread userListUpdate = new Thread(new userListListener());
		userListUpdate.start();
	}
	
	class userListListener implements Runnable{
		public void run() {
			try {
				while(true) {
					// Reduce the memory load
					Thread.sleep(500);
					
					// Get the user info to be displayed
					String[] namelist = client.getUserNameList();
					if(namelist[0] == "Manager closed the server") {
						JOptionPane.showMessageDialog(frame, "Manager has closed the server.");
			        	frame.dispose();
			        	client.disconnect();
			        	System.exit(0);
					}else if(namelist[0] == "Being kicked.") {
						JOptionPane.showMessageDialog(frame, "You are kicked by the manager");
			        	frame.dispose();
			        	client.disconnect();
			        	System.exit(0);
					}else {
						ListModel<String> UserList = Userlist.getModel();
						if(UserList.getSize() != namelist.length) {	
							Userlist.setListData(namelist);													
						}
					}
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(frame, "Server connection lost");		
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (RemoteException e) {
				e.printStackTrace();
			} 
		}
	}
	Thread check = new Thread(new Runnable() {
		public void run() {
			try {
				while(true){
					if (client.whiteBoard != null && client.remoteInterface.getbyte() == null){
						JOptionPane.showMessageDialog(client.whiteBoard.frame, "Manager has closed the WhiteBoard");
						client.whiteBoard.frame.dispose();
						client.whiteBoard = null;
						break;
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
	});
}
