package OthelloGUI;

import java.util.ArrayList;

public class Board {

    //Variables for the Boards values
	public static final int BLACK = 1;
	public static final int WHITE = -1;
	public static final int EMPTY = 0;
    
    //Immediate move that lead to this board
    private Move lastMove;

    /* Variable containing who played last; whose turn resulted in this board
     * Even a new Board has lastLetterPlayed value; it denotes which player will play first
     */
	private int lastLetterPlayed;

	private int [][] gameBoard;
	
	public int numberOfXpawns;
	public int numberOfOpawns;
	
    //vectors describing all 8 possible directions from any one on the board
private final int directions[][] = new int[][] {
		    {-1, 0}, // left
	        {-1, 1}, //diagonally upwards left
	        {0, 1}, //down
	        {0, -1}, //up
	        {1, 1}, //diagonally downwards right
	        {1, 0}, //etc
	        {1, -1},
	        {-1, -1} 
};
    
    private final int eval_table[][] = {
        {50, -1, 5, 2, 2, 5, -1, 50},
        {-1, 10, 1, 1, 1, 1, 10, -1},
        {5, 1, 1, 1, 1, 1, 1, 5},
        {2, 1, 1, 0, 0, 1, 1, 2},
        {2, 1, 1, 0, 0, 1, 1, 2},
        {5, 1, 1, 1, 1, 1, 1, 5},
        {-1, 10, 1, 1, 1, 1, 10, -1},
        {50, -1, 5, 2, 2, 5, -1, 50}
    };   
	
	
	public Board()
	{
		lastMove = new Move();
		lastLetterPlayed = WHITE;
		gameBoard = new int[8][8];
		for(int i=0; i<8; i++)
		{
			for(int j=0; j<8; j++)
			{
				if(i==3 && j==3){
					gameBoard[i][j] = BLACK;
				}else if(i==3 && j==4){
					gameBoard[i][j] = WHITE;
				}else if(i==4 && j==3){
					gameBoard[i][j] = WHITE;
				}else if(i==4 && j==4){
					gameBoard[i][j] = BLACK;
				}else{
					gameBoard[i][j] = EMPTY;
				}
				
			}
		}
	}
	
	public Board(Board board)
	{
		lastMove = board.lastMove;
		lastLetterPlayed = board.lastLetterPlayed;
		gameBoard = new int[8][8];
		for(int i=0; i<8; i++)
		{
			for(int j=0; j<8; j++)
			{
				gameBoard[i][j] = board.gameBoard[i][j];
			}
		}
	}
		
	public Move getLastMove()
	{
		return lastMove;
	}
	
	public int getLastLetterPlayed()
	{
		return lastLetterPlayed;
	}
	
	public int[][] getGameBoard()
	{
		return gameBoard;
	}
        
         public int getCurrentLetter()
        {
            return this.getLastLetterPlayed() == Board.BLACK?Board.WHITE : Board.BLACK;
           
        }
        
        public String letterToPlayer() {
            return this.getCurrentLetter() == Board.BLACK?"WHITE":"BLACK";
        }

	public void setLastMove(Move lastMove)
	{
		this.lastMove.setRow(lastMove.getRow());
		this.lastMove.setCol(lastMove.getCol());
		this.lastMove.setValue(lastMove.getValue());
	}
	
	public void setLastLetterPlayed(int lastLetterPlayed)
	{
		this.lastLetterPlayed = lastLetterPlayed;
	}
	
	public void setGameBoard(int[][] gameBoard)
	{
		for(int i=0; i<8; i++)
		{
			for(int j=0; j<8; j++)
			{
				this.gameBoard[i][j] = gameBoard[i][j];
			}
		}
	}

