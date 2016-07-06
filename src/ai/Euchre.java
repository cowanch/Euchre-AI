package ai;

import java.util.ArrayList;
import java.util.ListIterator;

public class Euchre 
{
	//////////////////////////////////
	//        Class Members         //
	//////////////////////////////////
	
	private Team team1;
	private Team team2;
	private Deck deck;
	private Suit trump;
	private int currentDeal;					// Let the dealing number map like so:
												// 0	Team1 / Player1
												// 1	Team2 / Player1
												// 2	Team1 / Player2
												// 3	Team2 / Player2
	private int currentOrder;
	private Card topCard;
	
	//////////////////////////////////
	//         Constructors         //
	//////////////////////////////////
	
	public Euchre()
	{
		team1 = new Team(0, PlayerType.HUMAN, 2, PlayerType.AI);
		team2 = new Team(1, PlayerType.AI, 3, PlayerType.AI);
		deck = new Deck();
	}
	
	public Euchre(Euchre other)
	{
		team1 = new Team(other.team1);
		team2 = new Team(other.team2);
		deck = new Deck(other.deck);
		
		trump = other.trump;
		currentDeal = other.currentDeal;
		currentOrder = other.currentOrder;
	}
	
	//////////////////////////////////
	//       Public Functions       //
	//////////////////////////////////
	
	public void SetupGame()
	{
		// Reset the players and the deck
		team1.ResetTeam();
		team2.ResetTeam();
		deck.ResetDeck();
	}
	
	public Card[] FirstDeal()
	{
		currentDeal = -1;
		Card[] cut = new Card[2];
		
		while(currentDeal < 0)
		{
			deck.Shuffle();
			cut[0] = deck.Draw();
			cut[1] = deck.Draw();
	
			if(cut[0].HasGreaterValue(cut[1])) currentDeal = 0;
			else if(cut[1].HasGreaterValue(cut[0])) currentDeal = 1;
			
			deck.ResetDeck();
		}
		
		return cut;
	}
	
	public Card Deal()
	{
		team1.ResetHands();
		team2.ResetHands();
		deck.ResetDeck();
		
		deck.Shuffle();
		
		//System.out.println("Player " + (currentDeal+1) + " is shuffling...");
		//System.out.println(deck);
		
		ArrayList<Player> order = GetDealOrder();
		
		int i, j;
		for(i=0; i<2; i++)
		{
			ListIterator<Player> iter = order.listIterator();
			while(iter.hasNext()) 
			{ 
				Player current = iter.next();
				for(j=0; j<3-i; j++) { current.AddCard(deck.Draw()); }
			}
		}
		
		//for(i=0; i<4; i++) { System.out.println("Player " + (i+1) + "'s hand " + GetPlayer(i)); }
		
		currentDeal = ++currentDeal % 4;
		
		topCard = deck.Draw();
		return topCard;
	}
	
	public ArrayList<Player> GetTurnOrder(int first)
	{
		ArrayList<Player> order = new ArrayList<Player>();
		
		int i, player;
		for(i=0; i<4; i++)
		{
			player = (first + i) % 4;
			order.add(GetPlayer(player));
		}
		
		return order;
	}
	
	public void OrderTrump(int player)
	{
		GetDealer().decideReplacedCard(topCard);
		currentOrder = player;
		trump = topCard.GetSuit();
		
		// Check to see if player must go alone (partner is the dealer)
		if(GetDealer() == GetPartner(player))
		{
			GetPlayer(player).SetAlone();
			GetDealer().ResetAlone();				// This is just in case the dealer decided to go alone
		}
	}
	
	public void DealerPickUp()
	{
		GetDealer().removeReplacedCard(topCard);
		topCard = null;
	}
	
	public void GoAlone(int player)
	{
		// The human should take priority in this
		if(GetPartner(player).IsAlone()) GetPartner(player).ResetAlone();
		GetPlayer(player).SetAlone();
	}
	
	public void ResetAllAlone()
	{
		int i;
		for(i=0; i<4; i++)
		{
			GetPlayer(i).ResetAlone();
		}
	}
	
