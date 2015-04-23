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
		//initialzed in init
	}

	@Override
	public void init(Map<String, String> attributes) {
		if (attributes.containsKey("color")){
			mColor = new Vec(attributes.get("color"));
		} else {
			mColor = new Vec(1,1,1);
		}
	}
	
	public abstract double getLightIntensity(int color);
}
