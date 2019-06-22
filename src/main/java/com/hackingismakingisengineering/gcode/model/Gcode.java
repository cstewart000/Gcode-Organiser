package com.hackingismakingisengineering.gcode.model; /**
 *
 */
//
//import com.sun.org.glassfish.gmbal.ParameterNames;
import com.hackingismakingisengineering.gcode.helper.BoundingBox;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * @author Cam
 *
 *
 *
 */


/**
 * Gcode object represents a group of operations to create a part.
 *
 *  *
 *  * Object Model:
 *  *
 *  * Gcode
 *  * 		Header - Gcode with comments modes and pre-amble
 *  * 		Opertaion(s)
 *  * 			Tool
 *  * 			Bounding Box
 *  * 		Tool(s) [from operations]
 *  * 		Bounding Box [from operations]
 *  * 		Filename
 *  * 		Gcode in text format
 *  *
 *  * 		Footer	- Gcode with closing comments.
 */

public class Gcode {

    /**
     *
     */
    public static final Boolean DEBUG_GCODE = false;

    String fileFormat;

    String filename;
    String programComment;

    String header;
    String footer;

    BoundingBox boundingBox;

    ArrayList<Operation> operations = new ArrayList<Operation>();
    ArrayList<Tool> tools;

    String gcode_complete;


    /*
     *@param
     */
    void parseGcode() {

        Pattern p = Pattern.compile("\\n[\\n]+");
        //String[] sectionsStrings = 	p.split(gcode_complete);

        //TODO: spit at new line
        String[] sectionsStrings = gcode_complete.split("\\n\\n");

        int sections = sectionsStrings.length - 1;

        //TODO -figure out what happened to first parenthesis
        String header = "(" + sectionsStrings[0];

        this.header = header;
        this.footer = sectionsStrings[sections];

        parseHeader(header);

        for (int i = 1; i < sections; i++) {
            parseOperation(sectionsStrings[i]);
        }
        if (DEBUG_GCODE) {
            System.out.println(header);
        }
    }

    /**
     *
     * @return the heder portion of the gcode file. The header contains the initialisation of the machine and sets the modal variables of the program
     */
    public String getHeader() {
        return header;
    }


    public void setHeader(String header) {
        this.header = header;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public ArrayList<Tool> getTools() {
        return tools;
    }

    public void setTools(ArrayList<Tool> tools) {
        this.tools = tools;
    }

    private void parseHeader(String header) {
        // TODO Auto-generated method stub


        String[] comments = header.split("\\n");

        setProgramComment(comments[0]);

        for (int i = 2; i < comments.length; i++) {

            //TODO populate tool list
            if (comments[i].contains("T")) {
                Tool tool = new Tool(comments[i]);
                if (DEBUG_GCODE) {
                    System.out.println(i);
                }
            }
        }
    }

    public void buildBoundingBox() {

        try {
            if (operations.size() == 0) {

                Exception e = new Exception();

                throw (e);


            } else if (operations.size() > 0) {
                ArrayList<BoundingBox> operationBoxes = new ArrayList<BoundingBox>();

                for (Operation operation : operations) {
                    operationBoxes.add(operation.getBoundingBox());
                }
                this.boundingBox = new BoundingBox(operationBoxes);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * @return the programComment
     */
    public String getProgramComment() {
        return programComment;
    }

    /**
     * @param programComment the programComment to set
     */
    public void setProgramComment(String programComment) {
        this.programComment = programComment;
    }


    private void parseOperation(String string) {
        // TODO Auto-generated method stub
        Operation operation = new Operation(string);
        operations.add(operation);
    }


    /**
     * @return the filename of the Input gcode used. The filename will be used to create a file in the case of saving GCODE to file.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename set the filename of the GCODE
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * @return and ArrayList of the Operation class. Each operation contains the tool, gcode, bounding box and other objects.
     */
    public ArrayList<Operation> getOperations() {
        return operations;
    }

    /**
     * @param operations set the operations of the Gcode.
     */
    public void setOperations(ArrayList<Operation> operations) {
        this.operations = operations;
        update();
    }

    /**
     * @param operation set the operation of the Gcode.
     */
    public void setOperations(Operation operation) {

        this.operations = new ArrayList<Operation>();
        this.operations.add(operation);
    }

    /**
     * @return CNC machine interpretable Gcode (gcode_complete)
     */
    public String getGcode_complete() {
        update();

        return gcode_complete;
    }

    /**
     * A procedure that goes through the member variables of the GCODE and updates other member variables as required.
     * May call the update function of subordinate variables.
     */
    private void update() {

        String updatedGcode = this.header +"\n"+"\n";

        for (Operation operation : operations) {
            updatedGcode += operation.getGcodeOperation()+"\n"+"\n";
        }

        updatedGcode += this.footer +"\n"+"\n";

        setGcode_complete(updatedGcode);

        buildBoundingBox();


    }

    /**
     * @param gcode_complete the CNC machine interpretable GCODE to be set
     */
    public void setGcode_complete(String gcode_complete) {
        this.gcode_complete = gcode_complete;

    }

    /**
     * @param fileFormat the file fromat of the GCODE file .nc, .tap, .txt etc
     */
    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    /**
     * @return boundingBox the bounding box of the Gcode operation - this is the minimum and maximum X and Y vlaues of the Gcode.
     */
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    /**
     *
     * @param boundingBox set the boundingbox for the gcode file.
     */
    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    /**
     * @param filename constructor for a GCODE object filenmae of the inputfile
     * @param gcode_complete the machine readable gcode
     */
    public Gcode(String filename, String gcode_complete) {

        //System.out.println(filename);
        fileFormat = filename.substring(filename.lastIndexOf('.'));
        this.filename = filename;
        this.gcode_complete = gcode_complete;

        parseGcode();

        buildBoundingBox();
    }

    public Gcode(String gcode) {
        this.filename = "filename";
        this.gcode_complete = gcode;
        parseGcode();

        buildBoundingBox();
    }

    /**
     * Blank constructor
     */
    public Gcode() {
        // TODO Auto-generated constructor stub
    }


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "gcode [filename=" + filename + ", gcode_complete=" + gcode_complete + "]";
    }


    /**
     *
     * @return the fileformat of the Ggcode
     */
    public String getFileFormat() {
        // TODO Auto-generated method stub
        return fileFormat;
    }

    /**
     * Function to offset the gcode by the input values. The function passes the gcode to the Helper class which
     * contains functional code to interpret the gcode aand offset each co-ordinate
     * @param offsetX value to offset the code in the X-direction
     * @param offsetY value to offset the code in the Y-direction
     */
    public void offset(float offsetX, int offsetY) {
        for (Operation operation : this.getOperations())
            operation.offset(offsetX, offsetY);
    }

    /**
     *
     * @param operations operations to be added in addition to the existing operations
     */
    public void addOperations(ArrayList<Operation> operations) {
        this.operations.addAll(operations);
        update();

    }


}