	public int GetRoundWinner(int tricks1, int tricks2)
	{
		// Need: 
		// - The number of tricks won for each team
		// - Who ordered trump
		// - If any players played alone
		
		int winner = (tricks1 > tricks2) ? 0 : 1;
		int loser = (winner == 0) ? 1 : 0;
		
		// if the winning team played alone AND took all tricks, award 4 points
		if((GetPlayer(winner).IsAlone() || GetPartner(winner).IsAlone()) &&
				(tricks1 == 5 || tricks2 == 5))
			GetTeam(winner).AddScore(4);
		
		// if the winning team played alone and did NOT take all tricks, award 2 points
		// Commented out because we don't play this way at my house
		//else if(GetPlayer(winner).IsAlone() || GetPartner(winner).IsAlone()) GetTeam(winner).AddScore(2);
		
		// if the losing team played alone, award 2 points
		else if(GetPlayer(loser).IsAlone() || GetPartner(loser).IsAlone()) GetTeam(winner).AddScore(2);
		
		// if all tricks were taken, award 2 points
		else if(tricks1 == 5 || tricks2 == 5) GetTeam(winner).AddScore(2);
		
		// if the winning team did NOT make trump, award 2 points
		else if(winner != currentOrder && (winner + 2) % 4 != currentOrder) GetTeam(winner).AddScore(2);
		
		// otherwise, award 1 point
		else GetTeam(winner).AddScore(1);
		
		return winner;
	}
	
	public int GetTrickWinner(Card[] played, Suit lead)
	{
		if(played.length != 4) return -1;
		
		int i;
		int winner = 0;
		
		// Start comparing the first and second cards (i==1)
		for(i=1; i<4; i++)
		{
			// If player i's card is null, continue
			if(played[i] == null) continue;
			
			// If the current winner's card is null,
			if(played[winner] == null) 
			{
				winner = i;
				continue;
			}
			
			// Current winner has the right bower, they will win this trick for sure
			if(played[winner].IsBower(trump) == 10) continue;
			
			// Player i has the right bower, they become the current winner
			if(played[i].IsBower(trump) == 10) 
			{
				winner = i;
				continue;
			}
			
			// Neither winner or player i have the right
			// Current winner has the left bower, they will remain winner
			if(played[winner].IsBower(trump) == 5) continue;
			
			// Player i has the left bower, they become the current winner
			if(played[i].IsBower(trump) == 5) 
			{
				winner = i;
				continue;
			}
			
			// Neither winner of player i have either bower
			// Current winner has trump and player i does not, they remain the winner
			if(played[winner].GetSuit() == trump && played[i].GetSuit() != trump) continue;
			
			// Player i has trump and winner does not, they become the current winner
			if(played[i].GetSuit() == trump && played[winner].GetSuit() != trump)
			{
				winner = i;
				continue;
			}
			
			// Either both players have trump, or neither player has trump
			// Current winner has the lead suit and player i does not, they remain the winner
			if(played[winner].GetSuit() == lead && played[i].GetSuit() != lead) continue;
			
			// Player i has the lead suit and winner does not, they become the winner
			if(played[i].GetSuit() == lead && played[winner].GetSuit() != lead)
			{
				winner = i;
				continue;
			}
			
			// Either both players have the lead suit (likely), or neither player does (unlikely)
			// The card with the highest value is the current winner
			winner = (played[winner].GetValue() > played[i].GetValue()) ? winner : i;
		}
		
		// The winning team gets a trick
		GetTeam(winner).AddTrick();
		
		return winner;
	}
	
	public String SetupAlone()
	{
		int i;
		ArrayList<Player> players = GetTurnOrder(0);
		ListIterator<Player> iter = players.listIterator();
		String msg = "";
		
		while(iter.hasNext())
		{
			Player temp = iter.next();
			
			if(temp.GetHandSize() == 0) continue;			// You can't go alone if you have no hand
			
			if(temp.IsAlone()) 
			{
				for(i=0; i<temp.GetHandSize(); i++) 
				{
					deck.AddToDeck(GetPartner(temp.GetPlayerNumber()).GetHand().get(i));
				}
				GetPartner(temp.GetPlayerNumber()).ResetHand();
				
				// Prepare the message
				if(msg.length() != 0) msg += "\n";
				msg += "Player " + (temp.GetPlayerNumber() + 1) + " is playing alone";
			}
		}
		
		return msg;
	}
	
	public Suit PassTrump()
	{
		Suit deadSuit = topCard.GetSuit();
		topCard = null;
		return deadSuit;
	}
	
	public void SetTrump(Suit suit)
	{
		SetTrump(suit, -1);
	}
	
	public void SetTrump(Suit suit, int orderer)
	{
		trump = suit;
		currentOrder = orderer;
	}
	
