package ex3.surfaces;

import math.Vec;

public abstract class Surface {
	private Vec mMaterialDiffuse;
	private Vec mMaterialSpecular;
	private Vec mMaterialAmbient;
	private Vec mMaterialEmission;
	private int mMaterialShininess;
	private double mReflectance;
	
	public Surface() {
		mMaterialDiffuse = new Vec(0.7, 0.7, 0.7);
		mMaterialSpecular = new Vec(1, 1, 1);
		mMaterialAmbient = new Vec(0.1, 0.1, 0.1);
		mMaterialEmission = new Vec(0, 0, 0);
		mMaterialShininess = 100;
		mReflectance = 0;
	}
}
