package QuantTTT;

/**
 * Created by ryanleung on 10/14/17.
 * For additional documentation, see <a href="http://algs4.cs.princeton.edu/15uf">Section 1.5</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class WeightedQuickUnion {
    private int[] parent; //parent[i] = parent of i
    private int[] size; //size[i] = size of component where i is found;
    private int count;

    public WeightedQuickUnion(int capacity) {
        parent = new int[capacity];
        size = new int[capacity];
        for (int i = 0; i < capacity; i++) {
            parent[i] = i;
            size[i] = 1;
        }
        count = 0;
    }
    public int findParent(int child) {
        check(child);
        if (parent[child] == child) {
            return child;
        }
        return findParent(parent[child]);
    }

    private boolean check(int value) {
        if (value > parent.length || value == 0) {
            throw new IllegalArgumentException("value out of range");
        }
        return true;
    }

    public boolean isCycle(int x, int y) {
        return findParent(x) == findParent(y);
    }

    public void join(int x, int y) {
        int xP = findParent(x);
        int yP = findParent(y);
        if (xP == yP) {
            return;
        }

        if (size[xP] > size[yP]) {
            parent[yP] = xP;
            size[xP] += size[yP];
        } else {
            parent[xP] = yP;
            size[yP] += size[xP];
        }
    }
}
