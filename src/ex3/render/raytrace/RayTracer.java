package ex3.render.raytrace;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import math.Point3D;
import math.Ray;
import math.Vec;
import e3.utils.Intersection;
import ex3.parser.Element;
import ex3.parser.SceneDescriptor;
import ex3.render.IRenderer;

public class RayTracer implements IRenderer {

	private Scene mScene;
	
	/**
	 * Inits the renderer with scene description and sets the target canvas to
	 * size (width X height). After init renderLine may be called
	 * 
	 * @param sceneDesc
	 *            Description data structure of the scene
	 * @param width
	 *            Width of the canvas
	 * @param height
	 *            Height of the canvas
	 * @param path
	 *            File path to the location of the scene. Should be used as a
	 *            basis to load external resources (e.g. background image)
	 */
	@Override
	public void init(SceneDescriptor sceneDesc, int width, int height, File path) {
		// TODO Implement this
		//you can initialize your scene object here
		mScene = new Scene();
		
		mScene.init(sceneDesc.getSceneAttributes());
		
		for (Element e : sceneDesc.getObjects()) {
			mScene.addObjectByName(e.getName(), e.getAttributes());
		}
		
		mScene.setCameraAttributes(sceneDesc.getCameraAttributes());
		
	}

	/**
	 * Renders the given line to the given canvas. Canvas is of the exact size
	 * given to init. This method must be called only after init.
	 * 
	 * @param canvas
	 *            BufferedImage containing the partial image
	 * @param line
	 *            The line of the image that should be rendered.
	 */
	@Override
	public void renderLine(BufferedImage canvas, int line) {
		int canvasWidth = canvas.getWidth();
		int canvasHeight = canvas.getHeight();
		for (int x = 0; x < canvasWidth; x++) {
			if (line == canvasHeight / 2 && x == canvasWidth / 2) {
				System.out.println("Hi stop here");
			}
			Ray ray = mScene.getCamera().constructRayThroughPixel(x, line, canvasHeight, canvasWidth);
			Color color = mScene.calcColor(ray, 0);
//			int rgb = ((int) color.x << 32) | ((int) color.y << 16) | (int) color.z;
			canvas.setRGB(x, line, color.getRGB());
		}
	}

}
