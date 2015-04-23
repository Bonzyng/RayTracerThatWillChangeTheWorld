package ex3.light;

import java.util.Map;

import e3.utils.eRGB;
import math.Vec;

public class DirectionalLight extends Light {
	
	private static final String ERR_DIRECTION_NOT_LEGAL = "Error: direction must be a 3D coordinate";
	private Vec mDirection;
	
	@Override
	public void init(Map<String, String> attributes) {
		super.init(attributes);
		
		if (attributes.containsKey("direction")) {
			mDirection = new Vec(attributes.get("direction"));
			mDirection.normalize();
		} else {
			throw new IllegalArgumentException(ERR_DIRECTION_NOT_LEGAL);
		}
	}

	public double getLightIntensity(eRGB color) {
		if (color == eRGB.RED) {
			return mColor.x;
		} else if (color == eRGB.GREEN) {
			return mColor.y;
		} else { // Return blue
			return mColor.z;
		}
	}
	
	public Vec getDirection() {
		return mDirection;
	}
}
