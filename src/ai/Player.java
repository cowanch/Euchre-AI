package ai;

import java.util.*;
import bot.*;

public class Player 
{
	//////////////////////////////////
	//        Class Members         //
	//////////////////////////////////
	
	private ArrayList<Card> hand;
	private PlayerType type;
	private int pNumber;
	private boolean alone;
	
	private boolean firstBid;
	private int bid;
	private int played;
	
	private UCTSearch uct;
	
	//////////////////////////////////
	//         Constructors         //
	//////////////////////////////////
	
	public Player(int number, PlayerType t)
	{
		hand = new ArrayList<Card>();
		type = t;
		pNumber = number;
		alone = false;
		
		bid = -1;
		
		uct = new UCTSearch(pNumber);
	}
	
	public Player(Player other)
	{
		this(other.pNumber, other.type);
		
		ListIterator<Card> iter = other.hand.listIterator();
		while(iter.hasNext()) { hand.add(new Card(iter.next())); }
	}
	
	//////////////////////////////////
	//       Public Functions       //
	//////////////////////////////////
	
	public void AddCard(Card card) { hand.add(card); }
	
	public void ResetPlayer() { ResetHand(); }
	
	public void ResetHand() 
	{ 
		hand.clear();
		alone = false;
	}
	
	public ArrayList<Card> GetHand() { return hand; }
	
	public int GetHandSize() { return GetHand().size(); }
	
	public int GetPlayerNumber() { return pNumber; }
	
	public boolean IsAlone() { return alone; }
	
	public void SetAlone() { alone = true; }
	
	public void ResetAlone() { alone = false; }
	
	public boolean IsHuman() { return type == PlayerType.HUMAN; }
	
	public String toString() { return hand.toString(); }
	
	public void firstBid(boolean partnerDealt)
	{
		// true == order / false == pass
		bid = -1;
		
		switch(type)
		{
			case HUMAN:		firstBid = humanFirstBid();
							break;
			case RNG:		firstBid = rngFirstBid();
							break;
			case AI:		firstBid = aiFirstBid(partnerDealt);
							break;
			default:		break;
		}
	}
	
	public void setFirstBid(int fBid) 
	{
		// 0 == false; 1 == true
		if(fBid != 0 && fBid != 1) return;
		bid = fBid; 
	}
	
	public boolean getFirstBid() { return firstBid; }
	
	public void secondBid(Suit deadSuit)
	{
		// 0 - 2 == make it / -1 == pass
		bid = -2;
		
		// Call the second bid functions for the player based on what type of player it is
		// These functions all modify the class member bid
		switch(type)
		{
			case HUMAN:		humanSecondBid();
							break;
			case RNG:		rngSecondBid();
							break;
			case AI:		aiSecondBid(deadSuit);
							break;
			default:		break;
		}
	}
	
	public void setSecondBid(int sBid) 
	{
		if(sBid != 0 && sBid != 1 && sBid != 2 && sBid != -1) return;
		bid = sBid; 
	}
	
	public Suit GetSecondBid(Suit deadSuit)
	{
		switch(bid)
		{
			case 0:		return (deadSuit == Suit.HEART) 
								? Suit.DIAMOND : Suit.HEART;
			case 1:		return (deadSuit == Suit.DIAMOND || deadSuit == Suit.HEART) 
								? Suit.SPADE : Suit.DIAMOND;
			case 2:		return (deadSuit == Suit.CLUB) 
								? Suit.SPADE : Suit.CLUB;
			default:	return null;
		}
	}
	
	public void decideReplacedCard(Card trump)
	{
		played = -1;
		
		switch(type)
		{
			case HUMAN:		humanPlayCard();
							break;
			case RNG:		rngPlayCard();
							break;
			case AI:		played = HeuristicCompare.ReplaceCard(hand, trump);
							break;
			default:		break;
		}
	}
	
	public boolean removeReplacedCard(Card trump) 
	{ 
		if(played >= 0 && played < hand.size())
		{
			hand.remove(played);
			AddCard(trump);
			return true;
		}
		else return false;
	}
	
	public void PlayCard(Suit lead, Suit trump, Card[] active)
	{
		do
		{
			played = -1;
		
			switch(type)
			{
				case HUMAN:		humanPlayCard();
								break;
				case RNG:		rngPlayCard();
								break;
				case AI:		aiPlayCard(lead, trump);
								break;
				default:		break;
			}
			
			//System.out.println("nNumber: " + pNumber + "  " + "played: " + played);
		} 
		while(!ValidMove(hand.get(played), lead, trump));
	}
	
	public void setCard(int c)
	{
		if(c < 0 || c >= hand.size()) return;
		played = c;
	}
	
	public Card getPlayedCard() { return hand.remove(played); }
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	/* Check for Valid Move Function */
	////////////////////////////////////////////////////////////////////////////////////////////////
	
