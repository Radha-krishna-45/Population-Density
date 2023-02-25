/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.populationdensity;

/**
 *
 * @author dell
 */
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import static java.lang.System.*;
public class SmarterAndParallel_V4 extends SimpleAndParallel_V2{
	
	int[][] grid;
	
	public SmarterAndParallel_V4(int x, int y, CensusData censusData,int cutOff) {
		super(x, y, censusData,cutOff);
        grid = new int[x][y];
	}
	public void findUSRectangle() {
		super.findUSRectangle();
		
		grid = fjPool.invoke(new SmarterPreprocessor(0, censusData.data_size));

        
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
		Long totPopInArea = new Long(0);
		totPopInArea += grid[(int) (inputRecBoundary.right - 1)][(int) (inputRecBoundary.bottom - 1)];
		totPopInArea -= (inputRecBoundary.top == y ? 0 : grid[(int) (inputRecBoundary.right- 1)][(int) inputRecBoundary.top]); // top right
		totPopInArea -= (inputRecBoundary.left == 1 ? 0 : grid[(int) (inputRecBoundary.left- 1 - 1)][(int) (inputRecBoundary.bottom - 1)]); // bottom left
		totPopInArea += (inputRecBoundary.left == 1 || inputRecBoundary.top == y ? 0 : grid[(int) (inputRecBoundary.left - 1 - 1)][(int) inputRecBoundary.top]); // top left
		return totPopInArea;
	}
	public void findPopulation() {
		Long popInArea = queryPopulation();
		out.println("Total Population in the Area: " + popInArea);
		out.println("Total Population: " + totalPopulation);
		float percent = (popInArea.floatValue() * 100)/totalPopulation.floatValue();
		out.printf("Percent of total population: %.2f \n",percent);
	}
	
	
	
    @SuppressWarnings("serial")
    class SmarterPreprocessor extends RecursiveTask<int[][]>{
        int hi, lo;

        
        SmarterPreprocessor(int lo, int hi) {
            this.lo  = lo;
            this.hi = hi;
        }

        /** {@inheritDoc} */
        @Override
        protected int[][] compute() {
            if(hi - lo <  cutoff) {
                CensusGroup group;
                int row, col;
                int[][] g = new int[x][y];

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
                    g[row][col] += group.population;

                }
                return g;

            } else {
                SmarterPreprocessor left = new SmarterPreprocessor(lo, (hi+lo)/2);
                SmarterPreprocessor right = new SmarterPreprocessor((hi+lo)/2, hi);

                left.fork(); 
                int[][] gRight = right.compute();
                int[][] gLeft = left.join();

                
                fjPool.invoke(new AddGrids(0, x, gLeft, gRight));
                return gRight;
            }

        }
    }

    @SuppressWarnings("serial")
    class AddGrids extends RecursiveAction{
        private int xhi, xlo;
        private int[][] l, r;
       
        AddGrids(int xlo, int xhi, int[][] l, int[][] r) {
            this.xlo  = xlo;
            this.xhi = xhi;
            this.l = l;
            this.r = r;
        }

        /** {@inheritDoc} */
        @Override
        protected void compute() {
                if((xhi-xlo) <  cutoff) {
                for(int i = xlo; i < xhi; i++) {
                    for(int j = 0; j < y; j++)
                        r[i][j] += l[i][j];
                }
            } else {   
            	
            	System.out.println("nothing");
//                AddGrids l = new AddGrids(xlo, (xhi+xlo)/2, l, r);
//                AddGrids r = new AddGrids((xhi+xlo)/2, xhi, l, r);
//
//                l.fork(); 
//                r.compute();
//                l.join();
            }

        }
    }
}
