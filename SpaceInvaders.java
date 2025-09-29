    import java.awt.*;
    import java.awt.event.*;
    import java.util.ArrayList;
    import java.util.Random;
    import javax.swing.*;

    class character {
        int x;
        int y;
        int width;
        int height;
        Image photo;
        boolean alive = true; // Alien life health
        boolean used = false; // Used bullets from ship
        int healthPoints = 0; //Hits each alien can survive

        character(int x, int y, int width, int height, Image photo){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.photo = photo;
        }

        character(int x, int y, int width, int height, Image photo, int healthPoints){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.photo = photo;
            this.healthPoints = healthPoints;
        }

    }

    public class SpaceInvaders extends JPanel implements ActionListener, KeyListener{

            final int SIZE_OF_TITLE = 32;
            int rows = 16;
            int columns = 16;
            int boardWith = SIZE_OF_TITLE * columns;
            int boardHeight = SIZE_OF_TITLE * rows;

            Image shipImg;
            Image pinkAlien;
            Image blueAlien;
            Image redAlien;
            Image greenAlien;
            ArrayList<Image>alienArrayList;
            Image spaceBackground;
            

            //ship characteristics

            int shipWidth = SIZE_OF_TITLE * 2;
            int shipHeight = SIZE_OF_TITLE;
            int shipX = SIZE_OF_TITLE * columns / 2 - SIZE_OF_TITLE;
            int shipY = boardHeight - SIZE_OF_TITLE * 2; 
            int shipVelocityX = SIZE_OF_TITLE / 3; // This is what makes the ship move faster or slower so if you want to change it to experiment a higher or lower difficulty you could

            character ship;

            //alien characteristic
            ArrayList<character> alienArray;
            int alienWidth = SIZE_OF_TITLE * 2;
            int alienHeight = SIZE_OF_TITLE;
            int alienX = SIZE_OF_TITLE;
            int alienY = SIZE_OF_TITLE;

            int alienRows = 2;
            int alienColumns = 3;
            int alienCounter = 0; //number of aliens defeated
            int alienMovement = 1;

            //Bullets
            ArrayList<character> bulletArray;
            int bulletWidth = SIZE_OF_TITLE / 8;
            int bulletHeight = SIZE_OF_TITLE / 2;
            int bulletVelocityY = -10; //Bullet movement speed
            


            //Just keep press the key so the ships move in one go instead of pressing multiple times
            boolean isMovingLeft = false;
            boolean isMovingRight = false;

            Timer loopOfGame;
            int score = 0;
            boolean gameOver = false;
            


            SpaceInvaders(){
                setPreferredSize(new Dimension(boardWith,boardHeight));
                setBackground(Color.black);
                setFocusable(true);
                addKeyListener(this);

                spaceBackground = new ImageIcon(getClass().getResource("./BACKGROUND.jpg")).getImage();

                shipImg = new ImageIcon(getClass().getResource("./SPACESHIP.png")).getImage();
                pinkAlien = new ImageIcon(getClass().getResource("./PINKALIEN.png")).getImage();
                blueAlien = new ImageIcon(getClass().getResource("./BLUEALIEN.png")).getImage();
                redAlien = new ImageIcon(getClass().getResource("./REDALIEN.png")).getImage();
                greenAlien = new ImageIcon(getClass().getResource("./GREENALIEN.png")).getImage();


                alienArrayList = new ArrayList<Image>();
                alienArrayList.add(pinkAlien);
                alienArrayList.add(blueAlien);
                alienArrayList.add(redAlien);
                alienArrayList.add(greenAlien);

            ship = new character(shipX, shipY, shipHeight, shipWidth, shipImg);
            alienArray = new ArrayList<character>();
            bulletArray = new ArrayList<character>();

            loopOfGame = new Timer(1000/60, this);
            createAliens();
            loopOfGame.start();
            }

            public void paintComponent(Graphics g) {
                
                super.paintComponent(g);
                g.drawImage(spaceBackground, 0, 0, boardWith, boardHeight, null);
                draw(g);
            }

            public void draw(Graphics g){
                //ship
                g.drawImage(ship.photo, ship.x + 15, ship.y ,ship.width, ship.height, null);

                //alien
                for(int i  = 0; i < alienArray.size(); i++){
                    character alien = alienArray.get(i);
                    if(alien.alive){
                g.drawImage(alien.photo, alien.x, alien.y , alien.width, alien.height, null);

                    }
                }

                //bullets
                g.setColor(Color.white);
                for(int i = 0; i < bulletArray.size(); i++){
                    character bullet = bulletArray.get(i);
                    if(!bullet.used){
                        g.fillRect(bullet.x, bullet.y, bullet.width, bullet.height);
                    }

                }

                //score
                g.setColor(Color.red);
                g.setFont(new Font("Arrial", Font.PLAIN, 32));

                if(gameOver){
                    g.drawString("GAME OVER: " + String.valueOf(score), 10, 35);
                }
                else{
                    g.drawString(String.valueOf(score), 10, 35);
                }

                // Alien Counter
                g.setColor(Color.green); // Alien counter color
                g.setFont(new Font("Arial", Font.PLAIN, 32));
                g.drawString("Aliens Left: " + alienCounter, boardWith - 220, 35); 
            }

            

            public void move(){
                //clear bullets 
                while (bulletArray.size() > 0 && (bulletArray.get(0).used || bulletArray.get(0).y < 0) ) {
                    bulletArray.remove(0);
                }

                //next level 
                if(alienCounter == 0){ 
                    //increase number of aliens after each defeat
                    score += alienColumns * alienRows * 100; // bonus points
                    alienColumns = Math.min(alienColumns + 1, columns/2 -2); 
                    alienRows = Math.min(alienRows + 1, rows - 6);
                    alienArray.clear();
                    bulletArray.clear();
                    alienMovement = 1;
                    createAliens();
                }


                //aliens 
                for(int i = 0; i <alienArray.size(); i++){
                    character alien = alienArray.get(i);
                    if(alien.alive){
                        alien.x += alienMovement;

                        //Border collision
                        if(alien.x + alien.width >= boardWith || alien.x <= 0){
                            alienMovement *= -1;
                            alien.x += alienMovement * 2;

                            //Move the aliens down the row
                            for(int j = 0; j < alienArray.size(); j++){
                                alienArray.get(j).y += alienHeight;
                            }
                        }
                        if(alien.y >= ship.y){
                            gameOver = true;
                        }
                    }
                }

                // bullets
for (int i = bulletArray.size() - 1; i >= 0; i--) { // Iterate in reverse
    character bullet = bulletArray.get(i);
    bullet.y += bulletVelocityY;

    // Check if bullet is out of bounds
    if (bullet.y + bullet.height < 0) {
        bulletArray.remove(i);
        continue; // Skip to next bullet
    }

    // Bullet collision with aliens
    for (int j = alienArray.size() - 1; j >= 0; j--) { // Iterate in reverse for safe removal
        character alien = alienArray.get(j);
        if (!bullet.used && alien.alive && detectCollision(bullet, alien)) {
            bullet.used = true; // Mark bullet as used
            alien.healthPoints--; // Decrease alien health
            if (alien.healthPoints <= 0) {
                alien.alive = false; // Alien dies if health is 0 or less
                alienCounter--; // Decrease alien counter
                score += 100; // Add points for alien kill
            }
            bulletArray.remove(i); // Remove bullet after hit
            break; // Exit the inner loop to avoid further checks for this bullet
        }
    }
    }
}

            public void createAliens() {
                Random random = new Random();
                for (int r = 0; r < alienRows; r++) {
                    for (int c = 0; c < alienColumns; c++) {
                        int randomImg = random.nextInt(alienArrayList.size());
                        Image selectedAlien = alienArrayList.get(randomImg);
                        int health = 0;
            
                        // Assign health based on alien type
                        if (selectedAlien == greenAlien) {
                            health = 1;  // Green aliens have 1 health
                        } else if (selectedAlien == blueAlien) {
                            health = 2;  // Blue aliens have 2 health
                        } else if (selectedAlien == pinkAlien) {
                            health = 3;  // Pink aliens have 3 health
                        } else if (selectedAlien == redAlien) {
                            health = 4;  // Red aliens have 4 health
                        }
            
                        // Create the alien with its health
                        character alien = new character(
                            alienX + c * alienWidth,
                            alienY + r * alienHeight,
                            alienWidth,
                            alienHeight,
                            selectedAlien,
                            health  // Pass health value to constructor
                        );
                        alienArray.add(alien);
                    }
                }
                alienCounter = alienArray.size(); // Update alien count
            }

            public boolean detectCollision(character a, character b){
            return  a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isMovingLeft && ship.x > 0) {
                    ship.x -= shipVelocityX; // Move left
                }
                if (isMovingRight && ship.x + shipVelocityX + ship.width <=  boardWith) {
                    ship.x += shipVelocityX; // Move right
                }
                move();     
                repaint();
                if(gameOver){
                    loopOfGame.stop();
                }
            }
            
            @Override
            public void keyTyped(KeyEvent e) {
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    isMovingLeft = true;  // Start moving left
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    isMovingRight = true; // Start moving right
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(gameOver){
                    ship.x = shipX;
                    alienArray.clear();
                    bulletArray.clear();
                    score = 0;
                    alienMovement = 1;
                    alienColumns = 3;
                    alienRows = 2;
                    gameOver = false;
                    createAliens();
                    loopOfGame.start();
                }


                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    isMovingLeft = false;  // Stop moving left
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    isMovingRight = false; // Stop moving right
                }
                if(e.getKeyCode() == KeyEvent.VK_SPACE){
                    character bullet = new character(ship.x + shipWidth / 2 - bulletWidth / 2 , ship.y , bulletWidth, bulletHeight, null);
                    bulletArray.add(bullet);

                }
            
            }

     }
    