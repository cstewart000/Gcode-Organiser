


# GCode-Organiser

A command line program to help manipulate GCODE files quickly.

Author: cstewart000@gmail.com
Created: 2019

Repository: github.com/cstewart000

Visualisation of GCODE is used through the webapp: https://ncviewer.com/ simply drag and drop GCODE text files to validate and verify that operations have performed as expected. Graphics below have been generated using this webpage. Future versions of the application may include GUI and self contained preview of GCODE operations


Subject Background
GCODE files are generally brocken into 3 sections:
	1. preamble - this section contains many modal codes for operation, units, co-ordinate systemsm, etc.
	2. body - this contains instructions for each of the operations. Several operations may be represented
	3. post-script - this section contains the wrap-up of the whole operation, parking the tool, shutting down the spindle etc.

How to run this program
The program requires JAVA to run. Most computers have java installed.

Open the command window

Navigate to the directory where you downloaded the jar file.

>> java -jar gcoder.jar

//TODO update menu
Set the correct arguments to modify your gcode files
 -a <arg>   define array geometry
 -c         input files
 -dir       process all files in the directory
 -f <arg>   files to process
 -o <arg>   output file name
 -r <arg>   angle to roatate input gcode
 -s         input files
 -v         verbose output of the program

Arguments can be brocken into three groups:
1. Input options/files - select which files are to be manipulated
2. Manipulation methods - select ONE of the available manupulations (eg: split, combine, array or rotate) 
3. Non-exclusive optional flags - override the output filename, verbose logging.  


Examples

All the following examples have been created from the following basic input files.


1. Input options/files - select which files are to be manipulated

all

Allows the user to sellect all the files in the current directory as input files

java -jar gcoder.jar -all {+ manupulation}

'
f

Allows the user to sellect specific files as input files

java -jar gcoder.jar -f triangle.tap 
java -jar gcoder.jar -f square.tap
java -jar gcoder.jar -f triangle.tap square.tap



2. Manipulation methods - select ONE of the available manupulations (eg: split, combine, array or rotate) 

Split
Use case: The split function takes a gcode file that has a series of operations and generates individual GCODE files for EACH of the operations. This is useful if you have completed a job but want to re-run one of the operations. Or, if there was some issue that interrupted one of the operations and the machining routine was interrupted prematurely, the operator can split the gcode into self contained operations (which include the preamble and post-script) and run each operation individually. Could also be used by the operator to re-order operations.

How to call 'split' function
java -jar gcoder.jar -f triangle.tap -s


Output

Combine
Use case: The combine function enables the user to load multiple different gcode files and combine them into one to save time when machining. The individual input files are automatically offset from one another (in the positive X direction) so the parts dont conflict. The operations are re-ordered so that if the same tool is used accross components, those operations are grouped reducing the number of tool changes required.

How to call 'combine' function

java -jar gcoder.jar -f triangle.tap square.tap -c

Output

Rotate
Use case: For some reason the GCODE generated assumes the stock is oriented differently and the GCODE needs to be corrected. An example could be that the CAM process has assumed a bed size of height = 2400mm and length = 1200mm rather than, height = 1200mm and length = 2400mm. Use of the command would remove need to reconfigure CAM program. Another use may be nesting parts into odd stock locations.

How to call 'rotate' function
java -jar gcoder.jar -f triangle.tap -r 90

Output

Array
Use case: The GCODE for a mass produced part has been prototyped tested and proven and is now ready for manufacture. Rather than returning to the CAM program, simply array the GCODE.

How to call 'array' function

java -jar gcoder.jar -f triangle.tap -a 2 2 10

Output

3. Non-exclusive optional flags - override the output filename, verbose logging.  

Output
Output filename will be automatically generated using the input filename(s) and details of the manipulations as well as the date and time of running. This can be overridden by the user specifying the name for the output file. 

How to call 'output' function
java -jar gcoder.jar -f triangle.tap -o TRIANGLE.tap


Verbose
Something going wrong? add the "-v" flag to get a detailed console output of the program's workings.

java -jar gcoder.jar -f triangle.tap -r 90 -v

#TODOS:
Triangle and square need a drill operation in the corners for split and combine demos

