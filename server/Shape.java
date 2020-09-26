package server;


import java.awt.*;

public class Shape {
    private int x, x1, y, y1;
    private String type;
    private Color c;
    private Stroke stroke;
    private String input;
    public Shape(Graphics g, int x, int y, int x1, int y1, String type, Color c,Stroke stroke)
    {
        this.x = x;
        this.y = y;
        this.x1 = x1;
        this.y1 = y1;
        this.type = type;
        this.c = c;
        this.stroke = stroke;
    }
    public Shape(Graphics g, int x, int y, String in, String type, Color color)
    {
        this.x =x;
        this.y = y;
        this.input = in;
        this.type = type;
        this.c =color;
    }
    public void rePaint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if(type.equals("Text")){
        	g.setColor(c);
            g.drawString(input,x,y);
        }
        else {
            g2.setColor(c);
            g2.setStroke(stroke);
            if (type.equals("Line")) {
                g.drawLine(x, y, x1, y1);
            } else if (type.equals("Rect")) {
                g.drawRect(Math.min(x, x1), Math.min(y, y1), Math.abs(x - x1), Math.abs(y - y1));
            } else if (type.equals("Oval")) {
                g.drawOval(Math.min(x, x1), Math.min(y, y1), Math.abs(x - x1), Math.abs(y - y1));
            }
        }
        
    }
}
