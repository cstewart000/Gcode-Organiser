package com.hackingismakingisengineering.gcode.helper;

import com.hackingismakingisengineering.gcode.model.Gcode;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SessionTest {

    @Before
    public void setUp() throws Exception {

        //Session.readin("square.nc");
        Session.readin("1001 screwdriver holder.tap");
        Session.readin("2019-02-09 Wood Saw.tap");
    }

    @Test
    public void split() {
        int outputGcodes = Session.getNumberOfOutputGcodes();

        Session.split();

        int outputGcodesSplit = Session.getNumberOfOutputGcodes();

        assertTrue(outputGcodes < outputGcodesSplit);
    }

    @Test
    public void array() {


        ArrayPattern arrayPattern = new ArrayPattern(2,2,10,100,100);
        Gcode inputGcode = Session.inputGcodes.get(0);
        Gcode arrayedGcode = Session.array(inputGcode, arrayPattern);

        double inputGcodeSize = inputGcode.getBoundingBox().getSize();
        double arrayedGcodeSize = arrayedGcode.getBoundingBox().getSize();

        assertTrue(inputGcodeSize<(4*arrayedGcodeSize));
    }

    @Test
    public void combineGcode() {

        int nops= 0;

        for(Gcode gc: Session.inputGcodes){
            int ops =  gc.getOperations().size();
            nops += ops;
        }

        Gcode combinedCode = Session.combineGcode(Session.inputGcodes);
        int combinedCodeOps = combinedCode.getOperations().size();

        assertTrue(nops ==combinedCodeOps);

        System.out.println(combinedCode.getGcode_complete());


    }

    @Test
    public void split1() {
        Gcode inputGcode = Session.inputGcodes.get(0);
        ArrayList<Gcode> splitCodes = Session.split(inputGcode);

        System.out.println(splitCodes.size());

        System.out.println(splitCodes.get(0).getGcode_complete());
    }



}