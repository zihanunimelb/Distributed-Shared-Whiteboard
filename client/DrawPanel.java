package client;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import remote.RMI;
public class DrawPanel extends JPanel {
	Color color = Color.BLACK;
	String command = "Line";
	JPanel jp;
	private String input;
	private RMI rmi;
	private BufferedImage image;
    private static ArrayList<Shape> shapelist = new ArrayList<Shape>();
	private Stroke selectStroke = new BasicStroke(1.0f);
    private int x,y;
    
    public void clear() {
    	shapelist = new ArrayList<Shape>();
    	image = null;
    }
    public void load(BufferedImage image) {
    	clear();
    	repaint();
    	this.image = image;
    }
    public void setInput(String input){
    	this.input = input;
    }
	public void setCommand(String command) {
		// TODO Auto-generated method stub
		this.command = command;
	}
	public void setColor(Color color) {
		// TODO Auto-generated method stub
		this.color = color;
	}
	public void setRMI(RMI rmi) {
		// TODO Auto-generated method stub
		this.rmi = rmi;
	}
	public void paint(Graphics g){
        super.paint(g);
        if(image != null) {
        	g.drawImage(image, 0, 0, this);
        }
        shapepaint(g);
    }
	public void shapepaint(Graphics g) {
   	 for (int i = 0; i < shapelist.size(); i++) {
            if (shapelist.get(i) != null)
                shapelist.get(i).rePaint(g);
        }
    }
    public BufferedImage save_image() {
    	BufferedImage image = new BufferedImage(this.getSize().width,this.getSize().height,BufferedImage.TYPE_INT_BGR);	
		Graphics2D graphics = image.createGraphics();
        this.paint(graphics);
        graphics.dispose();
        return image;
    }
	
	public byte[] tobyte(){
		BufferedImage image = save_image();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ImageIO.write(image,"png", out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] b = out.toByteArray();
		return b;
	}
	public void syn_image() {
		try {	        
			byte[] b = this.tobyte();
			rmi.draw(b);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Server disconnected", "error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public DrawPanel() {
        addMouseListener(new MouseListener() {
            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
                setColor(color);
            }
           public void mouseReleased(MouseEvent e) {
            	Graphics2D g = (Graphics2D)getGraphics();
            	g.setColor(color);
            	if(rmi != null) {
	                int x1 = e.getX();
	                int y1 = e.getY();
	                if(command.equals("Line")) {
	                	Shape s= new Shape(g,x,y,x1,y1,command,color,selectStroke);
	                    shapelist.add(s);
	                    g.drawLine(x,y, x1, y1);
	                }
	                if(command.equals("Rect")) {
	                    Shape s= new Shape(g,x,y,x1,y1,command,color,selectStroke);
		                shapelist.add(s);
	                    g.drawRect(Math.min(x, x1),Math.min(y, y1), Math.abs(x1 - x), Math.abs(y1 - y));
	                }
	                if(command.equals("Oval")) {
	                	Shape s= new Shape(g,x,y,x1,y1,command,color,selectStroke);
	                	shapelist.add(s);
	                    g.drawOval(Math.min(x, x1),Math.min(y, y1), Math.abs(x1 - x), Math.abs(y1 - y));
	                }
	                if(command.equals("Text")){
	                	Shape s = new Shape(g, x1, y1, input, command, color);
	                	shapelist.add(s);
	                	g.drawString(input,x1,y1);
	                }
	                syn_image();
            	}
           }
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
        });
        keepsyn.start();
    }
	public void load(byte[] b) throws IOException {
		ByteArrayInputStream in = new ByteArrayInputStream(b);
		BufferedImage image = ImageIO.read(in);
		this.load(image);
	}
	Thread keepsyn = new Thread(new Runnable() {
		public void run() {
			try{
				while(true){
					if(!Arrays.equals(tobyte(), rmi.getbyte())){
						load(rmi.getbyte());
					}
				}
			}catch(RemoteException e){
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	});
}
