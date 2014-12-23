package rescue3dviewer;

import effect.Effect;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import rescuecore2.misc.gui.ScreenTransform;
import rescuecore2.standard.entities.Building;
import rescuecore2.standard.entities.StandardEntityConstants.Fieryness;
import rescuecore2.worldmodel.Entity;

class BuildingShape extends AreaShape
{
	private PImage[] image;
  
	private float bHeight;
	private int scale;
  
	private Fieryness fieryness;
  
	private Effect effect;

	public BuildingShape(Entity entity, ScreenTransform transform, float bHeight, int scale,PImage[] image, PImage[] fireImage, PImage[] smokeImage, PImage[] icons)
	{
		super(entity,transform,scale,icons, bHeight);
		
		this.fieryness = Fieryness.UNBURNT;
    
		this.image = image;
    
		this.bHeight = bHeight;
		this.scale = 50;//agent size
    
		Building b = (Building)entity;
		this.fieryness = b.getFierynessEnum();  
		super.centerX = transform.xToScreen(b.getX());
		super.centerY = transform.yToScreen(b.getY());
    
		this.effect = new Effect(fireImage, smokeImage);
	}
	public void drawShape(int count, int animationRate, PApplet applet, ViewerConfig config)
	{
		if(!config.getFlag("Building") || nords == null) return;
    
		applet.fill(50);
		applet.stroke(200);
    
		switch(this.fieryness){
			case BURNING :
				//On fire a bit more.
				effect.buildingPre(2, 2, count);
				applet.ambient(200,100,0);
				break;
			case BURNT_OUT :
		        //Completely burnt out.
				effect.buildingPre(0, 2, count);
		        applet.ambient(50,50,50);
		        break;
			case HEATING :
		        //On fire a bit.
				effect.buildingPre(1,1,count);
		        applet.ambient(125,125,0);
		        break;
			case INFERNO :
		        //On fire a lot.
				effect.buildingPre(3,3,count);
		        applet.ambient(255,50,0);
		        break;
			case MINOR_DAMAGE :
		        //Extinguished but minor damage.
				effect.buildingPre(0,0,count);
		        applet.ambient(25,100,170);
		        break;
			case MODERATE_DAMAGE :
		        //Extinguished but moderate damage.
				effect.buildingPre(0,0,count);
		        applet.ambient(50,100,170);
		        break;
			case SEVERE_DAMAGE :
		        //Extinguished but major damage.
				effect.buildingPre(0,0,count);
		        applet.ambient(100,100,170);
		        break;
			case UNBURNT :
		        //Not burnt at all.
				effect.buildingPre(0,0,count);
		        applet.ambient(100,100,100);
		        break;
			case WATER_DAMAGE :
		        //Not burnt at all, but has water damage.
				effect.buildingPre(0,0,count);
		        applet.ambient(0,100,255);
		        break;
		}
		
		switch(config.getDetail()){
			case ViewerConfig.HIGH:
		        createBuilding(applet,this.image,this.bHeight);
		        super.drawShape(count,animationRate,applet,config);
		        break;
			case ViewerConfig.LOW :    
				createBuilding(applet,this.image,this.bHeight);
		        super.drawShape(count,animationRate,applet,config);
		        break;
			default :
		        //bottom
		        super.drawShape(count,animationRate,applet,config);
		        break;
		}
	}
	public int update(Entity entity, ScreenTransform transform)
	{
		super.update(entity,transform);
		Building b = (Building)entity;
    
		Fieryness f = b.getFierynessEnum();
    
		if(this.fieryness.equals(f)){
			return InformationManager.NO_CHANGE;
		} else {
			this.fieryness = f;
      
			switch(f){
				case BURNING :
					//On fire a bit more.
					return InformationManager.BUILDING_BURNING;
				case BURNT_OUT :
					//Completely burnt out.
					return InformationManager.BUILDING_BURNT_OUT;
				case HEATING :
					//On fire a bit.
					return InformationManager.BUILDING_HEATING;
				case INFERNO :
					//On fire a lot.
					return InformationManager.BUILDING_INFERNO;
				case MINOR_DAMAGE :
					//Extinguished but minor damage.
					return InformationManager.BUILDING_EXTINGUISH;
				case MODERATE_DAMAGE :
					//Extinguished but moderate damage.
					return InformationManager.BUILDING_EXTINGUISH;
				case SEVERE_DAMAGE :
					//Extinguished but major damage.
					return InformationManager.BUILDING_EXTINGUISH;
				case UNBURNT :
					//Not burnt at all.
					break;
				case WATER_DAMAGE :
					//Not burnt at all, but has water damage.
					break;
			}
		}
		return InformationManager.NO_CHANGE;
	}

