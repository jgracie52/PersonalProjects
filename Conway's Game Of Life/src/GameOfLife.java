import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
//import javax.swing.Timer;

public class GameOfLife {
	
	static int timerSpeed = 100;
	static Timer timer;
	static Timer retimer;
	static boolean stpClicked = false;
	static boolean startClicked = false;
	static GridHandler grid;
	
	public static void main(String[] args)
	{
		int gridSize = 200;
		
		JPanel masterPanel = new JPanel(new BorderLayout());
		//JPanel gamePanel = new JPanel(new GridLayout(gridSize,gridSize));
		JPanel controlPanel = new JPanel(new GridLayout(1,3));
		
		
		
		JFrame game = new JFrame("Game Of Life");

		JButton startbtn=new JButton("Start"); 
		JSlider tickSpeed = new JSlider(0, 200, 100);
		JButton stopbtn=new JButton("Pause");  
		
		//JSlider gameSize = new JSlider(100, 500, 200);
		
		/*gameSize.setToolTipText("Set Game Board Size");
		gameSize.setMajorTickSpacing(100);
		gameSize.setPaintTicks(true);
		gameSize.setSnapToTicks(true);*/
		
		tickSpeed.setToolTipText("Set Timer Speed");
		tickSpeed.setMajorTickSpacing(25);
		tickSpeed.setPaintTicks(true);
	    controlPanel.add(startbtn);
	    startbtn.setToolTipText("Start the Game of Life");
	    controlPanel.add(tickSpeed);
	    controlPanel.add(stopbtn);
	    stopbtn.setToolTipText("Pause the Game");
		//controlPanel.add(gameSize);
		
		stopbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae)
			{
				if(stpClicked)
				{
					timer.start();	
					stopbtn.setText("Pause");
					stpClicked = false;
				}
				else
				{
						timer.stop();					
					stopbtn.setText("Unpause");
					stpClicked = true;
				}
			}
		});
		
		/*gameSize.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent a)
			{
				timer.stop();
				//grid.gridResize(((JSlider)a.getSource()).getValue());
				timer.stop();
			}
		});*/
				
		
	    //masterPanel.add(gamePanel, BorderLayout.CENTER);
		masterPanel.add(controlPanel, BorderLayout.NORTH);
	    
	    game.add(masterPanel, BorderLayout.CENTER);
		
		game.setSize(800,800);//400 width and 500 height  
		game.setVisible(true);//making the frame visible
		
		gameStart(gridSize, masterPanel, startbtn, tickSpeed);
		
	}
	
	static void gameStart(int gridSize, JPanel masterPanel, JButton startbtn, JSlider tickSpeed)
	{
		grid = new GridHandler(gridSize);
		masterPanel.add(grid, BorderLayout.CENTER);
		
		tickSpeed.addChangeListener(new ChangeListener()
		{
			public void stateChanged(ChangeEvent e)
			{
				timer.stop();
				timerSpeed = ((JSlider)e.getSource()).getValue();
				timer.setDelay(timerSpeed);
				timer.start();
			}
		});
		
		//Instantiate grid
		startbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				if(!startClicked)
				{
					grid.Instantiate();
					//grid.repaint();
					startClicked = true;
					//Run game loop
					timer = new Timer(timerSpeed, new ActionListener() {
			            @Override
			            public void actionPerformed(ActionEvent ae) {
			                grid.updateGrid();
			                grid.repaint();
			            }
			        });
					timer.start();
				}
	    }
		});
	}
	
	
}
