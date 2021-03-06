package render.information.graph;

import processing.core.PApplet;

public class CreateLineChart {

	private PApplet applet;
	private Scrollbar bar;

	private double[] data;
	private int time = 0;

	private int vMax = 0;
	private int vMin = 0;

	private int labelSize;// ã©ãã«ã®æ?å­ã?®å¤§ãã
	private int costSize;// 0ã¨ã?1ã®æ°å­ã?®å¤§ãã

	private int xScale;// xè»¸æ¹åã?®å¤ã®å?å²æ°
	private int yScale;// yè»¸æ¹åã?®å¤ã®å?å²æ°

	private int showDisplayData;

	private String xName; // xè»¸æ¹åã?®ã©ãã«
	private String yName; // yè»¸æ¹åã?®ã©ãã«
	private String title;

	public CreateLineChart(PApplet applet) {
		this.applet = applet;
		this.defaultApp();
	}

	public void defaultApp() {
		// this.bar = bar = new Scrollbar(this.applet,width - width / 2.0,
		// height / 12, 200, 10, 10, 50);

		this.vMax = 100;
		this.vMin = 0;
		this.labelSize = 13;
		this.costSize = 10;
		this.xScale = 12;
		this.yScale = 10;
		this.showDisplayData = this.xScale;

		this.xName = "xxx";
		this.yName = "yyy";
		this.title = "test";

		this.data = new double[0];
	}

	public void setVMax(double[] data) {
		double max = 0;
		for (int i = 0; i < data.length; i++) {
			if (max < data[i]) {
				max = data[i];
			}
		}
		this.vMax = (int) max;
	}

	public void setData(double[] data, int time) {
		try {
			int dataSize = 0;
			for (int i = 0; i < data.length; i++) {
				if (data[i] != -1) {
					dataSize++;
				}
			}
			this.data = new double[dataSize - 1];
			for (int i = 0; i < this.data.length; i++) {
				this.data[i] = data[i];
			}
			/*
			 * List<Double> list = new ArrayList(); for(int i = 0; i <
			 * data.length; i++) { list.add(data[i]); } this.data = new
			 * double[list.size()]; for(int i = 0; i < list.size(); i++) {
			 * this.data[i] = list.get(i); }
			 */
			this.time = time;
		} catch (Exception e) {
			// System.out.println("data is null");
		}
	}

	public void setScale(int x, int y) {
		this.xScale = x;
		this.yScale = y;
	}

	public void setShowDisplayData(int s) {
		this.showDisplayData = s;
	}

	public void setMaxMin(int max, int min) {
		this.vMax = max;
		this.vMin = min;
	}

	public void setLabelSize(int label, int cost) {
		this.labelSize = label;
		this.costSize = cost;
	}

	public void setTittle(String tit) {
		this.title = tit;
	}

	public void setLabelName(String xLabel, String yLabel) {
		this.xName = xLabel;
		this.yName = yLabel;
	}

	public void draw(int w, int h) {// ã°ã©ãã?®æç»
		setShowDisplayData(this.xScale);
		this.drawScene(w, h);
		this.drawDataLine(w, h);
	}

	public void draw(double[] data, int time, int w, int h) {// ã°ã©ãã?®æç»

		// bar.update();
		// bar.display();

		// setScale((int)bar.getPos(), 10);
		setShowDisplayData(this.xScale);
		// setShowDisplayData((int)bar.getPos() / 2);

		this.setData(data, time);
		this.drawScene(w, h);
		this.drawDataLine(w, h);
	}

	// ç·ã§ã?ã¼ã¿ãçµã?¶
	public void drawDataLine(int w, int h) {
		int behind = 1;
		applet.strokeWeight(2);
		applet.noFill();
		applet.stroke(0, 0, 255);

		applet.beginShape();
		if (data.length > 0) {
			if (data.length <= showDisplayData) {
				for (int row = 0; row < data.length; row++) {
					float x = row * (w - w / 4) / xScale + w / 8;
					float y = h - h / 8 - (float) (data[row] - vMin)
							* (h - h / 4) / (vMax - vMin);
					if (data[row] < vMin) {
						applet.vertex(x, h - h / 8);
					} else if (data[row] > vMax) {
						applet.vertex(x, h / 8);
					} else {
						applet.vertex(x, y);
					}
				}
			} else {
				for (int row = showDisplayData; row >= 0; row--) {
					float x = row * (w - w / 4) / xScale + w / 8;
					float y = h - h / 8
							- (float) (data[data.length - behind] - vMin)
							* (h - h / 4) / (vMax - vMin);
					if (data[data.length - behind] < vMin) {
						applet.vertex(x, h - h / 8);
					} else if (data[data.length - behind] > vMax) {
						applet.vertex(x, h / 8);
					} else {
						applet.vertex(x, y);
					}
					behind++;
				}
			}
		}
		applet.endShape();
	}

