package ex3.surfaces;

import java.util.Map;

import e3.utils.eRGB;
import math.Point3D;
import math.Ray;
import math.Vec;

public abstract class Surface {
	
	private static final String ERR_SHININESS_NOT_POS_INTEGER = "Error: mtl-shininess "
			+ "must be a positive integer";
	private static final String ERR_REFLECTANCE_NOT_LEGAL = "Error: reflectance "
			+ "must be a double between 0.0 and 1.0";
	private static final String ERR_TRANSLUCENCY_NOT_LEGAL = "Error: translucency "
			+ "must be a double between 0.0 and 1.0";
	private static final String ERR_REFRACTIVE_INDEX_NOT_LEGAL = "Error: refractive "
			+ "index must be a double between 1.0 and 4.05";
	private static final String ERR_NO_REFRACTIVE_INDEX = "Error: no refractive index value given";
	
	public static final double EPSILON = 0.000001;
	
	protected Vec mMaterialDiffuse;
	protected Vec mMaterialSpecular;
	protected Vec mMaterialAmbient;
	protected Vec mMaterialEmission;
	protected int mMaterialShininess;
	protected double mReflectance;
	protected double mTranslucency;
	protected double mRefractiveIndex;
	
	public Surface() {}
	
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
		
		if (attributes.containsKey("translucency")) {
			try {
				mTranslucency = Double.parseDouble(attributes.get("translucency"));
				
				if (mTranslucency > 1.0 || mTranslucency < 0.0) {
					throw new IllegalArgumentException(ERR_TRANSLUCENCY_NOT_LEGAL);
				}
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(ERR_TRANSLUCENCY_NOT_LEGAL);
			}
		} else {
			mTranslucency = 0;
		}
		
		if(attributes.containsKey("refractive-index")) {
			try {
				mRefractiveIndex = Double.parseDouble(attributes.get("refractive-index"));
				
				if (mRefractiveIndex > 4.05 || mRefractiveIndex < 1.0) {
					throw new IllegalArgumentException(ERR_REFRACTIVE_INDEX_NOT_LEGAL);
				}
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(ERR_REFRACTIVE_INDEX_NOT_LEGAL);
			}
		} else {
			throw new IllegalArgumentException(ERR_NO_REFRACTIVE_INDEX);
		}
	}
	
	/**
	 * Check whether a ray intersects with this surface
	 * @param iRay
	 * @return the closest point of intersection if one exists, or null otherwise
	 */
	public abstract Point3D intersect(Ray iRay);
	
	/**
	 * Get the normal vector to the surface at the given point
	 * 
	 * @param point
	 * @return normal to the surface at that point
	 */
	public abstract Vec getNormalAtPoint(Point3D point);
	
	public Vec getEmissionColor() {
		return new Vec(mMaterialEmission);
	}
	
	public Vec getAmbientColor() {
		return new Vec(mMaterialAmbient);
	}
	
	public Vec getDiffuseColor() {
		return new Vec(mMaterialDiffuse);
	}
	
	public Vec getSpecularColor() {
		return new Vec(mMaterialSpecular);
	}
	
	public double getShininess() {
		return mMaterialShininess;
	}
	
	public double getReflectance() {
		return mReflectance;
	}
	
	public double getTranslucency() {
		return mTranslucency;
	}
	
	public double getRefractiveIndex() {
		return mRefractiveIndex;
	}
}
