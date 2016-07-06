package ai;

import gui.GameGUI;

import java.awt.event.*;
import java.awt.*;
import java.util.*;

import javax.swing.*;

public class Controller 
{
	//////////////////////////////////
	//        Class Members         //
	//////////////////////////////////
	
	private Euchre game;
	private GameGUI gui;
	private boolean waiting;
	private Player currentPlayer;
	
	private ActionListener startListener;
	private ActionListener firstDealListener;
	private ActionListener dealListener;
	private ActionListener passFirstListener;
	private ActionListener passSecondListener;
	private ActionListener nextListener;
	private ActionListener controlNextListener;
	private ActionListener aloneListener;
	private ActionListener suitSelectListener;
	private MouseListener cardListener;
	
	//////////////////////////////////
	//         Constructors         //
	//////////////////////////////////
	
	public Controller()
	{
		game = new Euchre();
		gui = new GameGUI();
		
		currentPlayer = null;
		
		startListener = new ActionListener() {
			public void actionPerformed (ActionEvent e) { StartGame(); }
		};
		
		firstDealListener = new ActionListener() {
			public void actionPerformed (ActionEvent e) { FirstDeal(); }
		};
		
		dealListener = new ActionListener() {
			public void actionPerformed (ActionEvent e) { Deal(); }
		};
		
		passFirstListener = new ActionListener() {
			public void actionPerformed (ActionEvent e) { PassFirst(); }
		};
		
		passSecondListener = new ActionListener() {
			public void actionPerformed (ActionEvent e) { PassSecond(); }
		};
		
		nextListener = new ActionListener() {
			public void actionPerformed (ActionEvent e) { Next(); }
		};
		
		controlNextListener = new ActionListener() {
			public void actionPerformed (ActionEvent e) { ControlNext(); }
		};
		
		aloneListener = new ActionListener() {
			public void actionPerformed (ActionEvent e) { ToggleAlone(); }
		};
		
		suitSelectListener = new ActionListener() {
			public void actionPerformed (ActionEvent e) { SelectSuit(e); }
		};
		
		cardListener = new MouseListener() {
			public void mousePressed(MouseEvent e) { }

		    public void mouseReleased(MouseEvent e) { }

		    public void mouseEntered(MouseEvent e) { HoverCard(e, true); }

		    public void mouseExited(MouseEvent e) { HoverCard(e, false); }

		    public void mouseClicked(MouseEvent e) { SelectCard(e); }
		};
		
		gui.getStartButton().addActionListener(startListener);
		gui.getAloneButton().addActionListener(aloneListener);
		SetSuitListeners();
		
		GameLoop();
	}
	
	public Controller(Controller other)
	{
		game = new Euchre(other.game);
		gui = new GameGUI();
		
		waiting = other.waiting;
	}
	
	//////////////////////////////////
	//       Public Functions       //
	//////////////////////////////////
	
	//////////////////////////////////
	//      Private Functions       //
	//////////////////////////////////
	
	private void StartGame()
	{
		game.SetupGame();
		
		gui.UpdateHands(null, null, null, null);
		gui.UpdatePlayed(null, null, null, null);
		
		gui.UpdateInfo("Cut the deck to see who deals first");
		
		gui.SetWinMarker(-1);
		gui.SetTurnMarker(-1);
		
		int[] score = game.GetScore();
		gui.UpdateScore(score[0], score[1]);
		
		int[] tricks = game.GetTricks();
		gui.UpdateTricks(tricks[0], tricks[1]);
		
		gui.UpdateTrump(null);
		
		RemoveAllActionListeners();
		gui.ToggleFirstDeal();
		gui.getActionButton().addActionListener(firstDealListener);
	}
	
