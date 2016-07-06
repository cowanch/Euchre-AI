package ai; 

public class Card 
{
	//////////////////////////////////
	//        Class Members         //
	//////////////////////////////////
	
	private final int value;
	private final Suit suit;
	
	//////////////////////////////////
	//         Constructors         //
	//////////////////////////////////
	
	public Card(int val, Suit s)
	{
		value = val;
		suit = s;
	}
	
	public Card(Card other)
	{
		value = other.value;
		suit = other.suit;
	}
	
	//////////////////////////////////
	//       Public Functions       //
	//////////////////////////////////
	
	public int GetValue() { return value; }
	
	public Suit GetSuit() { return suit; }
	
	public boolean IsSuit(Suit s) { return suit == s; }
	
	public boolean HasValue(int val) { return value == val; }
	
	public boolean HasGreaterValue(Card card) { return value > card.value; }
	
	public String toString() { return ConvertValueToString() + " of " + suit.toString(); }
	
	public int IsBower(Suit trump)
	{	
		if (value == 11 && suit == trump) return 10;
		else if (value == 11 && suit == GetOtherColor(trump)) return 5;
		else return 0;
	}
	
	//////////////////////////////////
	//      Private Functions       //
	//////////////////////////////////
	
	private Suit GetOtherColor(Suit s)
	{
		switch(s)
		{
			case HEART:		return Suit.DIAMOND;
			case DIAMOND:	return Suit.HEART;
			case SPADE:		return Suit.CLUB;
			case CLUB:		return Suit.SPADE;
			default:		return null;
		}
	}
	
	private String ConvertValueToString()
	{
		switch(value)
		{
			case 9:		return "Nine";
			case 10:	return "Ten";
			case 11:	return "Jack";
			case 12:	return "Queen";
			case 13:	return "King";
			case 14:	return "Ace";
			default:	return "[THIS CARD SHOULD NOT BE IN THE DECK FOR EUCHRE!]";
		}
	}
}
