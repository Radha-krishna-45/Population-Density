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
class VersionSelection
{
int a,b ;
CensusData cD;
public void v1()
{
SimpleAndSequential_V1 v1 = new SimpleAndSequential_V1(a, b, cD);
long startProcTimeV1 = nanoTime();
v1.findUSRectangle();
long endProcTimeV1 = nanoTime();
out.println("Total PreProcessing Time is: " + (endProcTimeV1 - startProcTimeV1)+"ns");
int returnType1 = ResultSimulation.DONE;
do 
{
       returnType1 = v1.takeInput();
      if(returnType1 ==  ResultSimulation.DONE) 
      {
        long startQueryTime = nanoTime();
       v1.findPopulation();
     long endQueryTime = nanoTime();
    out.println("Total Query Time is: " + (endQueryTime - startQueryTime)+"ns");
      }
}
while(returnType1 != ResultSimulation.EXIT);
}
public void v2()
{
 SimpleAndParallel_V2 v2 = new SimpleAndParallel_V2(a, b, cD, 5000);
 long startProcTimeV2 = nanoTime();
v2.findUSRectangle();
long endProcTimeV2 = nanoTime();
out.println("Total PreProcessing Time is: " + (endProcTimeV2 - startProcTimeV2)+"ns");
int returnType2 = ResultSimulation.DONE;
do {
      returnType2 = v2.takeInput();
    if(returnType2 ==  ResultSimulation.DONE) {
   long startQueryTime = nanoTime();
  v2.findPopulation();
  long endQueryTime = nanoTime();
 out.println("Total Query Time is: " + (endQueryTime - startQueryTime)+"ns");
 }
}while(returnType2 != ResultSimulation.EXIT);
}
public void v3()
{
  SmarterAndSequential_V3 v3 = new SmarterAndSequential_V3(a, b, cD);
long startProcessTimeV3 = nanoTime();
v3.findUSRectangle();
long endProcessTimeV3 = nanoTime();
out.println("Total PreProcessing Time is: " + (endProcessTimeV3 - startProcessTimeV3)+"ns");
int returnType3 = ResultSimulation.DONE;
do {
         returnType3 = v3.takeInput();
        if(returnType3 ==  ResultSimulation.DONE) {
        long startQueryTime = nanoTime();
      v3.findPopulation();
    long endQueryTime = nanoTime();
  out.println("Total Query Time is: " + (endQueryTime - startQueryTime)+"ns");
  }
} while(returnType3 != ResultSimulation.EXIT);
}
public void v4()
{
  SmarterAndParallel_V4 v4 = new SmarterAndParallel_V4(a, b, cD, 5000);
			long startProcessTimeV4 = nanoTime();
			v4.findUSRectangle();
			long endProcessTimeV4 = nanoTime();
			out.println("Total PreProcessing Time is: " + (endProcessTimeV4 - startProcessTimeV4)+"ns");
			int returnType4 = ResultSimulation.DONE;
			do {
				returnType4 = v4.takeInput();
				if(returnType4 ==  ResultSimulation.DONE) {
					long startQueryTime = nanoTime();
					v4.findPopulation();
					long endQueryTime = nanoTime();
					out.println("Total Query Time is: " + (endQueryTime - startQueryTime)+"ns");
				}
			}while(returnType4 != ResultSimulation.EXIT);

}
public void v5()
{
  SmarterAndLockedBased_V5 v5 = new SmarterAndLockedBased_V5(a, b, cD,5000);
			long startProcessTimeV5 = nanoTime();
			v5.findUSRectangle();
			long endProcessTimeV5 = nanoTime();
			out.println("Total PreProcessing Time is: " + (endProcessTimeV5 - startProcessTimeV5)+"ns");
			int returnType5 = ResultSimulation.DONE;
			do {
				returnType5 = v5.takeInput();
				if(returnType5 ==  ResultSimulation.DONE) {
					long startQueryTime = nanoTime();
					v5.findPopulation();
					long endQueryTime = nanoTime();
					out.println("Total Query Time is: " + (endQueryTime - startQueryTime)+"ns");
				}
			}while(returnType5 != ResultSimulation.EXIT);
}
}
