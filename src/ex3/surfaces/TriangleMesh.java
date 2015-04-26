package ex3.surfaces;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

import math.Plane;
import math.Point3D;
import math.Ray;
import math.Vec;

public class TriangleMesh extends Surface {

	public ArrayList<Triangle> mTriangles;

	public void init(Map<String, String> attributes) {
		super.init(attributes);
		
		mTriangles = new ArrayList<Triangle>();
		
		int i = 0;
		String triangle = "tri" + i ;
		while (attributes.containsKey(triangle)) {
			Triangle triangle1 = new Triangle(createTrianglesFromString((attributes.get(triangle))));
			
			mTriangles.add(triangle1);
			i++;
			triangle = "tri" + i;
		}
	}
	
	private Point3D[] createTrianglesFromString(String triangle) {
		double x;
		double y;
		double z;
		
		Point3D[] vertices = new Point3D[3];
		Scanner s = new Scanner(triangle);
		
		//reading the values of the three different points of the triangle
		//and creating new points accordingly
		for (int i = 0; i < 3; i++) {
			x = s.nextDouble();
			y = s.nextDouble();
			z = s.nextDouble();
			
			vertices[i] = new Point3D(x, y, z);
		}
		s.close();
		
		return vertices;
	}
	
	@Override
	public Point3D intersect(Ray iRay) {
		Point3D closestIntersection = null;
		double minDistance = Double.MAX_VALUE;
		
		int numOfTriangles = mTriangles.size();
		
		//going over all the triangles, and for each triangle we check if the ray intersects
		//with it, and then we make sure that we take the closest intersection
		for (int i = 0; i < numOfTriangles; i++) {
			Surface triangle = mTriangles.get(i);
			Point3D intersection = triangle.intersect(iRay);
			
			//found intersection
			if (intersection != null) {
				double distance = intersection.distance(iRay.mOriginPoint);
				
				//updating the closest intersection
				if (distance < minDistance && distance > EPSILON) {
					minDistance = distance;
					closestIntersection = intersection;
				}			
			}
		}		
		return closestIntersection;
	}

	@Override
	public Vec getNormalAtPoint(Point3D point) {
		Triangle triangle = null;
		int numOfTriangles = mTriangles.size();

		//go over every triangle to find the triangle that has the point on it
		for (int i = 0; i < numOfTriangles; i++) {
			Triangle currTriangle = mTriangles.get(i);
			
			//findTriangle is a method to find whether point is on triangle
			if (findTriangle(currTriangle, point)) {
				triangle = currTriangle;
				break;
			}
		}
		
		//the normal is being calculate for the specific triangle using triangle class
		Vec normal = triangle.getNormalAtPoint(point);
		return normal;
	}
	
	/**
	 * Finding whether the point is on the triangle
	 * @param triangle
	 * @param point
	 * @return true if it is, false if it isn't
	 */
	private boolean findTriangle(Triangle triangle, Point3D point) {
		Point3D p0 = triangle.mVertices.get(0);
		Point3D p1 = triangle.mVertices.get(1);
		Point3D p2 = triangle.mVertices.get(2);
		
		Plane trianglePlane = new Plane(p0, p1, p2);
		
		//checking whether the point is on the same plane as triangle
		if (!trianglePlane.checkPointOnPlane(point)) {
			return false;
		}
		
		//using sameSide method to find out whether the point is inside the triangle
		if (sameSide(point, p0, p1, p2) && sameSide(point, p1, p0, p2) && sameSide(point, p2, p0, p1)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Checks whether point is inside triangle (on the right side of the edge)
	 * further info in - http://www.blackpawn.com/texts/pointinpoly/
	 * @param point
	 * @param p0
	 * @param p1
	 * @param p2
	 * @return true if on the same side, false otherwise
	 */
	private boolean sameSide(Point3D point, Point3D p0, Point3D p1, Point3D p2) {
		Vec crossProd1 = Vec.crossProd(Point3D.getVec(p1, p2), Point3D.getVec(p1, point));
		crossProd1.normalize();
		Vec crossProd2 = Vec.crossProd(Point3D.getVec(p1, p2), Point3D.getVec(p1, p0));
		crossProd2.normalize();
		
		if (Vec.dotProd(crossProd1, crossProd2) >= 0) {
			return true;
		} 
		
		return false;
	}
}
