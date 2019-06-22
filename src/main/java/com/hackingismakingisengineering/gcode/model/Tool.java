package com.hackingismakingisengineering.gcode.model; /**
 * 
 */

import com.hackingismakingisengineering.gcode.helper.Helper;

/**
 * @author Cam
 *	Tool class represents a machine tool. Required to group similar operations that use the same tools and minimise required tool changes.
 */
public class Tool {

	public static final Boolean DEBUG_TOOL = false;

	int tool_code;
	float dia_mm;
	float len_mm;
	
	String tool_name;
	
	
	public float getDia_mm() {
		return dia_mm;
	}

	public void setDia_mm(float dia_mm) {
		this.dia_mm = dia_mm;
	}

	public float getLen_mm() {
		return len_mm;
	}

	public void setLen_mm(float len_mm) {
		this.len_mm = len_mm;
	}

	public String getTool_name() {
		return tool_name;
	}

	public void setTool_name(String tool_name) {
		this.tool_name = tool_name;
	}

	public int getTool_code() {
		return tool_code;
	}

	public void setTool_code(int tool_code) {
		this.tool_code = tool_code;
	}

	@Override
	public String toString() {
		return "com.hackingismakingisengineering.gcode.model.Tool [tool_code=" + tool_code + ", dia_mm=" + dia_mm + ", len_mm=" + len_mm + ", tool_name=" + tool_name
				+ "]";
	}

	
	
	
	/**
	 * Full constructor
	 * @param tool_code 8 bit number (0-255) that represents the tool in the CNC controller
	 * @param dia_mm diameter in mm of the tool
	 * @param len_mm length of the tool in mm.
	 * @param tool_name description of tool
	 */
	public Tool(int tool_code, float dia_mm, float len_mm, String tool_name) {
		super();
		this.tool_code = tool_code;
		this.dia_mm = dia_mm;
		this.len_mm = len_mm;
		this.tool_name = tool_name;
	}


	public Tool(String string) {
		// TODO Auto-generated constructor stub
		if(DEBUG_TOOL) {
			System.out.print(string);
		}
		

		if(string.length()<=4) {
			setTool_code(Integer.parseInt(string.substring(1)));
			if(DEBUG_TOOL) {
				System.out.println(this.tool_code);
			}
		}
		else {

			if(DEBUG_TOOL) {
				System.out.println(string);
			}

			String toolCode = Helper.findWordfromCharinGcode('T', string);
			if(DEBUG_TOOL) {
				System.out.println(toolCode);
			}
			setTool_code(Integer.parseInt(toolCode.substring(1)));

		}
	}

}
