package ex3.render.raytrace;

import java.util.Map;

import math.Point3D;
import math.Ray;
import math.Vec;


/**
 * Represents the scene's camera.
 * 
 */
public class Camera implements IInitable{
	
	private static final String ERR_MISSING_EYE = "Error: No eye value provided in XML";
	private static final String ERR_DIRECTION_OR_LOOK_AT = "Error: No direction or look-at values provided in XML";
	private static final String ERR_UP_DIRECTION = "Error: No up-direction value provided in XML";
	private static final String ERR_SCREEN_DIST = "Error: No screen-dist value provided in XML";

	private Point3D mEye;
	private Point3D mLookAt;
	private Vec mToDirection;
	private Vec mUpDirection;
	private Vec mRightDirection;
	private double mScreenDistance;
	private double mScreenWidth;
	private Point3D mImageCenter3D;
	
	public Camera() {}
	
	public void init(Map<String, String> attributes) {
		boolean gotDirection = false;
		boolean gotLookAt = false;
		
		// Check attributes validity and populate the members
		if (attributes.containsKey("eye")) {
			mEye = new Point3D(attributes.get("eye"));
		} else {
			throw new IllegalArgumentException(ERR_MISSING_EYE);
		}
		
		if (attributes.containsKey("direction")) {
			mToDirection = new Vec(attributes.get("direction"));
			gotDirection = true;
		}
		
		if (attributes.containsKey("look-at")) {
			mLookAt = new Point3D(attributes.get("look-at"));
			gotLookAt = true;
		}
		
		if (!gotDirection && !gotLookAt) {
			throw new IllegalArgumentException(ERR_DIRECTION_OR_LOOK_AT);
		} else if (gotLookAt && !gotDirection) {
			mToDirection = Point3D.getVec(mEye, mLookAt);
		}
		
		if (attributes.containsKey("up-direction")) {
			mUpDirection = new Vec(attributes.get("up-direction"));
		} else {
			throw new IllegalArgumentException(ERR_UP_DIRECTION);
		}
		
		if (attributes.containsKey("screen-dist")) {
			mScreenDistance = Double.parseDouble(attributes.get("screen-dist"));
		} else {
			throw new IllegalArgumentException(ERR_SCREEN_DIST);
		}
		
		if (attributes.containsKey("screen-width")) {
			mScreenWidth = Double.parseDouble(attributes.get("screen-width"));
		} else {
			mScreenWidth = 2.0;
		}
		// End populate members
		
		// Normalize member vectors
		mUpDirection.normalize();
		mToDirection.normalize();
		
		mRightDirection = Vec.crossProd(mToDirection, mUpDirection);
		mRightDirection.normalize();
		
		// Ensure up and to directions are orthogonal
		if (mUpDirection.dotProd(mToDirection) != 0) {		
			mUpDirection = Vec.crossProd(mRightDirection, mToDirection);
			mUpDirection.normalize();
		}
		
		// Initialize the global image center in 3D space
		mImageCenter3D = new Point3D(mEye, Vec.scale(mScreenDistance, mToDirection));		
	}
	
	/**
	 * Transforms image xy coordinates to view pane xyz coordinates. Returns the
	 * ray that goes through it.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	
	
	public Ray constructRayThroughPixel(double x, double y, double height, double width) {		
		double pixelRatio = mScreenWidth / width;
		Point3D ImageCenter2D = new Point3D(Math.floor(width / 2), Math.floor(height / 2), 0);
		
		Vec scaledRight = Vec.scale((x - ImageCenter2D.x) * pixelRatio, mRightDirection);
		Vec scaledUp = Vec.scale((y - ImageCenter2D.y) * pixelRatio, mUpDirection);
		
		scaledUp.negate(); // Negated to subtract from p
		
		// Find point p in relation to the 3D center of the image using the scaled
		// right and up vectors
		Point3D p = new Point3D(new Point3D(mImageCenter3D, scaledRight), scaledUp);
		
		// Find the vector going from the camera eye to the point
		Vec rayVec = Point3D.getVec(mEye, p);
		
		return new Ray(mEye, rayVec);
	}
	
	public Point3D getEye() {
		return mEye;
	}
	
	/**
	 * Construct a ray through a pixel and if super sampling is needed, through a grid
	 * point inside the pixel. Returns the ray
	 * 
	 * @param x - width value of the pixel
	 * @param y - height value of the pixel
	 * @param sampleX - width value of the pixel grid
	 * @param sampleY - height value of the pixel grid
	 * @param width - width of the canvas
	 * @param height - height of the canvas
	 * @param superSample - the super sampling modifier
	 * @return the ray through the pixel grid point
	 */
	public Ray constructRayThroughPixelWithSuperSample(double x, double y, 
			double sampleX, double sampleY, double width, double height, int superSample) {
		
		// FIND PIXEL IN THE IMAGE
		double pixelRatio = mScreenWidth / width;
		Point3D ImageCenter2D = new Point3D(Math.floor(width / 2), Math.floor(height / 2), 0);
		
		Vec scaledRight = Vec.scale((x - ImageCenter2D.x) * pixelRatio, mRightDirection);
		Vec scaledUp = Vec.scale((y - ImageCenter2D.y) * pixelRatio, mUpDirection);
		
		scaledUp.negate(); // Negated to subtract from p
		
		// Find point p in relation to the 3D center of the image using the scaled
		// right and up vectors
		Point3D pixel = new Point3D(new Point3D(mImageCenter3D, scaledRight), scaledUp);
		
		// Find the vector going from the camera eye to the point
		Vec rayVec = Point3D.getVec(mEye, pixel);
				
		Ray ray =  new Ray(mEye, rayVec);
		
		// FIND GRID POINT INSIDE THE PIXEL
		if (superSample > 1) {
			double superSampleRatio = pixelRatio / superSample;
			Point3D pixelCenter2D = new Point3D((pixelRatio / 2.0), (pixelRatio / 2.0), 0);
			
			Vec pixelRightDirection = Vec.crossProd(ray.mDirectionVector, mUpDirection);
			Vec pixelUpDirection;
			if (ray.mDirectionVector.dotProd(mUpDirection) != 0) {
				pixelUpDirection = Vec.crossProd(pixelRightDirection, ray.mDirectionVector);
			} else {
				pixelUpDirection = new Vec(mUpDirection);
			}
			
			Vec superScaledRight = Vec.scale((sampleX - pixelCenter2D.x) * superSampleRatio, pixelRightDirection);
			Vec superScaledUp = Vec.scale((sampleY - pixelCenter2D.y) * superSampleRatio, pixelUpDirection);
			superScaledUp.negate();
			
			Point3D p = new Point3D(new Point3D(pixel, superScaledRight), superScaledUp);
			
			Vec superRay = Point3D.getVec(mEye, p);
			
			return new Ray(mEye, superRay);
		} else {
			return ray;
		}
	}	
}