	private void createBuilding(PApplet applet, PImage[] img, float buildHeight) {
		if(super.areaName == super.REFUGE) return;
    
		int floorCount = (int)(buildHeight / this.scale);//
		float buildDivideHeight = buildHeight / floorCount;

	    //build top
	    applet.pushStyle();
	    applet.pushMatrix();
	    applet.translate(0,0,bHeight);
	    applet.noStroke();
	    applet.beginShape();
	    for(int i = 0; i < this.nords.length; i+=2){
	    	applet.vertex(this.nords[i],this.nords[i+1]);
	    }
	    applet.endShape();
	    applet.popMatrix();
	    //build side
	    applet.pushMatrix();
	    for(int cnt = 0; cnt < floorCount; cnt++){
	    	for(int i = 0; i < this.nords.length-2; i+=2){
	    		applet.beginShape(applet.QUAD);
	    		if(!passable[i/2]) {
	    			applet.texture(img[0]);
	    		} else {
	    			if(cnt == 0) applet.texture(img[1]);
	    			else applet.texture(img[0]);
	    		}
	        
		        applet.vertex(this.nords[i],this.nords[i+1],buildDivideHeight*cnt,                      0,1);
		        applet.vertex(this.nords[i+2],this.nords[i+3],buildDivideHeight*cnt,                    1,1);
		        applet.vertex(this.nords[i+2],this.nords[i+3],buildDivideHeight*cnt+buildDivideHeight,  1,0);
		        applet.vertex(this.nords[i],this.nords[i+1],buildDivideHeight*cnt+buildDivideHeight,    0,0);
		        applet.endShape();
	    	}
	    }

	    applet.popMatrix();
	    applet.pushMatrix();
	    applet.beginShape(applet.QUAD);
	    for(int cnt = 0; cnt < floorCount; cnt++) {
	    	if(!passable[passable.length-1]) {
	    		applet.texture(img[0]);
	    	} else {
	    		if(cnt == 0) applet.texture(img[1]);
	    		else applet.texture(img[0]);
	    	}
	    	applet.vertex(this.nords[0],this.nords[1],buildDivideHeight*cnt,                                              0,1);
	    	applet.vertex(this.nords[nords.length-2],this.nords[nords.length-1],buildDivideHeight*cnt,                    1,1);
	    	applet.vertex(this.nords[nords.length-2],this.nords[nords.length-1],buildDivideHeight*cnt+buildDivideHeight,  1,0);
	    	applet.vertex(this.nords[0],this.nords[1],buildDivideHeight*cnt+buildDivideHeight,                            0,0);
	    }
	    applet.endShape();
	    applet.popMatrix();
	    applet.popStyle();
	}
	public void drawEffect(PApplet applet, ViewerConfig config)
	{
		switch(config.getDetail()){
			case ViewerConfig.HIGH:
				if(effect.smoking > 0){
					//drawSmoke(x,y,bHeight,smoking,2,this.nords,smokeCount,0,smokeImage,applet);
					effect.drawSmoke(centerX,centerY,bHeight,effect.smoking,4,this.nords,effect.smokeCount,-0.1f,effect.smokeImage,applet);
				}
				if(effect.burning > 0){
					if(effect.smoking > 0){
						effect.drawSmokes(centerX,centerY,bHeight,effect.smoking,3,this.nords,effect.smokeCount,0.1f,effect.smokeImage,applet);
					}
				}
				if(effect.burning > 0){
					effect.drawFire(centerX,centerY,bHeight,effect.burning,this.nords,effect.fireCount,0.2f,effect.fireImage,applet);
					effect.drawFire(centerX,centerY,bHeight,effect.burning,this.nords,effect.fireCount,-3.0f,effect.fireImage,applet);
				}
				break;
			default :
				break;
		}   
	}
	public void setBHeight(float height)
	{
		this.bHeight = height;
	}
}
