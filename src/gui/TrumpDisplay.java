package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import ai.Suit;

public class TrumpDisplay extends JPanel
{
	//////////////////////////////////
	//        Class Members         //
	//////////////////////////////////
	
	private JLabel title;
	private JPanel trumpPanel;
	
	private BufferedImage imgTrump;
	private Suit trump;
	
	//////////////////////////////////
	//         Constructors         //
	//////////////////////////////////
	
	public TrumpDisplay(int panelSize)
	{
		super();
		
		setSize(panelSize, 95);
		setLocation(750, 615);
		setVisible(true);
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		
		GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        setLayout(layout);
        
        try 
		{ 
			imgTrump = ImageIO.read(new File("images/card-suits.jpeg"));
		}
		catch (IOException e) { System.out.println(e.toString()); }
        
        trump = null;
        
        title = new JLabel("Trump");
        constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.ipadx = 10;
        constraints.ipady = 10;
        constraints.anchor = GridBagConstraints.CENTER;
        layout.setConstraints(title, constraints);
		add(title);
		
		trumpPanel = new JPanel();
		trumpPanel.setOpaque(false);
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.ipadx = 40;
        constraints.ipady = 40;
        constraints.anchor = GridBagConstraints.CENTER;
        layout.setConstraints(trumpPanel, constraints);
		add(trumpPanel);
		
		repaint();
	}
	
	//////////////////////////////////
	//       Public Functions       //
	//////////////////////////////////
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		drawTrump(g, trumpPanel.getLocation().x, trumpPanel.getLocation().y, trump);
	}
	
	public void UpdateTrump(Suit suit) 
	{ 
		trump = suit;
		
		repaint();
	}
	
	//////////////////////////////////
	//      Private Functions       //
	//////////////////////////////////
	
	private void drawTrump(Graphics g, int positionX, int positionY, Suit suit)
	{
		if(suit == null) return;
		
		int suitWidth = 40;
		int suitHeight = 40;
		
		if(suit == Suit.CLUB) 
		{
			g.drawImage(imgTrump, positionX, positionY, suitWidth+positionX, suitHeight+positionY, 
						90, 0, 220, 135, null);
		}
		else if(suit == Suit.HEART)
		{
			g.drawImage(imgTrump, positionX, positionY, suitWidth+positionX, suitHeight+positionY, 
						90, 165, 220, 310, null);
		}
		else if(suit == Suit.SPADE)
		{
			g.drawImage(imgTrump, positionX, positionY, suitWidth+positionX, suitHeight+positionY, 
						300, 0, 420, 135, null);
		}
		else if(suit == Suit.DIAMOND)
		{
			g.drawImage(imgTrump, positionX, positionY, suitWidth+positionX, suitHeight+positionY, 
						300, 165, 420, 310, null);
		}
	}
}
