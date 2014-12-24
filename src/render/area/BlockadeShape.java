package render.area;

import processing.core.PApplet;

import java.applet.Applet;
import java.awt.geom.Rectangle2D;

import loader.OBJLoader;
import main.InformationManager;
import main.ViewerConfig;
import render.agent.EntityShape;
import render.area.render.BlockadeRender;
import render.effect.AreaEffect;
import rescuecore2.misc.gui.ScreenTransform;
import rescuecore2.worldmodel.Entity;
import rescuecore2.worldmodel.EntityID;
import rescuecore2.standard.entities.Blockade;

public class BlockadeShape implements EntityShape {
	private int id;
	private int scale;
	private int[] apexes;

	private float centerX;
	private float centerY;

	private float height;

	private OBJLoader blockades;

	private float minX;
	private float minY;
	private float maxX;
	private float maxY;

	private float bTotalArea;
	private int[] sideMaxApexes = new int[2];

	private int blockadeScale;
	private int blockadeNum;
	private float rotateY;

	private int repairCost;

	private BlockadeRender render;

	public BlockadeShape(Entity entity, ScreenTransform transform, int scale,
			OBJLoader blockade) {
		this.id = entity.getID().getValue();
		this.update(entity, transform);
		this.scale = scale;
		this.blockades = blockade;
		this.repairCost = 0;
		this.blockadeScale = 0;
		this.blockadeNum = 0;
		this.rotateY = 0;
		this.render = new BlockadeRender();
	}

	public void drawShape(int count, int animationRate, PApplet applet,
			ViewerConfig config) {
		if (!config.getFlag("Blockade") || apexes == null)
			return;

		this.render
				.drawBlockade(applet, blockades, apexes, blockadeNum, centerX,
						centerY, rotateY, blockadeScale, sideMaxApexes, config);
	}

	public int update(Entity entity, ScreenTransform transform) {
		Blockade b = (Blockade) entity;
		int[] apexes = b.getApexes();
		this.apexes = apexes;
		for (int i = 0; i < apexes.length; i += 2) {
			this.apexes[i] = transform.xToScreen(apexes[i]);
			this.apexes[i + 1] = transform.yToScreen(apexes[i + 1]);
		}
		Rectangle2D bounds = b.getShape().getBounds2D();
		this.centerX = transform.xToScreen(bounds.getCenterX());
		this.centerY = transform.yToScreen(bounds.getCenterY());
		this.repairCost = b.getRepairCost();

		minmaxXY();
		maxSide();

		this.blockadeScale = setBlockadeScale();
		this.blockadeNum = setBlockadeNum();
		this.rotateY = setRotateY();

		this.height = (transform.xToScreen(bounds.getWidth()) * transform
				.yToScreen(bounds.getHeight())) / 80000;

		return InformationManager.NO_CHANGE;
	}

	public int getID() {
		return this.id;
	}

	private float[] compare(float a, float b) {
		float[] result = new float[2];
		if (a > b) {
			result[0] = a;
			result[1] = b;
		} else {
			result[0] = b;
			result[1] = a;
		}

		return result;
	}

	private void minmaxXY() {
		for (int i = 0; i < apexes.length; i += 2) {
			if (i != 0) {
				if (this.minX > apexes[i])
					this.minX = apexes[i];
				if (this.minY > apexes[i + 1])
					this.minY = apexes[i + 1];
				if (this.maxX < apexes[i])
					this.maxX = apexes[i];
				if (this.maxY < apexes[i + 1])
					this.maxY = apexes[i + 1];
			} else {
				this.minX = apexes[i];
				this.minY = apexes[i + 1];
				this.maxX = apexes[i];
				this.maxY = apexes[i + 1];
			}
		}
	}

	private void maxSide() {
		float sideMax = 0;
		float side = 0;
		try {
			for (int i = 0; i < apexes.length - 2; i += 2) {
				if (i == apexes.length - 2) {
					side = ((apexes[i] - apexes[0]) * (apexes[i] - apexes[0]))
							+ ((apexes[i + 1] - apexes[1]) * (apexes[i + 1] - apexes[1]));
					if (sideMax < side) {
						sideMax = side;
						this.sideMaxApexes[0] = apexes.length - 2;
						this.sideMaxApexes[1] = 0;
					}
				} else if (i != 0) {
					side = ((apexes[i] - apexes[i + 2]) * (apexes[i] - apexes[i + 2]))
							+ ((apexes[i + 1] - apexes[i + 3]) * (apexes[i + 1] - apexes[i + 3]));
					if (sideMax < side) {
						sideMax = side;
						this.sideMaxApexes[0] = i;
						this.sideMaxApexes[1] = i + 2;
					}
				} else {
					sideMax = ((apexes[i] - apexes[i + 2]) * (apexes[i] - apexes[i + 2]))
							+ ((apexes[i + 1] - apexes[i + 3]) * (apexes[i + 1] - apexes[i + 3]));
					this.sideMaxApexes[0] = 0;
					this.sideMaxApexes[1] = 1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int setBlockadeScale() {
		if (repairCost <= 10)
			return 8;
		else if (repairCost <= 20)
			return 10;
		else if (repairCost <= 50)
			return 15;
		else
			return 25;
	}

	private int setBlockadeNum() {
		if (repairCost <= 35)
			return 1;
		else
			return 2;
	}

	private float setRotateY() {
		float numX, numY;
		numX = this.maxX - this.minX;
		numY = this.maxY - this.minY;
		float t = PApplet.atan(numY / numX);
		if (numX > numY)
			return 0;
		else
			return PApplet.PI / 2;
	}

	public int getRepairCost() {
		return this.repairCost;
	}
}
