package ex3.render.raytrace;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import math.Point3D;
import math.Ray;
import math.Vec;
import e3.utils.Intersection;
import e3.utils.eRGB;
import ex3.light.DirectionalLight;
import ex3.light.Light;
import ex3.light.OmniLight;
import ex3.light.SpotLight;
import ex3.surfaces.ConvexPolygon;
import ex3.surfaces.Disc;
import ex3.surfaces.Sphere;
import ex3.surfaces.Surface;
import ex3.surfaces.Triangle;
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
	public Intersection findIntersection(Ray ray) {
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
		
		Intersection intersection = new Intersection(closestIntersection, closestSurface);
		
		return intersection;
	}

	public Color calcColor(Ray ray, int level) {
		Intersection intersect = findIntersection(ray);
		
		double[] colors = new double[3];
		
		Color rgb = new Color((float) mBackgroundColor.x, (float) mBackgroundColor.y, (float) mBackgroundColor.z);
		
		if (intersect.getHit() != null) {
			int numOfLights = mLights.size();
			
			for (int i = 0; i < 3; i ++) {
				double colorValue;
				eRGB color = eRGB.values()[i];
				// Once for each of RED, GREEN, BLUE
				colorValue = calcEmissionColor(intersect.getSurface(), color) +
						calcAmbientColor(intersect.getSurface(), color);
				
				for (int j = 0; j < numOfLights; j++) {
					Light light = mLights.get(j);
					Ray rayToLight = constructRayToLight(intersect.getHit(), light);
					
					// Check for shadows
					Intersection lightOccluded = findIntersection(rayToLight);
					if (lightOccluded.getHit() == null) {
						colorValue += (calcDiffuseLight(intersect, light, color) +
								calcSpecularLight(intersect, light, color)) * light.getColor().getValue(color);
					}
				}
				
				if (colorValue > 1) {
					colorValue = 1;
				}
				
				if (colorValue < 0) {
					colorValue = 0;
				}
				colors[i] = colorValue;
			}
//			System.out.println(colors[0] + " " + colors[1] + " " + colors[2]);
			rgb = new Color((float) colors[0], (float) colors[1], (float) colors[2]);
		}
		return rgb;
	}
	
	private Ray constructRayToLight(Point3D hit, Light light) {
		if (light.getClass() == DirectionalLight.class) {
			DirectionalLight dLight = (DirectionalLight) light;
			
			return new Ray(hit, Vec.negate(dLight.getDirection()));
		} else if (light.getClass() == OmniLight.class) {
			OmniLight oLight = (OmniLight) light;
			
			return new Ray(hit, oLight.getPosition());
		} else {
			SpotLight sLight = (SpotLight) light;
			
			return new Ray(hit, sLight.getPosition());
		}
	}

	private double calcEmissionColor(Surface surface, eRGB color) {
		return surface.getEmissionColor(color);
	}
	
	private double calcAmbientColor(Surface surface, eRGB color) {
		if (color == eRGB.RED) {
			return mAmbientLight.x * surface.getAmbientColor(color);
		} else if (color == eRGB.GREEN) {
			return mAmbientLight.y * surface.getAmbientColor(color);
		} else  { // return BLUE
			return mAmbientLight.z * surface.getAmbientColor(color);
		}
	}
	
	private double calcDiffuseLight(Intersection intersection, Light light, eRGB color) {
		double diffuseIntensity;
		
		// All the equation factors
		double lightIntensity;
		double diffuseCoefficient;
		Vec vecToLight;
		Vec normalAtHit;
		
		// According to the type of light calculate the direction to it and it's
		// intensity
		if (light.getClass() == DirectionalLight.class) {
			DirectionalLight dLight = (DirectionalLight) light;
			vecToLight = dLight.getDirection();
			vecToLight.negate();
			lightIntensity = dLight.getLightIntensity(color);
			
		} else if (light.getClass() == OmniLight.class) {
			OmniLight oLight = (OmniLight) light;
			vecToLight = Point3D.getVec(intersection.getHit(), oLight.getPosition());
			lightIntensity = oLight.getLightIntensity(color, vecToLight.length());
			
		} else { // SpotLight
			SpotLight sLight = (SpotLight) light;
			vecToLight = Point3D.getVec(intersection.getHit(), sLight.getPosition());
			lightIntensity = sLight.getLightIntensity(color, vecToLight.length(), intersection.getHit());
		}
		
		normalAtHit = intersection.getSurface().getNormalAtPoint(intersection.getHit());
		
		// Ensure both the vector to the light and the normal at the point are normalized
		vecToLight.normalize();
		normalAtHit.normalize();
		
		diffuseCoefficient	= intersection.getSurface().getDiffuseColor(color);
		
		// Calculate the diffusive light according to the formula from the lecture (Lec03, slide 44)
		diffuseIntensity = diffuseCoefficient * (normalAtHit.dotProd(vecToLight)) * lightIntensity;
		
		return diffuseIntensity;
	}
	
	private double calcSpecularLight(Intersection intersection, Light light, eRGB color) {
		double specularIntensity;
		
		// All the equation factors
		Vec vecToEye;
		Vec vecToLight;
		Vec vecToLightReflection;
		Vec normalAtHit;
		double lightIntensity;
		double specularCoefficient;
		double shininess;
		
		// According to the type of light calculate the direction to it and it's
		// intensity
		if (light.getClass() == DirectionalLight.class) {
			DirectionalLight dLight = (DirectionalLight) light;
			vecToLight = dLight.getDirection();
			vecToLight.negate();
			lightIntensity = dLight.getLightIntensity(color);
			
		} else if (light.getClass() == OmniLight.class) {
			OmniLight oLight = (OmniLight) light;
			vecToLight = Point3D.getVec(intersection.getHit(), oLight.getPosition());
			lightIntensity = oLight.getLightIntensity(color, vecToLight.length());
			
		} else { // SpotLight
			SpotLight sLight = (SpotLight) light;
			vecToLight = Point3D.getVec(intersection.getHit(), sLight.getPosition());
			lightIntensity = sLight.getLightIntensity(color, vecToLight.length(), intersection.getHit());
		}
		
		normalAtHit = intersection.getSurface().getNormalAtPoint(intersection.getHit());
		
		// Ensure vectors are normalized
		normalAtHit.normalize();
		vecToLight.normalize();
		
		vecToEye = Point3D.getVec(intersection.getHit(), mCamera.getEye());
		vecToLightReflection = vecToLight.reflect(normalAtHit);
		vecToLightReflection.normalize();
		
		specularCoefficient = intersection.getSurface().getSpecularColor(color);
		
		shininess = intersection.getSurface().getShininess();
		
		specularIntensity = specularCoefficient * Math.pow(vecToEye.dotProd(vecToLightReflection), shininess) * lightIntensity;
		
		return specularIntensity;
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
