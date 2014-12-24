package loader;

import java.io.File;

import saito.objloader.*;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class OBJLoader {
	  private OBJModel model;
	  private PApplet applet;
	  private PVector modelSize = new PVector(0,0,0);

	  public OBJLoader(PApplet applet, String objPathName) {
	    this.applet = applet;
	    try {
	      this.model = new OBJModel(this.applet);
	      this.model.load(objPathName);
	      this.model.translateToCenter();
	      //this.model.setDrawMode(applet.QUAD);
	      this.model.setDrawMode(applet.POLYGON);
	      model_size(this.model);
	      applet.println("successful 3dmodel!");
	    } catch(NullPointerException npe) {
	      npe.printStackTrace();
	    }
	  }

	  public void draw(float x, float y, float rotateX, float rotateY, float rotateZ, float scale) {
	    if(this.model != null) {
	      applet.pushStyle();
	      applet.ambient(255,255,255);
	      applet.pushMatrix();
	      applet.translate(x, y, 5);
	      applet.scale(10);
	      applet.rotateX(rotateX);
	      applet.rotateY(rotateY);
	      applet.rotateZ(rotateZ);
	      model.setDrawMode(applet.POLYGON);
	      model.draw();
	      applet.popMatrix();
	      applet.popStyle();
	    }
	  }

	  void model_size(OBJModel model){
	    int numberOfVerts = model.getVertexCount();
	    PVector min = new PVector(applet.MAX_FLOAT, applet.MAX_FLOAT, applet.MAX_FLOAT);
	    PVector max = new PVector(applet.MIN_FLOAT, applet.MIN_FLOAT, applet.MIN_FLOAT);
	    PVector v;

	    for (int i = 0; i < numberOfVerts; i++) {
	      v = model.getVertex(i);
	        if(v.x < min.x) {min.x = v.x;}
	        if(v.x > max.x) {max.x = v.x;}
	        if(v.y < min.y) {min.y = v.y;}
	        if(v.y > max.y) {max.y = v.y;}
	        if(v.z < min.z) {min.z = v.z;}
	        if(v.z > max.z) {max.z = v.z;}
	    }

	    modelSize.x = (float) Math.sqrt((max.x - min.x) * (max.x - min.x));
	    modelSize.y = (float) Math.sqrt((max.y - min.y) * (max.y - min.y));
	    modelSize.z = (float) Math.sqrt((max.z - min.z) * (max.z - min.z));
	  }
	}