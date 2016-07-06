package bot;

import java.util.*;
import ai.*;

public class HeuristicCompare 
{
	// Create a function that gives a hand of cards a heuristic value
	// Add a card that will replace that card
	// Replace each card one at a time and return the new heuristic value
	// Select the card that gives the highest heuristic value
	// Need trump suit to expand the value
	// Range should be value of card + double if trump + 5 if left bower + 10 if right bower
	
	// Returns the index of the card that should be chosen to replace
	public static int ReplaceCard(ArrayList<Card> hand, Card replace)
	{
		int index, highest, h;
		ArrayList<Card> tempHand;
		int i;
		
		index = 0;
		highest = 0;
		
		for(i=0; i<hand.size(); i++)
		{
			tempHand = CopyHand(hand);
			tempHand.remove(i);
			tempHand.add(replace);
			
			h = GetHandHeuristic(tempHand, replace.GetSuit());		// The replace card's suit is trump

			if(h > highest || i == 0 || (h == highest && (Math.random() * 100) < 50))
			{
				highest = h;
				index = i;
			}
			
			tempHand.clear();
		}
		
		return index;
	}
	
	private static int GetHandHeuristic(ArrayList<Card> hand, Suit trump)
	{
		int result = 0;
		ListIterator<Card> iter = hand.listIterator();
		
		while(iter.hasNext())
		{
			Card temp = iter.next();
			result += temp.GetValue() * ((temp.GetSuit() == trump) ? 2 : 1) + temp.IsBower(trump);
		}
		
		return result;
	}
	
	private static ArrayList<Card> CopyHand(ArrayList<Card> hand)
	{
		ArrayList<Card> result = new ArrayList<Card>();
		ListIterator<Card> iter = hand.listIterator();
		
		while(iter.hasNext())
		{
			result.add(iter.next());
		}
		
		return result;
	}
}
