package ai;

import java.util.Collections;
import java.util.Stack;
import java.util.ListIterator;

public class Deck 
{
	//////////////////////////////////
	//        Class Members         //
	//////////////////////////////////
	
	private Stack<Card> deck;
	
	//////////////////////////////////
	//         Constructors         //
	//////////////////////////////////
	
	public Deck()
	{
		deck = new Stack<Card>();
	}
	
	public Deck(Deck other)
	{
		deck = new Stack<Card>();
		
		ListIterator<Card> iter = other.deck.listIterator();
		while(iter.hasNext()) { deck.add(new Card(iter.next())); }
	}
	
	//////////////////////////////////
	//       Public Functions       //
	//////////////////////////////////
	
	public void Shuffle() { Collections.shuffle(deck); }
	
	public Card Draw() { return deck.pop(); }
	
	public void AddToDeck(Card c) { deck.push(c); }
	
	public void ResetDeck()
	{
		deck.clear();
		
		int i;
		for(Suit s : Suit.values())
		{
			for(i=9; i<=14; i++)
			{
				deck.push(new Card(i, s));
			}
		}
	}
	
	public String toString() { return deck.toString(); }
}
