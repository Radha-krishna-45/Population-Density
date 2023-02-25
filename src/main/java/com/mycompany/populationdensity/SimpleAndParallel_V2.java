/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.populationdensity;

/**
 *
 * @author dell
 */
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import static java.lang.System.*;
public class SimpleAndParallel_V2 extends ResultSimulation{

	// for parallel programming!
    static final ForkJoinPool fjPool = new ForkJoinPool();
    	int cutoff = 5000;
		public SimpleAndParallel_V2(int x, int y, CensusData censusData, int cutOff) {
		this.x = x;
		this.y = y;
		this.censusData = censusData;
		this.cutoff = cutOff;
	}
	
	public void findUSRectangle() {
        if (censusData.data_size == 0)
            return;

        Result res = fjPool.invoke(new Preprocessor(0, censusData.data_size));
        usRectangle = res.rec;
        
        totalPopulation = (long) res.population;
        
	}
	
	public long queryPopulation() {
        double westBound = (usRectangle.left + (inputRecBoundary.left - 1) * (usRectangle.right - usRectangle.left) / x);
		double eastBound = (usRectangle.left + (inputRecBoundary.right) * (usRectangle.right - usRectangle.left) / x);
		double northBound = (usRectangle.bottom + (inputRecBoundary.top) * (usRectangle.top - usRectangle.bottom) / y);
		double southBound = (usRectangle.bottom + (inputRecBoundary.bottom - 1) * (usRectangle.top - usRectangle.bottom) / y);
		return (long) fjPool.invoke(new Query(0, censusData.data_size, westBound, eastBound, northBound, southBound));
	}
	
	public void findPopulation() {
		Long popInArea = queryPopulation();
		out.println("Total Population in the Area:: " + popInArea);
		out.println("Total Population:: " + totalPopulation);
		float percent = (popInArea.floatValue() * 100)/totalPopulation.floatValue();
		out.printf("Percent of total population:: %.2f \n",percent);
	}
	
    class Result {
        Rectangle rec;
        int population;
        Result(Rectangle rec, int pop) {
            this.rec = rec;
            population = pop;
        }
    }
    
	@SuppressWarnings("serial")
	class Preprocessor extends RecursiveTask<Result> {
        int hi, lo;

        // Look at data from lo (inclusive) to hi (exclusive)
        Preprocessor(int lo, int hi) {
            this.lo  = lo;
            this.hi = hi;
        }

        /** {@inheritDoc} */
        @Override
        protected Result compute() {
            if(hi - lo <  cutoff) {
                CensusGroup group = censusData.data[lo];
                int pop = group.population;
                Rectangle rec = new Rectangle(group.longitude, group.longitude,
                        group.latitude, group.latitude), temp;
                for (int i = lo + 1; i < hi; i++) {
                    group = censusData.data[i];
                    temp = new Rectangle(group.longitude, group.longitude,
                            group.latitude, group.latitude);
                    rec = rec.encompass(temp);
                    pop += group.population;
                }
                return new Result(rec, pop);
            } else {
                Preprocessor lft = new Preprocessor(lo, (hi+lo)/2);
                Preprocessor rht = new Preprocessor((hi+lo)/2, hi);
                
                ForkJoinTask<SimpleAndParallel_V2.Result> left = null;
				left.fork(); 
                Result rA = rht.compute();
                Result lA = lft.join();
                return new Result(rA.rec.encompass(lA.rec),
                        rA.population + lA.population);
            }

        }
    }
	
	@SuppressWarnings("serial")
	class Query extends RecursiveTask<Integer> {
        int hi, lo;
        double leftBound, rightBound, topBound, bottomBound;

        // Look at data from lo (inclusive) to hi (exclusive)
        // Query bounded by *Bound fields
        Query(int lo, int hi, double leftBound, double rightBound, double topBound, double bottomBound) {
            this.lo  = lo;
            this.hi = hi;
            this.leftBound = leftBound;
            this.rightBound = rightBound;
            this.topBound = topBound;
            this.bottomBound = bottomBound;
        }

        /** {@inheritDoc} */
        @Override
        protected Integer compute() {
            if(hi - lo <  cutoff) {
                CensusGroup group;
                int population = 0;
                double groupLong, groupLat;

                for (int i = lo; i < hi; i++) {
                    group = censusData.data[i];
                    groupLong = group.longitude;
                    groupLat = group.latitude;
                    // Defaults to North and/or East in case of tie
                    if (groupLat >= bottomBound &&
                            (groupLat < topBound ||
                                    (topBound - usRectangle.top) >= 0) &&
                                    (groupLong < rightBound ||
                                            (rightBound - usRectangle.right) >= 0) &&
                                            groupLong >= leftBound)
                        population += group.population;
                }

                return population;
            } else {
                Query left =
                        new Query(lo, (hi+lo)/2, leftBound, rightBound, topBound, bottomBound);
                Query right =
                        new Query((hi+lo)/2, hi, leftBound, rightBound, topBound, bottomBound);

                left.fork(); // fork a thread and calls compute
                Integer rightAns = right.compute(); // call compute directly
                Integer leftAns = left.join();
                return rightAns + leftAns;
            }

        }
    }
}
