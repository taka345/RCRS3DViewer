package render.area.draw;

import main.ViewerConfig;
import processing.core.PApplet;
import processing.core.PImage;
import render.effect.AreaEffect;

public class BuildingRender {
	RoadRender roadrender;

	public  BuildingRender() {
		this.roadrender = new RoadRender();
	}

	public void drawBuilding(PApplet applet, float[] nords, float x, float y,
			float areaHeight, boolean[] passable, float bHeight, int areaScale,
			int scale, int areaName, int REFUGE, PImage icon, PImage[] img,
			ViewerConfig config) {
		applet.fill(50);
		applet.stroke(200);
		switch (config.getDetail()) {
		case ViewerConfig.HIGH:
			createBuilding(applet, nords, bHeight, scale, areaName, passable,
					REFUGE, img, config);
			
			if(this.roadrender == null)
				System.out.println("Null!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");
				this.roadrender.drawRoad(applet, areaName, REFUGE, nords);

			if (config.getFlag("Icon") && icon != null) {
				AreaEffect.drawAreaIcon(applet, x, y, areaHeight, areaScale,
						icon, config);
			}
			break;
		case ViewerConfig.LOW:
			createBuilding(applet, nords, bHeight, scale, areaName, passable,
					REFUGE, img, config);
			this.roadrender.drawRoad(applet, areaName, REFUGE, nords);
			if (config.getFlag("Icon") && icon != null) {
				AreaEffect.drawAreaIcon(applet, x, y, areaHeight, areaScale,
						icon, config);
			}
			break;
		default:
			// bottom
			this.roadrender.drawRoad(applet, areaName, REFUGE, nords);
			if (config.getFlag("Icon") && icon != null) {
				AreaEffect.drawAreaIcon(applet, x, y, areaHeight, areaScale,
						icon, config);
			}
			break;
		}
	}

	public void createBuilding(PApplet applet, float[] nords, float bHeight,
			int scale, int areaName, boolean[] passable, int REFUGE,
			PImage[] img, ViewerConfig config) {
		if (areaName == REFUGE)
			return;
		int floorCount = (int) (bHeight / scale);//
		float buildDivideHeight = bHeight / floorCount;

		// build top
		applet.pushStyle();
		applet.pushMatrix();
		applet.translate(0, 0, bHeight);
		applet.noStroke();
		applet.beginShape();
		for (int i = 0; i < nords.length; i += 2) {
			applet.vertex(nords[i], nords[i + 1]);
		}
		applet.endShape();
		applet.popMatrix();
		// build side
		applet.pushMatrix();
		for (int cnt = 0; cnt < floorCount; cnt++) {
			for (int i = 0; i < nords.length - 2; i += 2) {
				applet.beginShape(applet.QUAD);
				if (!passable[i / 2]) {
					applet.texture(img[0]);
				} else {
					if (cnt == 0)
						applet.texture(img[1]);
					else
						applet.texture(img[0]);
				}

				applet.vertex(nords[i], nords[i + 1], buildDivideHeight * cnt,
						0, 1);
				applet.vertex(nords[i + 2], nords[i + 3], buildDivideHeight
						* cnt, 1, 1);
				applet.vertex(nords[i + 2], nords[i + 3], buildDivideHeight
						* cnt + buildDivideHeight, 1, 0);
				applet.vertex(nords[i], nords[i + 1], buildDivideHeight * cnt
						+ buildDivideHeight, 0, 0);
				applet.endShape();
			}
		}

		applet.popMatrix();
		applet.pushMatrix();
		applet.beginShape(applet.QUAD);
		for (int cnt = 0; cnt < floorCount; cnt++) {
			if (!passable[passable.length - 1]) {
				applet.texture(img[0]);
			} else {
				if (cnt == 0)
					applet.texture(img[1]);
				else
					applet.texture(img[0]);
			}
			applet.vertex(nords[0], nords[1], buildDivideHeight * cnt, 0, 1);
			applet.vertex(nords[nords.length - 2], nords[nords.length - 1],
					buildDivideHeight * cnt, 1, 1);
			applet.vertex(nords[nords.length - 2], nords[nords.length - 1],
					buildDivideHeight * cnt + buildDivideHeight, 1, 0);
			applet.vertex(nords[0], nords[1], buildDivideHeight * cnt
					+ buildDivideHeight, 0, 0);
		}
		applet.endShape();
		applet.popMatrix();
		applet.popStyle();
	}
}
