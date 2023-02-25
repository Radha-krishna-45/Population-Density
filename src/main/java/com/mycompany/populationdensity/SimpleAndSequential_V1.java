/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.populationdensity;

/**
 *
 * @author dell
 */
public class SimpleAndSequential_V1 extends ResultSimulation{
	public SimpleAndSequential_V1(int x, int y, CensusData censusData) {
		this.x = x;
		this.y = y;
		this.censusData = censusData;
	}

	public void findUSRectangle() 
		{
		usRectangle = new Rectangle(censusData.data[0].longitude, censusData.data[0].longitude, censusData.data[0].latitude, censusData.data[0].latitude);
		Rectangle t;
		totalPopulation += censusData.data[0].population;
		for (int i = 1; i < censusData.data_size; i++) {
			CensusGroup cG = censusData.data[i];
			if(cG != null) {
				t = new Rectangle(cG.longitude, cG.longitude, cG.latitude, cG.latitude);
				usRectangle = usRectangle.encompass(t);
				totalPopulation += cG.population;
			}
		}
	}
	
	public long queryPopulation() {
		Long totalPopulationInArea = new Long(0);
		float w = (usRectangle.right - usRectangle.left) / x;
		float h = (usRectangle.top - usRectangle.bottom) / y;
		
		float wBound = (usRectangle.left + (inputRecBoundary.left - 1) * (w));
		float eBound = (usRectangle.left + (inputRecBoundary.right) * (w));
		float nBound = (usRectangle.bottom + (inputRecBoundary.top) * (h));
		float sBound = (usRectangle.bottom + (inputRecBoundary.bottom - 1) * (h));
		
		for (int i = 0; i < censusData.data_size; i++) {
			CensusGroup censusGroup = censusData.data[i];
			
            float groupLong = censusGroup.longitude;
            float groupLat = censusGroup.latitude;
            // Defaults to North and/or East in case of tie
            if (groupLat >= sBound &&
                    (groupLat < nBound ||
                            (nBound - usRectangle.top) >= 0) &&
                            (groupLong < eBound ||
                                    (eBound - usRectangle.right) >= 0) &&
                                    groupLong >= wBound) {
            		totalPopulationInArea += censusGroup.population;
            }
		}
		return totalPopulationInArea;	
	}
	public void findPopulation() {
		Long popInArea = queryPopulation();
		System.out.println("Total Population in the Area: " + popInArea);
		System.out.println("Total Population: " + totalPopulation);
		float percent = (popInArea.floatValue() * 100)/totalPopulation.floatValue();
		System.out.printf("Percent of total population: %.2f \n",percent);
	}
}