	private void FirstDeal()
	{
		Card[] cuts = game.FirstDeal();
		gui.UpdatePlayed(cuts[0], cuts[1], null, null);
		gui.UpdateInfo("Team " + (game.GetCurrentDeal() + 1) + " deals first");
		gui.SetWinMarker(game.GetCurrentDeal());
		
		gui.ToggleDeal();
		gui.getActionButton().removeActionListener(firstDealListener);
		gui.getActionButton().addActionListener(dealListener);
	}
	
	private void Deal()
	{
		gui.SetTurnMarker(-1);
		gui.SetWinMarker(-1);
		
		Card draw = game.Deal();
		gui.UpdateHands(game.GetHand(0), game.GetHand(1), game.GetHand(2), game.GetHand(3));
		
		int currentDeal = game.GetCurrentDeal();
		Card p1 = (currentDeal-1 == 0) ? draw : null;
		Card p2 = (currentDeal-1 == 1) ? draw : null;
		Card p3 = (currentDeal-1 == 2) ? draw : null;
		Card p4 = (currentDeal-1 == -1) ? draw : null;
		gui.UpdatePlayed(p1, p2, p3, p4);
		
		waiting = false;
		gui.ToggleOff();
		gui.getActionButton().removeActionListener(dealListener);
	}
	
	private void PassFirst()
	{
		if(currentPlayer == null) return;
		if(!currentPlayer.IsHuman()) return;
		currentPlayer.setFirstBid(0);
		
		gui.ToggleOff();
		gui.getActionButton().removeActionListener(passFirstListener);
	}
	
	private void PassSecond()
	{
		if(currentPlayer == null) return;
		if(!currentPlayer.IsHuman()) return;
		currentPlayer.setSecondBid(-1);
		
		gui.ToggleOff();
		gui.getActionButton().removeActionListener(passSecondListener);
	}
	
	private void Next()
	{
		if(currentPlayer == null) return;
		if(currentPlayer.IsHuman()) return;
		waiting = false;
		
		gui.ToggleOff();
		gui.getActionButton().removeActionListener(nextListener);
	}
	
	private void ControlNext()
	{
		waiting = false;
		
		gui.ToggleOff();
		gui.getActionButton().removeActionListener(controlNextListener);
	}
	
	private void ToggleAlone()
	{
		if(gui.isAloneEnabled()) gui.ToggleAloneNo();
		else gui.ToggleAloneYes();
	}
	
	private void SelectSuit(ActionEvent e)
	{
		if(currentPlayer == null) return;
		if(!currentPlayer.IsHuman()) return;
		
		JButton[] options = gui.GetSuitButtons();
		if(options.length != 3) return;
		
		JButton source = (JButton)e.getSource();
		
		int i;
		for(i=0; i<3; i++)
		{
			if(source == options[i]) 
			{
				currentPlayer.setSecondBid(i);
				break;
			}
		}
		
		gui.ToggleOff();
		gui.getActionButton().removeActionListener(suitSelectListener);
	}
	
	private void HoverCard(MouseEvent e, boolean on)
	{
		JPanel cardPanel = (JPanel)e.getSource();
		cardPanel.setOpaque(on);
		cardPanel.setBackground(new Color(255, 0, 0, 100));
		gui.repaint();
	}
	
	private void SelectCard(MouseEvent e)
	{
		JPanel cardPanel = (JPanel)e.getSource();
		boolean deckCard = cardPanel == gui.GetActiveZone(game.GetCurrentDeal() - 1);
		int i;
		
		// If the card that was selected was on top of the deck
		if(deckCard)
		{
			if(currentPlayer == null) return;
			if(!currentPlayer.IsHuman()) return;
			currentPlayer.setFirstBid(1);
			
			gui.ToggleOff();
			gui.getActionButton().removeActionListener(passFirstListener);
		}
		// If the card that was selected was in the hand
		else
		{
			for(i=0; i<5; i++)
			{
				if(cardPanel == gui.GetP1Cards()[i])
				{
					currentPlayer.setCard(i);
				}
			}
		}
		
		cardPanel.setOpaque(false);
		cardPanel.setBackground(new Color(0,0,0,0));
		
		gui.repaint();
	}
	
