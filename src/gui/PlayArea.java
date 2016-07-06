package gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.geom.*;
import java.awt.image.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;

import ai.Card;
import ai.Suit;

public class PlayArea extends JPanel 
{
	//////////////////////////////////
	//        Class Members         //
	//////////////////////////////////
	
	private BufferedImage imgFront;
	private BufferedImage imgBack;
	private BufferedImage imgSuit;
	
	private double cardWidth;
	private double cardHeight;
	
	private JButton actionButton;
	private boolean actionEnabled;
	
	private JButton aloneButton;
	private boolean aloneEnabled;
	private JLabel aloneStatus;
	
	private JPanel[] p1Cards;
	private JPanel[] p2Cards;
	private JPanel[] p3Cards;
	private JPanel[] p4Cards;
	
	private JPanel p1Zone;
	private JPanel p2Zone;
	private JPanel p3Zone;
	private JPanel p4Zone;
	
	private ArrayList<Card> p1Hand;
	private ArrayList<Card> p2Hand;
	private ArrayList<Card> p3Hand;
	private ArrayList<Card> p4Hand;
	
	private JPanel[] playerMarkers;
	private int turnMarker;
	private int winMarker;
	
	private JButton[] suitButtons;
	private BufferedImage[] suitIcons;
	
	private Card[] played;
	
	//////////////////////////////////
	//         Constructors         //
	//////////////////////////////////
	
