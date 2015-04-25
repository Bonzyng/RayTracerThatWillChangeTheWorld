package ex3.light;

import java.util.Map;

import math.Point3D;
import math.Vec;

public class SpotLight extends Light {
	
	private static final String ERR_DIRECTION_NOT_LEGAL = "Error: direction must be a 3D coordinate";
	private static final String ERR_POSITION_NOT_LEGAL = "Error: Position must be a 3D point";
	private static final String ERR_COEFFICIENT_NOT_LEGAL = "Error: coefficent must be a double";


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
		
		double k_c;
		String inputFromXML;
		if (attributes.containsKey("kc")) {
			inputFromXML = attributes.get("kc");
			try {
				k_c = Double.parseDouble(inputFromXML);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(ERR_COEFFICIENT_NOT_LEGAL);
			}
		} else {
			k_c = 1.0;
		}

		double k_l;
		if (attributes.containsKey("kl")) {
			inputFromXML = attributes.get("kl");
			try {
				k_l = Double.parseDouble(inputFromXML);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(ERR_COEFFICIENT_NOT_LEGAL);
			}
		} else {
			k_l = 0.0;
		}

		double k_q;
		if (attributes.containsKey("kq")) {
			inputFromXML = attributes.get("kq");
			try {
				k_q = Double.parseDouble(inputFromXML);
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(ERR_COEFFICIENT_NOT_LEGAL);
			}
		} else {
			k_q = 0.0;
		}
		
		mAttentuation = new Vec(k_c, k_l, k_q);
	}

	public Vec getLightIntensity(double distance, Point3D hit) {
		
		Vec vecToHit = Point3D.getVec(mPosition, hit);
		Vec intensity = new Vec(mColor);
		Vec numerator;
		double attenuation;
		
		// I_0 * (D*L)
		numerator = Vec.scale(Vec.dotProd(mDirection, vecToHit), intensity);
		
		attenuation = 1 / (mAttentuation.x + (mAttentuation.y * distance) + (mAttentuation.z * distance * distance));
		
		// Numerator / (k_c + k_l*d + k_q*d^2) - see p.34 in lec 3 presentation
		return Vec.scale(attenuation, numerator);
	}
	
	public Point3D getPosition() {
		return mPosition;
	}
}