	public Suit[] GetRemainingSuits(Suit dead)
	{
		Suit[] result;
		
		switch(dead)
		{
			case HEART:		result = new Suit[] { Suit.DIAMOND, Suit.SPADE, Suit.CLUB };
							break;
			case DIAMOND:	result = new Suit[] { Suit.HEART, Suit.SPADE, Suit.CLUB };
							break;
			case SPADE:		result = new Suit[] { Suit.HEART, Suit.DIAMOND, Suit.CLUB };
							break;
			case CLUB:		result = new Suit[] { Suit.HEART, Suit.DIAMOND, Suit.SPADE };
							break;
			default:		result = null;
							break;
		}
		
		return result;
	}
	
	public ArrayList<Card> GetHand(int player) { return GetPlayer(player).GetHand(); }
	
	public int GetCurrentDeal() { return currentDeal; }
	
	public int GetCurrentOrder() { return currentOrder; }
	
	public Card GetTopCard() { return topCard; }
	
	public int[] GetAlonePlayers() 
	{
		int[] result = new int[2];
		int i, count;
		
		count = 0;
		result[0] = -1;
		result[1] = -1;
		
		for(i=0; i<4; i++)
		{
			if(GetPlayer(i).IsAlone()) result[count++] = i;
			if(count > 2) break;
		}
		
		return result;
	}
	
	public Player GetDealer() 
	{ 
		int deal = currentDeal - 1;
		deal = (deal < 0) ? 3 : deal;
		return GetPlayer(deal); 
	}
	
	public Suit GetTrump() { return trump; }
	
	public Suit GetOtherColor()
	{
		switch(trump)
		{
			case HEART:		return Suit.DIAMOND;
			case DIAMOND:	return Suit.HEART;
			case SPADE:		return Suit.CLUB;
			case CLUB:		return Suit.SPADE;
			default:		return null;
		}
	}
	
	public int[] GetScore() 
	{ 
		int[] result = new int[2];
		
		result[0] = team1.GetScore();
		result[1] = team2.GetScore();
		
		return result; 
	}
	
	public int[] GetTricks() 
	{ 
		int[] result = new int[2];
		
		result[0] = team1.GetTricks();
		result[1] = team2.GetTricks();
		
		return result; 
	}
	
	// Returns all players cards from their hands to the deck except for the player given in the parameter
	// Used for RandomGame class
	public void RandomReset(int player)
	{
		int i;
		for(i=0; i<4; i++)
		{
			if(i==player) continue;
			
			ArrayList<Card> hand = GetPlayer(i).GetHand();
			ListIterator<Card> iter = hand.listIterator();
			
			while(iter.hasNext())
			{
				Card temp = iter.next();
				deck.AddToDeck(temp);
			}
			
			hand.removeAll(hand);
		}
		
		deck.Shuffle();
	}
	
	// Randomly draw [size] amount of cards into a player's hand
	// Used in RandomGame class
	public int RandomDraw(int player, int size, boolean hasPlayed, Suit[] ms)
	{
		// Draw cards into each players hands randomly
		int i;
		int hp = (hasPlayed) ? 1 : 0;
		int handSize = size - GetPlayer(player).GetHandSize();
		for(i=0; i<handSize-hp; i++)
		{
			Card temp = deck.Draw();
			int recounts = 0;
			while(MissingSuitContains(ms, temp.GetSuit()))
			{
				deck.AddToDeck(temp);
				deck.Shuffle();
				temp = deck.Draw();
				recounts++;
				if(recounts >= 10) return -1;
			}
			
			GetPlayer(player).AddCard(temp);
		}
		
		return 0;
	}
	
	public Player GetPlayer(int player)
	{
		switch(player)
		{
			case 0:		return team1.GetPlayer1();
			case 1:		return team2.GetPlayer1();
			case 2:		return team1.GetPlayer2();
			case 3:		return team2.GetPlayer2();
			default:	return null;
		}
	}
	
	public Player GetPartner(int player)
	{
		return GetPlayer((player + 2) % 4);
	}
	
	//////////////////////////////////
	//      Private Functions       //
	//////////////////////////////////
	
	private ArrayList<Player> GetDealOrder()
	{
		ArrayList<Player> order = new ArrayList<Player>();
		
		int i, player;
		for(i=0; i<4; i++)
		{
			player = (currentDeal + i + 1) % 4;
			order.add(GetPlayer(player));
		}
		
		return order;
	}

	private Team GetTeam(int player)
	{
		Team result = (player % 2 == 0) ? team1 : team2;
		return result;
	}
	
	// Used for RandomGame functions
	private boolean MissingSuitContains(Suit[] ms, Suit s)
	{
		if(ms == null) return false;
		
		int i;
		for(i=0; i<4; i++)
		{
			if(ms[i] == null) continue;
			if(ms[i] == s) return true;
		}
		
		return false;
	}
}