	public void drawScene(int w, int h) {
		applet.fill(255);
		applet.rectMode(applet.CORNERS);
		applet.noStroke();
		applet.rect(w / 8, h / 8, w - w / 8, h - h / 8);

		drawTittle(w, h);
		drawAxisLabels(w, h);
		drawTimeLabel(this.time, w, h);
		drawYLabel(vMax, vMin, w, h);
	}

	public void drawTittle(int w, int h) {
		applet.fill(0);
		applet.textSize(20);
		applet.textAlign(applet.LEFT);

		applet.text(title, w / 8, h / 9);
	}

	public void drawAxisLabels(int w, int h) {
		applet.fill(0);
		applet.textSize(labelSize);
		applet.textLeading(15);

		applet.textAlign(applet.LEFT);
		applet.text(yName, w - (w / 16) + 20, h / 2);
		applet.textAlign(applet.CENTER);
		applet.text(xName, w / 2, h - (h / 10) + labelSize + 5);
	}

	public void drawTimeLabel(int time, int w, int h) {
		applet.fill(0);
		applet.textSize(costSize);
		applet.textAlign(applet.CENTER);

		applet.stroke(224);
		applet.strokeWeight(1);

		for (int row = 0; row <= xScale; row++) {
			float x = row * (w - w / 4) / xScale + w / 8;
			if (row > 0) {
				applet.line(x, h / 8, x, h - h / 8);
			}
			String str = "";
			if (data.length == 0) {
				str = "" + (time + row);
			} else if (data.length <= showDisplayData) {
				str = "" + (time + row - (data.length - 1));
			} else {
				str = "" + (time + row - showDisplayData);
			}
			applet.text(str, x, h - h / 8 + costSize * 1);
		}
	}

	public void drawYLabel(int vMax, int vMin, int w, int h) {
		applet.fill(0);
		applet.textSize(costSize);
		applet.textAlign(applet.RIGHT);

		applet.stroke(0);
		applet.strokeWeight(1);

		for (int row = 0; row <= yScale; row++) {
			float y = (-1) * row * (h - h / 4) / yScale + h - h / 8;
			double g = (double) row / yScale;
			String str = String.format("%.0f", vMin + (vMax - vMin) * g);
			applet.text(str, w / 8 - costSize, y + 2);
			if (row != yScale)
				applet.line(w / 8, y, w / 7, y);
		}
	}
}

class Scrollbar {
	private PApplet applet;
	float x, y; // ãã?¼ã®ä½ç½®
	float sw, sh; // ãã?¼ã®å¹?ã¨é«ã
	float pos; // ããã®ä½ç½®
	float posMin, posMax; // ããã®ä½ç½®ã®æ?å°å?¤ãæå¤§å¤
	boolean rollover; // ãã¦ã¹ãä¸ã«ããã°true
	boolean locked; // ã¹ã¯ã­ã¼ã«ãã?¼ãã¢ã¯ã?ã£ããªãtrue
	float minVal, maxVal; // ãã?¼ãè¿ãå¤ã®æ?å°ã?æå¤§

	// ã³ã³ã¹ãã©ã¯ã¿
	Scrollbar(PApplet applet, float xp, float yp, float w, float h, float miv,
			float mav) {
		this.applet = applet;
		x = xp;
		y = yp;
		sw = w;
		sh = h;
		minVal = miv;
		maxVal = mav;
		pos = x + sw / 2 - sh / 2;
		posMin = x;
		posMax = x + sw - sh;
	}

	// ãã?¼ã®ä¸ã«ãã¦ã¹ãããã?ã?ã¬ã¹ããã¦ã?ããªãã?ããã?®ä½ç½®ãè¨ç®?
	void update() {
		if (over() == true) {
			rollover = true;
		} else {
			rollover = false;
		}
		if (applet.mousePressed && rollover) {
			locked = true;
		} else {
			locked = false;
		}
		if (locked) {
			pos = applet.constrain(applet.mouseX - sh / 2, posMin, posMax);
		}
	}

	// ãã¦ã¹ããã¼ã®ä¸ã«ããã°ãtrueãè¿ã
	boolean over() {
		if ((applet.mouseX > x) && (applet.mouseX < x + sw)
				&& (applet.mouseY > y) && (applet.mouseY < y + sh)) {
			return true;
		} else {
			return false;
		}
	}

	// ãã?¼ãæã?
	void display() {
		applet.fill(255);
		applet.stroke(0);

		applet.rectMode(applet.CORNER);
		applet.rect(x, y, sw, sh);
		if (rollover || locked) {
			applet.fill(0);
		} else {
			applet.fill(102);
		}
		applet.rect(pos, y, sh, sh);
	}

	// ããã®ä½ç½®ãè¿ã
	float getPos() {
		float scalar = sw / (sw - sh);
		float ratio = (pos - x) * scalar;
		float offset = minVal + (ratio / sw * (maxVal - minVal));
		return offset;
	}
}