package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import server.DrawingBoard;

public interface RMI extends Remote{
	ArrayList<String> getUserInfo() throws RemoteException;
	void uploadUserInfo(String username) throws RemoteException;
	void RemoveUser(String username) throws RemoteException;
	void removeAllInfo() throws RemoteException;
	void setAllow(int i) throws RemoteException;
	int getRequire() throws RemoteException;	
	void setRequire(int i) throws RemoteException;
	int getAllow() throws RemoteException;
	void setKickUsername(String string) throws RemoteException;
	String KickUser() throws RemoteException;
	void createWhiteBoard() throws RemoteException;
	void closeManagerBoard() throws RemoteException;	
	void AllowClient() throws RemoteException;
	void draw(byte[] b) throws RemoteException;
	byte[] getbyte() throws RemoteException;
}