	private void SetCardListeners()
	{
		int i;
		JPanel[] card = gui.GetP1Cards();
		
		for(i=0; i<game.GetHand(0).size(); i++)
		{
			card[i].addMouseListener(cardListener);
		}
	}
	
	private void RemoveCardListeners()
	{
		int i;
		JPanel[] card = gui.GetP1Cards();
		
		for(i=0; i<5; i++)
		{
			card[i].removeMouseListener(cardListener);
		}
	}
	
	private void RemoveAllActionListeners()
	{
		gui.getActionButton().removeActionListener(firstDealListener);
		gui.getActionButton().removeActionListener(dealListener);
		gui.getActionButton().removeActionListener(passFirstListener);
		gui.getActionButton().removeActionListener(passSecondListener);
		gui.getActionButton().removeActionListener(nextListener);
		gui.getActionButton().removeActionListener(controlNextListener);
	}
	
	private void SetSuitListeners()
	{
		JButton[] options = gui.GetSuitButtons();
		if(options.length != 3) return;
		
		int i;
		for(i=0; i<3; i++)
		{
			options[i].addActionListener(suitSelectListener);
		}
	}
	
	private void SetAloneStatus()
	{
		if(gui.isAloneEnabled()) game.GoAlone(0);
	}
	
	private void WrapupBid()
	{
		// Remove the card listeners from the hand
		RemoveCardListeners();
		
		// Remove all turn markers
		gui.SetTurnMarker(-1);
		
		// Remove the alone button
		gui.ToggleAloneInvisible();
		
		// Set the player's alone status
		SetAloneStatus();
		
		// Remove the option for second bidding for the human player
		gui.ToggleSuitButtonsOff();
	}
	
	private void WrapupPlay()
	{
		// Remove the card listeners from the hand
		RemoveCardListeners();
		
		// Remove all cards from the play field
		gui.UpdatePlayed(null, null, null, null);
		
		// Remove all win markers
		gui.SetWinMarker(-1);
		
		// Reset the info area
		gui.UpdateInfo("");
		
		// Remove the trump suit from the display
		gui.UpdateTrump(null);
		
		// Reset the trick counters
		gui.UpdateTricks(0, 0);
		
		// Make sure that no player is still going alone
		game.ResetAllAlone();
		
		ClearAllMissing(game.GetTurnOrder(0));
	}
	
	private void GameLoop()
	{
		boolean run = true;
		boolean bid = false;
		
		while(run)
		{
			boolean gameOver = false;
			waiting = true;
			gui.UpdateInfo("Welcome to Euchre!\nPress start to play");
			
			while(!gameOver) 
			{
				gui.getStartButton().setEnabled(true);
				
				// Wait until the deal is ready
				while(waiting) { Wait(); }
				
				gui.getStartButton().setEnabled(false);
				
				// Bidding phase
				bid = Bid();
				WrapupBid();
				
				while(gui.isActionEnabled()) { Wait(); }
				
				if(!bid)
				{
					gui.ToggleDeal();
					gui.getActionButton().addActionListener(dealListener);
					waiting = true;
					
					continue;
				}
				
				// Play phase
				gameOver = Play();
				WrapupPlay();
				
				while(gui.isActionEnabled()) { Wait(); }
				
				if(!gameOver)
				{
					gui.ToggleDeal();
					gui.getActionButton().addActionListener(dealListener);
					waiting = true;
				}
			}
		}
	}
	
