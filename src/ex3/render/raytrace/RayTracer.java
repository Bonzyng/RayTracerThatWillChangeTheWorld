package ex3.render.raytrace;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import math.Ray;
import math.Vec;
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
		mScene = new Scene();
		mScene.setXMLPath(path.getParent());
		
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
			
//			if (line == 62 && x == 122) { // DEBUG HELPER
//				System.out.println("Half way there!");
//			}
//			System.out.println("x: " + x + " line: " + line);
			
			// get the super-sample level from the scene properties
			int superSampleFactor = mScene.getSuperSample();
			
			// Vector to hold the results from the super sampling process
			Vec[] superSamplers = new Vec[superSampleFactor * superSampleFactor];
			int k = 0;
			// Loop through the grid within each pixel
			for (int sampleY = 0; sampleY < superSampleFactor; sampleY++) {
				for (int sampleX = 0; sampleX < superSampleFactor; sampleX++) {
					// For each grid point, shoot a ray through it, calculate the color
					// and add to the vector array
					Ray superRay = mScene.getCamera().constructRayThroughPixelWithSuperSample(x, line, sampleX, sampleY, canvasWidth, canvasHeight, superSampleFactor);
					Vec color = mScene.calcColor(superRay, 0, x, line);
					mScene.ensureColorValuesLegal(color);
					superSamplers[k] = color;
					k++;
				}
			}
			// Get the average value from the vector array and color the pixel
			Vec color = Vec.getAverage(superSamplers);
			Color rgb = new Color((float) color.x, (float) color.y, (float) color.z);
			
			canvas.setRGB(x, line, rgb.getRGB());
		}
	}
	
	public Scene getScene() {
		return mScene;
	}
}
