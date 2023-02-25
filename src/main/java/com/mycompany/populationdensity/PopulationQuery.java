/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.populationdensity;

/**
 *
 * @author dell
 */
import java.io.*;
import static java.lang.System.*;
public class PopulationQuery {
	// next four constants are relevant to parsing
	public static final int TOKENS_PER_LINE  = 7;
	public static final int POPULATION_INDEX = 4; // zero-based indices
	public static final int LATITUDE_INDEX   = 5;
	public static final int LONGITUDE_INDEX  = 6;

	// argument 1: file name for input data: pass this to parse
	// argument 2: number of x-dimension buckets
	// argument 3: number of y-dimension buckets
	// argument 4: -v1, -v2, -v3, -v4, or -v5
	public static void main(String... args) {
		   if(args.length != 4)
			   {
		    err.printf("pass four Arguments ");
			exit(1);
			   }
		String fileName = args[0];
		CensusData censusData = PopulationQuery.parse(fileName);
		int x = Integer.valueOf(args[1]);
		int y = Integer.valueOf(args[2]);
		String v = (args[3]);
		VersionSelection obj = new VersionSelection();
		obj.a =x;
		obj.b =y;
		obj.cD = censusData;
		CutOff obj1  = new CutOff();
		obj1.c=x;
        obj1.d=y;
        obj1.cData = censusData;
		switch(v)
			{
		case "-v1":     
		    obj.v1();
			break;
		case "-v2":
			obj.v2();
			break;
		case "-v3":
			obj.v3();
			break;
		case "-v4":
			obj.v4();
			break;
		case "-v5":
			obj.v5();
			break;
		case "-c1":
			obj1.c1();
			break;
			
		case "-c2":
			obj1.c2();
			break;
			
		case "-p1":
			
			int grid = 10;
			do {
				SmarterAndParallel_V4 spV4 = new SmarterAndParallel_V4(grid,grid, censusData,1000);
				SmarterAndLockedBased_V5 slV5 = new SmarterAndLockedBased_V5(grid,grid, censusData,1000);
				long st1 = nanoTime();
				spV4.findUSRectangle();
				long et1 = nanoTime();			
				long st2 = nanoTime();
				slV5.findUSRectangle();
				long et2 = nanoTime();
				out.println("Grid: "+ grid + " V4 Time : " + (et1-st1)+ " V5 Time : " + (et2-st2));
				if(grid == 10) {
					grid = 50;
				}else {
					grid += 50;
				}
			}while(grid <= 500);
			
			break;
		case "-p2":
			SimpleAndSequential_V1 ssV1 = new SimpleAndSequential_V1(x, y, censusData);
			SmarterAndSequential_V3 ssV3 = new SmarterAndSequential_V3(x, y, censusData);
			long preSt1 = nanoTime();
			ssV1.findUSRectangle();
			long preEt1 = nanoTime();
			out.println("V1 Processing time: " + (preEt1 - preSt1));
			long preSt2 = nanoTime();
			ssV3.findUSRectangle();
			long preEt2 = nanoTime();
			out.println("V3 Processing time: " + (preEt2 - preSt2));
			int querySize = 50;
			do {
				ssV1.inputRecBoundary =  new Rectangle(1, querySize, querySize, 1);
				ssV3.inputRecBoundary =  new Rectangle(1, querySize, querySize, 1);
				long st1 = nanoTime();
				ssV1.queryPopulation();
				long et1 = nanoTime();
				long st2 = nanoTime();
				ssV3.queryPopulation();
				long et2 = nanoTime();
				out.println("Query Size: "+ querySize + " V1 Time : " + (et1-st1)+ " V3 Time : " + (et2-st2));
				querySize += 50;
			}while(querySize < x);
			break;		
		case "-p3":
			SimpleAndParallel_V2 spV2 = new SimpleAndParallel_V2(x, y, censusData,7000);
			SmarterAndParallel_V4 spV4 = new SmarterAndParallel_V4(x, y, censusData,7000);
			long preSt3 = nanoTime();
			spV2.findUSRectangle();
			long preEt3 = nanoTime();
			out.println("V2 Processing time: " + (preEt3 - preSt3));
			long preSt4 = nanoTime();
			spV4.findUSRectangle();
			long preEt4 = nanoTime();
			out.println("V4 Processing time: " + (preEt4 - preSt4));
			int querySize1 = 50;
			do {
				spV2.inputRecBoundary =  new Rectangle(1, querySize1, querySize1, 1);
				spV4.inputRecBoundary =  new Rectangle(1, querySize1, querySize1, 1);
				long st1 = nanoTime();
				spV2.queryPopulation();
				long et1 = nanoTime();
				long st2 = nanoTime();
				spV4.queryPopulation();
				long et2 = nanoTime();
				out.println("Query Size: "+ querySize1 + " V2 Time : " + (et1-st1)+ " V4 Time : " + (et2-st2));
				querySize1 += 50;
			}
			while(querySize1 < x);
			break;

		}

	}
	
			public static CensusData parse(String filename) {
			CensusData result = new CensusData();
			try {
				BufferedReader fileIn = new BufferedReader(new FileReader(filename));
				// Skip the first line of the file
				// After that each line has 7 comma-separated numbers (see constants above)
				// We want to skip the first 4, the 5th is the population (an int)
				// and the 6th and 7th are latitude and longitude (floats)
				// If the population is 0, then the line has latitude and longitude of +.,-.
				// which cannot be parsed as floats, so that's a special case
				//   (we could fix this, but noisy data is a fact of life, more fun
				//    to process the real data as provided by the government)
				String oneLine = fileIn.readLine(); // skip the first line
				// read each subsequent line and add relevant data to a big array
				while ((oneLine = fileIn.readLine()) != null) {
					String[] tokens = oneLine.split(",");
					if(tokens.length != TOKENS_PER_LINE)
						throw new NumberFormatException();
					int population = Integer.parseInt(tokens[POPULATION_INDEX]);
					if(population != 0)
						result.add(population,
								Float.parseFloat(tokens[LATITUDE_INDEX]),
			       				Float.parseFloat(tokens[LONGITUDE_INDEX]));
				}

				fileIn.close();
			} catch(IOException ioe) {
				err.println(ioe.getMessage());
				err.println("Error opening/reading/writing input or output file.");
				System.exit(1);
			} catch(Exception e) {
				err.println(e.toString());
				err.printf("Error in file format");
				exit(1);
			}
			return result;
		}

}