	private boolean Bid()
	{
		ArrayList<Player> order = game.GetTurnOrder(game.GetCurrentDeal());
		ListIterator<Player> iter = order.listIterator();
		Suit deadSuit, choice;
		
		// Bid for revealed card
		gui.UpdateInfo("Players will now bid for trump");
		
		// Set up the alone button
		gui.ToggleAloneVisible();
		
		// First bid
		while(iter.hasNext())
		{
			// Set the current player to the next one in the list
			currentPlayer = iter.next();
			
			// Move the marker to the current player to indicate whose turn it is
			gui.SetTurnMarker(currentPlayer.GetPlayerNumber());
			
			// Wait until the action button is disabled (the action button was causing problems without this)
			while(gui.isActionEnabled()) { Wait(); }
			
			// Set up the proper GUI elements
			// There are different options depending on if the current player is human or computer
			if(currentPlayer.IsHuman())
			{
				// Turn on the option to pass the card to the next player
				gui.TogglePass();
				gui.getActionButton().addActionListener(passFirstListener);
				
				// Make the top deck card clickable so the player can order it
				gui.GetActiveZone(game.GetCurrentDeal() - 1).addMouseListener(cardListener);
			}
			else
			{
				// Remove any options to click the top deck card if it's not the human player's turn
				gui.GetActiveZone(game.GetCurrentDeal() - 1).removeMouseListener(cardListener);
			}
			
			gui.AddInfo("Player " + (currentPlayer.GetPlayerNumber() + 1) + " is thinking...");
			currentPlayer.UpdateUCTGame(game);
			currentPlayer.UpdateUCTActive(null);
			currentPlayer.firstBid(game.GetDealer().GetPlayerNumber() == game.GetPartner(currentPlayer.GetPlayerNumber()).GetPlayerNumber());
			
			if(!currentPlayer.IsHuman())
			{
				gui.AddInfo("Player " + (currentPlayer.GetPlayerNumber() + 1) + " is ready");
				
				// Turn on the option to move to the next move done by a computer player
				gui.ToggleNext();
				gui.getActionButton().addActionListener(nextListener);
				waiting = true;
			}
			
			// Wait until the action button is disabled
			while(waiting) { Wait(); }
			
			// Get the player's first bid
			if(currentPlayer.getFirstBid())
			{
				// Remove the mouse listener from the trump card
				gui.GetActiveZone(game.GetCurrentDeal() - 1).removeMouseListener(cardListener);
				
				// Wait until the action button is disabled (the action button was causing problems without this)
				while(gui.isActionEnabled()) { Wait(); }
				
				// Set the appropriate listeners to start the next step
				// The action performed here is done by the dealer (which might not be the current player)
				if(game.GetDealer().IsHuman())
				{
					// Set the option to click a card in the hand to replace it
					SetCardListeners();
				}
				
				// Update the information on the order
				gui.UpdateInfo("Player " + (currentPlayer.GetPlayerNumber() + 1) + " ordered trump");
				gui.AddInfo("Dealer is picking up the top card...");
				
				// Need to change the currentPlayer to the dealer for the setCard function in the card listener
				// to work properly for selecting a card to replace
				int orderer = currentPlayer.GetPlayerNumber();
				currentPlayer = game.GetDealer();
				
				// Ordering trump. Finishes upon user input
				game.OrderTrump(orderer);
				
				if(!game.GetDealer().IsHuman())
				{
					// Set the option to advance to the next computer player's move
					gui.ToggleNext();
					gui.getActionButton().addActionListener(nextListener);
					waiting = true;
				}
				
				while(waiting) { Wait(); }
				
				game.DealerPickUp();
				
				// Update the GUI appropriately
				gui.UpdatePlayed(null, null, null, null);
				gui.UpdateTrump(game.GetTrump());
				
				// Trump is ready, finish bidding
				return true;
			}
			else
			{
				// Update the information
				gui.AddInfo("Player " + (currentPlayer.GetPlayerNumber() + 1) + " passes");
			}
		}
		
		// Flip down the top card and prepare for the second bid
		deadSuit = game.PassTrump(); 									// Store the suit that was turned down
		iter = order.listIterator();									// Reset the list iterator
		gui.UpdatePlayed(null, null, null, null);						// Remove all drawn cards form the area (excluding the hands)
		gui.GetActiveZone(game.GetCurrentDeal() - 1).removeMouseListener(cardListener);		// Remove the option to click on the top deck card
		
		// Second bid
		while(iter.hasNext())
		{
			currentPlayer = iter.next();
			
			gui.SetTurnMarker(currentPlayer.GetPlayerNumber());
			
			while(gui.isActionEnabled()) { Wait(); }
			
			if(currentPlayer.IsHuman())
			{
				gui.TogglePass();
				gui.getActionButton().addActionListener(passSecondListener);
				
				Suit[] suits = game.GetRemainingSuits(deadSuit);
				gui.ToggleSuitButtonsOn(suits);
			}
			else
			{
				gui.ToggleSuitButtonsOff();
			}
			
			gui.AddInfo("Player " + (currentPlayer.GetPlayerNumber() + 1) + " is thinking...");
			currentPlayer.UpdateUCTGame(game);
			currentPlayer.UpdateUCTActive(null);
			currentPlayer.secondBid(deadSuit);
			
			if(!currentPlayer.IsHuman())
			{
				gui.AddInfo("Player " + (currentPlayer.GetPlayerNumber() + 1) + " is ready");
				
				// Turn on the option to move to the next move done by a computer player
				gui.ToggleNext();
				gui.getActionButton().addActionListener(nextListener);
				waiting = true;
			}
			
			// Wait until the action button is disabled
			while(waiting) { Wait(); }
			
			choice = currentPlayer.GetSecondBid(deadSuit);
			
			if(choice != null) 
			{
				game.SetTrump(choice, currentPlayer.GetPlayerNumber());
				
				gui.UpdateInfo("Player " + (currentPlayer.GetPlayerNumber() + 1) + " makes " + game.GetTrump() + " trump");
				gui.UpdatePlayed(null, null, null, null);
				gui.UpdateTrump(game.GetTrump());
				
				return true;
			}
			else
			{
				gui.AddInfo("Player " + (currentPlayer.GetPlayerNumber() + 1) + " passes");
			}
		}
		
		// Bid not made, move on to next hand
		return false;
	}
	
