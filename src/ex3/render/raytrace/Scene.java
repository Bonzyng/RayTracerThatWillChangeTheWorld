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
	
	private final static double AIR_REFRACTIVE_INDEX = 1.000293;

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

	public Vec calcColor(Ray ray, int level) {
		
		if (level == mMaxRecursionLevel) {
			return new Vec(0, 0, 0);
		}
		
		Intersection intersect = findIntersection(ray);
		
		Vec rgb = new Vec(mBackgroundColor);
		
//		Color rgb = new Color((float) mBackgroundColor.x, (float) mBackgroundColor.y, (float) mBackgroundColor.z);
		
		if (intersect.getHit() != null) {
			int numOfLights = mLights.size();
			
			rgb = Vec.add(calcEmissionColor(intersect.getSurface()), calcAmbientColor(intersect.getSurface()));
			
			for (int i = 0; i < numOfLights; i++) {
				Light light = mLights.get(i);
				
				Ray rayToLight = constructRayToLight(intersect.getHit(), light);
				
				// Check for shadows
				Intersection lightOccluded = findIntersection(rayToLight);
				if (lightOccluded.getHit() == null) {
					rgb.add(calcDiffuseColor(intersect, light));
					rgb.add(calcSpecularColor(intersect, light, ray));
				}
			}
			
			ensureColorValuesLegal(rgb);						
		}
		
		return rgb;
	}
	
	// Used to check if the light to an intersection point is blocked by another
	// object or not
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
	
	// Used to create the reflective ray
	private Ray constructOutRay(Ray ray, Vec normal, Point3D hit) {
		Vec newRayDirection = ray.mDirectionVector.reflect(normal);
		return new Ray(hit, newRayDirection);
	}
	
	private Ray constructThroughRay(Ray ray, Vec normal, Intersection intersect) {
		
		// setting the reflective indices for the material the ray is coming to (i) 
		// to the material of the object it is intersecting (r)
		double reflectiveIndexI = AIR_REFRACTIVE_INDEX;
		double reflectiveIndexR = intersect.getSurface().getRefractiveIndex();
		
		// L is the vector from the hit point to the origin of the ray
		Vec L = Vec.negate(ray.mDirectionVector);
		
		double fractionOfReflectiveIndecies = reflectiveIndexI / reflectiveIndexR;
		
		// calculating the angle between the normal and the vector from hit point to ray
		// origin (L) (eta_i), and the angle between the normal and the new 
		// ray constructed to go through the surface
		double eta_i = Vec.angle(normal, L);
		double eta_r = Math.sinh(fractionOfReflectiveIndecies * Math.sin(eta_i));
		
		// calculating the direction vector of the new ray that goes through the surface
		// see Lec 3 p.68 (snell's law)
		Vec newNormal = Vec.scale((fractionOfReflectiveIndecies * Math.cos(eta_i)) - Math.cos(eta_r), normal);
		Vec T = Vec.sub(newNormal, Vec.scale(fractionOfReflectiveIndecies, L));
		
		// construct the new ray
		Ray throughRay = new Ray(intersect.getHit(), T);
		
		// finding the intersection between the new ray and the exit point of the
		// surface
		Intersection outIntersection = findIntersection(throughRay);
		
		// returning the ray constructed on the other side of the surface
		// using the original ray's direction vector and the exit point 
		// from the surface
		return new Ray(outIntersection.getHit(), ray.mDirectionVector);
	}

	
	
	
	private Vec calcEmissionColor(Surface surface) {
		return surface.getEmissionColor();
	}
	
	private Vec calcAmbientColor(Surface surface) {
		return Vec.scale(mAmbientLight, surface.getAmbientColor());
	}
	
	private Vec calcDiffuseColor(Intersection intersection, Light light) {
		Vec diffuseIntensity;
		
		// All the equation factors
		Vec lightIntensity;
		Vec diffuseCoefficient;
		Vec vecToLight;
		Vec normalAtHit;
		
		// According to the type of light calculate the direction to it and it's
		// intensity
		if (light.getClass() == DirectionalLight.class) {
			DirectionalLight dLight = (DirectionalLight) light;
			vecToLight = dLight.getDirection();
			vecToLight.negate();
			lightIntensity = dLight.getLightIntensity();
			
		} else if (light.getClass() == OmniLight.class) {
			OmniLight oLight = (OmniLight) light;
			vecToLight = Point3D.getVec(intersection.getHit(), oLight.getPosition());
			lightIntensity = oLight.getLightIntensity(vecToLight.length());
			
		} else { // SpotLight
			SpotLight sLight = (SpotLight) light;
			vecToLight = Point3D.getVec(intersection.getHit(), sLight.getPosition());
			lightIntensity = sLight.getLightIntensity(vecToLight.length(), intersection.getHit());
		}
		
		normalAtHit = intersection.getSurface().getNormalAtPoint(intersection.getHit());
		
		// Ensure both the vector to the light and the normal at the point are normalized
		vecToLight.normalize();
		normalAtHit.normalize();
		
		diffuseCoefficient	= intersection.getSurface().getDiffuseColor();
		
		// Calculate the diffusive light according to the formula from the lecture (Lec03, slide 44)
		diffuseIntensity = Vec.scale(diffuseCoefficient, lightIntensity);
		diffuseIntensity.scale(normalAtHit.dotProd(vecToLight));
		
		return diffuseIntensity;
	}
	
	private Vec calcSpecularColor(Intersection intersection, Light light, Ray ray) {
		Vec specularIntensity;
		
		// All the equation factors
		Vec vecToRayOrigin;
		Vec vecToLight;
		Vec vecToLightReflection;
		Vec normalAtHit;
		Vec lightIntensity;
		Vec specularCoefficient;
		double shininess;
		
		// According to the type of light calculate the direction to it and it's
		// intensity
		if (light.getClass() == DirectionalLight.class) {
			DirectionalLight dLight = (DirectionalLight) light;
			vecToLight = dLight.getDirection();
			vecToLight.negate();
			lightIntensity = dLight.getLightIntensity();
			
		} else if (light.getClass() == OmniLight.class) {
			OmniLight oLight = (OmniLight) light;
			vecToLight = Point3D.getVec(intersection.getHit(), oLight.getPosition());
			lightIntensity = oLight.getLightIntensity(vecToLight.length());
			
		} else { // SpotLight
			SpotLight sLight = (SpotLight) light;
			vecToLight = Point3D.getVec(intersection.getHit(), sLight.getPosition());
			lightIntensity = sLight.getLightIntensity(vecToLight.length(), intersection.getHit());
		}
		
		normalAtHit = intersection.getSurface().getNormalAtPoint(intersection.getHit());
		
		// Ensure vectors are normalized
		normalAtHit.normalize();
		vecToLight.normalize();
		
		vecToRayOrigin = Point3D.getVec(intersection.getHit(), ray.mOriginPoint);

		vecToLightReflection = vecToLight.reflect(normalAtHit);
		vecToLightReflection.normalize();
		vecToLightReflection.negate();
		vecToRayOrigin.normalize();
		
		specularCoefficient = intersection.getSurface().getSpecularColor();
		
		shininess = intersection.getSurface().getShininess();
		
		double dotProd = vecToRayOrigin.dotProd(vecToLightReflection);
		
		if (dotProd < 0) {
			dotProd = 0;
		}
		
		//specularCoefficient * Math.pow(dotProd, shininess) * lightIntensity;

		specularIntensity = Vec.scale(Math.pow(dotProd, shininess), specularCoefficient);
		specularIntensity.scale(lightIntensity);
		
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
	
	private void ensureColorValuesLegal(Vec rgb) {
		if (rgb.x > 1) {
			rgb.x = 1;
		}
		if (rgb.y > 1) {
			rgb.y = 1;
		}
		if (rgb.z > 1) {
			rgb.z = 1;
		}
		
		if (rgb.x < 0) {
			rgb.x = 0;
		}
		if (rgb.y < 0) {
			rgb.y = 0;
		}
		if (rgb.z < 0) {
			rgb.z = 0;
		}
	}
}
