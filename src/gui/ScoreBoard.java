package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.*;

public class ScoreBoard extends JPanel 
{
	//////////////////////////////////
	//        Class Members         //
	//////////////////////////////////
	
	private JPanel[] pegs;
	private int score1;
	private int score2;
	
	//////////////////////////////////
	//         Constructors         //
	//////////////////////////////////
	
	public ScoreBoard(int panelSize)
	{
		super();
		
		setSize(panelSize / 2, panelSize);
		setLocation(750, 10);
		setVisible(true);
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		
		GridBagLayout layout = new GridBagLayout();
	    GridBagConstraints constraints = new GridBagConstraints();
	    setLayout(layout);
	    
	    int i;
	    pegs = new JPanel[21];
	    
	    pegs[0] = new JPanel();
		pegs[0].setOpaque(false);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.ipadx = 10;
        constraints.ipady = 10;
        constraints.anchor = GridBagConstraints.CENTER;
        layout.setConstraints(pegs[0], constraints);
		add(pegs[0]);
	    
	    for(i=1; i<=10; i++)
		{
			pegs[i] = new JPanel();
			pegs[i].setOpaque(false);
			constraints.gridx = 0;
			constraints.gridy = 11-i;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			constraints.weightx = 1;
	        constraints.weighty = 1;
	        constraints.ipadx = 10;
	        constraints.ipady = 10;
	        constraints.anchor = GridBagConstraints.CENTER;
	        layout.setConstraints(pegs[i], constraints);
			add(pegs[i]);
			
			pegs[i+10] = new JPanel();
			pegs[i+10].setOpaque(false);
			constraints.gridx = 1;
			constraints.gridy = 11-i;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			constraints.weightx = 1;
	        constraints.weighty = 1;
	        constraints.ipadx = 10;
	        constraints.ipady = 10;
	        constraints.anchor = GridBagConstraints.CENTER;
	        layout.setConstraints(pegs[i+10], constraints);
			add(pegs[i+10]);
		}
	    
	    UpdateScore(0, 0);
	}
	
	//////////////////////////////////
	//       Public Functions       //
	//////////////////////////////////
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		int i;
		
		for(i=0; i<pegs.length; i++)
		{
			if(i < 11 && i-1 == score1 || i > 10 && i-11 == score2 ||
					i==0 && score1 == 10 || i==0 && score2 == 10)
				g.fillOval(pegs[i].getLocation().x, pegs[i].getLocation().y, pegs[i].getWidth(), pegs[i].getHeight());
			else
				g.drawOval(pegs[i].getLocation().x, pegs[i].getLocation().y, pegs[i].getWidth(), pegs[i].getHeight());
		}
	}
	
	public void UpdateScore(int t1, int t2)
	{
		score1 = t1;
		score2 = t2;
		
		repaint();
	}
}
