package ex3.light;

import java.util.Map;

import math.Point3D;
import math.Vec;

public class SpotLight extends Light {
	
	private static final String ERR_DIRECTION_NOT_LEGAL = "Error: direction must be a 3D coordinate";
	private static final String ERR_POSITION_NOT_LEGAL = "Error: Position must be a 3D point";

	private Point3D mPosition;
	private Vec mDirection;
	private Vec mAttentuation;
	
	@Override
	public void init(Map<String, String> attributes) {
		super.init(attributes);
		
		if (attributes.containsKey("pos")) {
			mPosition = new Point3D(attributes.get("pos"));
		} else {
			throw new IllegalArgumentException(ERR_POSITION_NOT_LEGAL);
		}
		
		if (attributes.containsKey("direction")) {
			mDirection = new Vec(attributes.get("direction"));
			mDirection.normalize();
		} else {
			throw new IllegalArgumentException(ERR_DIRECTION_NOT_LEGAL);
		}
		
		if (attributes.containsKey("attenuation")) {
			mAttentuation = new Vec(attributes.get("attenuation"));
		} else {
			mAttentuation = new Vec(1, 0, 0);
		}
		mAttentuation.normalize();
	}
}
