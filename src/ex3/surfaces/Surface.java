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
		setMaterialDiffuse(new Vec(0.7, 0.7, 0.7));
		setMaterialSpecular(new Vec(1, 1, 1));
		setMaterialAmbient(new Vec(0.1, 0.1, 0.1));
		setMaterialEmission(new Vec(0, 0, 0));
		setMaterialShininess(100);
		setReflectance(0);
	}

	public Vec getMaterialDiffuse() {
		return mMaterialDiffuse;
	}

	public void setMaterialDiffuse(Vec iMaterialDiffuse) {
		this.mMaterialDiffuse = iMaterialDiffuse;
	}
	
	public void setMaterialDiffuse(double red, double green, double blue) {
		mMaterialDiffuse = new Vec(red, green, blue);
	}

	public Vec getMaterialSpecular() {
		return mMaterialSpecular;
	}

	public void setMaterialSpecular(Vec iMaterialSpecular) {
		this.mMaterialSpecular = iMaterialSpecular;
	}
	
	public void setMaterialSpecular(double red, double green, double blue) {
		mMaterialSpecular = new Vec(red, green, blue);
	}

	public Vec getMaterialAmbient() {
		return mMaterialAmbient;
	}

	public void setMaterialAmbient(Vec iMaterialAmbient) {
		this.mMaterialAmbient = iMaterialAmbient;
	}
	
	public void setMaterialAmbient(double red, double green, double blue) {
		mMaterialAmbient = new Vec(red, green, blue);
	}

	public Vec getMaterialEmission() {
		return mMaterialEmission;
	}

	public void setMaterialEmission(Vec iMaterialEmission) {
		this.mMaterialEmission = iMaterialEmission;
	}
	
	public void setMaterialEmission(double red, double green, double blue) {
		mMaterialEmission = new Vec(red, green, blue);
	}

	public int getMaterialShininess() {
		return mMaterialShininess;
	}

	public void setMaterialShininess(int iMaterialShininess) {
		this.mMaterialShininess = iMaterialShininess;
	}

	public double getReflectance() {
		return mReflectance;
	}

	public void setReflectance(double iReflectance) {
		this.mReflectance = iReflectance;
	}
}
