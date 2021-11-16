import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;

public class SeamCarver {

    private Picture picture;
    private double[][] energy;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = picture;
        computeenergy();
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

    private void computeenergy() {
        double[][] energy = new double[width()][height()];
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                energy[i][j] = energy(i, j);
            }
        }
        this.energy = energy;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (!(x >= 0 && x <= width() - 1)) {
            throw new IllegalArgumentException();
        }
        if (!(y >= 0 && y <= height() - 1)) {
            throw new IllegalArgumentException();
        }
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) {
            return 1000;
        } else {
            //x-gradient
            int left = this.picture.getRGB(x - 1, y);
            int right = this.picture.getRGB(x + 1, y);
            int R_x = ((left >> 16) & 0xFF) - ((right >> 16) & 0xFF);
            int G_x = ((left >> 8) & 0xFF) - ((right >> 8) & 0xFF);
            int B_x = ((left) & 0xFF) - ((right) & 0xFF);
            int RGB_x = R_x * R_x + G_x * G_x + B_x * B_x;
            //int a = (left >> 0) & 0xFF;
            //int b = (right >> 0) & 0xFF;

            //Y-gradient
            int top = this.picture.getRGB(x, y + 1);
            int bottom = this.picture.getRGB(x, y - 1);
            int R_y = ((top >> 16) & 0xFF) - ((bottom >> 16) & 0xFF);
            int G_y = ((top >> 8) & 0xFF) - ((bottom >> 8) & 0xFF);
            int B_y = ((top) & 0xFF) - ((bottom) & 0xFF);
            int RGB_y = R_y * R_y + G_y * G_y + B_y * B_y;

            return java.lang.Math.sqrt(RGB_x + RGB_y);
        }
    }

    // sequence of indices for horizontal seam
    public int[] findVerticalSeam() {
        double disto[][] = new double[width()][height()];
        int pathto[][] = new int[width()][height()];

        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                if (row == 0) disto[col][row] = energy[col][row];
                else disto[col][row] = Double.MAX_VALUE;
            }

        }
        //因为是拓扑排序，先遍历0，0 0，1
        for (int row = 0; row < height() - 1; row++) {
            for (int col = 0; col < width(); col++) {
                if (col == 3 && row == 2) {
                    int a = 0;
                }
                relax(col - 1, row + 1, col, row, disto, pathto);
                relax(col, row + 1, col, row, disto, pathto);
                relax(col + 1, row + 1, col, row, disto, pathto);
            }

        }
        //find min mindisto
        int min_disto = -1;
        double min_energy = Double.MAX_VALUE;

        //遍历最后一行
        for (int col = 0; col < width(); col++) {
            if (disto[col][height() - 1] < min_energy) {
                min_energy = disto[col][height() - 1];
                min_disto = col;
            }
        }
        //find the path
        Stack<Integer> stack = new Stack<>();
        for (int i = height() - 1; i >= 0; i--) {
            stack.push(min_disto);
            min_disto = pathto[min_disto][i];
        }
        int[] rs = new int[height()];
        int i = 0;
        for (int n : stack) {
            rs[i++] = n;
        }
        return rs;
    }

    // sequence of indices for Horizontal seam
    public int[] findHorizontalSeam() {
        trans(this.picture);
        int[] res;
        res = findVerticalSeam();
        trans(this.picture);
        return res;
    }

    public void trans(Picture picture) {
        int newheight = width();
        int newwidth = height();
        Picture newpicture = new Picture(newwidth, newheight);
        for (int i = 0; i < newheight; i++) {
            for (int j = 0; j < newwidth; j++) {
                newpicture.setRGB(j, i, picture.getRGB(i, j));
            }
        }
        this.picture = newpicture;
        computeenergy();
        newpicture = null;
    }

    private void relax(int col, int row, int lastcol, int lastrow, double disto[][], int pathto[][]) {
        if ((col >= 0 && col <= width() - 1 && row >= 0 && row <= height() - 1)) {
            //out_of_bound
            double w = energy[col][row];
            //double a = disto[col][row];
            if (disto[col][row] > disto[lastcol][lastrow] + w) {

                //double b = disto[lastcol][row - 1] + w;
                disto[col][row] = disto[lastcol][lastrow] + w;
                pathto[col][row] = lastcol;
            }
        }
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam.length != height()) {
            throw new IllegalArgumentException();
        }
        if (width() <= 1) {
            throw new IllegalArgumentException();
        }
        if (seam == null) {
            throw new IllegalArgumentException();
        }
        Picture newpicture = new Picture(width() - 1, height());
        int del_row = 0;
        for (int j = 0; j < height(); j++) {
            int ii = 0;
            for (int i = 0; i < width() - 1; i++) {
                if (seam[j] == i) {
                    ii++;
                }
                newpicture.setRGB(i, j, this.picture.getRGB(ii, j));
                ii++;
            }

        }
        this.picture = newpicture;
        computeenergy();
        newpicture = null;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        trans(this.picture);
        removeVerticalSeam(seam);
        trans(this.picture);
    }

    //  unit testing (optional)
    public static void main(String[] args) {

    }


}
