package QuantTTT;

/**
 * Created by ryanleung on 10/14/17.
 */
public class Board {
    private WeightedQuickUnion components;
    private static Square[] field; //3x3 grid
    private int[][] moves; //odd index are X moves; first and second element are position of marks
    private int turn_player; //X=1, O=2
    private int turn_num;
    private static double Xscore;
    private static double Oscore;
    private boolean exists_cycle;

    public Board(){
        field = new Square[10];
        for (int i = 1; i < 10; i++) {
            field[i] = new Square(i);
        }
        moves = new int[10][2];
        turn_player = 1;
        components = new WeightedQuickUnion(10);
        turn_num = 1;
        exists_cycle = false;
        Xscore = 0;
        Oscore = 0;
    }

    public void makeMove(int mv1, int mv2) {

        if (components.isCycle(mv1, mv2)) {
            exists_cycle = true;
        }

        components.join(mv1, mv2);
        field[mv1].place(turn_player, mv2);
        field[mv2].place(turn_player, mv1);
        Square.nextTurn();

        if (turn_player == 1) {
            turn_player = 2;
        } else {
            turn_player = 1;
        }
        moves[turn_num] = new int[] {mv1, mv2};
        turn_num += 1;

        if (exists_cycle) {
            exists_cycle = false;

            //open GUI and prompt user which square they would like to take and untangle board
            if (mv1 == mv1) {
                release(field[mv2], mv1);
            } else {
                release(field[mv1], mv2);
            }
            endGame();
        }
    }

    private void release(Square curr, int link) {
        if (curr.getClassification() != 0) {
            return;
        }
        for (int i = 0; i < 13; i++) {
            if ((curr.getGrid()[i] % 10) == link) {
                if (curr.getGrid()[i] > 19) {
                    curr.classify(2, i);
                } else {
                    curr.classify(1, i);
                }
            } else if (curr.getGrid()[i] != 0){
                release(field[curr.getGrid()[i] % 10], curr.getPos());
            }
        }
    }

    private void endGame() {
        int three_Os = Owins();
        int three_Xs = Xwins();
        if (three_Os < 20 && three_Xs < 20) {
            System.out.print(three_Os);
            System.out.print(three_Xs);
            if (three_Os < three_Xs) {
                Xscore += .5;
                Oscore += 1;
            } else {
                Oscore += .5;
                Xscore += 1;
            }
            return;
        } else if (three_Os < 20) {
            Oscore += 1;
        } else if (three_Xs < 20) {
            Xscore += 1;
        }
    }

    private boolean checkWin(int mark, int one, int two, int three) {

        if (field[one].getClassification() == mark
                && field[one].getClassification() == field[two].getClassification()
                && field[two].getClassification() == field[three].getClassification()) {
            return true;
        }
        return false;
    }

    private int maxSubcript(int one, int two, int three) {
        return Math.max(Math.max(field[one].getSub(), field[two].getSub()), field[three].getSub());
    }

    //returns if O has 3 in a row
    private int Owins() {

        int minSub = 20;
        if (checkWin(2, 1,2,3)) {
            minSub = Math.min(minSub, maxSubcript(1,2,3));
        }
        if (checkWin(2, 4,5,6)) {
            minSub = Math.min(minSub, maxSubcript(4,5,6));
        }
        if (checkWin(2, 7,8,9)) {
            minSub = Math.min(minSub, maxSubcript(7,8,9));
        }
        if (checkWin(2, 1,4,7)) {
            minSub = Math.min(minSub, maxSubcript(1,4,7));
        }
        if (checkWin(2, 2,5,8)) {
            minSub = Math.min(minSub, maxSubcript(2,5,8));
        }
        if (checkWin(2, 3,6,9)) {
            minSub = Math.min(minSub, maxSubcript(3,6,9));
        }
        if (checkWin(2, 1,5,9)) {
            minSub = Math.min(minSub, maxSubcript(1,5,9));
        }
        if (checkWin(2, 3,5,7)) {
            minSub = Math.min(minSub, maxSubcript(3,5,7));
        }

        return minSub;
    }

    //returns if X has 3 in a row
    private int Xwins() {
        int minSub = 20;
        if (checkWin(1, 1,2,3)) {
            minSub = Math.min(minSub, maxSubcript(1,2,3));
        }
        if (checkWin(1, 4,5,6)) {
            minSub = Math.min(minSub, maxSubcript(4,5,6));
        }
        if (checkWin(1, 7,8,9)) {
            minSub = Math.min(minSub, maxSubcript(7,8,9));
        }
        if (checkWin(1, 1,4,7)) {
            minSub = Math.min(minSub, maxSubcript(1,4,7));
        }
        if (checkWin(1, 2,5,8)) {
            minSub = Math.min(minSub, maxSubcript(2,5,8));
        }
        if (checkWin(1, 3,6,9)) {
            minSub = Math.min(minSub, maxSubcript(3,6,9));
        }
        if (checkWin(1, 1,5,9)) {
            minSub = Math.min(minSub, maxSubcript(1,5,9));
        }
        if (checkWin(1, 3,5,7)) {
            minSub = Math.min(minSub, maxSubcript(3,5,7));
        }

        return minSub;
    }

    public static void printScore() {
        System.out.printf("player X score: %f\n", Xscore);
        System.out.printf("player O score: %f\n", Oscore);
    }

    public static void main(String[] args) {
        // write your code here
        Board newgame = new Board();
        newgame.makeMove(1,4);
        newgame.makeMove(9,6);
        newgame.makeMove(1,7);
        newgame.makeMove(9,2);
        newgame.makeMove(8,1);
        newgame.makeMove(3,6);
        newgame.makeMove(1,2);
        newgame.makeMove(5,6);
        newgame.makeMove(1,5);
        printScore();
        printBoard();

    }

    public static void printBoard() {
        for (int i = 1; i < 10; i++) {
            System.out.printf("print square %d\n", i);
            for (int j = 1; j < 13; j++) {
                if (field[i].getClassification() > 0) {
                    System.out.print(field[i].getClassification());
                    System.out.print(field[i].getSub());
                    break;
                }
                if (field[i].getGrid()[j] > 0) {
                    if (j % 2 == 0) {
                        System.out.printf("O %d\n", field[i].getGrid()[j]);
                    } else {
                        System.out.printf("X %d\n", field[i].getGrid()[j]);
                    }
                }

            }
        }
    }


}
