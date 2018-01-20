/*
File Name:     TTT4X4AI.java
Names:         Kelly Jia and Amy Zhou
Class:         ICS3U (D)
Date:          December 18, 2015
Description:   This program creates a 1-player 4X4 Tic Tac Toe game against AI.
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TTT4X4AI implements MouseListener
{
   //declare all the global variables:
   Drawing draw = new Drawing();
   int [][] board = new int [4][4]; //creating and initializing the board to 0's (blank)
   boolean playerTurn = true;       //the game starts with the player going first
   boolean win = false;             //creating a boolean for win, and setting it to false
   int countClick = 0;              //counts how many times the player's clicked after winning
                                    //(used to determine when the board should get reset)
   JLabel message = new JLabel("Luigi(Player)'s turn");
   ImageIcon[] boardPictures = new ImageIcon[3];

   public TTT4X4AI()      // constructor
   {
      for (int i = 0; i < boardPictures.length; i++)
         boardPictures[i] = new ImageIcon(i + ".jpg"); 
      JFrame frame = new JFrame("TTT4X4AI");
      frame.add(draw);
      draw.addMouseListener(this);
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //if the user closes the java window,
                                                               //only this window is closed, not the whole
                                                               //game
      frame.setSize(420, 480);
      message.setFont(new Font("Serif",Font.BOLD,20));
      message.setForeground(Color.blue);
      message.setHorizontalAlignment(SwingConstants.CENTER);
      frame.add(message, "South");
      frame.setVisible(true);
   }

// A method that checks if the board is full or not
// returns true if the board is full, and false if it is not
   public boolean fullBoard()
   {
      int countBlank=0; //initializes the number of blank spots in the board to 0
                        //looks through the whole array, counting the number of blank spots
      for(int r=0;r<4;r++)	
      {
         for(int col=0;col<4;col++)
         {
            if(board[r][col]==0)
               countBlank++;		
         }	
      }
      //if the board has 0 blank spots, that means the board's full
      //returns true if the board has 0 blank spots, and false otherwise
      return(countBlank==0);
   }//fullBoard method   
             
// A method that checks if any of the players has won yet
// returns true if someone has won, and false if no one has won yet
   public boolean checkForWin()
   { 
      int count;  //counts how many pairs of equal markers there are in each row/column/diagonal
      
      //checks each row of the board
      for (int r=0; r < 4; r++)
      {
         count=0; //initializing the count to 0 as it checks a new row
         for (int c=0; c < 3; c++)
         {
            //checks if a spot in the row is equal to the next spot in that row
            //ends up checking if [row,0] == [row,1] && [row,1] == [row,2] && [row,2] == [row,3]
            if (board[r][c] == board[r][c+1] && board[r][c] != 0)
               count++;
         }
         //if the 3 pairs of spots in that row are equal, that means 
         //all markers in that row are the same, which means someone has won
         //it then turns the win boolean to true
         if (count == 3)
            win = true;          
      }
      
      //checks each column of the board
      for (int c=0; c < 4; c++)
      {
         count=0; //initializing the count to 0 as it checks a new column
         for (int r=0; r < 3; r++)
         {
            //checks if a spot in the column is equal to the next spot in that column
            //ends up checking if 
            //[0, column] == [1,column] && [1,column] == [2,column] && [2,column] == [3,column]
            if (board[r][c] == board[r+1][c] && board[r][c] != 0)
               count++;
         }
         //if the 3 pairs of spots in that column are equal, that means 
         //all markers in that column are the same, which means someone has won
         //it then turns the win boolean to true
         if (count == 3)
            win = true;
      }
      
      //checks if each marker of the top right - bottom left diagonal are equal 
      //if they are, it turns the win boolean to true
      if (board[0][3] == board[1][2] && board[1][2] == board[2][1] && board[2][1] == board[3][0] && board[3][0] != 0)
         win = true;
   	
      //checks if each marker of the top left - bottom right diagonal are equal 
      //if they are, it turns the win boolean to true
      if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[2][2] == board[3][3] && board[3][3] != 0)
         win = true;	
      
      //if win is true, then output the appropriate win message
      if (win)
      {
         //since once a player goes, and the playerTurn boolean changes value,
         //then if playerTurn = false, the player wins, and if playerTurn = true, the computer wins
         if (playerTurn)
            message.setText("Mario(Computer) wins!!!!");
         else
            message.setText("Luigi(Player) wins!!!");
      }
      //return the value of the win boolean 
      return(win);
   }//checkForWin method

/*   
A method that generates the spot where the computer places their O for its turn

-If the computer has 3 O's in a row/column/diagonal and the last spot is 
 empty, the computer places its O there
-If the computer cannot win in its turn, it then blocks the 
 player if the player has 3 in a row/column/diagonal
-If the computer cannot block either, it then checks if it has 2 O's in a  
 row/column/diagonal and the other 2 spots are empty, the computer places  
 an O in that row/column/diagonal
-The computer then checks if the any of the 4 middle spots are filled. The computer chooses these
 spots for its turn because these spots are the most strategic
-If all the above conditions are false, the computer just randomly picks a spot to place its O

Returns the computer's chosen row and column in the form of a string: "r c"
*/
   public String compTurn()
   {
      int countPlayer=0;      //counting how many X's the player has in a certain row/column/diagonal
      int countComp=0;        //counting how many O's the computer has in a certain row/column/diagonal
      int countEmpty=0;       //counting the number of empty spots in a certain row/column/diagonal
      boolean compPick=false; //determines if the computer has picked a spot yet or not
      boolean ifEmpty=false;  //determines if a certain row/column/diagonal contains an empty spot
      String compNums="";     //holds the coordinates of the computers chosen spot
      int emptyR=0, emptyC=0; //holds the row and column of the spot that is empty
                              //used when the player is looking for a winning spot, and when it's blocking
                              //the player from winning
                              //also used to hold the randomly generated rows and columns
      int column = 3;         //variable used as the column coordinate when looking through the 
                              //top right - bottom left diagonal
   
   //**************************************************** WINNING THE GAME
      
   //if the computer has 3 O's in a row/column/diagonal and the last spot is empty, 
   //the computer places their O there
   
      //checks if the computer has 3 O's in any of the rows
      for (int r=0; r < 4; r++) 
      {
         countComp = 0;             //initializing the number of O's in the row to 0
         ifEmpty = false;           //initializing if there is an empty spot in the row to false
         for (int c=0; c < 4; c++)
         {
            if (board[r][c] == 2)   //checking if the spot in the row contains an O
               countComp++;         //adding up the number of O's in the row
            
            if (board[r][c] == 0)   //checking if the spot in the row contains an empty spot
            {
               emptyR = r;          //saving the coordinates of the row and column that contains 
               emptyC = c;          //the empty spot
               ifEmpty = true;      //the ifEmpty boolean becomes true, since an empty spot exists
            }
             
            //once the counter reaches the 4th column in the row, it checks if the computer has 3 O's in the row
            if (c == 3) 
            {
               //if the computer has 3 O's in a row, and it has not picked a spot yet, and if that row
               //contains an empty spot, then it saves the row and column of that empty spot for its turn
               if (countComp == 3 && compPick == false && ifEmpty == true)  
               {
                  compNums = emptyR + " " + emptyC; //saving the row and column for the computer's turn
                  compPick = true;                  //the computer has now picked a spot for its turn so
                                                    //compPick becomes true
               }
            }
         }
      }//for loop for checking if the computer can win in any of the rows
      
      //checks if the computer has 3 O's in any of the columns
      for (int c=0; c < 4; c++)
      {
         countComp = 0;             //initializing the number of O's in the column to 0
         ifEmpty = false;           //initializing if there is an empty spot in the column to false
         for (int r=0; r < 4; r++)
         {
            if (board[r][c] == 2)   //checking if the spot in the column contains an O
               countComp++;         //adding up the number of O's in the column
               
            if (board[r][c] == 0)   //checking if the spot in the column contains an empty spot
            {
               emptyR = r;          //saving the coordinates of the row and column that contains 
               emptyC = c;          //the empty spot
               ifEmpty = true;      //the ifEmpty boolean becomes true, since an empty spot exists
            } 
            
            //once the counter reaches the 4th row in the column, it checks if the computer has 3 O's in the column
            if (r == 3)
            {
               //if the computer has 3 O's in a column, and it has not picked a spot yet, and if that column
               //contains an empty spot, then it saves the row and column of that empty spot for its turn
               if (countComp == 3 && compPick == false &&  ifEmpty == true)
               {
                  compNums = emptyR + " " + emptyC; //saving the row and column for the computer's turn
                  compPick = true;                  //the computer has now picked a spot for its turn so
                                                    //compPick becomes true
               }
            }
         }
      }//for loop for checking if the computer can win in any of the columns
      
      //checks if the computer has 3 O's in the top left - bottom right diagonal
      countComp = 0;    //initializing the number of O's in the diagonal to 0
      ifEmpty = false;  //initializing if there is an empty spot in the diagonal to false
      
      //checks the diagonal for 3 O's if the computer still has not picked a spot
      if (!(compPick))  
      {
         for (int i=0; i < 4; i++)
         {
            if (board[i][i] == 2)   //checking if the spot in the diagonal contains an O
               countComp++;         //adding up the number of O's in the diagonal
            
            if (board[i][i] == 0)   //checking if the spot in the diagonal contains an empty spot
            {
               emptyR = i;          //saving the coordinates of the row and column that contains 
               emptyC = i;          //the empty spot
               ifEmpty = true;      //the ifEmpty boolean becomes true, since an empty spot exists
            }
         }//for loop for adding up the O's in the diagonal, and checking if an empty
          //spot exists
         
         //if the diagonal contains 3 O's and an empty spot exists in the diagonal, then it 
         //saves the row and column of that empty spot for its turn
         if (countComp == 3 && ifEmpty == true)
         {
            compNums = emptyR + " " + emptyC; //saving the row and column for the computer's turn
            compPick = true;                  //the computer has now picked a spot for its turn so
                                              //compPick becomes true
         }
      }//if statement to check if the computer can win in the top left - bottom right diagonal
      
      //checks if the computer has 3 O's in the top right - bottom left diagonal
      countComp = 0;    //initializing the number of O's in the diagonal to 0
      ifEmpty = false;  //initializing if there is an empty spot in the diagonal to false
      
      //checks the diagonal for 3 O's if the computer still has not picked a spot
      if (!(compPick))
      {
         for (int r=0; r < 4; r++)
         {
            if (board[r][column] == 2) //checking if the spot in the diagonal contains an O
               countComp++;            //adding up the number of O's in the diagonal
         
            if (board[r][column] == 0) //checking if the spot in the diagonal contains an empty spot
            {
               emptyR = r;             //saving the coordinates of the row and column that contains 
               emptyC = column;        //the empty spot
               ifEmpty = true;         //the ifEmpty boolean becomes true, since an empty spot exists
            }
            column--;                  //the column coordinate decreases each time the counter
                                       //goes down the diagonal
         }
         
         //if the diagonal contains 3 O's and an empty spot exists in the diagonal, then it 
         //saves the row and column of that empty spot for its turn
         if (countComp == 3 && ifEmpty == true)
         {
            compNums = emptyR + " " + emptyC; //saving the row and column for the computer's turn
            compPick = true;                  //the computer has now picked a spot for its turn so
                                              //compPick becomes true
         }
      }//if statement to check if the computer can win in the top right - bottom left diagonal
   
   //**************************************************** BLOCKING THE PLAYER
   
   //if the player has 3 in a row/column/diagonal and the last spot in is empty, the computer
   //blocks the player from winning by placing its O in the empty spot
   
      column = 3; //initializing the column for the top right - bottom left diagonal
                  //back to 3
   
      //checks if the player has 3 X's in any of the rows
      for (int r=0; r < 4; r++) 
      {
         countPlayer = 0;           //initializing the number of X's in the row to 0
         ifEmpty = false;           //initializing if there is an empty spot in the row to false
         
         for (int c=0; c < 4; c++)
         {
            if (board[r][c] == 1)   //checking if the spot in the row contains an X
               countPlayer++;       //adding up the number of X's in the row
            
            if (board[r][c] == 0)   //checking if the spot in the row contains an empty spot
            {
               emptyR = r;          //saving the coordinates of the row and column that contains 
               emptyC = c;          //the empty spot
               ifEmpty = true;      //the ifEmpty boolean becomes true, since an empty spot exists
            }
             
            //once the counter reaches the 4th column in the row, checks if the player has 
            //3 X's in the row
            if (c == 3) 
            {                  
               //if the player has 3 X's in the row, and the computer has not picked a spot for its
               //turn yet, and the row contains an empty spot, the computer saves the row and column
               //of that empty spot for its turn
               if (countPlayer == 3 && compPick == false && ifEmpty == true)  
               {
                  compNums = emptyR + " " + emptyC; //saving the row and column for the computer's turn
                  compPick = true;                  //the computer has now picked a spot for its turn so
                                                    //compPick becomes true
               }
            }
         }
      }//for loop that checks if the computer can block the player from winning in
       //any of the rows
       
      //checks if the player has 3 X's in any of the columns
      for (int c=0; c < 4; c++)
      {
         countPlayer = 0;           //initializing the number of X's in the column to 0
         ifEmpty = false;           //initializing if there is an empty spot in the column to false
         
         for (int r=0; r < 4; r++)
         {
            if (board[r][c] == 1)   //checking if the spot in the column contains an X
               countPlayer++;       //adding up the number of X's in the column
               
            if (board[r][c] == 0)   //checking if the spot in the column contains an empty spot
            {
               emptyR = r;          //saving the coordinates of the row and column that contains 
               emptyC = c;          //the empty spot
               ifEmpty = true;      //the ifEmpty boolean becomes true, since an empty spot exists
            }  
            
            //once the counter reaches the 4th row in the column, checks if the player has 
            //3 X's in the column
            if (r == 3)
            {
               //if the player has 3 X's in the column, and the computer has not picked a spot for its
               //turn yet, and the column contains an empty spot, the computer saves the row and column
               //of that empty spot for its turn
               if (countPlayer == 3 && compPick == false &&  ifEmpty == true)
               {
                  compNums = emptyR + " " + emptyC; //saving the row and column for the computer's turn
                  compPick = true;                  //the computer has now picked a spot for its turn so
                                                    //compPick becomes true
               }
            }
         }
      }//for loop that checks if the computer can block the player from winning in
       //any of the columns
      
      //checks if the player has 3 X's in the top left - bottom right diagonal
      countPlayer = 0;  //initializing the number of X's in the diagonal to 0
      ifEmpty = false;  //initializing if there is an empty spot in the diagonal to false
      
      //checks the diagonal for 3 X's if the computer still has not picked a spot
      if (!compPick)
      {
         for (int i=0; i < 4; i++)
         {
            if (board[i][i] == 1)   //checking if the spot in the diagonal contains an X
               countPlayer++;       //adding up the number of X's in the diagonal
            
            if (board[i][i] == 0)   //checking if the spot in the column contains an empty spot
            {
               emptyR = i;          //saving the coordinates of the row and column that contains 
               emptyC = i;          //the empty spot
               ifEmpty = true;      //the ifEmpty boolean becomes true, since an empty spot exists
            }
         }
         //if the player has 3 X's in the diagonal and an empty spot exists in the diagonal, the
         //computer saves the row and column of that empty spot for its turn
         if (countPlayer == 3 && ifEmpty == true)
         {
            compNums = emptyR + " " + emptyC; //saving the row and column for the computer's turn
            compPick = true;                  //the computer has now picked a spot for its turn so
                                              //compPick becomes true
         }
      }//if statement to check if the computer can block the player from winning in the
       //top left - bottom right diagonal
      
      //checks if the player has 3 X's in the top right - bottom left diagonal
      countPlayer = 0;  //initializing the number of X's in the diagonal to 0
      ifEmpty = false;  //initializing if there is an empty spot in the diagonal to false
      
      //checks the diagonal for 3 X's if the computer still has not picked a spot      
      if (!compPick)
      {
         for (int r=0; r < 4; r++)
         {
            if (board[r][column] == 1)    //checking if the spot in the diagonal contains an X
               countPlayer++;             //adding up the number of X's in the diagonal
         
            if (board[r][column] == 0)    //checking if the spot in the column contains an empty spot
            {
               emptyR = r;                //saving the coordinates of the row and column that contains 
               emptyC = column;           //the empty spot
               ifEmpty = true;            //the ifEmpty boolean becomes true, since an empty spot exists
            }
            column--;                     //the column coordinate decreases each time the counter
                                          //goes down the diagonal
         }
         //if the player has 3 X's in the diagonal and an empty spot exists in the diagonal, the
         //computer saves the row and column of that empty spot for its turn
         if (countPlayer == 3 && ifEmpty == true)
         {
            compNums = emptyR + " " + emptyC; //saving the row and column for the computer's turn
            compPick = true;                  //the computer has now picked a spot for its turn so
                                              //compPick becomes true
         }
      }//if statement to check if the computer can block the player from winning in the
       //top right - bottom left diagonal
   
   //**************************************************** 
   //PLAYING A TURN IF THERE ARE 2 O'S AND 2 EMPTY SPOTS IN A ROW/COLUMN/DIAGONAL
      
   //if the computer has 2 O's in a row/column/diagonal and the other 2 spots are empty, the 
   //computer places an O in that row/column/diagonal
   
   //once the computer goes its turn, the 2 O's, 2 empty turns into
   //3 O's, 1 empty, so then the winning conditions will be true for
   //the computer's next turn, if the player does not block the computer's win on their turn
   
      column = 3; //initializing the column for the top right - bottom left diagonal
                  //back to 3
      
      //checks if the computer has 2 O's and 2 empty spots in any row
      for (int r=0; r < 4; r++)
      {
         countComp = 0;             //initializing the number of O's in the row to 0
         countEmpty = 0;            //initializing the number of empty spots in the row to 0
         for (int c=0; c < 4; c++)
         {
            if (board[r][c] == 2)   //checking if the spot in the row contains an O
               countComp++;         //adding up the number of O's in the row
               
            if (board[r][c] == 0)   //checking if the spot in the row is empty
            {
               countEmpty++;        //adding up the number of empty spots in the row
               emptyR = r;          //saving one set of coordinates for one of the empty spots
               emptyC = c;          //so the computer can use it for its turn
            }
            
            //once the counter reaches the 4th column in the row, it checks if the computer has 
            //2 O's and 2 empty spots in the row
            if (c == 3)
            {
               //if the computer has 2 O's, 2 empty spots in the row, and the computer still has not picked
               //a spot to go for its turn, it saves the row and column of one of the 2 empty spots
               if (countComp == 2 && countEmpty == 2 && compPick == false)
               {
                  compNums = emptyR + " " + emptyC; //saving the row and column for the computer's turn
                  compPick = true;                  //the computer has now picked a spot for its turn so
                                                    //compPick becomes true
               }
            }
         }
      }//for loop to check if any of the rows contains 2 O's and 2 empty spots, so the computer can
       //place an O in one of the empty spots of a row
       
      //checks if the computer has 2 O's and 2 empty spots in any column   
      for (int c=0; c < 4; c++)
      {
         countComp = 0;             //initializing the number of O's in the column to 0
         countEmpty = 0;            //initializing the number of empty spots in the column to 0
         
         for (int r=0; r < 4; r++)
         {
            if (board[r][c] == 2)   //checking if the spot in the column contains an O
               countComp++;         //adding up the number of O's in the column
               
            if (board[r][c] == 0)   //checking if the spot in the column is empty
            {
               countEmpty++;        //adding up the number of empty spots in the column
               emptyR = r;          //saving one set of coordinates for one of the empty spots
               emptyC = c;          //so the computer can use it for its turn
            }
            
            //once the counter reaches the 4th row in the column, it checks if the computer has 
            //2 O's and 2 empty spots in the column
            if (r == 3)
            {
               //if the computer has 2 O's, 2 empty spots in the column, and the computer still has not picked
               //a spot to go for its turn, it saves the row and column of one of the 2 empty spots
               if (countComp == 2 && countEmpty == 2 && compPick == false)
               {
                  compNums = emptyR + " " + emptyC; //saving the row and column for the computer's turn
                  compPick = true;                  //the computer has now picked a spot for its turn so
                                                    //compPick becomes true
               }
            }
         }
      }//for loop to check if any of the columns contains 2 O's and 2 empty spots, so the computer can
       //place an O in one of the empty spots of a row
      
      //checks if the computer has 2 O's and 2 empty spots in the top left - bottom right diagonal
      countComp = 0;  //initializing the number of O's in the diagonal to 0
      countEmpty = 0; //initializing the number of empty spots in the diagonal to 0
      
      //checks the diagonal for 2 O's and 2 empty spots if the computer still has not picked a spot 
      if (!compPick)
      {
         for (int i=0; i < 4; i++)
         {
            if (board[i][i] == 2)   //checking if the spot in the diagonal contains an O
               countComp++;         //adding up the number of O's in the diagonal
            
            if (board[i][i] == 0)   //checking if the spot in the diagonal is empty
            {
               countEmpty++;        //adding up the number of empty spots in the diagonal
               emptyR = i;          //saving one set of coordinates for one of the empty spots
               emptyC = i;          //so the computer can use it for its turn
            }
         }
         
         //if the diagonal contains 2 O's and 2 empty spots, the computer saves the row and column of an
         //empty spot of the diagonal for its turn
         if (countComp == 2 && countEmpty == 2)
         {
            compNums = emptyR + " " + emptyC; //saving the row and column for the computer's turn
            compPick = true;                  //the computer has now picked a spot for its turn so
                                              //compPick becomes true
         }
      
      }//if statement to check if the computer has 2 O's and 2 empty spots in the top left-
       //bottom right diagonal, so they can place its turn in 1 of the empty spots
      
      //checks if the computer has 2 O's and 2 empty spots in the top right - bottom left diagonal
      countComp = 0;  //initializing the number of O's in the diagonal to 0
      countEmpty = 0; //initializing the number of empty spots in the diagonal to 0
      
      //checks the diagonal for 2 O's and 2 empty spots if the computer still has not picked a spot 
      if (!compPick)
      {
         for (int r=0; r < 4; r++)
         {
            if (board[r][column] == 2) //checking if the spot in the diagonal contains an O
               countComp++;            //adding up the number of O's in the diagonal
            
            if (board[r][column] == 0) //check if the spot in the diagonal is empty   
            {
               countEmpty++;           //adding up the number of empty spots in the diagonal
               emptyR = r;             //saving one set of coordinates for one of the empty spots
               emptyC = column;        //so the computer can use it for its turn
            }        
         }
         
         //if the diagonal contains 2 O's and 2 empty spots, the computer saves the row and column of one
         //of the empty spots for its turn
         if (countComp == 2 && countEmpty == 2)
         {
            compNums = emptyR + " " + emptyC; //saving the row and column for the computer's turn
            compPick = true;                  //the computer has now picked a spot for its turn so
                                              //compPick becomes true
         }
      }//if statement to check if the computer has 2 O's and 2 empty spots in the top right-
       //bottom left diagonal, so they can place its turn in 1 of the empty spots
   
    //**************************************************** 
    //PICKING THE MIDDLE SPOTS, OR GENERATE A RANDOM SPOT
    
    //the computer checks through the 4 middle spots, and if any of them are still empty, the
    //computer chooses 1 of those for its turn
    //if all the middle spots are taken too, then the computer just generates a random empty
    //spot for its turn
      if (!compPick)
      {
         //if atleast 1 of the spots in the middle 4 spots are empty, the computer randomly chooses
         //one of those spots as its turn
         if (board[1][1] == 0 || board[1][2] == 0 || board[2][1] == 0 || board[2][2] == 0)
         {
            do                                        
            {
               emptyR = (int)(Math.random()*2+1);     //generates a random row (1-2)
               emptyC = (int)(Math.random()*2+1);     //generates a random column (1-2)
            } while(board[emptyR][emptyC] != 0);      //keeps generating until the generated spot is empty
            
            compNums = emptyR + " " + emptyC;         //the randomly generated row and column is saved as
                                                      //the coordinates for the computer's turn
         }
         
         else                                         //if all the middle spots are taken, the computer 
         {                                            //generates a random empty spot
            do
            {
               emptyR = (int)(Math.random()*4);       //generate a random row (between 0-3)
               emptyC = (int)(Math.random()*4);       //generate a random column (between 0-3)
            }while(board[emptyR][emptyC] != 0);       //keep generating a random row and column until it 
                                                      //generates a spot on the board that is empty
         
            compNums = emptyR + " " + emptyC;         //the randomly generated row and column is saved as
                                                      //the coordinates for the computer's turn    
         }
      }
      
      return(compNums); //return the row and column for the computer's turn
          
   }//compTurn method

