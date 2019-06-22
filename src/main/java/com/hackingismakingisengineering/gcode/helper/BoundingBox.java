package com.hackingismakingisengineering.gcode.helper;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Object that contains the maximum and minimum X and Y values to define the extents of a Gcode file
 *
 */
public class BoundingBox {

    float minX;
    float maxX;

    float minY;
    float maxY;

    float minZ;
    float maxZ;

    double size;

    public BoundingBox(float minX, float maxX, float minY, float maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;

        calculateSize();
    }

    private void calculateSize() {
        this.size = Math.abs(minX-maxX)*Math.abs(maxY-minY);
    }

    public BoundingBox(float minX, float maxX, float minY, float maxY, float minZ, float maxZ) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
        this.minZ = minZ;
        this.maxZ = maxZ;

        calculateSize();
}

    public BoundingBox(ArrayList<BoundingBox> boxes) {

        new BoundingBox(boxes.get(0));
        // get the minimum and maximum
        for (BoundingBox boundingBox : boxes){

            //TODO expand for each
            this.maxX =(this.maxX > boundingBox.getMaxX()) ? this.maxX : boundingBox.getMaxX();
            this.maxY =(this.maxY > boundingBox.getMaxY()) ? this.maxY : boundingBox.getMaxY();
            this.maxZ =(this.maxZ > boundingBox.getMaxZ()) ? this.maxZ : boundingBox.getMaxZ();
            this.minX =(this.minX < boundingBox.getMinX()) ? this.minX : boundingBox.getMinX();
            this.minY =(this.minY < boundingBox.getMinY()) ? this.minY : boundingBox.getMinY();
            this.minZ =(this.minZ < boundingBox.getMinZ()) ? this.minZ : boundingBox.getMinZ();
        }
        calculateSize();
    }

    public BoundingBox(BoundingBox boundingBox) {
        this.minX = boundingBox.getMinX();
        this.maxX = boundingBox.getMaxX();
        this.minY = boundingBox.getMinY();
        this.maxY = boundingBox.getMaxY();
        this.minZ = boundingBox.getMinZ();
        this.maxZ = boundingBox.getMaxZ();

        calculateSize();
    }

    public BoundingBox(String gCode) {

        try{
        this.minX = Collections.min(Helper.getAllCoordValuesFromGcode('X',gCode));
        this.maxX = Collections.max(Helper.getAllCoordValuesFromGcode('X',gCode));
        this.minY = Collections.min(Helper.getAllCoordValuesFromGcode('Y',gCode));
        this.maxY = Collections.max(Helper.getAllCoordValuesFromGcode('Y',gCode));
        this.minZ = Collections.min(Helper.getAllCoordValuesFromGcode('Z',gCode));
        this.maxZ = Collections.max(Helper.getAllCoordValuesFromGcode('Z',gCode));

        calculateSize();
        }catch(Exception e){
            e.getMessage();
        }
    }


    @Override
    public String toString() {
        return "BoundingBox{" +
                "minX=" + minX +
                ", maxX=" + maxX +
                ", minY=" + minY +
                ", maxY=" + maxY +
                ", minZ=" + minZ +
                ", maxZ=" + maxZ +
                '}';
    }

    public float getMinX() {
        return minX;
    }

    public void setMinX(float minX) {
        this.minX = minX;
    }

    public float getMaxX() {
        return maxX;
    }

    public void setMaxX(float maxX) {
        this.maxX = maxX;
    }

    public float getMinY() {
        return minY;
    }

    public void setMinY(float minY) {
        this.minY = minY;
    }

    public float getMaxY() {
        return maxY;
    }

    public void setMaxY(float maxY) {
        this.maxY = maxY;
    }

    public float getMinZ() {
        return minZ;
    }

    public void setMinZ(float minZ) {
        this.minZ = minZ;
    }

    public float getMaxZ() {
        return maxZ;
    }

    public void setMaxZ(float maxZ) {
        this.maxZ = maxZ;
    }

    public float getXDim() {
        return this.maxX-this.minX;
    }

    public float getYDim() {
        return this.maxY-this.minY;
    }


    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }
}
