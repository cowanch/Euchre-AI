package bot;

import java.util.*;

import ai.*;

public class UCTNode 
{
	private int visits;
	private int playerTricks;
	private int partnerTricks;
	
	private UCTNode parent;
	private ArrayList<UCTNode> children;
	
	private int move;
	private int bestMove;
	
	private int player;
	
	public UCTNode(UCTNode p, int m)
	{
		visits = 0;
		playerTricks = 0;
		partnerTricks = 0;
		
		parent = p;
		children = null;
		
		move = m;
		bestMove = 0;
		
		if(p != null) player = p.player;
	}
	
	public UCTNode(int pl)
	{
		this(null, -1);
		player = pl;
	}
	
	public int GetMove() { return move; }
	
	public int GetVisits() { return visits; }
	
	public int GetPlayerTricks() { return playerTricks; }
	
	public int GetPartnerTricks() { return partnerTricks; }
	
	public UCTNode GetParent() { return parent; }
	
	public ArrayList<UCTNode> GetChildren() { return children; }
	
	public int GetBestMove() { return bestMove; }
	
	public void Expand(ArrayList<Card> hand, Suit lead, Suit trump)
	{
		children = new ArrayList<UCTNode>();
		
		int i;
		for(i=0; i<hand.size(); i++)
		{
			Card temp = hand.get(i);
			if(ValidMove(hand, temp, lead, trump)) children.add(new UCTNode(this, i)); 
		}
	}
	
	public void SetBestMove()
	{
		if(children == null) 
		{
			bestMove = -1;
			return;
		}
		
		ListIterator<UCTNode> iter = children.listIterator();
		double highestWin = 0;
		double winRate;
		int highestIndex = 0;
		
		
		while(iter.hasNext())
		{
			UCTNode temp = iter.next();
			
			if(temp.GetVisits() > 0)
				winRate = ((double)temp.GetPlayerTricks() + (double)temp.GetPartnerTricks()) / (double)temp.GetVisits();
			else
				winRate = 0;
			
			if(winRate > highestWin) 
			{
				highestWin = winRate;
				highestIndex = iter.nextIndex()-1;
			}
		}
		
		bestMove = highestIndex;
	}
	
	public void AddTrickValues(int total, int player, int partner)
	{
		visits += total;
		playerTricks += player;
		partnerTricks += partner;
	}
	
	public boolean ValidMove(ArrayList<Card> hand, Card card, Suit suit, Suit trump)
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
				!(suit == otherColor && card.IsBower(trump) == 5)) 
			return true;
		
		// Trump is lead, and the player attempts to play the left bower
		if(suit == trump && card.IsBower(trump) == 5) return true;
		
		else
		{
			ListIterator<Card> iter = hand.listIterator();
			
			while(iter.hasNext())						// Check the hand for any card that has the lead suit
			{
				Card temp = iter.next();
				if(temp.GetSuit() == suit &&
						!(suit == otherColor && card.IsBower(trump) == 5)) 
					return false;
				
				// The left bower is in the player's hand when trump is lead
				if(suit == trump && card.IsBower(trump) == 5) return false;
			}
			
			return true;								// If none exist, this is a valid move
		}
	}
	
	public UCTNode UCTSelect()
	{
		if(children == null) return null;
		
		double bestUCT = 0;
		UCTNode next = null;
		ListIterator<UCTNode> iter = children.listIterator();
		
		while(iter.hasNext())
		{
			UCTNode temp = iter.next();
			
			double winRate;
			double uct;
			double uctValue;
			
			if(temp.GetVisits() > 0)
			{
				winRate = (temp.GetPlayerTricks() + temp.GetPartnerTricks()) / temp.GetVisits();
				uct = Math.sqrt(Math.log(visits) / (5*temp.GetVisits()));
				uctValue = winRate + uct;
			}
			else
			{
				uctValue = 10000 + (Math.random() * 1000);
			}
			
			if(uctValue > bestUCT)
			{
				bestUCT = uctValue;
				next = temp;
			}
		}
		
		return next;
	}
}
