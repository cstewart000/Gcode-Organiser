package com.hackingismakingisengineering.gcode.helper;

import com.hackingismakingisengineering.gcode.model.Gcode;
import com.hackingismakingisengineering.gcode.model.Operation;
import com.hackingismakingisengineering.gcode.model.OperationComparator;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

/**
 * Singleton that represents the User's session manipulating gcode files.
 */
public class Session {

    public static boolean DEBUG_SESSION = false;

    public static ArrayList<String> inputFiles = new ArrayList();
    public static ArrayList<Gcode> inputGcodes = new ArrayList<Gcode>();

    public static ArrayList<Gcode> outputGcodes = new ArrayList<Gcode>();

    public static Boolean splitFileMode = false;
    public static Boolean multipleJobs = false;
    public static Boolean arrayMode = false;
    public static Boolean rotateMode = false;
    public static Boolean combineMode = false;


    public static ArrayList<Gcode> split() {
        splitFileMode = true;
        return split(inputGcodes);
    }

    public static ArrayList<Gcode> split(Gcode gcode) {
        splitFileMode = true;
        return split(new ArrayList<Gcode>(Arrays.asList(gcode)));
    }


    public static ArrayList<Gcode> split(ArrayList<Gcode> gcodes) {
        arrayMode = true;
        for(Gcode gcode: gcodes){
            for (Operation operation : gcode.getOperations()) {
                Gcode outputGcode = new Gcode();

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss");
                Date date = new Date();
                if(DEBUG_SESSION) {
                    System.out.println(dateFormat.format(date));
                }

                //TODO add gcode format
                outputGcode.setFilename(gcode.getFilename() +
                        " - " + dateFormat.format(date) +
                        " - " + operation.getOperationName() +
                        gcode.getFileFormat());
                outputGcode.setHeader(gcode.getHeader());
                outputGcode.setFooter(gcode.getFooter());

                outputGcode.setOperations(operation);

                outputGcodes.add(outputGcode);
            }
        }
        return outputGcodes;
    }



    public static Gcode array(ArrayPattern arrayPattern) {

        arrayMode = true;

        return array(inputGcodes.get(0), arrayPattern);
    }

    /**
     * Applies an arraypattern to the gcode object to make multiple operations
     * @param code the gcode to be arrayed
     * @param arrayPattern the array pattern description to be applied
     * @return the modified gcode - gcode with operations patterned multiple times in X and Y directions as defined in
     * the array pattern object.
     */
    public static Gcode array(Gcode code, ArrayPattern arrayPattern) {

        arrayMode = true;

        float xLen = code.getBoundingBox().getXDim() + arrayPattern.getxKerf();
        float yLen = code.getBoundingBox().getYDim() + arrayPattern.getxKerf();

        ArrayList<Operation> arrayedOpperations = new ArrayList<Operation>();

        for (Operation operation : code.getOperations()) {
            for (int i = 0; i < arrayPattern.getxNum(); i++) {
                for (int j = 0; j < arrayPattern.getyNum(); j++) {
                    if (i != 0 || j != 0) {
                        arrayedOpperations.add(new Operation(operation).offset(xLen * i, yLen * j));
                    }else
                    arrayedOpperations.add(operation);
                }
            }

            code.setOperations(arrayedOpperations);


        }
        outputGcodes.add(code);

        return code;
    }

    public static boolean readin(String s) {

        //String input_filename = "2018-01-26 earmuffs.tap";
        //String output_filename = args[1];
        InputStream inputFileStream = null;
        //FileOutputStream outputFileStream = null;

        Gcode inputGcode =null;
        String filereadString = null;

        try {

            //URL resource = Session.class.getResource(input_filename)
            //ClassLoader classLoader = Session.class.getClassLoader();
            //inputFileStream = new FileInputStream(classLoader.getResource(input_filename).getFile());

            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            //InputStream is = classloader.getResourceAsStream(s);


            //inputFileStream = Session.class.getResourceAsStream(input_filename);
            //inputFileStream = new FileInputStream(input_filename);

            File file = new File("./" +s);
            FileInputStream fis = new FileInputStream(file);



            StringBuilder out = new StringBuilder();

            int c;
            while ((c = fis.read()) != -1) {

                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

                String line;
                while ((line = reader.readLine()) != null) {
                    out.append(line);
                    out.append('\n');
                }
            }

            inputGcodes.add(new Gcode(s, out.toString()));

            return false;
        } catch (
                //TODO - throw File not found
                IOException e) {

            e.printStackTrace();

        } finally {
            try {
                if (inputFileStream != null) {
                    inputFileStream.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Writes out the output gcode member variable to file system
     * @return returns a true boolean if the write out to file was successful
     */
    public static void writeOutToFile() {

        //Gcode outputGcode = new Gcode();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss");
        Date date = new Date();
        if(DEBUG_SESSION) {
            System.out.println(dateFormat.format(date));
        }

        //TODO add gcode format
        /*
        outputGcode.setFilename(inputGcode.getFilename() + " - " + dateFormat.format(date) + " - " + operation.getOperationName() + inputGcode.getFileFormat());
        outputGcode.setHeader(inputGcode.getHeader());
        outputGcode.setFooter(inputGcode.getFooter());
        outputGcode.setOperations(operation);
        */

        String outputName;

        if(splitFileMode){
            outputName = "Split "+ inputGcodes.get(0).getFilename();

        }else if(arrayMode){
            outputName = "Arrayed "+ inputGcodes.get(0).getFilename();
        }else if(rotateMode){
            outputName = "Rotated "+ inputGcodes.get(0).getFilename();

        }else if(combineMode) {
            outputName = "combined "+ inputGcodes.get(0).getFilename();
        }else{
            outputName = "output ";
        }


        for(Gcode outCode: outputGcodes) {
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(outputName));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (DEBUG_SESSION) {
                System.out.println(outCode.toString());
            }
            try {
                writer.write(outCode.getGcode_complete());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }

    /**
     *  Function that combines multiple gcode objects into a single gcode objects for machining.
     *  Use case is where multiple different gcode files for designs are available and are to be machined on the same
     *  piece of material
     *
     */

    public static int getNumberOfOutputGcodes(){
        return outputGcodes.size();
    }

    public static Gcode combineGcode(ArrayList<Gcode> gcodes){

        combineMode = true;

        Gcode code = new Gcode();

        //Use the header and footer of the first gcode
        code.setHeader(gcodes.get(0).getHeader());
        code.setFooter(gcodes.get(0).getFooter());

        code.setFilename("Combined");


        float offsetX = 0;

        for(Gcode gcode: gcodes) {

            gcode.offset(offsetX, 0);

            offsetX = offsetX + gcode.getBoundingBox().getXDim()+10;

            code.addOperations(gcode.getOperations());
        }

        Collections.sort(code.getOperations(), new OperationComparator());

        outputGcodes.add(code);

        return code;

    }

    public static Gcode combineGcode() {
        return combineGcode(inputGcodes);
    }


    public static Gcode rotate(String s) {
        int rotationAngle = Integer.parseInt(s);
        return rotate(rotationAngle);
    }

    private static Gcode rotate(int rotationAngle) {

        rotateMode = true;

        Gcode roatedCode = Helper.rotate(inputGcodes.get(0), rotationAngle);
        Session.outputGcodes.add(roatedCode);
        return roatedCode;
    }

    public static void verboseLogging() {
        DEBUG_SESSION = true;
    }
}




