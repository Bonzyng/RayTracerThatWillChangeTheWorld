package ex3.surfaces;

import java.util.Map;

import math.Point3D;
import math.Ray;
import math.Vec;

public abstract class Surface {
	
	private static final String ERR_SHININESS_NOT_POS_INTEGER = "Error: mtl-shininess "
			+ "must be a positive integer";
	private static final String ERR_REFLECTANCE_NOT_LEGAL = "Error: reflectance "
			+ "must be a double between 0.0 and 1.0";
	
	protected Vec mMaterialDiffuse;
	protected Vec mMaterialSpecular;
	protected Vec mMaterialAmbient;
	protected Vec mMaterialEmission;
	protected int mMaterialShininess;
	protected double mReflectance;
	
	public Surface() {
		// All initialized in init
//		setMaterialDiffuse(new Vec(0.7, 0.7, 0.7));
//		setMaterialSpecular(new Vec(1, 1, 1));
//		setMaterialAmbient(new Vec(0.1, 0.1, 0.1));
//		setMaterialEmission(new Vec(0, 0, 0));
//		setMaterialShininess(100);
//		setReflectance(0);
	}
	
	public void init(Map<String, String> attributes) {
		
		// Check attributes validity and populate the members
		if (attributes.containsKey("mtl-diffuse")) {
			mMaterialDiffuse = new Vec(attributes.get("mtl-diffuse"));
		} else {
			mMaterialDiffuse = new Vec(0.7, 0.7, 0.7);
		}
		
		if (attributes.containsKey("mtl-specular")) {
			mMaterialSpecular = new Vec(attributes.get("mtl-specular"));
		} else {
			mMaterialSpecular = new Vec(1, 1, 1);
		}
		
		if (attributes.containsKey("mtl-ambient")) {
			mMaterialAmbient = new Vec(attributes.get("mtl-ambient"));
		} else {
			mMaterialAmbient = new Vec(0.1, 0.1, 0.1);
		}
		
		if (attributes.containsKey("mtl-emission")) {
			mMaterialEmission = new Vec(attributes.get("mtl-emission"));
		} else {
			mMaterialEmission = new Vec(0, 0, 0);
		}
		
		if (attributes.containsKey("mtl-shininess")) {
			try {
				mMaterialShininess = Integer.parseInt(attributes.get("mtl-shininess"));
				
				if (mMaterialShininess < 1) {
					throw new IllegalArgumentException(ERR_SHININESS_NOT_POS_INTEGER);
				}
			} catch (NumberFormatException e){
				throw new IllegalArgumentException(ERR_SHININESS_NOT_POS_INTEGER);
			}		
		} else {
			mMaterialShininess = 100;
		}
		
		if (attributes.containsKey("reflectance")) {
			try {
				mReflectance = Double.parseDouble(attributes.get("reflectance"));
				
				if (mReflectance > 1.0 || mReflectance < 0.0) {
					throw new IllegalArgumentException(ERR_REFLECTANCE_NOT_LEGAL);
				}
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(ERR_REFLECTANCE_NOT_LEGAL);
			}	
		} else {
			mReflectance = 0;
		}
	}
	
	/**
	 * Check whether a ray intersects with this surface
	 * @param iRay
	 * @return the closest point of intersection if one exists, or null otherwise
	 */
	public abstract Point3D intersect(Ray iRay);
}
