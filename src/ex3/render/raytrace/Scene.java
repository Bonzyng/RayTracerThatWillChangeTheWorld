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
	
	private final static String ERR_MISSING_BACKGROUND_TEXTURE = "Error: No background-tex given";
	private final static String ERR_MAX_RECURSION_NOT_A_NUM = "Error: max-recursion-level value must "
			+ "be a positive integer";

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
			throw new IllegalArgumentException(ERR_MISSING_BACKGROUND_TEXTURE);
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
	 * @return
	 */
	public void findIntersection(Ray ray) {
		//TODO find ray intersection with scene, change the output type, add whatever you need
	}

	public Vec calcColor(Ray ray, int level) {
		//TODO implement ray tracing recursion here, add whatever you need
		return null;
	}

	/**
	 * Add objects to the scene by name
	 * 
	 * @param name Object's name
	 * @param attributes Object's attributes
	 */
	public void addObjectByName(String name, Map<String, String> attributes) {
		//TODO this adds all objects to scene except the camera
		//here is some code example for adding a surface or a light. 
		//you can change everything and if you don't want this method, delete it
		
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
}
