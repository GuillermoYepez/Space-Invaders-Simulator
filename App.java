
import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        //Variables that will be used to open the window of the game

        final int SIZE_OF_TITLE = 32;
        int rows = 16;
        int columns = 16;

        // Next will decide how big/ small we want the board of the game to be
        int boardWith = SIZE_OF_TITLE * columns;
        int boardHeight = SIZE_OF_TITLE * rows;

        JFrame frame = new JFrame("SPACE INVADERS");
        
        //Keep an eye on this part of the code 
        SpaceInvaders gamePanel = new SpaceInvaders();
        frame.add(gamePanel);

        frame.setVisible(true);
        frame.setSize(boardWith, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SpaceInvaders spaceInvaders = new SpaceInvaders();
        frame.add(spaceInvaders);
        frame.pack();
        spaceInvaders.requestFocus();
        frame.setVisible(true);

    }
}
