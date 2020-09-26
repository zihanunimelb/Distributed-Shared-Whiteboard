package server;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import remote.RMI;

public class RMIServer extends UnicastRemoteObject implements RMI {
	
	private DrawingBoard whiteboard = null;
	private ArrayList<String> clientInfo;
	private int allow = 0;
	private int require = 0;
	private String KickUser;
	public byte[] getbyte(){
		if(this.whiteboard == null)
			return null;
		else
			return this.whiteboard.getpanel().tobyte();
	}
	protected RMIServer() throws RemoteException{
		this.clientInfo = new ArrayList<String>();
	}
	public void uploadUserInfo(String username) throws RemoteException {
		this.clientInfo.add(username);		
	}
	public void RemoveUser(String username) throws RemoteException {
		this.clientInfo.remove(username);		
	}
	public void removeAllInfo() throws RemoteException {
		this.clientInfo.clear();
		System.out.println("All users have left the system.");
	}
	public ArrayList<String> getUserInfo() {
		return this.clientInfo;
	}
	
	public void AllowClient(){
		this.require = 1;
	}
	public int getRequire(){
		return this.require;
	}
	public void setAllow(int i){
		this.allow = i;
	}
	public void setRequire(int i){
		this.require = i;
	}
	public int getAllow(){
		return this.allow;
	}
	public void createWhiteBoard(){
		this.whiteboard = new DrawingBoard();
		this.whiteboard.frame.setVisible(false);
	}
	public void setKickUsername(String string){
		this.KickUser = string;
	}
	public String KickUser(){
		if(this.KickUser.equals("")) {
			return "NOTSELECTED";
		}else if (!this.clientInfo.contains(this.KickUser)){
			return "NOTINTHELIST";
		}else{
			return this.KickUser;
		}
	}
	public void closeManagerBoard(){
		this.whiteboard.frame.dispose();
		this.whiteboard = null;
	}
	public void draw(byte[] b){
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(b);
			BufferedImage image = ImageIO.read(in);
			this.whiteboard.getpanel().load(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
