package QuantTTT;
import javax.swing.*;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;

/**
 * Created by ryanleung on 10/14/17.
 */
public class Board extends JFrame implements ActionListener{
    JPanel game = new JPanel();
    XOButton buttons[] = new XOButton[9];
    private WeightedQuickUnion components;
    private static Square[] field; //3x3 grid
    private int[][] moves; //odd index are X moves; first and second element are position of marks
    private int turn_player; //X=1, O=2
    private int turn_num;
    private static double Xscore;
    private static double Oscore;
    private boolean exists_cycle;
    private boolean same_turn;
    private int last_turn;

    public Board() {
        super("Board");
        setSize(400, 400);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        game.setLayout(new GridLayout(3,3));
        this.add(game);
        field = new Square[10];
        for (int i = 0; i < 10; i++) {
            field[i] = new Square(i);
            if (i < 9) {
                buttons[i] = new XOButton();
                buttons[i].setEnabled(true);
                buttons[i].addActionListener(this);
                game.add(buttons[i]);
            }
        }
        //this.pack();
        this.setVisible(true);
        moves = new int[10][2];
        turn_player = 1;
        components = new WeightedQuickUnion(10);
        turn_num = 1;
        exists_cycle = false;
        Xscore = 0;
        Oscore = 0;
        same_turn = false;
    }

    public void actionPerformed(ActionEvent e) {

        String mark;
        int turnID;
        if (turn_player == 1) {
            mark = "X";
            turnID = 2;
        } else {
            mark = "O";
            turnID = 1;
        }

        if (exists_cycle) {

            for (int i = 0; i < 9; i++) {
                if (buttons[i] == e.getSource()) {
                    //open GUI and prompt user which square they would like to take and untangle board
                    if (i + 1 == moves[turn_num - 1][0]) {
                        exists_cycle = false;
                        release(field[moves[turn_num - 1][0]], turnID * 10 + moves[turn_num - 1][1]);
                    } else if (i + 1 == moves[turn_num - 1][1]) {
                        exists_cycle = false;
                        release(field[moves[turn_num - 1][1]], turnID * 10 + moves[turn_num - 1][0]);
                    }
                    endGame();
                }
            }
        } else {

            for (int i = 0; i < 9; i++) {
                if (buttons[i] == e.getSource()) {

                    //buttons[i].setText("asdf");
                    if (field[i + 1].getClassification() == 0 && ((i + 1 != last_turn && same_turn == true) || same_turn == false)) {
                        if (buttons[i].marks == "") {
                            buttons[i].marks = mark + Integer.toString(turn_num);
                        } else {
                            buttons[i].marks = buttons[i].marks + ", " + mark + Integer.toString(turn_num);
                        }
                        buttons[i].setText(buttons[i].marks);
                        if (same_turn == true) {
                            makeMove(last_turn, i + 1);
                            same_turn = false;
                        } else {
                            same_turn = true;
                            last_turn = i + 1;
                        }
                    }
                }
            }
        }

    }
    public void makeMove(int mv1, int mv2) {
        if (components.isCycle(mv1, mv2)) {
            exists_cycle = true;
            JOptionPane.showMessageDialog(null, "Cycle detected");
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



//        if (exists_cycle) {
//            exists_cycle = false;
//
//            //open GUI and prompt user which square they would like to take and untangle board
//
//            if (mv1 == mv1) {
//                release(field[mv2], mv1);
//            } else {
//                release(field[mv1], mv2);
//            }
//            endGame();
//        }
    }

    // finds matching mark in Square curr with link, then classifies it.
    private void release(Square curr, int link) {
        if (curr.getClassification() != 0) {
            return;
        }
        int button = curr.getPos() - 1;


        for (int i = 0; i < 13; i++) {
            if (curr.getGrid()[i] == link) {

                if (curr.getGrid()[i] > 19) {
                    curr.classify(2, i);
                    buttons[button].setText("O" + Integer.toString(i));
                    buttons[button].setForeground(Color.RED);
                } else {
                    curr.classify(1, i);
                    buttons[button].setText("X" + Integer.toString(i));
                    buttons[button].setForeground(Color.GREEN);
                }
            } else if (curr.getGrid()[i] != 0){

                release(field[curr.getGrid()[i] % 10],curr.getGrid()[i] / 10 * 10 + curr.getPos());
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
                JOptionPane.showMessageDialog(null, "O wins");
            } else {
                Oscore += .5;
                Xscore += 1;
                JOptionPane.showMessageDialog(null, "X wins");
            }
            return;
        } else if (three_Os < 20) {
            Oscore += 1;
            JOptionPane.showMessageDialog(null, "O wins");
        } else if (three_Xs < 20) {
            Xscore += 1;
            JOptionPane.showMessageDialog(null, "X wins");
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
//        newgame.makeMove(1,4);
//        newgame.makeMove(9,6);
//        newgame.makeMove(1,7);
//        newgame.makeMove(9,2);
//        newgame.makeMove(8,1);
//        newgame.makeMove(3,6);
//        newgame.makeMove(1,2);
//        newgame.makeMove(5,6);
//        newgame.makeMove(1,5);
//        printScore();
//        printBoard();
//        newgame.makeMove(1,4);
//        newgame.makeMove(1,4);
//        newgame.release(field[1], 4);
//        newgame.printBoard();
//        System.out.println(newgame.field[1].getClassification());
//        System.out.println(newgame.field[4].getClassification());


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
