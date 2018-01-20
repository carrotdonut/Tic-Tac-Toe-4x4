/*
File Name:     OpenScreen.java
Names:         Kelly Jia and Amy Zhou
Class:         ICS3U (D)
Date:          December 18, 2015
Description:   This program creates an opening screen of a Tic Tac Toe game. It then lets
               the user choose between playing a 1-player game or 2-player game. After the user
               makes their choice, the program invokes the appropriate class.
               (TTT4X4AI for 1-player and TTT4X4 for 2-player)
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class OpenScreen implements MouseListener
{
   //declare all the global variables:
   Drawing draw = new Drawing();
   JLabel message = new JLabel("Welcome to Tic Tac Toe!! Click to play!");
   ImageIcon beginPic;
   ImageIcon oneP;
   ImageIcon twoP;
   boolean pastBeginning = false;   //the variable to see if the user clicked past
                                    //the title screen
   
   public OpenScreen()  //constructor
   {
      JFrame frame = new JFrame("OpenScreen");
      beginPic = new ImageIcon("background.jpg");
      oneP = new ImageIcon("player.jpg");
      twoP = new ImageIcon("2player.jpg");
      frame.add(draw);
      draw.addMouseListener(this);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setSize(420, 480);
      message.setFont(new Font("Serif",Font.BOLD,20));
      message.setForeground(Color.blue);
      message.setHorizontalAlignment(SwingConstants.CENTER);
      frame.add(message, "South");
      frame.setVisible(true);
   }
   
   class Drawing extends JComponent
   {
      public void paint(Graphics g)
      {
         Graphics2D g2 = (Graphics2D) g;
         
         //if the user still hasn't clicked past the title screen, output the title screen
         if (!pastBeginning)
            g.drawImage(beginPic.getImage(),0,0,this);
         //if the user did click past the title screen, split the screen into 2 halves down the middle
         //left half: 1-player and right half: 2-player
         //the player can then click on a half to choose their game
         else
         {
            g.drawImage(oneP.getImage(),0,0,this);
            g.drawImage(twoP.getImage(),200,0,this);
         
         }
      }
   }

// --> starting implementing MouseListener - it has 5 methods 
   public void mousePressed(MouseEvent e)
   {
   }
   
   public void mouseReleased(MouseEvent e)
   {
      
      // find coords of mouse click
      int row = e.getX();
      int col = e.getY();
         //Once the user clicks past the title screen, the screen is split down the middle 
      if (!pastBeginning)
      {
         pastBeginning = true;  
         draw.repaint();
         message.setText("Choose: 1P (against AI) or 2P");
      }
      else
      {      
            //if the user clicks on the left half, TTT4X4AI is called
            //if the user clicks on the right half, TTT4X4 is called
         draw.repaint();
         if (row >=0 && row <=200 && col >=0 && col <= 600)
            new TTT4X4AI();
         else
            new TTT4X4();
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
      new OpenScreen();
   }//main method
}//OpenScreen class