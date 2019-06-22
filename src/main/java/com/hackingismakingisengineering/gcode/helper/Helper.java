package com.hackingismakingisengineering.gcode.helper; /**
 * 
 */

//import javafx.scene.chart.XYChart;
import com.hackingismakingisengineering.gcode.model.Gcode;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Cam
 *
 */

/**
 * Helper class is used for interpretation of gcode files.
 * It contains a series of static methods for interpretting and modifying gcode.
 */
public class Helper {

	/**
	 * DEBUGGING FLAG - set to true to enable verbose output to command line
	 */
	static final boolean DEBUG_HELPER = false;
	static final int FLOAT_PRECISION = 4;

	
	public static String findWordfromCharinGcode(char searchChar, String gcode) {
		int position = gcode.indexOf(searchChar);
		String wordString = null;
		
		if(position>0 && Character.isDigit(gcode.charAt(position+1))) {
			
			wordString = gcode.substring(position,position+4);

			if(DEBUG_HELPER) {
				System.out.println(wordString);
			}
			wordString = wordString.split(" ")[0];

			if(DEBUG_HELPER) {
				System.out.println(wordString);
			}
					
			
			if(Character.isDigit(wordString.charAt(wordString.length()-1))){
				if(DEBUG_HELPER) {
					System.out.println(wordString);
				}
			
				return wordString;
			}else {
				return "T1";
			}
				
		}
		return "T1";
	}

	/**
	 *
	 * @param searchString
	 * @param gcode
	 * @return
	 */
	public static String findFirstWordPrefixInGcode(String searchString, String gcode) {

		String xWordpatternString = "\\b" + searchString +"\\S*";

		ArrayList<String> allMatches = new ArrayList<String>();
		ArrayList<Float> allMatchesNumber = new ArrayList<Float>();
		Matcher m = Pattern.compile(xWordpatternString)
				.matcher(gcode);
		while (m.find()) {
			allMatches.add(m.group());
		}
		//System.out.println(allMatchesNumber);

		return allMatches.get(0);
	}

	/**
	 *
	 * @param searchChar the search character for words to be extracted.
	 * @param gcode
	 * @return
	 */
	public static ArrayList<String> getAllWordsFromGcode(char searchChar, String gcode) {	

	String xWordpatternString = "\\b" + searchChar +"\\S*";
	
	ArrayList<String> allMatches = new ArrayList<String>();
	ArrayList<Float> allMatchesNumber = new ArrayList<Float>();
	 Matcher m = Pattern.compile(xWordpatternString)
	     .matcher(gcode);
	 while (m.find()) {
	   allMatches.add(m.group());
	   allMatchesNumber.add(Float.parseFloat(m.group().substring(1)));
	   
	 }
		if(DEBUG_HELPER) {
			System.out.println(allMatchesNumber);
		}
	
	
	return allMatches;
	}

	/**
	 * Method manipulates gcode to offset in a direction
	 *
	 * @param angleRotate the andgle that to gcode will be rotated
	 * @return returns a string object of CNC machine readible gcode that is offset from the input gcode.
	 */


	// TODO: need to be able to deal with lines where ONLY one coordinate is changed. ie:
	// X10 Y20 	= [10,20]
	// X30 		= [30,20] NOT [30,0]

