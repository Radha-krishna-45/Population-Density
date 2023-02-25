/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.populationdensity;

/**
 *
 * @author dell
 */
public class CensusData 
	{
	public static final int INITIAL_SIZE = 100;
	public CensusGroup[] data;
	public int data_size;
	
	public CensusData() 
		{
		data = new CensusGroup[INITIAL_SIZE];
		data_size = 0;
	    }
	
	public void add(int population, float latitude, float longitude) {
		if(data_size == data.length) 
			{ // resize
			CensusGroup[] new_data = new CensusGroup[data.length*2];
			for(int i=0; i < data.length; ++i)
				new_data[i] = data[i];
			data = new_data;
		    }
		CensusGroup g = new CensusGroup(population,latitude,longitude); 
		data[data_size++] = g;
	}
}

