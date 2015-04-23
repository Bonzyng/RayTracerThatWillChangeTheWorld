package ex3.light;

import java.util.Map;

import e3.utils.eRGB;
import math.Point3D;
import math.Vec;

public class OmniLight extends Light {
	
	private static final String ERR_POSITION_NOT_LEGAL = "Error: Position must be a 3D point";
	
	private Point3D mPosition;
	private Vec mAttentuation; // x = K_c, y = K_l, z = K_q
	
	@Override
	public void init(Map<String, String> attributes) {
		super.init(attributes);
		
		if (attributes.containsKey("pos")) {
			mPosition = new Point3D(attributes.get("pos"));
		} else {
			throw new IllegalArgumentException(ERR_POSITION_NOT_LEGAL);
		}
		
		if (attributes.containsKey("attenuation")) {
			mAttentuation = new Vec(attributes.get("attenuation"));
		} else {
			mAttentuation = new Vec(1, 0, 0);
		}
		mAttentuation.normalize();
	}

	public double getLightIntensity(eRGB color, double distance) {
		double intensity;
		
		if (color == eRGB.RED) {
			intensity = mColor.x;
		} else if (color == eRGB.GREEN) {
			intensity = mColor.y;
		} else { // Blue
			intensity = mColor.z;
		}
		
		// I_0 / (k_c + k_l*d + k_q*d^2) - see p.33 in lec 3 presentation
		intensity /= mAttentuation.x + (mAttentuation.y * distance) + (mAttentuation.z * distance * distance);
		
		return intensity;
	}
	
	public Point3D getPosition() {
		return mPosition;
	}
}
