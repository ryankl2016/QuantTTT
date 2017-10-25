package QuantTTT;

/**
 * Created by ryanleung on 10/14/17.
 */
public class Square {
    private static int mv_num;
    private int[] grid;
    private int classification; //1 if X, 2 if O
    private int subscript;
    private int pos;

    public Square(int pos) {
        this.mv_num = 1;
        this.grid = new int[13];
        this.classification = 0;
        this.pos = pos;
    }

    public void place(int mark, int twin) {
        if (mark == 1) {
            grid[mv_num] = 10 + twin;
        } else if (mark == 2) {
            grid[mv_num] = 20 + twin;
        }
    }

    public void classify(int mark, int sub) {
        this.classification = mark;
        this.subscript = sub;
    }

    public int getClassification() {
        return classification;
    }

    public int getSub() {
        return subscript;
    }

    public int[] getGrid() {
        return grid;
    }

    public int getPos() {
        return pos;
    }

    public static void nextTurn() {
        mv_num += 1;
    }

//    public static void main(String[] args) {
//        // write your code here
//        Square test = new Square(1);
//        Square test1 = new Square(1);
//        Square test2 = new Square(1);
//        Square test3 = new Square(1);
//        test1.place(1, 4);
//        System.out.println(test2.mv_num);
//    }
}
