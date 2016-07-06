package bot;

import java.util.*;

import ai.*;

public class RandomGame 
{
	private Euchre game;
	private Card[] played;
	private Player player;
	private int tricksPlayed;
	private int playerTricks;
	private int partnerTricks;
	
	private Card topCard;
	private Suit potentialTrump;
	private int dealer;
	
	public RandomGame() 
	{
		played = new Card[4];
		topCard = null;
		dealer = -1;
		potentialTrump = null;
	}
	
	// Add a parameter for a collection of missing suits
	// These missing suits will not be added to the hand of the player who is missing it
	public boolean PlayRandomGames(Euchre g, Card[] p, int pl, Suit[][] ms)
	{
		int i, j;
		
		tricksPlayed = 0;
		playerTricks = 0;
		partnerTricks = 0;
		
		for(i=0; i<50; i++)
		{	
			game = new Euchre(g);
			player = game.GetPlayer(pl);
			
//			System.out.println("Hand Size: " + player.GetHandSize());
			if(player.GetHandSize() == 0) return false;
			
			for(j=0; j<played.length; j++)
			{
				if(p[j] == null) played[j] = null;
				else played[j] = new Card(p[j]);
			}
		
			game.RandomReset(pl);
			int redrawSuccess = 0;
			
			if(topCard != null)
			{
				if(dealer == pl)
				{
					// Intelligently replace a card from the hand with the top card
					int rep = HeuristicCompare.ReplaceCard(player.GetHand(), topCard);
					player.GetHand().remove(rep);
					player.AddCard(new Card(topCard));
					game.SetTrump(topCard.GetSuit());
				}
				// Otherwise add the card to the dealer's hand
				else if(dealer > -1) 
				{
					game.GetPlayer(dealer).AddCard(new Card(topCard));
					game.SetTrump(topCard.GetSuit());
				}
			}
			else if(potentialTrump != null)
			{
				game.SetTrump(potentialTrump);
			}
			
			for(j=0; j<4; j++)
			{
				if(j==pl) continue;
				redrawSuccess = game.RandomDraw(j, player.GetHandSize(), played[j] != null, ms[j]);
				if(redrawSuccess < 0) break;
			}
			
			if(redrawSuccess < 0) 
			{
				i--;
//				System.out.println("Redo!!!");
				continue;
			}
			
			//played[pl] = player.GetHand().remove(move);

/*			System.out.println("=====================");
			System.out.println(game.GetHand(0));
			System.out.println(game.GetHand(1));
			System.out.println(game.GetHand(2));
			System.out.println(game.GetHand(3));
			System.out.println("=====================");
*/						
			RandomRound(pl);
		}
		
		return true;
		
/*		System.out.println(playerTricks);
		System.out.println(partnerTricks);
		System.out.println(playerTricks + partnerTricks);
		System.out.println(tricksPlayed);
		System.out.println("------------------------------------\n");
*/	}
	
	public int GetPlayerTricks() { return playerTricks; }
	
	public int GetPartnerTricks() { return partnerTricks; }
	
	public int GetTricksPlayed() { return tricksPlayed; }
	
	public void SetTopCard(int d, Card tc)
	{
		dealer = d;
		if(tc == null) topCard = null;
		else topCard = new Card(tc);
	}
	
	public void SetPotentialTrump(Suit s)
	{
		potentialTrump = s;
	}
	
	private void RandomRound(int currentPlay)
	{
		int i;
		Suit lead;
		
		lead = GetLead(currentPlay);
		
		ArrayList<Player> players = game.GetTurnOrder(currentPlay);
		ListIterator<Player> iter = players.listIterator();
		
		int playIndex;
		int trickWinner;
		
		// Every player plays a card
		while(iter.hasNext())
		{
			Player temp = iter.next();
			if(played[temp.GetPlayerNumber()] != null) break;
			
			do
			{
				do
				{
					playIndex = (int)(Math.random() * temp.GetHandSize());
				}
				while(playIndex < 0 || playIndex >= temp.GetHandSize());
			}
			while(!temp.ValidMove(temp.GetHand().get(playIndex), lead, game.GetTrump()));
			
			played[temp.GetPlayerNumber()] = temp.GetHand().remove(playIndex);
			if(lead == null) lead = played[temp.GetPlayerNumber()].GetSuit();
		}
		
		trickWinner = game.GetTrickWinner(played, lead);
		
		if(trickWinner == player.GetPlayerNumber()) playerTricks++;
		else if(trickWinner == game.GetPartner(player.GetPlayerNumber()).GetPlayerNumber()) partnerTricks++;
		
		tricksPlayed++;
		
		for(i=0; i<4; i++)
		{
			played[i] = null;
		}
		
		if(player.GetHandSize() > 0) RandomRound(trickWinner);
		else return;
	}
	
	private Suit GetLead(int pl)
	{
		int i;
		for(i=1; i<=4; i++)
		{
			if(played[i%4] != null) 
			{
				return played[i%4].GetSuit();
			}
		}
		
		return null;
	}
}
