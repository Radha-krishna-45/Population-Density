/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.populationdensity;

/**
 *
 * @author dell
 */
import java.util.InputMismatchException;
import java.util.Scanner;
import static java.lang.System.*;
public class ResultSimulation {
	
	public static int INVALID_ENTRY = 0;
	public static int EXIT = 1;
	public static int DONE = 2;
	
	float wB;
	float sB;
	float eB;
	float nB;

	
	private static Scanner scanner;
	
	int x;
	int y;
	CensusData censusData;
	
	Long totalPopulation = new Long(0);
	
	Rectangle usRectangle;
	Rectangle inputRecBoundary;
	
	int takeInput() {
       out.println("-----------------------------------------------------------");
       	out.println("Please give west, south, east, north coordinates of your query rectangle: ");
		
		scanner = new Scanner(System. in);

		try {
			wB = scanner.nextInt();
			if(wB < 1 || wB > x) {
				out.printf("/n>>>Invalid Western Boundary/n");
				return INVALID_ENTRY;
			}

			sB = scanner.nextInt();
			if(sB < 1 || sB > y) {
				out.printf("/n>>>Invalid Southern Boundary/n");
				return INVALID_ENTRY;
			}

			eB = scanner.nextInt();
			if(eB < wB || eB > x) {
				out.printf("/nInvalid Eastern Boundary/n");
				return INVALID_ENTRY;
			}

			nB = scanner.nextInt();
			if(nB < sB || nB > y) {
				out.printf("/n>>>Invalid Northern Boundary/n");
				return INVALID_ENTRY;
			}

			inputRecBoundary = new Rectangle(wB, eB, nB, sB);
		} 
		catch (InputMismatchException e) 
			{
			return EXIT;
		    }
		return DONE;
	}
	
	public void findUSRectangle() 
		{

	    }
	
	public void findPopulation() {

	}
	
	
}
