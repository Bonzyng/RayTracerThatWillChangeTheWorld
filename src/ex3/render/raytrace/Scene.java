package ex3.render.raytrace;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import ex3.light.DirectionalLight;
import ex3.light.Light;
import ex3.light.OmniLight;
import ex3.light.SpotLight;
import ex3.surfaces.ConvexPolygon;
import ex3.surfaces.Disc;
import ex3.surfaces.Sphere;
import ex3.surfaces.Surface;
import ex3.surfaces.Triangle;
import math.Point3D;
import math.Ray;
import math.Vec;
/**
 * A Scene class containing all the scene objects including camera, lights and
 * surfaces. Some suggestions for code are in comment
 * If you uncomment these lines you'll need to implement some new types like Surface
 * 
 * You can change all methods here this is only a suggestion! This is your world, 
 * add members methods as you wish
 */
public class Scene implements IInitable {
	
	private final static int RED = 0;
	private final static int GREEN = 1;
	private final static int BLUE = 2;
	
	private final static String ERR_MISSING_BACKGROUND_TEXTURE = "Error: No background-tex given";
	private final static String ERR_MAX_RECURSION_NOT_A_NUM = "Error: max-recursion-level value must "
			+ "be a positive integer";
	private static final String ERR_COLOR_CODE = "Error: Must provide a RED (0), GREEN (1),"
			+ " or BLUE (2) color code only.";

	private List<Surface> mSurfaces;
	private List<Light> mLights;
	private Camera mCamera;
	
	private Vec mBackgroundColor; // v1 is red, v2 green and v3 blue
	private String mBackgroundTexture;
	private int mMaxRecursionLevel;
	private Vec mAmbientLight;


	public Scene() {
		 mSurfaces = new ArrayList<Surface>();
		 mLights = new ArrayList<Light>();
		 mCamera = new Camera();
	}

	public void init(Map<String, String> attributes) {
	
		// Check attributes validity and populate the members
		if (attributes.containsKey("background-col")) {
			mBackgroundColor = new Vec(attributes.get("background-col"));
		} else {
			mBackgroundColor = new Vec(0, 0, 0);
		}
		
		if (attributes.containsKey("background-tex")) {
			mBackgroundTexture = attributes.get("background-tex");
		} else {
			// TODO: Add default background texture?
//			throw new IllegalArgumentException(ERR_MISSING_BACKGROUND_TEXTURE);
			mBackgroundTexture = "";
		}
		
		if (attributes.containsKey("max-recursion-level")) {
			try {
				mMaxRecursionLevel = Integer.parseInt(attributes.get("max-recursion-level"));
				
				if (mMaxRecursionLevel < 1) {
					throw new IllegalArgumentException(ERR_MAX_RECURSION_NOT_A_NUM);
				}
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException(ERR_MAX_RECURSION_NOT_A_NUM);
			}
		} else {
			mMaxRecursionLevel = 10;
		}
		
		if (attributes.containsKey("ambient-light")) {
			mAmbientLight = new Vec(attributes.get("ambient-light"));
		} else {
			mAmbientLight = new Vec(0, 0, 0);
		}
		// End populate members
	}

	/**
	 * Send ray return the nearest intersection. Return null if no intersection
	 * 
	 * @param ray
	 * @return the closest intersection point to the ray
	 */
	public Point3D findIntersection(Ray ray) {
		//TODO find ray intersection with scene, change the output type, add whatever you need
		double minDistance = Double.MAX_VALUE;
		Surface closestSurface = null;
		Point3D closestIntersection = null;
		
		int numOfSurfaces = mSurfaces.size();
		
		// Loop through all surfaces in the scene
		for (int i = 0; i < numOfSurfaces; i++) {
			Surface surface = mSurfaces.get(i);
			
			// Find intersection point with each of them
			Point3D intersection = surface.intersect(ray);
			
			// If there's an intersection, calculate it's distance from the ray's
			// origin and return the closest one
			if (intersection != null) {
				double distance = intersection.distance(ray.mOriginPoint);
				
				if (distance < minDistance) {
					minDistance = distance;
					closestSurface = surface;
					closestIntersection = intersection;
				}
			}
		}
		
		return closestIntersection;
	}

	public Vec calcColor(Ray ray, int level) {
		//TODO implement ray tracing recursion here, add whatever you need
		for (int i = 0; i < 3; i ++) {
			// Once for each of RED, GREEN, BLUE
		}
		return null;
	}
	
	private double calcEmissionColor(Surface surface, int color) {
		return surface.getEmissionColor(color);
	}
	
	private double calcAmbientColor(Surface surface, int color) {
		if (color == RED) {
			return mAmbientLight.x * surface.getAmbientColor(RED);
		} else if (color == GREEN) {
			return mAmbientLight.y * surface.getAmbientColor(GREEN);
		} else if (color == BLUE) {
			return mAmbientLight.z * surface.getAmbientColor(BLUE);
		} else {
			throw new IllegalArgumentException(ERR_COLOR_CODE);
		}
	}
	
	private double calcDiffuseLight(Point3D hit, Light light, int color) {
		return 0;
	}
	
	private double calcSpecularLight(Point3D hit, Light light, int color) {
		return 0;
	}

	/**
	 * Add objects to the scene by name
	 * 
	 * @param name Object's name
	 * @param attributes Object's attributes
	 */
	public void addObjectByName(String name, Map<String, String> attributes) {		
		Surface surface = null;
		Light light = null;
	
		// Check if element is a surface. If yes, initialize the corresponding object
		if ("sphere".equals(name)) {
			surface = new Sphere();
		} else if ("disc".equals(name)) {
			surface = new Disc();
		} else if ("convexpolygon".equals(name)) {
			surface = new ConvexPolygon();
		} else if ("triangle".equals(name)) {
			surface = new Triangle();
		}
		
		// Check if element is a light. If yes, initialize the corresponding object
		if ("omni-light".equals(name)) {
			light = new OmniLight();
		} else if ("dir-light".equals(name)) {
			light = new DirectionalLight();
		} else if ("spot-light".equals(name)) {
			light = new SpotLight();
		}

		//adds a surface to the list of surfaces
		if (surface != null) {
			surface.init(attributes);
			mSurfaces.add(surface);
		}
		
		// adds a light to the list of lights
		if (light != null) {
			light.init(attributes);
			mLights.add(light);
		}

	}

	public void setCameraAttributes(Map<String, String> attributes) {
		mCamera.init(attributes);
	}
	
	public Camera getCamera() {
		return mCamera;
	}
}
