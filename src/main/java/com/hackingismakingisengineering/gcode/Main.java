package com.hackingismakingisengineering.gcode;

import com.hackingismakingisengineering.gcode.helper.ArrayPattern;
import com.hackingismakingisengineering.gcode.helper.Session;
import org.apache.commons.cli.*;


//TODO - Fix additional '.' after decimal
//TODO - round values down to 0.0000 precision





/**
 * Youtube video: "Demonstration of Apache Commons CLI API"
 * https://ncviewer.com/
 */

public class Main {

	public static void main(String[] args) {

		/*
		-f triangle.tap -s
				-f triangle.tap square.tap -c
				-f triangle.tap --r 90
				-f triangle.tap -a 2 2 10
				-f triangle.tap -o TRIANGLE.tap
				-f triangle.tap -r 90 -v
		 */


		Options options = new Options();

		OptionGroup optionGroupInput = new OptionGroup();

		Option optionAll = Option.builder("all")
				.desc("add all files in current directory as input files")
				.required(false)
				.valueSeparator(',')
				.build();

		optionGroupInput.addOption(optionAll);

		Option optionFiles = Option.builder("f")
				.hasArgs()
				.desc("list input files to process")
				.required(false)
				.valueSeparator(',')
				.build();

		optionGroupInput.addOption(optionFiles);

		options.addOptionGroup(optionGroupInput);

		//Non-exclusive options
		//options.addOption("f", true, "files to process");
		//options.addOption("all", false, "process all files in the directory");

		//options.addOption("r", true, "angle to roatate input gcode");


		//Exclusive options
		OptionGroup optionGroupManipulate = new OptionGroup();
		Option optionCombine = Option.builder("c")
				.desc("combine input files")
				.required(false)
				.valueSeparator(',')
				.build();

		Option optionSplit = Option.builder("s")
				.desc("split out operations of input files")
				.required(false)
				.valueSeparator(',')
				.build();

		Option optionRotate = Option.builder("r")
				.hasArgs()
				.desc("sangle to roatate input gcode")
				.required(false)
				.valueSeparator(',')
				.build();


		Option optionArray = Option.builder("a")
				.hasArgs()
				.desc("define array geometry")
				.required(false)
				.valueSeparator(',')
				.build();

		optionGroupManipulate.addOption(optionArray);
		optionGroupManipulate.addOption(optionRotate);
		optionGroupManipulate.addOption(optionCombine);
		optionGroupManipulate.addOption(optionSplit);


		options.addOptionGroup(optionGroupManipulate);

		options.addOption("v", false,"verbose output of the program");
		options.addOption("o", true, "override default output file name");

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		}
		catch( ParseException exp ) {

			//In case of parsing issue create the help formater
			String header ="Set the correct arguments to modify your gcode files \n";
			//TODO add repo name once on github
			String footer ="please report any issues to github.com/cstewart000";

			new HelpFormatter().printHelp("GCcode ", header ,options,"\n"+exp.getMessage() + "\n"+ footer);

		}

		if(cmd.hasOption("v")){

			Session.verboseLogging();

			System.out.println("verbose logging enabled.");
		}


		if(cmd.hasOption("f")){

			String[] files = cmd.getOptionValues("f");
				for(String fileIn: files){

						Session.readin(fileIn);

				}
			System.out.println("Importing files completed");

		}

		if(cmd.hasOption("a")){

			String[] arrayProps = cmd.getOptionValues("a");

			ArrayPattern arrayPattern = new ArrayPattern(Integer.parseInt(arrayProps[0]),
					Integer.parseInt(arrayProps[1]),
					Float.parseFloat(arrayProps[2]));

			Session.array(arrayPattern);

			Session.writeOutToFile();

			System.out.println("Arraying");



		}

		if(cmd.hasOption("s")){

			Session.split();
			Session.writeOutToFile();
			System.out.println("splitting files!");

		}
		if(cmd.hasOption("c")){

			Session.combineGcode();
			Session.writeOutToFile();
			System.out.println("combining files!");
		}

		if(cmd.hasOption("r")){
			String angle = cmd.getOptionValue("r");

			Session.rotate(angle);
			Session.writeOutToFile();
			System.out.println("rotating files");
		}

		if(cmd.hasOption("o")){
			String[] filenames = cmd.getOptionValues("o");
			String filename = filenames[0];

			Session.outputGcodes.get(0).setFilename(filename);
			Session.writeOutToFile();
			System.out.println("output file name: " + filename);
		}

		System.out.println("finsihing");

	}
}
