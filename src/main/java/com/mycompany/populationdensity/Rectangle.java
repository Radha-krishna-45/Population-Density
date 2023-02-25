/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.populationdensity;

/**
 *
 * @author dell
 */


// A class to represent a Rectangle
// You do not have to use this, but it's quite convenient
public class Rectangle {
        // invariant: right >= left and top >= bottom (i.e., numbers get bigger as you move up/right)
        // note in our census data longitude "West" is a negative number which nicely matches bigger-to-the-right
	public float left;
	public float right;
	public float top;
	public float bottom;
	
	public Rectangle(float l, float r, float t, float b) {
		left   = l;
		right  = r;
		top    = t;
		bottom = b;
	}
	
	// a functional operation: returns a new Rectangle that is the smallest rectangle
	// containing this and that
	public Rectangle encompass(Rectangle rec) {
		return new Rectangle(Math.min(this.left,   rec.left),
						     Math.max(this.right,  rec.right),             
						     Math.max(this.top,    rec.top),
				             Math.min(this.bottom, rec.bottom));
	}
	
	public String toString() {
		return "[left=" + left + " right=" + right + " top=" + top + " bottom=" + bottom + "]";
	}
}
