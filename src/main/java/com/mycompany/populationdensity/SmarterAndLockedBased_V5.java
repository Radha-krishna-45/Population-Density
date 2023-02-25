/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.populationdensity;

/**
 *
 * @author dell
 */
import java.util.concurrent.locks.ReentrantLock;
import static java.lang.System.*;
public class SmarterAndLockedBased_V5 extends SimpleAndParallel_V2 {
    
    private final ReentrantLock[][] locks;
	int[][] grid;
    
    public SmarterAndLockedBased_V5(int x, int y, CensusData data,int cutoff) {
        super(x, y, data,cutoff);
        grid = new int[x][y];
        locks = new ReentrantLock[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                locks[i][j] = new ReentrantLock();
            }
        }
    }
    
    public void findUSRectangle() {
		super.findUSRectangle();
		
		SmarterPreprocessor sp = new SmarterPreprocessor(0, censusData.data_size);
        sp.run();

       
        for (int i = 1; i < grid.length; i++) {
            grid[i][grid[0].length - 1] += grid [i - 1][grid[0].length - 1];
        }

        
        for (int i = grid[0].length - 2; i >= 0; i--) {
            grid[0][i] += grid [0][i + 1];
        }

        
        for (int j = grid[0].length - 1 - 1; j >= 0; j--) {
            for (int i = 1; i < grid.length; i++) {
                grid[i][j] += (grid[i-1][j] + grid[i][j+1] - grid[i-1][j+1]);
            }
        }
	}
    
    public long queryPopulation() {
    		Long totalPopulationInArea = new Long(0);
		
		totalPopulationInArea += grid[(int) (inputRecBoundary.right - 1)][(int) (inputRecBoundary.bottom - 1)];
		totalPopulationInArea -= (inputRecBoundary.top == y ? 0 : grid[(int) (inputRecBoundary.right- 1)][(int) inputRecBoundary.top]); // top right
		totalPopulationInArea -= (inputRecBoundary.left == 1 ? 0 : grid[(int) (inputRecBoundary.left- 1 - 1)][(int) (inputRecBoundary.bottom - 1)]); // bottom left
		totalPopulationInArea += (inputRecBoundary.left == 1 || inputRecBoundary.top == y ? 0 : grid[(int) (inputRecBoundary.left - 1 - 1)][(int) inputRecBoundary.top]); // top left

		return totalPopulationInArea;
    	
    }
    
	public void findPopulation() {
		Long popInArea = queryPopulation();
		out.println("Total Population in the Area: " + popInArea);
		out.println("Total Population: " + totalPopulation);
		float percent = (popInArea.floatValue() * 100)/totalPopulation.floatValue();
		out.printf("Percent of total population: %.2f \n",percent);
	}
     
    class SmarterPreprocessor extends java.lang.Thread {
        int hi, lo;
        
        SmarterPreprocessor(int lo, int hi) {
            this.lo  = lo;
            this.hi = hi;
        }

        /** {@inheritDoc} */
        @Override
        public void run() {
            if(hi - lo <  cutoff) {
                CensusGroup group;
                int row, col;
                
                float yAxis = usRectangle.left;
                float xAxis = usRectangle.bottom;
                float gridSquareWidth = (usRectangle.right - usRectangle.left) / x;
                float gridSquareHeight = (usRectangle.top - usRectangle.bottom) / y;
                		
                for (int i = lo; i < hi; i++) {
                    group = censusData.data[i];
                    col = (int) ((group.latitude - xAxis) / gridSquareHeight);
                  
                    if (group.latitude >= (col + 1) * gridSquareHeight + xAxis)
                        col++;
                    col = (col == y ?  y - 1: col); 
                    row = (int) ((group.longitude - yAxis) / gridSquareWidth);
                
                    if (group.longitude >= (row + 1) * gridSquareWidth + yAxis)
                        col++;
                    row = (row == x ? x - 1 : row); 

                   
                    locks[row][col].lock();
                    try {
                        grid[row][col] += group.population;
                    } finally {
                        locks[row][col].unlock();
                    }
                }

            } else {
                SmarterPreprocessor l = new SmarterPreprocessor(lo, (hi+lo)/2);
                SmarterPreprocessor r = new SmarterPreprocessor((hi+lo)/2, hi);

                l.start(); 
                r.run(); 
                try {
                    l.join();
                } catch (InterruptedException e) 
					{
                    out.println(e);
                    }
            }

        }

    }
}
