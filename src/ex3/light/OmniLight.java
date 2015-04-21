package ex3.light;

import math.Point3D;
import math.Vec;

public class OmniLight extends Light {
	private Point3D mPosition;
	private Vec mAttentuation; // x = K_c, y = K_l, z = K_q
	
	public OmniLight() {
		mAttentuation = new Vec(1, 0, 0);
	}
}
