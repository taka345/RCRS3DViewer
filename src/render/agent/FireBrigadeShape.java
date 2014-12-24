package render.agent;

import main.ViewerConfig;
import processing.core.PApplet;
import processing.core.PImage;
import rescuecore2.misc.gui.ScreenTransform;
import rescuecore2.worldmodel.Entity;

public class FireBrigadeShape extends HumanShape{
	private int targetX;
    private int targetY;
    private float targetZ;
    
	public FireBrigadeShape(Entity entity, ScreenTransform transform, int scale, PImage[] images)
	{
		super(entity,transform,scale,images);
		this.targetX = 0;
    	this.targetY = 0;
	}  
	public void drawShape(int count, int animationRate, PApplet applet, ViewerConfig config)
	{
		if(!config.getFlag("FireBrigade")) return;
		
		/*----------------------------------------
		 * action Fire fighting of FireBrigade ã€?
		 ---------------------------------------*/
	    if(super.action){
	    	applet.stroke(188,226,255,230);
	    	switch(config.getDetail()){
	        	case ViewerConfig.HIGH :
	        		applet.pushStyle();
	        		applet.noFill();
	        		applet.stroke(188,226,255,230);
	        		applet.strokeWeight(15);
	        		applet.bezier(posX,posY,0,
	                        	  targetX-((targetX-posX)/4),targetY-((targetY-posY)/4),targetZ+(targetZ/5),
	                              targetX-((targetX-posX)/2),targetY-((targetY-posY)/2),targetZ+(targetZ/10),
	                              targetX,targetY,targetZ-(targetZ/10));
	        		applet.popStyle();
	        		break;
	        	case ViewerConfig.LOW :
	        		break;
	        	default :
	        		applet.line(posX,posY,targetX,targetY);
	        		break;
	    	}
	    }
	    /*----------------------------------------
		 * Marker of Agent colorã€?
		 ---------------------------------------*/
	    applet.ambient(255,100,100);
	    applet.stroke(200);
	    super.drawShape(count,animationRate,applet,config);
	}
	public void setActionTarget(int x, int y, float z)
    {
    	this.targetX = x;
    	this.targetY = y;
    	this.targetZ = z;
    	super.action = true;
    }
}
