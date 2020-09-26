package server;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import remote.RMI;

import javax.swing.JToolBar;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

public class DrawingBoard {
	private JPanel jp;
	private DrawPanel panel;
	JFrame frame;
	private JTextField textField;
	private RMI rmi;
	public DrawingBoard() {
		initialize();
	}
	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 798, 598);
		jp = new JPanel();
		frame.setContentPane(jp);
		jp.setLayout(null);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setBounds(15, 0, 868, 23);
		jp.add(toolBar);
		
		JButton btnLine = new JButton("Line");
		btnLine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panel.setCommand("Line");
			}
		});
		btnLine.setBackground(Color.WHITE);
		toolBar.add(btnLine);
		
		JButton btnRect = new JButton("Rect");
		btnRect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panel.setCommand("Rect");
			}
		});
		btnRect.setBackground(Color.WHITE);
		toolBar.add(btnRect);
		
		JButton btnOval = new JButton("Oval");
		btnOval.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panel.setCommand("Oval");
			}
		});
		btnOval.setBackground(Color.WHITE);
		toolBar.add(btnOval);
		
		JButton btnBlue = new JButton("BLU");
		btnBlue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panel.setColor(Color.BLUE);
			}
		});
		
		JButton btnText = new JButton("Text");
		btnText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String input = textField.getText().trim().toString();
				panel.setInput(input);
				panel.setCommand("Text");
			}
		});
		btnText.setBackground(Color.WHITE);
		toolBar.add(btnText);
		btnBlue.setForeground(Color.BLUE);
		btnBlue.setBackground(Color.BLUE);
		toolBar.add(btnBlue);
		
		JButton btnGreen = new JButton("GRE");
		btnGreen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panel.setColor(Color.GREEN);
			}
		});
		btnGreen.setForeground(Color.GREEN);
		btnGreen.setBackground(Color.GREEN);
		toolBar.add(btnGreen);
		
		JButton btnRed = new JButton("Red");
		btnRed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panel.setColor(Color.RED);
			}
		});
		btnRed.setBackground(Color.RED);
		btnRed.setForeground(Color.RED);
		toolBar.add(btnRed);
		
		JButton btnYellow = new JButton("YEL");
		btnYellow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panel.setColor(Color.YELLOW);
			}
		});
		btnYellow.setForeground(Color.YELLOW);
		btnYellow.setBackground(Color.YELLOW);
		toolBar.add(btnYellow);
		
		panel = new DrawPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(15, 38, 746, 468);
		jp.add(panel);
		
		textField = new JTextField();
		textField.setBounds(15, 515, 746, 27);
		jp.add(textField);
		textField.setColumns(10);
	}

	public void setRMI(RMI remoteInterface) {
		this.rmi = remoteInterface;
	}


	public DrawPanel getpanel() {
		return panel;
	}
}
