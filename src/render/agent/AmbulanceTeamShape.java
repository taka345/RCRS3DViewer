package render.agent;

import main.ViewerConfig;
import processing.core.PApplet;
import processing.core.PImage;
import rescuecore2.misc.gui.ScreenTransform;
import rescuecore2.worldmodel.Entity;

public class AmbulanceTeamShape extends HumanShape{
	public AmbulanceTeamShape(Entity entity, ScreenTransform transform, int scale, PImage[] images)
	{
		super(entity,transform,scale,images);
	}
	
	public void drawShape(int count, int animationRate, PApplet applet, ViewerConfig config)
	{
		if(!config.getFlag("AmbulanceTeam")) return;
	    
		if(super.action){
			switch(config.getDetail()){
	        	case ViewerConfig.HIGH :
	        		//viewer action icon
	        		super.drawIcon(applet, config);
	        		break;
	        	case ViewerConfig.LOW :
	        		break;
	        	default :
	        		break;
			}
		}
	    
	    applet.ambient(255,255,255);
	    applet.fill(255);
	    applet.stroke(100);
	    super.drawShape(count,animationRate,applet,config);
	}
	
	public void setCarry(){
    	super.carry = true;
    	super.action = true;
    }
    public void setnoCarry(){
    	super.carry = false;
    	super.action = false;
    }
}