    //Make a move; it places a letter in the board
	public void makeMove(int row, int col, int letter)
	{
		
		if(validate(row, col)) 
               {
                    gameBoard[row][col] = letter;
                    transform(row, col);
                    
                    this.setLastMove(new Move(row, col));
                    this.setLastLetterPlayed(letter);
		}


		
	}
	
	
    /*	validates whether proposed move is legal
@param grid - the playing grid
@param player - the player. 'WHITE' or 'BLACK'
@param row - player's choice row
@param col - player's choice column
    */

public boolean validate(int row, int col)
{	
        try
        {
            if(!(this.getGameBoard()[row][col] == EMPTY))
            {
                return false;
            }
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            System.out.println("Index out of bounds");
            System.out.println("row: " + row +"\ncol: " + col);
           
        } 
	//for all possible directions
	for(int x = 0; x<directions.length; x++)
	{
		int dr = directions[x][0];
		int dc = directions[x][1];
		
		//start moving along the direction. step is the one specified by the tuple in directions array 
		for(int i = row + dr, j = col + dc; i < this.getGameBoard().length && j < this.getGameBoard().length; i = i + dr, j += dc) 
		{       
                            //if following this direction we get out of bounds
			if(i > this.getGameBoard().length || j > this.getGameBoard().length || i < 0 || j < 0)
			{
				break;
			}
                            
			//if adjacent token in this direction is the same
			if(i == row + dr && j == col + dc && this.getGameBoard()[i][j] == this.getCurrentLetter() )
			{
				break;
			}
			//if grid at this position is empty break
			if(this.getGameBoard()[i][j] == EMPTY)
			{
				break;
			}
			// up to now there were only different tokens, so when we find same we return true
			else if(this.getGameBoard()[i][j] == this.getCurrentLetter())
			{
				return true;
			}
		}
	}

	return false;
}
    
public int[][] transform(int row, int col)
{
	for(int k = 0; k < directions.length; k++)
		{
			int dr = directions[k][0];
			int dc = directions[k][1];
			
			for(int i = row + dr, j = col + dc; i < this.getGameBoard().length && j < this.getGameBoard().length; i += dr, j += dc)
			{
				
                                    if(i > this.getGameBoard().length || j > this.getGameBoard().length || i < 0 || j < 0)
                                    {
                                            break;
                                    }
                                    
				//if token adjacent to this direction is same kind
				if(i == row + dr && j == col + dc && this.getGameBoard()[i][j] == this.getCurrentLetter())
				{
					break;
				} 
				
				//if grid at this position is empty break
				if(this.getGameBoard()[i][j] == EMPTY)
				{
					break;
				}
				
                                    
				//if found token same as player's again, flip all up to there with the same color
				if(this.getGameBoard()[i][j] == this.getCurrentLetter())
				{
                                            
					for(int a = row + dr, b = col + dc; a*dr <= i*dr && b*dc <= j*dc; a+= dr, b += dc)
					{	
						this.getGameBoard()[a][b] = this.getCurrentLetter();
                                                    
						
					}
                                            //we break here because if tokens have already been flipped in a direction, no more can be flipped
                                            break;
				} 
				
				
			}
			
			
		}
            
    
	
            return this.getGameBoard();
    }
    
	public ArrayList<Board> getChildren(int letter)
	{
		ArrayList<Board> children = new ArrayList<Board>();
		for(int row=0; row<8; row++)
		{
			for(int col=0; col<8; col++)
			{
				if(validate(row, col))
				{
					Board child = new Board(this);
					child.makeMove(row, col, letter);
					children.add(child);
				}
			}
		}
		return children;
	}
	
	public int counter(int pawn)
	{
        int counter=0;
        for(int row=0; row<8; row++){
        	
        	for(int col=0; col<8; col++){
        		
        		if(gameBoard[row][col] == pawn){
        			counter++;
        		}
        	}
        }
		return counter;
	}
	
	//general evaluation function. comment and uncomment appropriately for specific evaluation method
        //we could use different evaluation functions when playing ai vs ai but first we nned to narmalize
        ////normalize according to: x - min(x)/(max(x) - min(x))
        public int evaluate()
        {
            //return this.evaluate_mobility();
            //return this.evaluate_generic();
            //return this.evaluate_discSquare();
            return this.evaluate_mobilityWithDiscSquareKnowledge();
        }
	
