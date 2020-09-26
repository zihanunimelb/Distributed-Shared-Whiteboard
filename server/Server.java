package server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.ArrayIndexOutOfBoundsException;
import javax.swing.JOptionPane;

import remote.RMI;

public class Server {
	public static RMI rmi;
	public static void main(String[] args) {
		Server server = new Server();
		try {
			boolean isInitiated = Server.initiateRMI(args[0]);
			if(isInitiated) {
				System.out.println("RMI initiated. Waiting for connection...");
			}
			else {
				System.out.println("RMI initiation failed.");
			}
		} catch (ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
			System.out.println( "RMI initiation failed. Lack Parameters");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println( "RMI initiation failed.");
		}
		
	}
	public static boolean initiateRMI(String port){
		try {
			if (Integer.parseInt(port) <= 1024 || Integer.parseInt(port) >= 49151) {
				System.out.println("Invalid Port Number");
				System.exit(-1);
			}
			rmi = new RMIServer();
			Registry registry = LocateRegistry.createRegistry(Integer.parseInt(port.trim()));
			registry.rebind("WhiteBoard", rmi);
			System.out.println("port: " + Integer.parseInt(port.trim()));
			return true;
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		} catch (NumberFormatException e) {
			System.out.println("Port must be a number");
			return false;
		}
	}
	
}
