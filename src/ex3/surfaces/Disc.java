package ex3.surfaces;

import java.util.Map;

import math.Point3D;
import math.Ray;
import math.Vec;

public class Disc extends Sphere {
	
	private static final String ERR_NO_NORMAL = "Error: No normal value given";
	
	private Vec mNormal;
	
	@Override
	public void init(Map<String, String> attributes) {
		super.init(attributes);
		
		if (attributes.containsKey("normal")) {
			mNormal = new Vec(attributes.get("normal"));
		} else {
			throw new IllegalArgumentException(ERR_NO_NORMAL);
		}
		
		// Ensure the normal is normalized
		mNormal.normalize();
	}
	
	@Override
	public Point3D intersect(Ray iRay) {
		// TODO implement disc-ray intersection
		return null;
	}
}
