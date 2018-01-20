/*
File Name:     TTT4X4.java
Names:         Kelly Jia and Amy Zhou
Class:         ICS3U (D)
Date:          December 18, 2015
Description:   This program creates a 2-player 4X4 Tic Tac Toe game.
*/

   import javax.swing.*;
   import java.awt.*;
   import java.awt.event.*;

    public class TTT4X4 implements MouseListener
   {
   //declare all the global variables:
      Drawing draw = new Drawing();
      int x, y;
      int [][] board = new int [4][4];
      boolean xTurn = true;
      boolean win = false;
      JLabel message = new JLabel("Luigi's turn!");
      ImageIcon[] boardPictures = new ImageIcon[3];
      int countClick = 0;              //counts how many times the player's clicked after winning
                                    //(used to determine when the board should get reset)
      
       public TTT4X4()      // constructor
      {
         for (int i = 0; i < boardPictures.length; i++)
            boardPictures[i] = new ImageIcon(i + ".jpg"); 
         JFrame frame = new JFrame("TTT4X4");
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
         //since once a player goes, and the xTurn boolean changes value,
         //then if xTurn = false, X wins, and if xTurn = true, O wins
            if (xTurn)
               message.setText("Mario wins!!!");
            else
               message.setText("Luigi wins!!!");
         }
      //return the value of the win boolean 
         return(win);
      }//checkForWin method
   
   // places the current player's marker in the desired square if possible
   // checks for a win/tie, and if the game hasn't ended yet
   // tells the other player that it's their turn
       public void markSquare(int row, int col)
      {        
         if (!(checkForWin()))         //let a player go if no one has won yet
         {
            if (board[row][col] == 0)  //only let the player pick the spot if it is empty
            {
               if (xTurn)              //if it's X's turn:
               {
                  board[row][col] = 1; //change the value of the array to 1
                  xTurn = false;       //xTurn becomes false, so it becomes O's turn
               
               //if no one still hasn't won, change the game message back to O's turn
                  if (!(checkForWin()))            
                     message.setText("Mario's turn!"); 
               }
               else                    //if it's O's turn:
               {
                  board[row][col] = 2; //change the value of the array to 2
                  xTurn = true;        //xTurn becomes true again, so it becomes X's turn
               //if no one still hasn't won, change the game message back to X's turn
                  if (!(checkForWin()))
                     message.setText("Luigi's turn!"); 
               }
            }      
         } //markSquare method      
      
      //if the board is full, but no one has won, then a tie message is printed
         if (fullBoard())
         {
            if (!(checkForWin()))
               message.setText("TIE!!!");
         }
      
      }
   
       class Drawing extends JComponent
      {
          public void paint(Graphics g)
         {
            Graphics2D g2 = (Graphics2D) g;
         // draw the content of the board (refreshes every turn)
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
         if (!win)
         {
         // find coords of mouse click
            int row = e.getY()/100;
            int col = e.getX()/100;
         // handle the move that the player has made on the game board
            markSquare(row, col);
         // get paint to be called to reflect your mouse click
            draw.repaint();
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
         new TTT4X4();
      }//main method
   }//TTT4X4 class
