package rescue3dviewer;

import java.awt.Color;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import rescuecore2.misc.gui.ScreenTransform;
import rescuecore2.worldmodel.Entity;

class CivilianShape extends HumanShape{
	String sayMessage;
	public CivilianShape(Entity entity, ScreenTransform transform, int scale)
	{
		super(entity,transform,scale);
		this.sayMessage = null;
	}
	  
	public void drawShape(int count, int animationRate, PApplet applet, ViewerConfig config)
	{
		if(!config.getFlag("Civilian")) return;
	    /*----------------------------------------
		 * Civilian say message
		 ---------------------------------------*/
	    if(config.getFlag("AgentEffect") && sayMessage != null) {
	    	applet.pushStyle();
	        applet.ambient(255,240,220);
	        applet.stroke(80);
	        applet.strokeWeight(3);
	        applet.pushMatrix();
	        applet.translate(moveX,moveY,markHeight +50);
	        applet.rotateZ((float)(Math.PI*2-config.getRoll()));
	        applet.rotateX((float)(Math.PI*2-config.getYaw()));
	        applet.textSize(20);
	        applet.textAlign(applet.CENTER, applet.TOP);
	        applet.beginShape();
	        applet.vertex(-30,30);
	        applet.vertex(-30,-30);
	        applet.vertex(50,-30);
	        applet.vertex(50,30);
	        applet.vertex(15,30);
	        applet.vertex(0,50);
	        applet.vertex(5,30);
	        applet.endShape();
	        applet.ambient(10,10,10);
	        applet.text(sayMessage,10,-10,0);
	        applet.popMatrix();
	        applet.popStyle();
	    }
	    
	    /*----------------------------------------
		 * Marker of Agent color　
		 ---------------------------------------*/
	    applet.ambient((int)super.HP/100,(int)((super.HP/100)*(2.55)),(int)super.HP/100);
	    applet.fill(255);
	    applet.stroke(200);
	    super.drawShape(count,animationRate,applet,config);
	}

	public int update(Entity entity,ScreenTransform transform)
	{
		this.sayMessage = null;
		return super.update(entity,transform);
	}
	public void setSay(String s)
    {
    	sayMessage = s;
    }
}