// places the current player's marker in the desired square if possible
// checks for a win/tie, and if the game hasn't ended yet
// tells the other player that it's their turn
// updates the board after the player's turn and the computer's turn
   public void markSquare(int row, int col)
   {   
      int compRow, compColumn;      //the variables to hold the computer's row and column
      String[] compNumbers;         //the array to hold the computer's row and column after splitting
      
      if (!(checkForWin()))         //let the player/computer go if no one has won yet
      {
         if (board[row][col] == 0 ) //updating the board if the spot is blank and it's the player's turn
         {
            board[row][col] = 1;    //Change the value of the array to 1 for the player
            draw.repaint();         //Update the graphics of the board
            if (!(checkForWin()))   //If no one still hasn't won, switch the game message to 
                                    //the Computer's turn
               message.setText("Mario(Computer)'s turn!"); 
         }
        
         compNumbers = compTurn().split(" ");            //Split the string containing the computer's 
                                                         //row and column
         compRow = Integer.parseInt(compNumbers[0]);     //Parse the elements of the string array                                                      
         compColumn = Integer.parseInt(compNumbers[1]);  //containing the computer's row and column to 
                                                         //integers
         board[compRow][compColumn] = 2;                 //Change the value of the array to 2 for the computer
         draw.repaint();                                 //Update the graphics of the board
         if (!(checkForWin()))                           //If no one still hasn't won, switch the game message
                                                         //back to X's turn
            message.setText("Luigi(Player)'s turn!");       
      }     
   
      //if the board is full, but no one has won, then a tie message is printed
      if (fullBoard())
      {
         if (!(checkForWin()))
            message.setText("TIE!!!");
      }
   
   }//markSquare method
   
   //Graphics:
   class Drawing extends JComponent
   {
      public void paint(Graphics g)
      {
         Graphics2D g2 = (Graphics2D) g;
      // draw the content of the board
         
         for (int row = 0; row < 4; row++)
            for (int col = 0; col < 4; col++)
               g.drawImage(boardPictures[board[row][col]].getImage(),col * 100, row * 100,100,100,this);
      // draw grid
      
         g2.setStroke(new BasicStroke(10));
         g.fillRect(100,5,5,395);
         g.fillRect(200,5,5,395);
         g.fillRect(300,5,5,395);
      
         g.fillRect(5,100,395,5);
         g.fillRect(5,200,395,5);
         g.fillRect(5,300,395,5);
      
      	//If all the values of a row/column/diagonal are equal, and atleast 1 does not equal to 0, draw a line 
         //through that row/column/diagonal to indicate a win
         if (board[0][0]==board[0][1] && board[0][1]==board[0][2] && board[0][2] == board[0][3] && board[0][3] != 0)
            g.drawLine(0, 50, 395, 50);
         else if (board[1][0]==board[1][1] && board[1][1]==board[1][2] && board[1][2] == board[1][3] && board[1][3] != 0)
            g.drawLine(0,150,395,150);
         else if (board[2][0]==board[2][1] && board[2][1]==board[2][2] && board[2][2] == board[2][3] && board[2][3] != 0)
            g.drawLine(0,250,395,250);
         else if (board[3][0]==board[3][1] && board[3][1]==board[3][2] && board[3][2] == board[3][3] && board[3][3] != 0)
            g.drawLine(0,350,395,350);
         else if (board[0][0]==board[1][0] && board[1][0]==board[2][0] && board[2][0] == board[3][0] && board[3][0] != 0)
            g.drawLine(50,0,50,395);
         else if (board[0][1]==board[1][1] && board[1][1]==board[2][1] && board[2][1] == board[3][1] && board[3][1] != 0)
            g.drawLine(150,0,150,395);
         else if (board[0][2]==board[1][2] && board[1][2]==board[2][2] && board[2][2] == board[3][2] && board[3][2] != 0)
            g.drawLine(250,0,250,395);
         else if (board[0][3]==board[1][3] && board[1][3]==board[2][3] && board[2][3] == board[3][3] && board[3][3] != 0)
            g.drawLine(350,0,350,395);
         else if (board[0][0]==board[1][1] && board[1][1]==board[2][2] && board[2][2] == board[3][3] && board[3][3] != 0)
            g.drawLine(0,0,395,395);
         else if (board[3][0]==board[2][1] && board[2][1]==board[1][2] && board[1][2] == board[0][3] && board[0][3] != 0)
            g.drawLine(10,395,395,10);
      }
   }

// --> starting implementing MouseListener - it has 5 methods 
   public void mousePressed(MouseEvent e)
   {
   }
   
   //takes care of all the clicking of the mouse
   public void mouseReleased(MouseEvent e)
   {
		//If no one has won yet, and it is the player's turn, they can click
      if (!win && playerTurn)
      {
      // find coords of mouse click
         int row = e.getY()/100;
         int col = e.getX()/100;
      // handle the move that the player has made on the game board
         markSquare(row, col);
      }
   }

   public void mouseClicked(MouseEvent e)
   {
   }

   public void mouseEntered(MouseEvent e)
   {
   }

   public void mouseExited(MouseEvent e)
   {
   }
// finishing implementing MouseListener  <---
   public static void main(String[] args)
   {
      new TTT4X4AI();
   }//main method
}//TTT4X4AI class