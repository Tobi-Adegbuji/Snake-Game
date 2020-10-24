import javax.swing.*;

public class Game {

    public static void main(String[] args) {
        do {
            Board board = new Board();
            board.playGame();
            boolean isValid = false;
            do {
                JFrame window = board.getWindow();
                switch (JOptionPane.showConfirmDialog(window, "Would you like to try again?", "Game Over",
                        JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE)) {
                    case JOptionPane.YES_OPTION:
                        isValid = true;
                        board.getWindow().dispose();
                        break;
                    case JOptionPane.CLOSED_OPTION:
                        // Closing the prompt will do the same as clicking No - closed case falls to no case
                    case JOptionPane.NO_OPTION:
                        System.exit(0);
                }
            } while (!isValid);
        } while (true);
    }

}
