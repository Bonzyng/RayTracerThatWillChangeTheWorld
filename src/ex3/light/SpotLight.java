package ex3.light;

import math.Point3D;
import math.Vec;

public class SpotLight extends Light {
	private Point3D mPosition;
	private Vec mDirection;
	private Vec mAttentuation;
	
	public SpotLight() {
		mAttentuation = new Vec(1, 0, 0);
	}
}