	public PlayArea(int panelSize)
	{
		super();
		
		setSize(panelSize, panelSize);
		setLocation(10, 10);
		setVisible(true);
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		
		GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        setLayout(layout);
        
        int i;
		
		try 
		{ 
			imgBack = ImageIO.read(new File("images/card-back.jpg"));
			imgFront = ImageIO.read(new File("images/all-cards.png")); 
			imgSuit = ImageIO.read(new File("images/card-suits.jpeg"));
		}
		catch (IOException e) { System.out.println(e.toString()); }
		
		cardWidth = imgFront.getWidth() / 13;
		cardHeight = imgFront.getHeight() / 4;
		
		p1Cards = new JPanel[5];
		p2Cards = new JPanel[5];
		p3Cards = new JPanel[5];
		p4Cards = new JPanel[5];
		
		played = new Card[4];
        
		actionButton = new JButton("ACTION");
		actionButton.setEnabled(false);
		constraints.gridx = 3;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        layout.setConstraints(actionButton, constraints);
		add(actionButton);
		
		aloneButton = new JButton("");
		aloneButton.setEnabled(false);
		constraints.gridx = 2;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        layout.setConstraints(aloneButton, constraints);
		add(aloneButton);
		
		actionEnabled = false;
		
		aloneStatus = new JLabel("");
		constraints.gridx = 1;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.anchor = GridBagConstraints.CENTER;
        layout.setConstraints(aloneStatus, constraints);
		add(aloneStatus);
		
		for(i=0; i<5; i++)
		{
			p1Cards[i] = new JPanel();
			p1Cards[i].setOpaque(false);
			constraints.gridx = i+1;
			constraints.gridy = 6;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			constraints.weightx = 1;
	        constraints.weighty = 1;
	        constraints.ipadx = (int)cardWidth;
	        constraints.ipady = (int)cardHeight;
	        constraints.anchor = GridBagConstraints.CENTER;
	        layout.setConstraints(p1Cards[i], constraints);
			add(p1Cards[i]);
		}
		
		for(i=0; i<5; i++)
		{
			p2Cards[i] = new JPanel();
			p2Cards[i].setOpaque(false);
			constraints.gridx = 0;
			constraints.gridy = i+1;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			constraints.weightx = 1;
	        constraints.weighty = 1;
	        constraints.ipadx = (int)cardHeight;
	        constraints.ipady = (int)cardWidth;
	        constraints.anchor = GridBagConstraints.CENTER;
	        layout.setConstraints(p2Cards[i], constraints);
			add(p2Cards[i]);
		}
		
		for(i=0; i<5; i++)
		{
			p3Cards[i] = new JPanel();
			p3Cards[i].setOpaque(false);
			constraints.gridx = i+1;
			constraints.gridy = 0;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			constraints.weightx = 1;
	        constraints.weighty = 1;
	        constraints.ipadx = (int)cardWidth;
	        constraints.ipady = (int)cardHeight;
	        constraints.anchor = GridBagConstraints.CENTER;
	        layout.setConstraints(p3Cards[i], constraints);
			add(p3Cards[i]);
		}
		
		for(i=0; i<5; i++)
		{
			p4Cards[i] = new JPanel();
			p4Cards[i].setOpaque(false);
			constraints.gridx = 6;
			constraints.gridy = i+1;
			constraints.gridwidth = 1;
			constraints.gridheight = 1;
			constraints.weightx = 1;
	        constraints.weighty = 1;
	        constraints.ipadx = (int)cardHeight;
	        constraints.ipady = (int)cardWidth;
	        constraints.anchor = GridBagConstraints.CENTER;
	        layout.setConstraints(p4Cards[i], constraints);
			add(p4Cards[i]);
		}
		
		p1Zone = new JPanel();
		p1Zone.setOpaque(false);
		constraints.gridx = 3;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 2;
		constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.ipadx = (int)cardWidth;
        constraints.ipady = (int)cardHeight;
        constraints.anchor = GridBagConstraints.CENTER;
        layout.setConstraints(p1Zone, constraints);
		add(p1Zone);
		
		p2Zone = new JPanel();
		p2Zone.setOpaque(false);
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.ipadx = (int)cardHeight;
        constraints.ipady = (int)cardWidth;
        constraints.anchor = GridBagConstraints.CENTER;
        layout.setConstraints(p2Zone, constraints);
		add(p2Zone);
		
		p3Zone = new JPanel();
		p3Zone.setOpaque(false);
		constraints.gridx = 3;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 2;
		constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.ipadx = (int)cardWidth;
        constraints.ipady = (int)cardHeight;
        constraints.anchor = GridBagConstraints.CENTER;
        layout.setConstraints(p3Zone, constraints);
		add(p3Zone);
		
		p4Zone = new JPanel();
		p4Zone.setOpaque(false);
		constraints.gridx = 4;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.gridheight = 1;
		constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.ipadx = (int)cardHeight;
        constraints.ipady = (int)cardWidth;
        constraints.anchor = GridBagConstraints.CENTER;
        layout.setConstraints(p4Zone, constraints);
		add(p4Zone);
		
		playerMarkers = new JPanel[4];
		turnMarker = -1;
		winMarker = -1;
		
		playerMarkers[0] = new JPanel();
		playerMarkers[0].setOpaque(false);
		constraints.gridx = 4;
		constraints.gridy = 5;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.ipadx = 30;
        constraints.ipady = 30;
        constraints.anchor = GridBagConstraints.CENTER;
        layout.setConstraints(playerMarkers[0], constraints);
		add(playerMarkers[0]);
		
		playerMarkers[1] = new JPanel();
		playerMarkers[1].setOpaque(false);
		constraints.gridx = 1;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.ipadx = 30;
        constraints.ipady = 30;
        constraints.anchor = GridBagConstraints.CENTER;
        layout.setConstraints(playerMarkers[1], constraints);
		add(playerMarkers[1]);
		
		playerMarkers[2] = new JPanel();
		playerMarkers[2].setOpaque(false);
		constraints.gridx = 2;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.ipadx = 30;
        constraints.ipady = 30;
        constraints.anchor = GridBagConstraints.CENTER;
        layout.setConstraints(playerMarkers[2], constraints);
		add(playerMarkers[2]);
		
		playerMarkers[3] = new JPanel();
		playerMarkers[3].setOpaque(false);
		constraints.gridx = 5;
		constraints.gridy = 2;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.ipadx = 30;
        constraints.ipady = 30;
        constraints.anchor = GridBagConstraints.CENTER;
        layout.setConstraints(playerMarkers[3], constraints);
		add(playerMarkers[3]);
		
		suitButtons = new JButton[3];
		
		suitButtons[0] = new JButton();
		constraints.gridx = 2;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.ipadx = 30;
        constraints.ipady = 30;
        constraints.anchor = GridBagConstraints.CENTER;
        layout.setConstraints(suitButtons[0], constraints);
		add(suitButtons[0]);
		
		suitButtons[1] = new JButton();
		constraints.gridx = 3;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.ipadx = 30;
        constraints.ipady = 30;
        constraints.anchor = GridBagConstraints.CENTER;
        layout.setConstraints(suitButtons[1], constraints);
		add(suitButtons[1]);
		
		suitButtons[2] = new JButton();
		constraints.gridx = 4;
		constraints.gridy = 4;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.ipadx = 30;
        constraints.ipady = 30;
        constraints.anchor = GridBagConstraints.CENTER;
        layout.setConstraints(suitButtons[2], constraints);
		add(suitButtons[2]);
		
		suitIcons = new BufferedImage[3];
		suitIcons[0] = null;
		suitIcons[1] = null;
		suitIcons[2] = null;
		
		ToggleOff();
		ToggleAloneInvisible();
		
		ToggleSuitButtonsOff();
	}
	