	private boolean Play()
	{
		gui.AddInfo("Beginning play round");
		
		String msg = game.SetupAlone();
		boolean someAlone = msg.length() != 0;
		
		if(someAlone) gui.AddInfo(msg);
		
		int i;
		int firstPlay = -1;
		
		// This code follows the rule that the player left of the alone player plays first
		// It is commented out because we don't play this way at my house
/*		
		if(someAlone)
		{
			int[] aloneNumbers = game.GetAlonePlayers();
			
			for(i=0; i<2; i++)
			{
				if(aloneNumbers[i] == -1) continue;
				if(aloneNumbers[i] == game.GetCurrentOrder() || (aloneNumbers[i] + 2) % 4 == game.GetCurrentOrder())
				{
					firstPlay = (aloneNumbers[i] + 1) % 4;
					break;
				}
			}
		}
*///		else
//		{
			firstPlay = game.GetCurrentDeal();		// The current deal is set to the next dealer at this point.
													// Use this to tag who goes first during the first round
//		}
		
		ArrayList<Player> turnOrder;
		ListIterator<Player> iter;
		Card[] played = new Card[4];
		Suit lead = null;
		int roundWin = -1;
		
		ClearAllMissing(game.GetTurnOrder(0));
		
		for(i=0; i<5; i++)
		{
			turnOrder = game.GetTurnOrder(firstPlay);
			iter = turnOrder.listIterator();
			
			played[0] = null;
			played[1] = null;
			played[2] = null;
			played[3] = null;
			lead = null;
			
			gui.UpdatePlayed(played[0], played[1], played[2], played[3]);
			gui.UpdateHands(game.GetHand(0), game.GetHand(1), game.GetHand(2), game.GetHand(3));
			gui.SetWinMarker(-1);
			
			while(iter.hasNext())
			{
				currentPlayer = iter.next();
				if(currentPlayer.GetHand().size() == 0) continue;
				
				gui.SetTurnMarker(currentPlayer.GetPlayerNumber());
				
				while(gui.isActionEnabled()) { Wait(); }
				
				// Set the appropriate listeners to start the next step
				// The action performed here is done by the dealer (which might not be the current player)
				if(currentPlayer.IsHuman())
				{
					// Set the option to click a card in the hand to replace it
					SetCardListeners();
				}
				else
				{
					RemoveCardListeners();
				}
				
				gui.AddInfo("Player " + (currentPlayer.GetPlayerNumber() + 1) + " is thinking...");
				currentPlayer.UpdateUCTGame(game);
				currentPlayer.UpdateUCTActive(played);
				currentPlayer.PlayCard(lead, game.GetTrump(), played);
				
				if(!currentPlayer.IsHuman())
				{
					gui.AddInfo("Player " + (currentPlayer.GetPlayerNumber() + 1) + " is ready");
					
					// Turn on the option to move to the next move done by a computer player
					gui.ToggleNext();
					gui.getActionButton().addActionListener(nextListener);
					waiting = true;
				}
				
				// Wait until the action button is disabled
				while(waiting) { Wait(); }
				
				played[currentPlayer.GetPlayerNumber()] = currentPlayer.getPlayedCard();
				
				if(lead == null) 
				{
					Card tempPlayed = played[currentPlayer.GetPlayerNumber()];
					
					if(tempPlayed.IsBower(game.GetTrump()) == 5) lead = game.GetTrump(); 
					else lead = tempPlayed.GetSuit();
				}
				
				UpdateMissing(turnOrder, played, lead);
				gui.UpdatePlayed(played[0], played[1], played[2], played[3]);
				gui.UpdateHands(game.GetHand(0), game.GetHand(1), game.GetHand(2), game.GetHand(3));
			}
			
			firstPlay = game.GetTrickWinner(played, lead);
			gui.AddInfo("Player " + (firstPlay + 1) + " wins the trick");
			
			gui.SetTurnMarker(-1);
			gui.SetWinMarker(firstPlay);
			
			int[] tricks = game.GetTricks();
			gui.UpdateTricks(tricks[0], tricks[1]);
			
			// Wait for next button to be clicked
			waiting = true;
			
			// Next button to pull in the cards
			gui.ToggleNext();
			gui.getActionButton().addActionListener(controlNextListener);
			
			while(waiting) { Wait(); }
		}
		
		int[] tricks = game.GetTricks();
		roundWin = game.GetRoundWinner(tricks[0], tricks[1]);
		
		int[] score = game.GetScore();
		gui.UpdateScore(score[0], score[1]);
		
		if(score[0] == 10 || score[1] == 10)
		{
			gui.UpdateInfo("Team " + (roundWin + 1) + " wins the game!");
			return true;
		}
		
		return false;
	}
	
	private void UpdateMissing(ArrayList<Player> players, Card[] active, Suit lead)
	{
		int i;
		ListIterator<Player> iter = players.listIterator();
		while(iter.hasNext())
		{
			Player temp = iter.next();
			
			for(i=0; i<active.length; i++)
			{
				if(active[i] == null) continue;
				
				Suit playedSuit = active[i].GetSuit();
				if(playedSuit == game.GetOtherColor() && active[i].IsBower(game.GetTrump()) == 5)
				{
					playedSuit = game.GetTrump();
				}
				
				if(playedSuit != lead)
				{
					temp.UpdateUCTMissing(i, lead);
				}
			}
		}
	}
	
	private void ClearAllMissing(ArrayList<Player> players)
	{
		ListIterator<Player> iter = players.listIterator();
		while(iter.hasNext())
		{
			Player temp = iter.next();
			temp.ClearUCTMissing();
		}
	}
	
	private void Wait()
	{
		try { Thread.sleep(100); }
		catch (Exception e) {}
	}
}