	private int evaluate_generic()
	{
            int sum=0;
            for(int row=0; row<8; row++)
            {

                    for(int col=0; col<8; col++)
                    {
                            if ((row==0 && col==0) || (row==0 && col==7) || (row==7 && col==0) || (row==7 && col==7)) 
                            {
                                    sum = sum + 10*gameBoard[row][col];
                            }
                            else if (row==0 || row==7 || col==0 || col==7) 
                            {
                                    sum = sum + 3*gameBoard[row][col];
                            }
                            else 
                            {
                                    sum = sum + gameBoard[row][col];
                            }
                    }

            }
            
            return sum;
	}
        
        //use the values array to determine move with greatest value
        private int evaluate_discSquare() 
        {
            int value = 0;
            
            for(int row = 0; row < this.getGameBoard().length; row++)
            {
                for (int col = 0; col < this.getGameBoard().length; col++)
                {
                    if(this.validate(row, col))
                    {
                        value += this.eval_table[row][col];
                    }
                }
            }
            
            
            return value;
        }
        
        //minimize oponent's possible move options
        //maximize own possible move options
        private int evaluate_mobility() 
        {
            //counters for available moves
            int ownPossibleMoves = 0;
            int opponentPossibleMoves = 0;
            
            //board on which opponent's mobility is calculated
            Board tmp_board = new Board(this);
            tmp_board.setLastLetterPlayed(this.getCurrentLetter());
            
            for(int row = 0; row < this.getGameBoard().length; row++)
            {
                for(int col = 0; col < this.getGameBoard().length; col++)
                {
                    if(this.validate(row, col))
                    {
                        ownPossibleMoves++;
                    }
                    if(tmp_board.validate(row, col))
                    {
                        opponentPossibleMoves++;
                    }
                }
            }
            
            
            return ownPossibleMoves - opponentPossibleMoves;
            
        }
        
        private int evaluate_mobilityWithDiscSquareKnowledge()
        {
         //counters for available moves
            int ownPossibleMoves = 0;
            int opponentPossibleMoves = 0;
            
            //board on which opponent's mobility is calculated
            Board tmp_board = new Board(this);
            tmp_board.setLastLetterPlayed(this.getCurrentLetter());
            
            for(int row = 0; row < this.getGameBoard().length; row++)
            {
                for(int col = 0; col < this.getGameBoard().length; col++)
                {
                    if(this.validate(row, col))
                    {
                        ownPossibleMoves += this.eval_table[row][col];
                    }
                    if(tmp_board.validate(row, col))
                    {
                        opponentPossibleMoves += this.eval_table[row][col];
                                
                    }
                }
            }
            
            //just to return a normalized number 
            return 100*(ownPossibleMoves - opponentPossibleMoves) / (ownPossibleMoves + opponentPossibleMoves);   
        }
	
    public boolean isTerminal()
    {	
    	
    	boolean isTerminal = true;
           
           if(counter(BLACK) + counter(WHITE) == 64 )
           {
               return isTerminal;
           } 
           
           if(counter(WHITE) == 0 || counter(BLACK) == 0)
           {
               return isTerminal;
           } 
            
           if(this.hasMoves(this.getCurrentLetter()))
           {
               
               return false;
           }
           //implement turn exchange here
           else if(this.hasMoves(this.lastLetterPlayed))
           {    
               
               this.setLastLetterPlayed(this.getCurrentLetter());
               
               return false;
           }
           
        
            return isTerminal; 
    }
    
    /*checks whether there are available moves, by duplicating the current board and testing every move on the duplicate */
    public boolean hasMoves(int player)
    {
       Board tmp_board = new Board(this);
       
       if(player == Board.BLACK)
       {
            tmp_board.setLastLetterPlayed(Board.WHITE);
       }
       else
       {
           tmp_board.setLastLetterPlayed(Board.BLACK);
       }
        
       for(int row = 0; row < this.getGameBoard().length; row++)
       {
           for(int col = 0; col < this.getGameBoard().length; col++)
           {
              if(tmp_board.validate(row, col))
              {
                  return true;
              }
           }
       }

        return false;   
    }
    
   
    
}