	public static Gcode rotate(Gcode code, int angleRotate){

		String[] gcodeLines = code.getGcode_complete().split("\\n");

		String outGcode;
		outGcode = "";

		float prevXcoord = 0;
		float prevYcoord = 0;

		for(String gcodeLine: gcodeLines){
			float xcoord =0;
			float ycoord =0;

			if(gcodeLine.contains("X") || gcodeLine.contains("Y")) {
				if(gcodeLine.contains("X") ) {
					xcoord = Float.parseFloat(findFirstWordPrefixInGcode("X", gcodeLine).substring(1));
				}else{
					xcoord = prevXcoord;
				}
				if(gcodeLine.contains("Y") ) {
					ycoord = Float.parseFloat(findFirstWordPrefixInGcode("Y", gcodeLine).substring(1));
				}else{
					ycoord = prevYcoord;
				}

				double hypo = Math.sqrt(Math.pow(xcoord, 2) + Math.pow(ycoord, 2));
				double angleCurrent;
				if(hypo!=0) {
					angleCurrent = Math.atan2(ycoord, xcoord);
				}else{
					angleCurrent=0;
				}
				double angleUpdated = angleCurrent + Math.toRadians(angleRotate);

				float newXcoord = (float) (hypo * Math.cos(angleUpdated));
				float newYcoord = (float) (hypo * Math.sin(angleUpdated));

				float newIcoord = 0;
				float newJcoord = 0;

				//TODO - finish I and J component
				if(gcodeLine.contains("I") && gcodeLine.contains("J")) {

					float icoord = Float.parseFloat(findFirstWordPrefixInGcode("I", gcodeLine).substring(1));
					float jcoord = Float.parseFloat(findFirstWordPrefixInGcode("J", gcodeLine).substring(1));

					double hypoArc = Math.sqrt(Math.pow(icoord, 2) + Math.pow(jcoord, 2));
					double angleCurrentArc = Math.atan2(jcoord, icoord);
					double angleUpdatedArc = angleCurrentArc + Math.toRadians(angleRotate);

					newIcoord = (float) (hypoArc * Math.cos(angleUpdatedArc));
					newJcoord = (float) (hypoArc * Math.sin(angleUpdatedArc));
				}


				String[] lineWords = gcodeLine.split(" ");

				String outLine = "";

					//TODO CS: Add words where not previously in the line

					boolean lineWordsContainsX = false;
					boolean lineWordsContainsY = false;


					for(String word: lineWords){
					if(word.charAt(0)=='X'){
						outLine += "X"+ String.format("%.04f", newXcoord) +" ";
						lineWordsContainsX = true;
					}else if(!lineWordsContainsX){
						outLine += "X"+ String.format("%.04f", newXcoord) +" ";

					}else if(word.charAt(0)=='Y'){
						outLine += "Y"+String.format("%.04f", newYcoord)+" ";
						lineWordsContainsY = true;
					}else if(!lineWordsContainsY) {
						outLine += "Y" + String.format("%.04f", newYcoord) + " ";

					}else if(word.charAt(0)=='I'){
						outLine += "I"+String.format("%.04f", newIcoord)+" ";
					}else if(word.charAt(0)=='J'){
						outLine += "J"+String.format("%.04f", newJcoord)+" ";
					}else{
						outLine +=word+" ";
					}

				}

				outGcode +=outLine+"\n";

					prevXcoord = newXcoord;
					prevYcoord = newXcoord;


			}else{
				outGcode +=gcodeLine+"\n";
			}
		}
		Gcode outputGcode = new Gcode(outGcode);

		return outputGcode;
	}

	public static String offsetWordsGcode(char searchChar, String gcode, float offset) {



		String out;

		String xWordpatternString = "\\b" + searchChar +"\\S*";

		ArrayList<String> allMatches = new ArrayList<String>();
		ArrayList<Float> allMatchesNumber = new ArrayList<Float>();
		Matcher m = Pattern.compile(xWordpatternString)
				.matcher(gcode);

		StringBuilder stringBuilder = new StringBuilder();

		int previousIndexEnd = 0;

		while (m.find()) {

			m.group();
			float currentX = Float.parseFloat(m.group().substring(1))+offset;

			if(DEBUG_HELPER) {
				System.out.println(currentX);
			}

			int len = m.group().length();

			stringBuilder.append(gcode.substring(previousIndexEnd, m.start()));
			previousIndexEnd = m.start()+len-1;
			stringBuilder.append(Character.toString(searchChar) + (Float.parseFloat(m.group().substring(1))+offset));

			if(DEBUG_HELPER) {
				System.out.println();
			}
			//m.replaceFirst("d" + Float.parseFloat(m.group().substring(1))+offset);
			//out = m.replaceFirst("X" + Float.parseFloat(m.group().substring(1))+offset);
			if(DEBUG_HELPER) {
				System.out.print(out);
			}
			//allMatches.add(m.group());
			//allMatchesNumber.add(Float.parseFloat(m.group().substring(1)));

		}

		return stringBuilder.toString();
	}

