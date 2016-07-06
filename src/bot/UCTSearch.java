package bot;

import java.util.*;

import ai.*;

public class UCTSearch 
{
	private RandomGame randGame;
	private Suit[][] missing;
	
	private UCTNode root;
	private int player;
	
	private Euchre gameState;
	private Card[] activeZone;
	
	private Euchre initGameState;
	private Card[] initActiveZone;
	
	public UCTSearch(int pl)
	{
		randGame = new RandomGame();
		missing = new Suit[4][4];
		
		activeZone = new Card[4];
		gameState = null;
		
		root = null;
		player = pl;
		
		initGameState = null;
		initActiveZone = new Card[4];
	}
	
	public double GetSuitConfidence() { return CalcSuitConfidence(); }
	
	public double GetAloneConfidence() { return CalcAloneConfidence(); }
	
	public int UCTSearchMove()
	{
		int i;
		boolean simulate = true;
		
		if(initGameState == null) return -1;
		
		root = new UCTNode(player);
		
		while(simulate)
		{
			// Reset the initial play area
			for(i=0; i<4; i++)
			{
				if(initActiveZone == null) activeZone[i] = null;
				else if(initActiveZone[i] == null) activeZone[i] = null;
				else activeZone[i] = new Card(initActiveZone[i]);
			}
			
			// Reset the game to the initial state
			gameState = new Euchre(initGameState);
			
			// Start simulation
			simulate = PlaySimulation(root);
		}
		
		return root.GetBestMove();
	}
	
	public int FindBestCounter(ArrayList<Integer> legalMoves)
	{
		int i, winner;
		Card[] potenitalActive = new Card[initActiveZone.length];
		int counter = -1;
		int low = -1;
		int partnerWin = -1;
		int partnerValue = -1;
		
		gameState = new Euchre(initGameState);
		
		for(i=0; i<4; i++)
		{
			if(initActiveZone == null) potenitalActive[i] = null;
			else if(initActiveZone[i] == null) potenitalActive[i] = null;
			else potenitalActive[i] = new Card(initActiveZone[i]);
		}
		
		for(i=0; i<gameState.GetHand(player).size(); i++)
		{
			if(!legalMoves.contains(i)) continue;
			
			potenitalActive[player] = gameState.GetHand(player).get(i);
			
			winner = gameState.GetTrickWinner(potenitalActive, GetLead());
			
			// Store the fact that the partner can win the trick, as well as its value.
			// It should be okay to overtake a partner's lower card
			if(winner == gameState.GetPartner(player).GetPlayerNumber()) 
			{
				Card partnerCard = potenitalActive[(player+2)%4];
				
				partnerWin = i;
				partnerValue = partnerCard.GetValue() + partnerCard.IsBower(gameState.GetTrump());
			}
			
			// Set the winning card's index. Take the lesser value card
			if(winner == player) 
			{
				int counterValue = -1;
				int newCounterValue = gameState.GetHand(player).get(i).GetValue() 
						+ gameState.GetHand(player).get(i).IsBower(gameState.GetTrump());
				
				if(counter >= 0) counterValue = gameState.GetHand(player).get(counter).GetValue()
						+ gameState.GetHand(player).get(counter).IsBower(gameState.GetTrump());
				
				if(newCounterValue < counterValue || counterValue < 0) 
				{
					counter = i;
				}
			}
			
			if(low == -1) low = i;
			
			boolean iIsLower = gameState.GetHand(player).get(i).GetValue() + gameState.GetHand(player).get(i).IsBower(gameState.GetTrump()) 
								< gameState.GetHand(player).get(low).GetValue() + gameState.GetHand(player).get(low).IsBower(gameState.GetTrump());
			boolean iIsTrump = gameState.GetHand(player).get(i).GetSuit() == gameState.GetTrump();
			boolean lowIsTrump = gameState.GetHand(player).get(low).GetSuit() == gameState.GetTrump();
			boolean bothTrump = iIsTrump && lowIsTrump;
			
			if(iIsLower) 
			{
				if(!iIsTrump || bothTrump)
					low = i;
			}
		}
		
		// If no moves can win, pick the card that has the lowest value
		boolean partnerLead = partnerWin > 0;
		if(!LastMove()) partnerLead = partnerLead && partnerValue > 11;
	
		if(counter > 0 && !partnerLead) return counter;
		else return low;
	}
	
	public void SetInitialGameState(Euchre g) 
	{ 
		initGameState = g;
		Card top = initGameState.GetTopCard();
		
		if(top == null) return;
		randGame.SetTopCard(initGameState.GetDealer().GetPlayerNumber(), initGameState.GetTopCard());
		randGame.SetPotentialTrump(null);
	}
	
	public void SetInitialActiveZone(Card[] az) { initActiveZone = az; }
	
	public void SetPotentialTrump(Suit s) 
	{
		randGame.SetPotentialTrump(s);
		randGame.SetTopCard(-1, null);
	}
	
	private boolean PlaySimulation(UCTNode node)
	{
		boolean gamePlayed = true;
		
		if(node.GetVisits() == 0)
		{
			gamePlayed = randGame.PlayRandomGames(gameState, activeZone, player, missing);
			
			if(!gamePlayed) return false;		// No games could be played if leaf was reached (no cards in hand)
			else node.AddTrickValues(randGame.GetTricksPlayed(), randGame.GetPlayerTricks(), randGame.GetPartnerTricks());
		}
		else
		{
			if(node.GetChildren() == null) 
			{
				node.Expand(gameState.GetHand(player), GetLead(), gameState.GetTrump());
			}
			
			UCTNode next = node.UCTSelect();
			gameState.GetHand(player).remove(next.GetMove());
			
			if(!PlaySimulation(next)) 
				return false;
		}
		
		if(node.GetParent() != null)
		{
			node.GetParent().AddTrickValues(randGame.GetTricksPlayed(), randGame.GetPlayerTricks(), randGame.GetPartnerTricks());
		}
		
		if(node.GetChildren() != null)
		{
			node.SetBestMove();
		}
		
		return true;
	}
	
	public void AddMissingSuit(int player, Suit s)
	{
		int i;
		for(i=0; i<missing[0].length; i++)
		{
			if(missing[player][i] == s) return;
			
			if(missing[player][i] == null) 
			{
				missing[player][i] = s;
				break;
			}
		}
	}
	
	public void ClearMissing()
	{
		int i, j;
		for(i=0; i<missing.length; i++)
		{
			for(j=0; j<missing[0].length; j++)
			{
				missing[i][j] = null;
			}
		}
	}
	
	public boolean NoActiveCards()
	{
		int i;
		for(i=0; i<initActiveZone.length; i++)
		{
			if(initActiveZone[i] != null) return false;
		}
		
		return true;
	}
	
	public boolean LastMove()
	{
		int i;
		for(i=0; i<initActiveZone.length; i++)
		{
			if(i==player) continue;
			if(gameState.GetPartner(i).IsAlone()) continue;
			if(initActiveZone[i] == null) return false;
		}
		
		return true;
	}
	
	private Suit GetLead()
	{
		if(initActiveZone == null) return null;
		
		int i;
		for(i=1; i<=4; i++)
		{
			if(initActiveZone[i%4] != null) 
			{
				return initActiveZone[i%4].GetSuit();
			}
		}
		
		return null;
	}
	
	private double CalcSuitConfidence()
	{
		return ((double)root.GetPlayerTricks() + (double)root.GetPartnerTricks()) / (double) root.GetVisits();
	}
	
	private double CalcAloneConfidence()
	{
		return (double)root.GetPlayerTricks() / (double) root.GetVisits();
	}
}
