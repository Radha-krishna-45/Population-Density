/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.populationdensity;

/**
 *
 * @author dell
 */

import static java.lang.System.*;
public class SmarterAndSequential_V3 extends ResultSimulation {

	int[][] grid;
	
    public SmarterAndSequential_V3(int x, int y, CensusData data) {
		this.x = x;
		this.y = y;
		this.censusData = data;
        grid = new int[x][y];
    }
        
	public void findUSRectangle() {
        if (censusData.data_size == 0)
            return;
        long pop = 0;
        CensusGroup group = censusData.data[0];
        Rectangle rec = new Rectangle(group.longitude, group.longitude,
                group.latitude, group.latitude), temp;
        pop += group.population;

        for (int i = 1; i < censusData.data_size; i++) {
            group = censusData.data[i];
            temp = new Rectangle(group.longitude, group.longitude,
                    group.latitude, group.latitude);
            rec = rec.encompass(temp);
            pop += group.population;
        }
        usRectangle = rec;
        float yAxis = usRectangle.left;
        float xAxis = usRectangle.bottom;
        float gridSquareWidth = (usRectangle.right - usRectangle.left) / x;
        float gridSquareHeight = (usRectangle.top - usRectangle.bottom) / y;
        totalPopulation = pop;
        int row, col;
        for (int i = 0; i < censusData.data_size; i++) {
            group = censusData.data[i];
            col = (int) ((group.latitude - xAxis) / gridSquareHeight);
           
            if (group.latitude >= (col + 1) * gridSquareHeight + xAxis)
                col++;
            col = (col == y ?  y - 1: col); 
            row = (int) ((group.longitude - yAxis) / gridSquareWidth);
                      if (group.longitude >= (row + 1) * gridSquareWidth + yAxis)
                col++;
            row = (row == x ? x - 1 : row); 
            grid[row][col] += group.population;

        }
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
}

