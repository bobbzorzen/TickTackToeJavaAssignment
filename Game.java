package tk.bobbzorzen;

public class Game
{
	private int gameField[] = {0,0,0,0,0,0,0,0,0};
	private int nrOfMarks;
	private int enemyNrOfMarks;
	Game()
	{
		nrOfMarks = 0;
	}
	
	public int click(int buttonClicked)
	{
		int result = 0;
		if(this.nrOfMarks < 3)
		{
			if(gameField[buttonClicked] == 0)
			{
				gameField[buttonClicked] = 1;
				this.nrOfMarks++;
				result = 1;
			}
		} 
		else
		{
			if(gameField[buttonClicked] != 0)
			{
				gameField[buttonClicked] = 0;
				this.nrOfMarks--;
				result = 2;
			}
		}
		
		return result;
	}
	public int enemyClick(int buttonClicked)
	{
		int result = 0;
		if(this.enemyNrOfMarks < 3)
		{
			if(gameField[buttonClicked] == 0)
			{
				gameField[buttonClicked] = 2;
				this.enemyNrOfMarks++;
				result = 1;
			}
		} 
		else
		{
			if(gameField[buttonClicked] != 0)
			{
				gameField[buttonClicked] = 0;
				this.enemyNrOfMarks--;
				result = 2;
			}
		}
		
		return result;
	}
	public boolean winning()
	{
		boolean win = false;
			//horizontal wins
		if( 		gameField[0] == gameField[1] && gameField[1] == gameField[2] && gameField[0] != 0){win = true;}
			else if(gameField[3] == gameField[4] && gameField[4] == gameField[5] && gameField[3] != 0){win = true;}
			else if(gameField[6] == gameField[7] && gameField[7] == gameField[8] && gameField[6] != 0){win = true;}

			//Vertical wins
			else if(gameField[0] == gameField[3] && gameField[3] == gameField[6] && gameField[0] != 0){win = true;}
			else if(gameField[1] == gameField[4] && gameField[4] == gameField[7] && gameField[1] != 0){win = true;}
			else if(gameField[2] == gameField[5] && gameField[5] == gameField[8] && gameField[2] != 0){win = true;}

			//diagonal wins
			else if(gameField[0] == gameField[4] && gameField[4] == gameField[8] && gameField[0] != 0){win = true;}
			else if(gameField[2] == gameField[4] && gameField[4] == gameField[6] && gameField[2] != 0){win = true;}
		return win;
	}

}