	/**
	 * This method searches the machine readible gcode file for words starting with the specified search character
	 * The method can be used to extract all of the X or Y co-ordinates within a gcode string.
	 *
 	 * @param searchChar the search character to be found wiyhin the gcode string.
	 * @param gcode the CNC machine readible gcode nrhat is to be interpretted
	 * @return an arraylist of float values from the words
	 */
	public static ArrayList<Float> getAllCoordValuesFromGcode(char searchChar, String gcode) {	

		
	String xWordpatternString = "\\b" + searchChar +"\\S*";
	
	ArrayList<Float> allMatchesNumber = new ArrayList<Float>();
	 Matcher m = Pattern.compile(xWordpatternString)
	     .matcher(gcode);
	 while (m.find()) {
	 		String numString = m.group().substring(1);
	 		if(numString.contains("Y")) {
				int endInt = numString.indexOf("Y");
				numString = numString.substring(0,endInt);
	 		}


			allMatchesNumber.add(Float.parseFloat(numString));

	 }
	 //System.out.println(allMatchesNumber);
	
	
	return allMatchesNumber;
	}

	/**com.hackingismakingisengineering.gcode.helper
	 *
	 * Used with the getAllWordsFromGcode() method to determine the minimum co-ordinate value in the X or Y direction
	 * @param values an ArrayList of float values representing all the X or Y co-ordinates from the GCODE passed.
	 * @return the minimum value from the set of values passed
	 */
	public static float getMinValue(ArrayList<Float> values) {		
		Float minValue = null;	
		for(Float value: values) {
			if(minValue==null) {
				minValue = value;
			}else if(value<minValue){
				minValue = value;
			}	
		}
		return (float)minValue; 
	}

	/**
	 * Used with the getAllWordsFromGcode() method to determine the MAXIMUM co-ordinate value in the X or Y direction
	 * @param values an ArrayList of float values representing all the X or Y co-ordinates from the GCODE passed.
	 * @return the MAXIMUM value from the set of values passed
	 */
	public static float getMaxValue(ArrayList<Float> values) {		
		Float maxValue = null;	
		for(Float value: values) {
			if(maxValue==null) {
				maxValue = value;
			}else if(value>maxValue){
				maxValue = value;
			}	
		}
		return (float)maxValue; 
	}

	//TODO - make the XY series chart include curves
	//TODO - different series for retracted moves, feed moves etc.
	/**
	 *
	 * Takes machine gcode and creates a XY line graph.
	 * @param gcodeString the machine readible gcode file to  be plotted
	 * @return an XYChart.Series object that contains a line plot of the gcode file passed.
	 */

	/*
	public static XYChart.Series gcodeToGraph(String gcodeString) {
		//Prepare XYChart.Series objects by setting data
		XYChart.Series series = new XYChart.Series();


		Scanner scanner = new Scanner(gcodeString);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();

			float xCoord = Helper.getValueFromLine('X', line);
			float yCoord = Helper.getValueFromLine('Y', line);

			series.getData().add(new XYChart.Data(xCoord, yCoord));
			// process the line
		}
		scanner.close();

		return series;
	}

	/**
	 * Takes gcode and creates a XY line graph.
	 * @param gcode a Gcode object file
	 * @return an XYChart.Series object that contains a line plot of the gcode file passed.
	 */
	/*
	public static XYChart.Series gcodeToGraph(Gcode gcode) {

		return gcodeToGraph(gcode.getGcode_complete());
	}
    */
	/**
	 * Get the value from a search character
	 * @param x the character to be searched for in the line.
	 * @param line the line of machine readible gcode to be processed
	 * @return the float number value of the searched word.
	 *
	 * EG: ret = getValueFromLine('T', "T161 M6")
	 * ret = 161
	 *
	 */


	private static float getValueFromLine(char x, String line) {

		String xWordpatternString = "\\b" + x +"\\S*";

		Matcher m = Pattern.compile(xWordpatternString)
				.matcher(line);

		m.find();
		return Float.parseFloat(m.group().substring(1));

	}
	
	
}
