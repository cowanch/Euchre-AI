package ai;

public class Main 
{
	public static void main(String args[])
	{		
		//---------------------------------------------------------------------
		
		new Controller();
		
		//---------------------------------------------------------------------
/*		
		Euchre g = new Euchre();
		g.SetupGame();
		Card top = g.Deal();
		g.SetTrump(Suit.HEART, -1);
		
		UCTSearch uct = new UCTSearch(0);
		Card[] cards = new Card[] {null, null, null, null};
		
		System.out.println(g.GetPlayer(0).GetHand());
		System.out.println(g.GetPlayer(1).GetHand());
		System.out.println(g.GetPlayer(2).GetHand());
		System.out.println(g.GetPlayer(3).GetHand());
		System.out.println(top);
		
//		uct.AddMissingSuit(1, Suit.HEART);
//		uct.AddMissingSuit(3, Suit.CLUB);
		
		uct.SetTopCard(1, top);
		
//		System.out.println("Searching...");
//		System.out.println(uct.UCTSearchMove());
//		System.out.println("Done");
		
		System.out.println("Suit confidence " + uct.GetSuitConfidence());
		System.out.println("Alone confidence " + uct.GetAloneConfidence());
*/		
		//---------------------------------------------------------------------
	}
}
