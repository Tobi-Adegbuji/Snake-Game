import java.util.ArrayList;

public class Snake {
    private final ArrayList<int[]> body;
    private char direction;
    private char tailDirection;

    public Snake(int y, int x) {
        body = new ArrayList<>();
        body.add(new int[]{y, x});
        body.add(new int[]{y - 1, x});
        direction = 'D';
        tailDirection = 'D';
    }

    public void move(int moveY, int moveX) {
        if (moveY == 0 && moveX == 0) {
            moveY = direction == 'U' ? -1 : direction == 'D' ? 1 : 0;   // keeps the snake moving if there's no input
            moveX = direction == 'L' ? -1 : direction == 'R' ? 1 : 0;
        } else {
            switch (String.format("%d,%d", moveY, moveX)) {
                case "-1,0":
                    direction = 'U';
                    break;
                case "1,0":
                    direction = 'D';
                    break;
                case "0,-1":
                    direction = 'L';
                    break;
                case "0,1":
                    direction = 'R';
                    break;
                default:
                    throw new IllegalStateException("Unknown direction configuration");
            }
        }
        int y = -1;
        int x = -1;
        for (int i = 0; i < body.size(); i++) {
            if (i == 0) {
                y = body.get(0)[0];
                body.get(0)[0] += moveY;
                x = body.get(0)[1];
                body.get(0)[1] += moveX;
                continue;
            }
            int yy = body.get(i)[0];
            body.get(i)[0] = y;
            if (i == body.size() - 1) {
                if (y - yy == 1) {
                    tailDirection = 'D';
                } else if (y - yy == -1) {
                    tailDirection = 'U';
                }
            }
            y = yy;
            int xx = body.get(i)[1];
            body.get(i)[1] = x;
            if (i == body.size() - 1) {
                if (x - xx == 1) {
                    tailDirection = 'R';
                } else if (x - xx == -1) {
                    tailDirection = 'L';
                }
            }
            x = xx;
        }
    }

    public void grow() {
        int[] tail = body.get(body.size() - 1);
        switch (tailDirection) {
            case 'D':
                body.add(new int[]{tail[0] - 1, tail[1]});
                break;
            case 'U':
                body.add(new int[]{tail[0] + 1, tail[1]});
                break;
            case 'R':
                body.add(new int[]{tail[0], tail[1] - 1});
                break;
            case 'L':
                body.add(new int[]{tail[0], tail[1] + 1});
                break;
        }
    }

    public ArrayList<int[]> getBody(){
        return body;
    }

    public char getDirection() {
        return direction;
    }
}