	public boolean ValidMove(Card card, Suit suit, Suit trump)
	{
		if(suit == null) return true;					// Nothing has been played yet.
	
		Suit otherColor;
		
		switch(trump)
		{
			case HEART:		otherColor = Suit.DIAMOND;
							break;
			case DIAMOND:	otherColor = Suit.HEART;
							break;
			case SPADE:		otherColor = Suit.CLUB;
							break;
			case CLUB:		otherColor = Suit.SPADE;
							break;
			default:		return false;
		}		
	
		// No problem if the suit is the same UNLESS the player tries to play the left bower
		if(card.GetSuit() == suit && 
				!(suit == otherColor && card.IsBower(trump) == 5)) return true;
		
		// Trump is lead, and the player attempts to play the left bower
		if(suit == trump && card.IsBower(trump) == 5) return true;
		
		else
		{
			ListIterator<Card> iter = hand.listIterator();
			
			while(iter.hasNext())						// Check the hand for any card that has the lead suit
			{
				Card temp = iter.next();
				if(temp.GetSuit() == suit &&
						!(suit == otherColor && temp.IsBower(trump) == 5)) 
					return false;
				
				// The left bower is in the player's hand when trump is lead
				if(suit == trump && temp.IsBower(trump) == 5) return false;
			}
	
			return true;								// If none exist, this is a valid move
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	/* UCT Search Functions */
	////////////////////////////////////////////////////////////////////////////////////////////////
	
	public void UpdateUCTMissing(int pl, Suit s)
	{
		uct.AddMissingSuit(pl, s);
	}
	
	public void ClearUCTMissing()
	{
		uct.ClearMissing();
	}
	
	public void UpdateUCTGame(Euchre g)
	{
		uct.SetInitialGameState(g);
	}
	
	public void UpdateUCTActive(Card[] a)
	{
		uct.SetInitialActiveZone(a);
	}
	
	//////////////////////////////////
	//      Private Functions       //
	//////////////////////////////////
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	/* First Bid Decision Functions */
	////////////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean humanFirstBid()
	{	
		// Wait until a decision is made by the player
		while(bid < 0) { Wait(); }
		
		// 0 == false; 1 == true
		return bid == 1;
	}
	
	private boolean rngFirstBid()
	{
		// NOTE: 60% Pass / 40% Order
		// NOTE: 5% Alone / 95% Together
		do
		{
			bid = (int)(Math.random() * 100);
		}
		while(bid < 0 || bid > 100);
		
		alone = RNGDecideAlone();
		
		return bid < 40;
	}
	
	private boolean aiFirstBid(boolean partnerDealt)
	{
		// Add the AI agent for deciding
		// TODO: Change this		
		boolean ordering;
		
		uct.UCTSearchMove();
		ordering = uct.GetSuitConfidence() > 0.60;
		alone = ordering && uct.GetAloneConfidence() > 0.6;
		
		if(partnerDealt) ordering = alone;
		
		System.out.println("======================");
		System.out.println("Player: " + pNumber);
		System.out.println("Order confidence = " + uct.GetSuitConfidence());
		System.out.println("Alone confidence = " + uct.GetAloneConfidence());
		System.out.println("======================");
		
		return ordering;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	/* Second Bid Decision Functions */
	////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void humanSecondBid()
	{
		// Wait until a decision is made by the player
		while(bid < -1) { Wait(); }
	}
	
	private void rngSecondBid()
	{
		do
		{
			bid = (int)(Math.random() * 60);
		}
		while(bid < 0 || bid > 60);
		
		if(bid < 30)
		{
			bid /= 10;
			bid--;
		}
		else bid = -1;
		
		alone = RNGDecideAlone();
	}
	
	private void aiSecondBid(Suit deadSuit)
	{
		// Add the AI agent for deciding
		// TODO: Change this
		int i;
		Suit[] options = new Suit[3];
		double[] suitConf = new double[3];
		double[] aloneConf = new double[3];
		double highestSuitConf = 0;
		int suitIndex = -1;
		
		options[0] = (deadSuit == Suit.HEART) ? Suit.DIAMOND : Suit.HEART;
		options[1] = (deadSuit == Suit.DIAMOND || deadSuit == Suit.HEART) ? Suit.SPADE : Suit.DIAMOND;
		options[2] = (deadSuit == Suit.CLUB) ? Suit.SPADE : Suit.CLUB;
		
		System.out.println("=========================");
		System.out.println("Player: " + pNumber);
		
		for(i=0; i<options.length; i++)
		{
			uct.SetPotentialTrump(options[i]);
			uct.UCTSearchMove();
			suitConf[i] = uct.GetSuitConfidence();
			aloneConf[i] = uct.GetAloneConfidence();
			
			if(suitConf[i] > highestSuitConf) 
			{
				highestSuitConf = suitConf[i];
				suitIndex = i;
			}
			
			System.out.println("-------------------------");
			System.out.println("Suit: " + options[i]);
			System.out.println("Order confidence = " + suitConf[i]);
			System.out.println("Alone confidence = " + aloneConf[i]);
			System.out.println("-------------------------");
		}
		
		System.out.println("=========================");
		
		if(suitConf[suitIndex] > 0.6)
		{
			bid = suitIndex;
			alone = aloneConf[bid] > 0.6;
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	/* Trump Order Decision Functions */
	////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void humanPlayCard()
	{
		// Wait until a decision is made by the player
		while(played < 0 || played >= hand.size()) { Wait(); }
	}
	
	private void rngPlayCard()
	{
		do
		{
			played = (int)(Math.random() * hand.size());
		}
		while(played < 0 || played >= hand.size());
	}
	
	private void aiPlayCard(Suit lead, Suit trump) 
	{
		int i;
		boolean first = true;
	
		first = uct.NoActiveCards();
		
		if(first)
		{
			played = uct.UCTSearchMove();
		}
		else
		{
			ArrayList<Integer> legalMoves = new ArrayList<Integer>();
			
			for(i=0; i<hand.size(); i++)
			{
				if(ValidMove(hand.get(i), lead, trump)) legalMoves.add(i);
			}
			
			played = uct.FindBestCounter(legalMoves);
		}
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	/* Wait Function */
	////////////////////////////////////////////////////////////////////////////////////////////////
	
	private boolean RNGDecideAlone()
	{
		int al = -1;
		
		do
		{
			al = (int)(Math.random() * 100);
		}
		while(al < 0 || al > 100);
		
		return al < 5;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	/* Wait Function */
	////////////////////////////////////////////////////////////////////////////////////////////////
	
	private void Wait()
	{
		try { Thread.sleep(100); }
		catch (Exception e) {}
	}
}
