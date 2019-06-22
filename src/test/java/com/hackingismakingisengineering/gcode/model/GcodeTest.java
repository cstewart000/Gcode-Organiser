package com.hackingismakingisengineering.gcode.model;

import com.hackingismakingisengineering.gcode.helper.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GcodeTest {

    Gcode testedGcode;
    Session session;

    @Before
    public void setUp() throws Exception {
        Session.readin("square.nc");
        testedGcode = Session.inputGcodes.get(0);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void parseGcode() {
    }

    @Test
    public void buildBoundingBox() {
    }

    @Test
    public void offset() {
    }

    @Test
    public void addOperations() {
    }
}