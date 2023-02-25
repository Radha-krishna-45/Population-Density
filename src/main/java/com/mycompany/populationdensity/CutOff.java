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
class CutOff
{
int c,d ;
CensusData cData;
public void c1()
{
SimpleAndSequential_V1 simple = new SimpleAndSequential_V1(c, d, cData);
SimpleAndParallel_V2 simPar = new SimpleAndParallel_V2(c, d, cData, 0);
long sTime = nanoTime();
simple.findUSRectangle();
long eTime = nanoTime();
long seqDuration = eTime - sTime;
out.println("Total PreProcessing Time ---> Simple and Sequential: " + seqDuration +"ns");
long parDuration = 0;
int iCutoff = 1;			
do {
    iCutoff += 1;
    simPar = new SimpleAndParallel_V2(c, d, cData, (iCutoff));
   long startTime1 = nanoTime();
   simPar.findUSRectangle();
  long endTime1 = nanoTime();
  parDuration = endTime1 - startTime1;
 out.println("Cutoff:  " + iCutoff + " Total Time : " + parDuration);
 }while(parDuration > seqDuration);
out.println("Sequential_Cutoff ---> " + iCutoff);
}
public void c2()
{
SmarterAndSequential_V3 smaSeq = new SmarterAndSequential_V3(c, d, cData);
SmarterAndParallel_V4 smaPar = new SmarterAndParallel_V4(c, d, cData, 0);
long sTime3 = nanoTime();
smaSeq.findUSRectangle();
long eTime3 = nanoTime();
long smaSeqDur = eTime3 - sTime3;
out.println("Total PreProcessing Time ---> Smarter and Sequential: " + smaSeqDur +"ns");
long smaParDur = 0;
int cutoff = 0;
do {
cutoff += 500;
smaPar = new SmarterAndParallel_V4(c, d, cData, (cutoff));
long sTime1 = nanoTime();
smaPar.findUSRectangle();
long eTime1 = nanoTime();
smaParDur = eTime1 - sTime1;
out.println("Cutoff:  " + cutoff + " Total Time : " + smaParDur);
}while(smaParDur > smaSeqDur);
out.println("Sequential Cutoff  --->:  " + cutoff);
}
}
