import edu.princeton.cs.algs4.Picture;

import java.awt.*;

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
        if(x==0||x==this.picture.width() - 1||y==0||y==this.picture.height()-1){
            return 1000;
        }
        else{
            //x-gradient
            int R_x= (this.picture.getRGB(x-1,y)>> 16)&0xFF-
                    (this.picture.getRGB(x+1,y)>> 16)&0xFF;
            int G_x= (this.picture.getRGB(x-1,y)>> 8)&0xFF-
                    (this.picture.getRGB(x+1,y)>> 8)&0xFF;
            int B_x= (this.picture.getRGB(x - 1, y))&0xFF-
                    (this.picture.getRGB(x + 1, y))&0xFF;
            int RGB_x=R_x*R_x+G_x*G_x+B_x*B_x;

            //Y-gradient
            int R_y= (this.picture.getRGB(x,y-1)>> 16)&0xFF-
                    (this.picture.getRGB(x,y+1)>> 16)&0xFF;
            int G_y= (this.picture.getRGB(x,y-1)>> 8)&0xFF-
                    (this.picture.getRGB(x,y+1)>> 8)&0xFF;
            int B_y= (this.picture.getRGB(x, y-1))&0xFF-
                    (this.picture.getRGB(x, y+1))&0xFF;
            int RGB_y=R_y*R_y+G_y*G_y+B_y*B_y;

            return java.lang.Math.sqrt(RGB_x+RGB_y);
        }
    }

    //  unit testing (optional)
    public static void main(String[] args) {

    }


}
