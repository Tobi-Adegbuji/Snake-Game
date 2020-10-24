import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class Board extends JPanel implements ActionListener {
    private boolean isDead;
    private final Snake snake;
    private int[] apple;
    private final int[] dimensions = new int[]{20, 20};
    private final Score score;
    private String[][] thisBoard = new String[dimensions[0]][dimensions[1]];

    //GUI VARIABLES
    private JFrame window;
    private static final int WIDTH =637;
    private static final int HEIGHT =660;
    private static final int BOX_SIZE = 31;
    Timer timer;

    public Board() {
        snake = new Snake(4, 4);
        isDead = false;
        score = new Score(0);
    }

    //Code for Window and panel:
    public void generateWindow(){
        window = new JFrame("Snake Game");
        window.setIconImage(Toolkit.getDefaultToolkit().getImage(".\\src\\cartoon.png"));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.setSize(WIDTH, HEIGHT);
        window.setResizable(false);
        this.setBackground(Color.BLACK);
        window.add(this);
        window.addKeyListener(new SnakeKeyListener());
        window.setLocationRelativeTo(null);
        timer = new Timer(250, this);
        timer.start();
    }


    //DRAWS GRAPHICS ON THE GUI
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        String[][] array = thisBoard;

//        //CREATE GRID ON BOARD
//        for (int x = 0; x < array.length * BOX_SIZE; x += BOX_SIZE) {
//            for (int y = 0; y < array[0].length * BOX_SIZE; y += BOX_SIZE) {
//                graphics.drawRect(x, y, BOX_SIZE, BOX_SIZE);
//            }
//        }

        //GENERATE APPLE
        graphics.setColor(Color.red);
        graphics.fillOval((apple[1] * BOX_SIZE),(apple[0] * BOX_SIZE), BOX_SIZE, BOX_SIZE);

        //GENERATE SNAKE PARTS
        for(int i = 0; i < snake.getBody().size(); i++){
            graphics.setColor(Color.PINK);
            graphics.fillOval(snake.getBody().get(i)[1] * BOX_SIZE,snake.getBody().get(i)[0] * BOX_SIZE, BOX_SIZE, BOX_SIZE);
        }

        //DRAW SCOREBOARD
        graphics.setFont(graphics.getFont().deriveFont(30.0f));
        graphics.setColor(Color.WHITE);
        graphics.drawString("Score: " + score.getScore(), 8 *BOX_SIZE, 20 * BOX_SIZE);

        //GAME OVER GRAPHICS
        if(isDead){
            graphics.setColor(Color.RED);
            graphics.drawString("GAME OVER",(thisBoard.length/2) * BOX_SIZE,(thisBoard[0].length/2) * BOX_SIZE);
        }

    }


    //Getter used to close window in Game.java
    public JFrame getWindow() {
        return window;
    }

    public void printArray() {
        System.out.println("Score: " + score.getScore());
        thisBoard = new String[dimensions[0]][dimensions[1]];
        for (String[] strings : thisBoard) {
            Arrays.fill(strings, "*");
        }
        if (snake.getBody() != null) {
            for (int[] snakePart : snake.getBody()) {
                thisBoard[snakePart[0]][snakePart[1]] = "S";
            }
        }
        if (apple != null) {
            thisBoard[apple[0]][apple[1]] = "@";
        }
        IntStream.range(0, 41).forEach(i -> System.out.printf("%s-%s", i != 0 ? "" : "+", i != 40 ? "" : "+\n"));
        for (String[] str : thisBoard) {
            System.out.print("| ");
            for (String s : str) {
                System.out.print(s + " ");
            }
            System.out.println("|");
        }
        IntStream.range(0, 41).forEach(i -> System.out.printf("%s-%s", i != 0 ? "" : "+", i != 40 ? "" : "+\n"));
    }

    public void gameOver() {
        isDead = true;
        System.out.println("Game Over!");
        timer.stop();
    }

    public boolean setApple(int row, int column) {
        ArrayList<int[]> snakeBody = snake.getBody();
        for (int[] section : snakeBody) {
            if (section[0] == row && section[1] == column) {
                System.out.println("Cannot add apple to top of snake");
                return false;
            }
        }
        try {
            apple = new int[]{row, column};
            return true;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Cannot add apple outside of the map");
            return false;
        }
    }

    public void appleEaten() {
        int[] snakeHead = snake.getBody().get(0);
        if (apple[0] == snakeHead[0] && apple[1] == snakeHead[1]) {
            snake.grow();
            Random rand = new Random();
            while (!setApple(rand.nextInt(20), rand.nextInt(20))) {}
            score.setScore(score.getScore() + 1);
            repaint();//Redraw apple on game board
        }
    }

    public void snakeCollision(){
        int[] snakeHead = snake.getBody().get(0);
        for(int i = 1; i < snake.getBody().size(); i++){
            if (snakeHead[0] == snake.getBody().get(i)[0] &&
                    snakeHead[1] == snake.getBody().get(i)[1]){
                gameOver();
            }
        }
    }

    void playGame() {
        setApple(6, 6);
        printArray();
        //Generating a new window
        generateWindow();
        do {
            System.out.print("");
            repaint();
        } while (!isDead);
        repaint(); //in order to show game over graphic
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        initMove(snake.getDirection());
    }

    private void initMove(char dir) {
        switch (dir) {
            case 'U':
                if (snake.getBody().get(0)[0] == 0) {
                    gameOver();
                } else {
                    snake.move(-1, 0);
                }
                break;
            case 'R':
                if (snake.getBody().get(0)[1] == 19) {
                    gameOver();
                } else {
                    snake.move(0, 1);
                }
                break;
            case 'D':
                if (snake.getBody().get(0)[0] == 19) {
                    gameOver();
                } else {
                    snake.move(1, 0);
                }
                break;
            case 'L':
                if (snake.getBody().get(0)[0] == 0) {
                    gameOver();
                } else {
                    snake.move(0, -1);
                }
                break;
            default:
                throw new IllegalStateException("Invalid direction");
        }
        if (snake.getBody().get(0)[0] < 0 || snake.getBody().get(0)[1] < 0 || snake.getBody().get(0)[0] > 19 || snake.getBody().get(0)[1] > 19) {
            gameOver();
        }
        if (!isDead) {
            appleEaten();
            printArray();
        }
        snakeCollision();
    }

    //Made as inner class so we can still access Board variables
    class SnakeKeyListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    initMove('U');
                    break;
                case KeyEvent.VK_LEFT:
                    initMove('L');
                    break;
                case KeyEvent.VK_DOWN:
                    initMove('D');
                    break;
                case KeyEvent.VK_RIGHT:
                    initMove('R');
                    break;
                default:
                    System.out.println("?");
            }
        }
    }
}

