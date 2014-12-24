package render.agent;

import main.ViewerConfig;
import render.action.AgentAction;
import processing.core.PApplet;
import processing.core.PImage;
import rescuecore2.misc.gui.ScreenTransform;
import rescuecore2.worldmodel.Entity;

public class PoliceForceShape extends HumanShape
{  
	  PImage policeAction;
	  public PoliceForceShape(Entity entity, ScreenTransform transform, int scale, PImage actionImage, PImage[] images)
	  {
	    super(entity,transform,scale,images);
	    this.policeAction  = actionImage;
	  }
	  
	  public void drawShape(int count, int animationRate, PApplet applet, ViewerConfig config)
	  {
	    if(!config.getFlag("PoliceForce")) return;
	    if(super.action){
	      AgentAction.drawPoliceForceAction(applet,super.moveX,super.moveY,super.markHeight,posX, posY, targetX, targetY,this.policeAction,config);
	    }
	    
	    applet.ambient(100,100,255);
	    applet.stroke(200);
	    super.drawShape(count,animationRate,applet,config);
	  }
	}
