package tk.bobbzorzen;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class GuiMain extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private static Game game;
	private static String markerSign = "";
	private static ServerSocket server;
	private static Socket client;
	private static DataPackage dPackage;
	
	private static Container contentPane;
	
	private ButtonListener buttonListener = new ButtonListener();
	
	private JLabel menuLabel = new JLabel("Tic-Tac-Toe");
	private JButton menuCreate = new JButton("Create");
	private JButton menuJoin = new JButton("Join");
	private JTextField menuIp = new JTextField("");
	private JTextField menuPort = new JTextField("");
	private JButton menuPlay = new JButton("Play");
	private static JLabel winText = new JLabel();
	private static JLabel waitingText = new JLabel();
	
	private static ArrayList<JButton> buttonGrid;
	
	
	
	
	
	
	
	private static Runnable accept = new Runnable()
	{
		public void run()
		{
			try
			{
				client = server.accept();
				game();
			}
			catch (Exception e)
			{
				
			}
		}
	};
	
	private static Runnable waiting = new Runnable()
	{
		public void run()
		{
			try
			{
				changeButtons(false);
				ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
				DataPackage move = (DataPackage)ois.readObject();
				if(move.getOldPos() != 9)
				{
					game.enemyClick(move.getOldPos());
					buttonGrid.get(move.getOldPos()).setText("");
				}
				game.enemyClick(move.getNewPos());
				
				String enemyMarker;
				if(markerSign == "X"){enemyMarker = "O";}else{enemyMarker = "X";}
				
				buttonGrid.get(move.getNewPos()).setText(enemyMarker);
				changeButtons(true);
				disableEnemy();
				if(game.winning())
				{
					changeButtons(false);
					winText.setText("Opponent Won");
				}
			}
			catch (Exception e)
			{
				
			}
		}
	};
	
	
	
	
	
	
	
	
	
	
	
	
	public GuiMain()
	{
		super();
		this.initVars();
		this.configFrame();
		this.menu();
	}
	
	private void initVars()
	{
		game = new Game();
		
		contentPane = this.getContentPane();
		
		this.menuCreate.addActionListener(buttonListener);
		this.menuJoin.addActionListener(buttonListener);
		this.menuPlay.addActionListener(buttonListener);
		buttonGrid = new ArrayList<JButton>();
		
		for(int i = 0; i<9; i++) {
			buttonGrid.add(new JButton());
			buttonGrid.get(i).setText("");
			buttonGrid.get(i).addActionListener(buttonListener);
			buttonGrid.get(i).setActionCommand(""+i);
		}
		dPackage = new DataPackage();
	}
	
	private void configFrame()
	{
		this.setSize(600, 500);
		this.setTitle("TIC TAC ROW");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
	}
	private void waitingScreen()
	{
		clearPane();
		contentPane.setLayout(new GridLayout(1, 1));
		waitingText.setHorizontalAlignment(JLabel.CENTER);
		waitingText.setText("Waiting for other player to join!");
		contentPane.add(waitingText);
	}
	private void menu()	{
		clearPane();
		
		contentPane.setLayout(new GridLayout(6, 1));
		
		menuIp.setBorder(BorderFactory.createTitledBorder("IP-adress"));
		menuPort.setBorder(BorderFactory.createTitledBorder("Port"));
		
		this.menuIp.setVisible(false);
		this.menuPort.setVisible(false);
		this.menuPlay.setEnabled(false);
		
		contentPane.add(menuLabel);
		contentPane.add(menuCreate);
		contentPane.add(menuJoin);
		contentPane.add(menuIp);
		contentPane.add(menuPort);
		contentPane.add(menuPlay);
	}
	private static void changeButtons(Boolean b)
	{
		for(int i = 0; i < 9; i++)
		{
			buttonGrid.get(i).setEnabled(b);
		}
	}
	private static void disableEnemy()
	{
		String enemyMarker;
		if(markerSign == "X"){enemyMarker = "O";}else{enemyMarker = "X";}
		for(int i = 0; i < 9; i++)
		{
			if(buttonGrid.get(i).getText().equals(enemyMarker))
			{
				buttonGrid.get(i).setEnabled(false);
			}
		}
	}
	private static void clearPane()
	{
		contentPane.removeAll();
		contentPane.revalidate();
		contentPane.repaint();
	}
	private static void game()
	{
		clearPane();
		contentPane.setLayout(new GridLayout(4, 3));
		for(int i = 0; i < 9; i++)
		{
			contentPane.add(buttonGrid.get(i));
		}
		
		contentPane.add(new JLabel(""));
		contentPane.add(winText);
		winText.setHorizontalAlignment(JLabel.CENTER);
		System.out.println(contentPane.getComponentCount());
		contentPane.revalidate();
	}
	
	private class ButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event)
		{
			String buttonText = event.getActionCommand();
			
			if(buttonText == "Create")
			{
				create();
			}
			else if(buttonText == "Join")
			{
				join();
			}
			else if(buttonText == "Play")
			{
				play();
			}
			else
			{
				int iTemp = game.click(Integer.parseInt(buttonText));
				if(iTemp == 1)
				{
					buttonGrid.get(Integer.parseInt(buttonText)).setText(markerSign);
					dPackage.setNewPos(Integer.parseInt(buttonText));
					ObjectOutputStream oos;
					try
					{
						oos = new ObjectOutputStream(client.getOutputStream());
						oos.writeObject(dPackage);
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					
					new Thread(waiting).start();
					
				}
				else if(iTemp == 2)
				{
					buttonGrid.get(Integer.parseInt(buttonText)).setText("");
					buttonGrid.get(Integer.parseInt(buttonText)).setEnabled(false);
					dPackage.setOldPos(Integer.parseInt(buttonText));
				}
			}
			
			if(game.winning())
			{
				changeButtons(false);
				winText.setText("You Won");
			}
		}
	}
	private void create() {
		this.menuIp.setVisible(false);
		this.menuPort.setVisible(true);
		this.menuPlay.setEnabled(true);
		this.menuIp.setText("");
		this.menuPort.setText("4444");
	}
	private void join() {
		this.menuIp.setVisible(true);
		this.menuPort.setVisible(true);
		this.menuPlay.setEnabled(true);
		this.menuIp.setText("192.168.0.100");
		this.menuPort.setText("4444");
	}
	private void play() {
		if(this.menuIp.isVisible()) //Join game
		{
			if(this.menuIp.getText().length() != 0 && this.menuPort.getText().length() != 0)
			{
				markerSign = "X";
				try
				{
					client = new Socket(this.menuIp.getText(), Integer.parseInt(this.menuPort.getText()));
					new Thread(waiting).start();
				}
				catch(Exception e)
				{
					JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Alert", JOptionPane.ERROR_MESSAGE);
				}
				game();
			}		
		}
		else if(!this.menuIp.isVisible()) //Host game
		{
			if(this.menuPort.getText().length() != 0)
			{
				markerSign = "O";
				try
				{
					server = new ServerSocket(Integer.parseInt(this.menuPort.getText()),0,InetAddress.getLocalHost());
					waitingScreen();
					new Thread(accept).start();
					
				}
				catch(IOException ex)
				{
					JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Alert", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
				
			}
		}
	}
	public static void main(String[] args)
	{
		GuiMain TTT = new GuiMain();
		TTT.setVisible(true);
	}
}