	//////////////////////////////////
	//       Public Functions       //
	//////////////////////////////////
	
	public void UpdateHands(ArrayList<Card> p1, ArrayList<Card> p2, ArrayList<Card> p3, ArrayList<Card> p4)
	{
		p1Hand = p1;
		p2Hand = p2;
		p3Hand = p3;
		p4Hand = p4;
		
		repaint();
	}
	
	public void UpdatePlayed(Card p1, Card p2, Card p3, Card p4)
	{
		played[0] = p1;
		played[1] = p2;
		played[2] = p3;
		played[3] = p4;
		
		repaint();
	}
	
	public JButton getActionButton() { return actionButton; }
	
	public boolean isActionEnabled() { return actionEnabled; }
	
	public JButton getAloneButton() { return aloneButton; }
	
	public boolean isAloneEnabled() { return aloneEnabled; }
	
	public JPanel[] getP1Cards() { return p1Cards; }
	
	public JPanel getActiveZone(int player) 
	{ 
		player = (player < 0) ? 3 : player;
		
		switch(player)
		{
			case 0:		return p1Zone;
			case 1:		return p2Zone;
			case 2:		return p3Zone;
			case 3:		return p4Zone;
			default:	return null;
		}
	}
	
	public JButton[] getSuitButtons() { return suitButtons; }
	
	public void setTurnMarker(int marker) 
	{ 
		turnMarker = marker;
		repaint();
	}
	
	public void setWinMarker(int marker) 
	{ 
		winMarker = marker;
		repaint();
	}
	
	public void ToggleOff()
	{
		actionButton.setEnabled(false);
		actionButton.setVisible(false);
		actionButton.setText("");
		actionEnabled = false;
	}
	
	public void ToggleOn()
	{
		actionButton.setEnabled(true);
		actionButton.setVisible(true);
		actionEnabled = true;
	}
	
	public void ToggleFirstDeal()
	{
		ToggleOn();
		actionButton.setText("Cut");
	}
	
	public void ToggleDeal()
	{
		ToggleOn();
		actionButton.setText("Deal");
	}
	
	public void TogglePass()
	{
		ToggleOn();
		actionButton.setText("Pass");
	}
	
	public void ToggleNext()
	{
		ToggleOn();
		actionButton.setText("Next");
	}
	
	public void ToggleAloneInvisible()
	{
		aloneButton.setEnabled(false);
		aloneButton.setVisible(false);
		aloneButton.setText("");
		aloneStatus.setText("");
	}
	
	public void ToggleAloneVisible()
	{
		aloneButton.setEnabled(true);
		aloneButton.setVisible(true);
		ToggleAloneNo();
	}
	
	public void ToggleAloneYes()
	{
		aloneButton.setText("Go Together");
		aloneEnabled = true;
		aloneStatus.setText("<html>You are currently<br>going ALONE</html>");
	}
	
	public void ToggleAloneNo()
	{
		aloneButton.setText("Go Alone");
		aloneEnabled = false;
		aloneStatus.setText("<html>You are currently<br>going TOGETHER</html>");
	}
	
	public void ToggleSuitButtonsOn(Suit[] suits)
	{
		if(suits.length != 3) return;
		
		int i;
		for(i=0; i<3; i++)
		{
			suitButtons[i].setEnabled(true);
			suitButtons[i].setVisible(true);
			
			suitIcons[i] = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
			drawSuitToIcon(suitIcons[i], suits[i]);
			
			suitButtons[i].setIcon(new ImageIcon(suitIcons[i]));
		}
	}
	
