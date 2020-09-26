package client;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import remote.RMI;

public class Client extends UnicastRemoteObject{
	private String host;
	private String port;
	public String username;	
	public RMI remoteInterface;
	public DrawingBoard whiteBoard;
	public Client(String hostname, String port, String username) throws RemoteException {
		// Get the host name and port number
		this.host = hostname;
		this.port = port;
		this.username = username;
	}
	public boolean buildConnection(){
		try {			
			Registry registry = LocateRegistry.getRegistry(this.host.trim(), Integer.parseInt(this.port));
			this.remoteInterface = (RMI) registry.lookup("WhiteBoard");
			return true;
		} catch (Exception e) {			
			e.printStackTrace();
			return false;
		}
	}
	
	public String[] getUserNameList() throws RemoteException {
		try {
			ArrayList<String> userList = this.remoteInterface.getUserInfo();
			if (userList.contains(username)){
				return (String[])userList.toArray(new String[0]);
			}else if(userList.isEmpty()) {
				return new String[]{"Manager closed the server"};
			}else{
				return new String[]{"Being kicked."};
			}
		} catch (NullPointerException e1) {
			e1.printStackTrace();
			return null;
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("ArrayIndexOutOfBoundsException");
			return null;
		} 
		
	}
	public void setDrawingBoard(DrawingBoard whiteboard){
		this.whiteBoard = whiteboard;
	}
	public void disconnect() {
		this.remoteInterface = null;
	}
}
