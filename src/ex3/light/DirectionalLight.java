package ex3.light;

import java.util.Map;

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

	public Vec getLightIntensity() {
		return new Vec(mColor);
	}
	
	public Vec getDirection() {
		return mDirection;
	}
}
