package ex3.light;

import java.util.Map;
import java.util.Scanner;

import ex3.render.raytrace.IInitable;
import math.Point3D;
import math.Vec;

/**
 * Represent a point light
 * 
 * Add methods as you wish, this can be a super class for other lights (think which)
 */
public abstract class Light implements IInitable {
//TODO add methods. If you don't like this class you can write your own.
	protected Vec mColor; // Intensity of light. I_0 from lecture.

	public Light() {
		mColor = new Vec(1,1,1);
	}
	
	public Light(String l) {
		Scanner s = new Scanner(l);
		mColor.x = s.nextDouble();
		mColor.y = s.nextDouble();
		mColor.z = s.nextDouble();
		s.close();
	}

	@Override
	public void init(Map<String, String> attributes) {
		if (attributes.containsKey("color")){
			//TODO to uncomment this line you should inplement constructor 
			//with a string argument for Vec. You have an example in Point3D class
			
			//color = new Vec(attributes.get("color"));
		}
	}
	

	
}
