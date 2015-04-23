package ex3.light;

import java.util.Map;

import e3.utils.eRGB;
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
		
		if (attributes.containsKey("dir")) {
			mDirection = new Vec(attributes.get("dir"));
			mDirection.normalize();
		} else {
			throw new IllegalArgumentException(ERR_DIRECTION_NOT_LEGAL);
		}
		
		double k_c;
		String inputFromXML;
		if (attributes.containsKey("kc")) {
			inputFromXML = attributes.get("kc");
			k_c = Double.parseDouble(inputFromXML);
		} else {
			k_c = 0.0;
		}
		
		double k_l;
		if (attributes.containsKey("kl")) {
			inputFromXML = attributes.get("kl");
			k_l = Double.parseDouble(inputFromXML);
		} else {
			k_l = 0.0;
		}
		
		double k_q;
		if (attributes.containsKey("kq")) {
			inputFromXML = attributes.get("kq");
			k_q = Double.parseDouble(inputFromXML);
		} else {
			k_q = 0.0;
		}
		
		mAttentuation = new Vec(k_c, k_l, k_q);
		mAttentuation.normalize();
	}

	public double getLightIntensity(eRGB color, double distance, Point3D hit) {
		
		Vec vecToHit = Point3D.getVec(mPosition, hit);
		double intensity;
		double numerator;
		
		if (color == eRGB.RED) {
			intensity = mColor.x;
		} else if (color == eRGB.GREEN) {
			intensity = mColor.y;
		} else { // Blue
			intensity = mColor.z;
		}
		
		// I_0 * (D*L)
		numerator = intensity * Vec.dotProd(mDirection, vecToHit);
		
		// Numerator / (k_c + k_l*d + k_q*d^2) - see p.34 in lec 3 presentation
		return numerator / (mAttentuation.x + (mAttentuation.y * distance) + (mAttentuation.z * distance * distance));
	}
	
	public Point3D getPosition() {
		return mPosition;
	}
}
