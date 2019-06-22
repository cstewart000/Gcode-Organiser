package com.hackingismakingisengineering.gcode.model; /**
 *
 */

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;


/**
 * @author Cam
 * Object to describe the offset
 */
// add dependency: http://commons.apache.org/proper/commons-collections/
public class WorkOffset {

    public static BidiMap<Integer, String> wcsHashMap = new DualHashBidiMap();



	/*
	 * https://www.cnczone.com/forums/g-code-programing/12276-using-g-code-setting-offsets.html
	 * G10 L2 P1 X Y Z ;
	 * P - Offset number
	 * X -X-axis offset amount (absolute)
	 * U -X-axis offset amount (incremental)
	 * Z -Z-axis offset amount (absolute)
	 * W -Z-axis offset amount (incremental)
	 */
    String offSetName;

    public String getOffsetString() {
        return offsetString;
    }

    public void setOffsetString(String offsetString) {
        this.offsetString = offsetString;
    }

    public void buildOffsetString() {

        this.offsetString = "G10 L2";
        this.offsetString += " P" + this.offsetIndex;
        this.offsetString += " X" + offsetXCo;
        this.offsetString += " Y" + offsetYCo;
        this.offsetString += " Z" + offsetZCo;


    }


    int offsetIndex;
    String offsetString;

    float offsetXCo;
    float offsetYCo;
    float offsetZCo;


    public String getOffSetName() {
        return offSetName;
    }

    public void setOffSetName(String offSetName) {
        this.offSetName = offSetName;
    }

    public int getOffsetIndex() {
        return offsetIndex;
    }

    public void setOffsetIndex(int offsetIndex) {
        this.offsetIndex = offsetIndex;
    }

    public float getOffsetXCo() {
        return offsetXCo;
    }

    public void setOffsetXCo(float offsetXCo) {
        this.offsetXCo = offsetXCo;
    }

    public float getOffsetYCo() {
        return offsetYCo;
    }

    public void setOffsetYCo(float offsetYCo) {
        this.offsetYCo = offsetYCo;
    }

    public float getOffsetZCo() {
        return offsetZCo;
    }

    public void setOffsetZCo(float offsetZCo) {
        this.offsetZCo = offsetZCo;
    }

    public WorkOffset(String offSetName, float offsetXCo, float offsetYCo, float offsetZCo) {
        super();
        populateHashMap();
        this.offSetName = offSetName;
        this.offsetXCo = offsetXCo;
        this.offsetYCo = offsetYCo;
        this.offsetZCo = offsetZCo;
        this.offsetIndex = wcsHashMap.getKey(offSetName);

        buildOffsetString();
    }
	
	/*
	// add dependency: http://commons.apache.org/proper/commons-collections/
		BidiMap<Integer, String> wcsHashMap = new BidiMap<Integer, String>();
		wcsHashMap.put(1,"G54");
		wcsHashMap.put(2,"G55");
		wcsHashMap.put(3,"G56");
		wcsHashMap.put(	4,"G57");
		wcsHashMap.put(	5,"G58");
		wcsHashMap.put(	6,"G59");
	*/


    public WorkOffset(String wcsString) {
        populateHashMap();
        //
        this.offSetName = wcsString;
        this.offsetIndex = wcsHashMap.getKey(wcsString);

    }

    @Override
    public String toString() {
        return "WorkOffset{" +
                "offSetName='" + offSetName + '\'' +
                ", offsetIndex=" + offsetIndex +
                ", offsetString='" + offsetString + '\'' +
                ", offsetXCo=" + offsetXCo +
                ", offsetYCo=" + offsetYCo +
                ", offsetZCo=" + offsetZCo +
                '}';
    }

    public WorkOffset(int wcsIndex) {
        populateHashMap();
        this.offsetIndex = wcsIndex;
        this.offSetName = wcsHashMap.get(wcsIndex);
    }

    public void populateHashMap() {
        wcsHashMap.put(1, "G54");
        wcsHashMap.put(2, "G55");
        wcsHashMap.put(3, "G56");
        wcsHashMap.put(4, "G57");
        wcsHashMap.put(5, "G58");
        wcsHashMap.put(6, "G59");
    }

}