	public void ToggleSuitButtonsOff()
	{
		int i;
		for(i=0; i<3; i++)
		{		
			suitIcons[i] = null;
	
			suitButtons[i].setEnabled(false);
			suitButtons[i].setVisible(false);
		}
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		int i, value, suit;
		
		value = 0;
		suit = 0;
		
		for(i=0; i<5; i++)
		{
			
			if(p1Hand != null)
			{
				if(p1Hand.size() > i)
				{
					value = GetCardValue(p1Hand.get(i));
					suit = GetCardSuit(p1Hand.get(i));
					if(p1Hand.size() > i) drawCard(g, 0, p1Cards[i].getLocation().x, p1Cards[i].getLocation().y, value, suit);
				}
			}
			
			if(p2Hand != null)
			{
/*				if(p2Hand.size() > i)
				{
					value = GetCardValue(p2Hand.get(i));
					suit = GetCardSuit(p2Hand.get(i));
					drawCard(g, 90, p2Cards[i].getLocation().x, p2Cards[i].getLocation().y, value, suit);
				}
*/				if(p2Hand.size() > i) drawBack(g, 90, p2Cards[i].getLocation().x, p2Cards[i].getLocation().y);
			}
			
			if(p3Hand != null)
			{
/*				if(p3Hand.size() > i)
				{
					value = GetCardValue(p3Hand.get(i));
					suit = GetCardSuit(p3Hand.get(i));
					drawCard(g, 180, p3Cards[i].getLocation().x, p3Cards[i].getLocation().y, value, suit);
				}
*/				if(p3Hand.size() > i) drawBack(g, 180, p3Cards[i].getLocation().x, p3Cards[i].getLocation().y);
			}
			
			if(p4Hand != null)
			{
/*				if(p4Hand.size() > i)
				{
					value = GetCardValue(p4Hand.get(i));
					suit = GetCardSuit(p4Hand.get(i));
					drawCard(g, 270, p4Cards[i].getLocation().x, p4Cards[i].getLocation().y, value, suit);
				}
*/				if(p4Hand.size() > i) drawBack(g, 270, p4Cards[i].getLocation().x, p4Cards[i].getLocation().y);
			}
		}
		
		if(played[0] != null) 
		{
			value = GetCardValue(played[0]);
			suit = GetCardSuit(played[0]);
			
			drawCard(g, 0, p1Zone.getLocation().x, p1Zone.getLocation().y, value, suit);
		}
		
		if(played[1] != null) 
		{
			value = GetCardValue(played[1]);
			suit = GetCardSuit(played[1]);
			
			drawCard(g, 90, p2Zone.getLocation().x, p2Zone.getLocation().y, value, suit);
		}
		
		if(played[2] != null) 
		{
			value = GetCardValue(played[2]);
			suit = GetCardSuit(played[2]);
			
			drawCard(g, 180, p3Zone.getLocation().x, p3Zone.getLocation().y, value, suit);
		}
		
		if(played[3] != null) 
		{
			value = GetCardValue(played[3]);
			suit = GetCardSuit(played[3]);
			
			drawCard(g, 270, p4Zone.getLocation().x, p4Zone.getLocation().y, value, suit);
		}
		
		if(turnMarker > -1) drawMarker(g, playerMarkers[turnMarker].getLocation().x, playerMarkers[turnMarker].getLocation().y, Color.BLUE);
		if(winMarker > -1) drawMarker(g, playerMarkers[winMarker].getLocation().x, playerMarkers[winMarker].getLocation().y, Color.GREEN);
	}
	
	//////////////////////////////////
	//      Private Functions       //
	//////////////////////////////////
	
