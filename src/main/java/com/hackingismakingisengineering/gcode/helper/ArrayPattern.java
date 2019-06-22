package com.hackingismakingisengineering.gcode.helper;

/**
 * ArrayPattern Class defines the structure for a rectangular array of gcode operations. The application o this class
 * is to create multiple copies of the same component from a base GCODE file.
 * The array pattern is defined bythe number of components to be created in the x direction, y direction and the spacing
 * between the bounding boxes of the components.
 */
public class ArrayPattern {

    int xNum;
    int yNum;
    float xKerf;

    int ySpacing;
    int xSpacing;

    public ArrayPattern(int xNum, int yNum, float xKerf, int ySpacing, int xSpacing) {
        this.xNum = xNum;
        this.yNum = yNum;
        this.xKerf = xKerf;
        this.ySpacing = ySpacing;
        this.xSpacing = xSpacing;
    }

    public ArrayPattern(int xNum, int yNum, float xKerf) {
        this.xNum = xNum;
        this.yNum = yNum;
        this.xKerf = xKerf;
    }


    @Override
    public String toString() {
        return "ArrayPattern{" +
                "xNum=" + xNum +
                ", yNum=" + yNum +
                ", xKerf=" + xKerf +
                ", ySpacing=" + ySpacing +
                ", xSpacing=" + xSpacing +
                '}';
    }

    public int getxNum() {
        return xNum;
    }

    public void setxNum(int xNum) {
        this.xNum = xNum;
    }

    public int getyNum() {
        return yNum;
    }

    public void setyNum(int yNum) {
        this.yNum = yNum;
    }

    public float getxKerf() {
        return xKerf;
    }

    public void setxKerf(float xKerf) {
        this.xKerf = xKerf;
    }

    public int getySpacing() {
        return ySpacing;
    }

    public void setySpacing(int ySpacing) {
        this.ySpacing = ySpacing;
    }

    public int getxSpacing() {
        return xSpacing;
    }

    public void setxSpacing(int xSpacing) {
        this.xSpacing = xSpacing;
    }
}
