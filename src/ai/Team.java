package ai;

public class Team 
{
	//////////////////////////////////
	//        Class Members         //
	//////////////////////////////////
	
	private Player player1;
	private Player player2;
	private int score;
	private int tricks;
	
	//////////////////////////////////
	//         Constructors         //
	//////////////////////////////////
	
	public Team(int p1, PlayerType pt1, int p2, PlayerType pt2)
	{
		player1 = new Player(p1, pt1);
		player2 = new Player(p2, pt2);
		score = 0;
		tricks = 0;
	}
	
	public Team(Team other)
	{
		player1 = new Player(other.player1);
		player2 = new Player(other.player2);
		score = other.score;
		tricks = other.tricks;
	}
	
	//////////////////////////////////
	//       Public Functions       //
	//////////////////////////////////
	
	public void AddScore(int points) 
	{ 
		score += points;
		if(score > 10) score = 10; 
	}
	
	public void AddTrick() { tricks++; }
	
	public int GetScore() { return score; }
	
	public int GetTricks() { return tricks; }
	
	public Player GetPlayer1() { return player1; }
	
	public Player GetPlayer2() { return player2; }
	
	public void ResetTeam()
	{
		player1.ResetPlayer();
		player2.ResetPlayer();
		ResetScore();
		ResetTricks();
	}
	
	public void ResetHands()
	{
		player1.ResetHand();
		player2.ResetHand();
		ResetTricks();
	}
	
	//////////////////////////////////
	//      Private Functions       //
	//////////////////////////////////
	
	private void ResetScore() { score = 0; }
	
	private void ResetTricks() { tricks = 0; }
}
