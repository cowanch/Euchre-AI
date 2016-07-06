package gui;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import ai.Card;
import ai.Suit;

public class GameGUI extends JFrame 
{
	//////////////////////////////////
	//        Class Members         //
	//////////////////////////////////
	
	private PlayArea playArea;
	private ScoreBoard score;
	private TrickBoard tricks;
	private TrumpDisplay trump;
	
	private JButton startButton;
	private JTextArea info;
	
	//////////////////////////////////
	//         Constructors         //
	//////////////////////////////////
	
	public GameGUI()
	{
		super("Euchre");
		
		setSize(1200, 800);
		setVisible(true);
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		playArea = new PlayArea(700);
		add(playArea);
		
		score = new ScoreBoard(350);
		add(score);
		
		tricks = new TrickBoard(175);
		add(tricks);
		
		trump = new TrumpDisplay(175);
		add(trump);
		
		startButton = new JButton("Start Game");
		startButton.setLocation(950, 250);
		startButton.setSize(200, 50);
		startButton.setVisible(true);
		add(startButton);
		
		info = new JTextArea();
		info.setLocation(950, 10);
		info.setSize(200, 200);
		info.setVisible(true);
		info.setEditable(false);
		info.setLineWrap(true);
		info.setWrapStyleWord(true);
		add(info);
		
		repaint();
	}
	
	//////////////////////////////////
	//       Public Functions       //
	//////////////////////////////////
	
	@Override
	public void paint(Graphics g)
	{
		super.paintComponents(g);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	/* Update Functions */
	////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void UpdateHands(ArrayList<Card> p1, ArrayList<Card> p2, ArrayList<Card> p3, ArrayList<Card> p4) 
	{ 
		playArea.UpdateHands(p1, p2, p3, p4); 
	}
	
	public void UpdatePlayed(Card p1, Card p2, Card p3, Card p4) { playArea.UpdatePlayed(p1, p2, p3, p4); }
	
	public void UpdateScore(int t1, int t2) { score.UpdateScore(t1, t2); }
	
	public void UpdateTricks(int tricks1, int tricks2) { tricks.UpdateTricks(tricks1, tricks2); }
	
	public void UpdateInfo(String msg) { info.setText(msg); }
	
	public void AddInfo(String msg) { UpdateInfo(info.getText() + "\n" + msg); }
	
	public void UpdateTrump(Suit suit) { trump.UpdateTrump(suit); }
	
	public void SetTurnMarker(int marker) { playArea.setTurnMarker(marker); }
	
	public void SetWinMarker(int marker) { playArea.setWinMarker(marker); }
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	/* Action Button Toggle Functions */
	////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void ToggleOff() { playArea.ToggleOff(); }
	
	public void ToggleFirstDeal() { playArea.ToggleFirstDeal(); }
	
	public void ToggleDeal() { playArea.ToggleDeal(); }
	
	public void TogglePass() { playArea.TogglePass(); }
	
	public void ToggleNext() { playArea.ToggleNext(); }
	
	public void ToggleAloneInvisible() { playArea.ToggleAloneInvisible(); }
	
	public void ToggleAloneVisible() { playArea.ToggleAloneVisible(); }
	
	public void ToggleAloneYes() { playArea.ToggleAloneYes(); }
	
	public void ToggleAloneNo() { playArea.ToggleAloneNo(); }
	
	public void ToggleSuitButtonsOn(Suit[] suits) { playArea.ToggleSuitButtonsOn(suits); }
	
	public void ToggleSuitButtonsOff() { playArea.ToggleSuitButtonsOff(); }
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	/* Get Functions */
	////////////////////////////////////////////////////////////////////////////////////////////////
	
	public JButton getStartButton() { return startButton; }
	
	public JButton getActionButton() { return playArea.getActionButton(); }
	
	public boolean isActionEnabled() { return playArea.isActionEnabled(); }
	
	public JButton getAloneButton() { return playArea.getAloneButton(); }
	
	public boolean isAloneEnabled() { return playArea.isAloneEnabled(); }
	
	public JPanel[] GetP1Cards() { return playArea.getP1Cards(); }
	
	public JPanel GetActiveZone(int player) { return playArea.getActiveZone(player); }
	
	public JButton[] GetSuitButtons() { return playArea.getSuitButtons(); }
}
