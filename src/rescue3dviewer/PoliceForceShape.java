package rescue3dviewer;

import effect.Effect;
import processing.core.PApplet;
import processing.core.PImage;
import rescuecore2.misc.gui.ScreenTransform;
import rescuecore2.worldmodel.Entity;

class PoliceForceShape extends HumanShape{
	private int targetX;
    private int targetY;
    
	public PoliceForceShape(Entity entity, ScreenTransform transform, int scale, PImage[] images)
	{
		super(entity,transform,scale,images);
		this.targetX = 0;
    	this.targetY = 0;
	}
	
	public void drawShape(int count, int animationRate, PApplet applet, ViewerConfig config)
	{
		if(!config.getFlag("PoliceForce")) return;
			
		/*----------------------------------------
		 * action　Debris removal of PoliceForce 　
		 ---------------------------------------*/
		if(super.action){
			switch(config.getDetail()){
				case ViewerConfig.HIGH :
					//viewer action icon
					super.drawIcon(applet, config);
					rectPoint(super.posX,super.posY,this.targetX,this.targetY,applet);
					break;
				case ViewerConfig.LOW :
					rectPoint(super.posX,super.posY,this.targetX,this.targetY,applet);
					break;
				default :
					rectPoint(super.posX,super.posY,this.targetX,this.targetY,applet);
					break;
			}
	    }
		/*----------------------------------------
		 * Marker of Agent color　
		 ---------------------------------------*/
	    applet.ambient(100,100,255);
	    applet.stroke(200);
	    super.drawShape(count,animationRate,applet,config);
	}
	
	private void rectPoint(int posX, int posY, int targetX, int targetY, PApplet applet)
	{
		int y0 = 0;
		int x0 = 0;
		float x_width = 0, y_height = 0;
		float pointX1, pointX2, pointX3, pointX4;
		float pointY1, pointY2, pointY3, pointY4;
		if((targetY - posY) == 0) {
			y0 = 10;
		} else if((targetX - posX) == 0) {
		    x0 = 10;
		}else {
			float hypotenuse = applet.sqrt((targetY - posY) * (targetY - posY) + (targetX - posX) * (targetX - posX));
		    x_width = 20 * ((targetY - posY) / hypotenuse);
		    y_height = (-1) * (20 * ((targetX - posX) / hypotenuse));
		}
		pointX1 = posX + x0 + x_width;
		pointY1 = posY + y0 + y_height;
		pointX2 = posX - x0 - x_width;
		pointY2 = posY - y0 - y_height;
		pointX3 = targetX - x0 - x_width;
		pointY3 = targetY - y0 - y_height;
		pointX4 = targetX + x0 + x_width;
		pointY4 = targetY + y0 + y_height;
		  
		applet.pushMatrix();
		applet.pushStyle();
		applet.stroke(255,0,0);
		applet.noFill();
		applet.translate(0,0,8);
		applet.quad(pointX1, pointY1, pointX2, pointY2, pointX3, pointY3, pointX4, pointY4);
		applet.translate(0,0,0);
		applet.popStyle();
		applet.popMatrix();
	}
	public void setActionTarget(int x, int y)
    {
    	this.targetX = x;
    	this.targetY = y;
    }
	public void setClearAction()
    {
    	this.action = true;
    }
}
