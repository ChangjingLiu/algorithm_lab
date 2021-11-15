package src;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    private final Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = picture;
    }

    // current picture
    public Picture picture() {
        return this.picture;
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (!(x >= 0 && x <= this.picture.width() - 1)) {
            throw new IllegalArgumentException();
        }
        if (!(y >= 0 && y <= this.picture.height() - 1)) {
            throw new IllegalArgumentException();
        }
    }

    //  unit testing (optional)
    public static void main(String[] args) {

    }


}
