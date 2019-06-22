package com.hackingismakingisengineering.gcode.model; /**
 * 
 */

import com.hackingismakingisengineering.gcode.helper.BoundingBox;
import com.hackingismakingisengineering.gcode.helper.Helper;
import org.apache.commons.lang3.StringUtils;


/**
 * @author Cam
 *
 */

/**
 * Operation class represents a machining operation (eg: face milling, profiling, pocketing etc.)
 * The operation class contains tool, bounding box and work offset objects.
 *
 */
public class Operation {
	public static final Boolean DEBUG_OPERATION = false;
	Tool tool;
	
	String operationName;
	
	String gcodeOperation;
	
	BoundingBox boundingBox;

	WorkOffset workOffset;

	/**
	 *
	 * @param operation clone constructor
	 */
	public Operation(Operation operation) {
		this.tool = operation.getTool();
		this.operationName = operation.getOperationName();
		this.boundingBox = operation.getBoundingBox();
		this.gcodeOperation = operation.getGcodeOperation();
		this.workOffset = operation.getWorkOffset();

	}


	/**
	 * @return the tool
	 */
	public Tool getTool() {
		return tool;
	}
	/**
	 * @param tool the tool to set
	 */
	public void setTool(Tool tool) {
		this.tool = tool;
	}
	/**
	 * @return the operationName
	 */
	public String getOperationName() {
		return operationName;
	}

	public com.hackingismakingisengineering.gcode.helper.BoundingBox getBoundingBox() {
		return boundingBox;
	}

	public void setBoundingBox(BoundingBox boundingBox) {
		this.boundingBox = boundingBox;
	}

	public WorkOffset getWorkOffset() {
		return workOffset;
	}

	public void setWorkOffset(WorkOffset workOffset) {
		this.workOffset = workOffset;
	}

	/**
	 * @param operationName the operationName to set
	 */
	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}
	/**
	 * @return the gcodeOperation
	 */
	public String getGcodeOperation() {
		return gcodeOperation;
	}
	/**
	 * @param gcodeOperation the gcodeOperation to set
	 */
	public void setGcodeOperation(String gcodeOperation) {
		this.gcodeOperation = gcodeOperation;
	}
	/**
	 * @return the minX
	 */

	public Operation(String gcodeOperation) {
		super();
		this.gcodeOperation = gcodeOperation;
		parseGcode();
	}


	private void parseGcode() {
		// TODO Auto-generated method stub
		
		
		//TODO make this a regex expression
		setOperationName(StringUtils.substringBetween(this.gcodeOperation, "(", ")"));

		//Get the offset for this operation
		//TODO
//		String wcsString = Helper.findFirstWordPrefixInGcode("G5", this.gcodeOperation);
		//this.workOffset = new WorkOffset(wcsString);

		if(DEBUG_OPERATION) {
			System.out.println(gcodeOperation);
		}

		String toolString = Helper.findWordfromCharinGcode('T', this.gcodeOperation);

		if(DEBUG_OPERATION) {
			System.out.println(toolString);
		}

		if(toolString != null) {
			
			setTool(new Tool(toolString));
		}
		//com.hackingismakingisengineering.gcode.helper.findWordfromCharinGcode2('T', this.gcodeOperation);

		this.boundingBox = new BoundingBox(this.gcodeOperation);

		if(DEBUG_OPERATION) {
			System.out.println(this.toString());
		}
		
	}

	/**
	 * Procedure to create bounding box object. The method calls out to the Helper class to find the minimum and maximum values of the X and Y values of the Gcode.
	 *
	 */
	public void buildBoundingBox() {

		boundingBox = new BoundingBox(Helper.getMinValue(Helper.getAllCoordValuesFromGcode('X', this.gcodeOperation)),
				Helper.getMaxValue(Helper.getAllCoordValuesFromGcode('X', this.gcodeOperation)),
				Helper.getMinValue(Helper.getAllCoordValuesFromGcode('Y', this.gcodeOperation)),
				Helper.getMaxValue(Helper.getAllCoordValuesFromGcode('Y', this.gcodeOperation)),
				Helper.getMinValue(Helper.getAllCoordValuesFromGcode('Z', this.gcodeOperation)),
				Helper.getMaxValue(Helper.getAllCoordValuesFromGcode('Z', this.gcodeOperation)));
	}





	@Override
	public String toString() {
		return "Operation{" +
				"tool=" + tool +
				", operationName='" + operationName + '\'' +
				", gcodeOperation='" + gcodeOperation + '\'' +
				", boundingBox=" + boundingBox +
				", workOffset=" + workOffset +
				'}';
	}

	/**
	 * Function to offset the operation by the input values. The function passes the operation to the Helper class which
	 * contains functional code to interpret the operation and offset each co-ordinate
	 * @param offsetX value to offset the code in the X-direction
	 * @param offsetY value to offset the code in the Y-direction
	 */
	public Operation offset(float offX, float offY) {

		String offsetGcode = this.getGcodeOperation();
		if(DEBUG_OPERATION) {
			System.out.println("1:  " + offsetGcode);
		}
		String offsetGcode1 = Helper.offsetWordsGcode('X', offsetGcode ,offX);
		String offsetGcode2 = Helper.offsetWordsGcode('Y', offsetGcode1 ,offY);
		if(DEBUG_OPERATION) {
			System.out.println("3:  " + offsetGcode2);
		}
		this.setGcodeOperation(offsetGcode2);
		return this;
	}

	/**
	 * Update method
	 */
	public void update(){

	}

}