	// Draw the front image of a card. You can specify which number and suit to use
	private void drawCard(Graphics g, int rotation, int positionX, int positionY, int card, int suit)
	{
		// This function only supports one of the 4 cardinal rotations
		if(rotation != 0 && rotation != 90 && rotation != 180 && rotation != 270) return;
		
		AffineTransform tx = new AffineTransform();
		AffineTransformOp op;
		BufferedImage drawnCard;
		
		// Define the rotation anchors
		double anchorX = (rotation < 180) ? (cardHeight / 2) + (cardHeight / 2) * suit : (cardWidth / 2) + (cardWidth / 2) * card;
		double anchorY = (rotation > 180) ? (cardWidth / 2) + (cardWidth / 2) * card : (cardHeight / 2) + (cardHeight / 2) * suit;
		
		// Special condition if the image is not rotated
		int wOffset = (rotation == 0) ? card : 0;
		int hOffset = (rotation == 0) ? suit : 0;
		
		// Special conditions if the image is rotated sideways (90 or 270 degrees)
		int cardOffset = (rotation == 90) ? card : 0;
		int suitOffset = (rotation == 270) ? suit : 0;
		
		// Rotate the image
		tx.rotate(Math.toRadians(rotation), anchorX, anchorY);
		op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		drawnCard = op.filter(imgFront, null);
		
		// Draw the rotated image
		if(rotation == 0 || rotation == 180)
			g.drawImage(drawnCard, positionX, positionY, (int)cardWidth+positionX, (int)cardHeight+positionY, (int)cardWidth*wOffset, (int)cardHeight*hOffset, (int)cardWidth+((int)cardWidth*wOffset), (int)cardHeight+((int)cardHeight*hOffset), null);
		
		else if(rotation == 90 || rotation == 270)
			g.drawImage(drawnCard, positionX, positionY, (int)cardHeight+positionX, (int)cardWidth+positionY, 0+((int)cardHeight*suitOffset), 0+((int)cardWidth*cardOffset), (int)cardHeight+((int)cardHeight*suitOffset), (int)cardWidth+((int)cardWidth*cardOffset), null);
	}
	
	// Draw the back image of the card
	private void drawBack(Graphics g, int rotation, int positionX, int positionY)
	{
		// This function only supports one of the 4 cardinal rotations
		if(rotation != 0 && rotation != 90 && rotation != 180 && rotation != 270) return;
		
		AffineTransform tx = new AffineTransform();
		AffineTransformOp op;
		
		// Define the rotation anchors
		double anchorX = (rotation < 180) ? (imgBack.getHeight() / 2) : (imgBack.getWidth() / 2);
		double anchorY = (rotation > 180) ? (imgBack.getWidth() / 2) : (imgBack.getHeight() / 2);
		
		// Rotate the image
		tx.rotate(Math.toRadians(rotation), anchorX, anchorY);
		op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		imgBack = op.filter(imgBack, null);
		
		// Draw the rotated image and border
		if(rotation == 0 || rotation == 180)
		{
			g.drawRect(positionX, positionY, (int)cardWidth, (int)cardHeight);
			g.drawImage(imgBack, positionX+1, positionY+1, (int)cardWidth-2, (int)cardHeight-2, null);
		}
		else if(rotation == 90 || rotation == 270)
		{
			g.drawRect(positionX, positionY, (int)cardHeight, (int)cardWidth);
			g.drawImage(imgBack, positionX+1, positionY+1, (int)cardHeight-2, (int)cardWidth-2, null);
		}
	}
	
	private int GetCardValue(Card card)
	{
		if(card.GetValue() == 14) return 0;
		else return card.GetValue()-1;
	}
	
	private int GetCardSuit(Card card)
	{
		switch(card.GetSuit())
		{
			case CLUB:		return 0;
			case SPADE:		return 1;
			case HEART: 	return 2;
			case DIAMOND: 	return 3;
			default:		return -1;
		}
	}
	
	private void drawMarker(Graphics g, int positionX, int positionY, Color c)
	{
		g.setColor(Color.BLACK);
		g.drawOval(positionX, positionY, 30, 30);
		g.setColor(c);
		g.fillOval(positionX, positionY, 30, 30);
		
		g.setColor(Color.BLACK);
	}
	
	private void drawSuitToIcon(BufferedImage icon, Suit suit)
	{
		if(suit == null) return;
		
		int suitWidth = 40;
		int suitHeight = 40;
		
		Graphics2D g2d = icon.createGraphics();
		
		if(suit == Suit.CLUB) 
		{
			g2d.drawImage(imgSuit, 0, 0, suitWidth, suitHeight, 
						90, 0, 220, 135, null);
		}
		else if(suit == Suit.HEART)
		{
			g2d.drawImage(imgSuit, 0, 0, suitWidth, suitHeight, 
						90, 165, 220, 310, null);
		}
		else if(suit == Suit.SPADE)
		{
			g2d.drawImage(imgSuit, 0, 0, suitWidth, suitHeight, 
						300, 0, 420, 135, null);
		}
		else if(suit == Suit.DIAMOND)
		{
			g2d.drawImage(imgSuit, 0, 0, suitWidth, suitHeight, 
						300, 165, 420, 310, null);
		}
	}
}
