package render.agent;

import java.util.ArrayList;

import main.InformationManager;
import main.ViewerConfig;
import processing.core.PApplet;
import processing.core.PImage;
import rescuecore2.misc.gui.ScreenTransform;
import rescuecore2.standard.entities.Human;
import rescuecore2.worldmodel.Entity;

public abstract class HumanShape implements EntityShape
{
	protected int id;
	protected int scale;
  
    protected int posX;
    protected int posY;
  
    protected int moveX;
    protected int moveY;
  
    protected int history[];
    protected float direction;
  
    protected int HP; //max 10000
  
    protected PImage[] images;
  
    protected boolean action;
    protected boolean carry; // AmbulanceTeamShape

    protected float markHeight; 
    private boolean dead;
    
    public HumanShape(Entity entity, ScreenTransform transform, int scale)
    {
    	if(entity == null) return;
    	
    	this.id = entity.getID().getValue();    
    	Human human = (Human)entity;
    	this.HP = human.getHP();

    	this.scale = scale/150; //magic number for calculating agent size
    	
    	try{
    		this.posX = transform.xToScreen(human.getX());
    		this.posY = transform.yToScreen(human.getY());
    	} catch(Exception e) {
    		//e.printStackTrace();
    		this.posX = 0;
    		this.posY = 0;
    	}
    	this.dead = false;
    
    	this.history = null;
    	this.direction = 0;
    
    	this.moveX = 0;
    	this.moveY = 0;
    
    	this.images = null;
    
    	this.action = false;
    	this.carry = false;
    }
    public HumanShape(Entity entity, ScreenTransform transform, int scale, PImage[] images)
    {
    	this(entity,transform,scale);
    	this.HP = ((Human)entity).getHP();
    	this.images = images;
    }
    public void drawShape(int count, int animationRate, PApplet applet, ViewerConfig config)
    {
    	//calculation agent animation path
    	moveX = 0;
    	moveY = 0;

    	if(animationRate <= 0 || history == null || count >= animationRate || history.length <= 3) {
    		moveX = this.posX; moveY = this.posY;
    	} else {
    		float rate = (float)animationRate / ((this.history.length/2)-1);
    		int index = (int)((count / rate))*2;

    		if(this.history.length <= index+2){
    			moveX = this.posX; moveY = this.posY;
    		} else {
    			float rateX = (this.history[index  ] - this.history[index+2])/rate;
    			float rateY = (this.history[index+1] - this.history[index+3])/rate;
    			moveX = (int) (this.history[index] - (rateX*(count%rate)));
    			moveY = (int) (this.history[index+1] - (rateY*(count%rate)));
    			float x = moveX - this.history[index];
    			float y = moveY - this.history[index+1];
    			float r = applet.sqrt((x*x)+(y*y));
    			float t = applet.acos(x/r);
    			if(r != 0) this.direction = t;  
    		}
    	}
    
    	int min = scale/2 * -1;
    	int max = scale/2;

    	switch(config.getDetail()) {
    		case ViewerConfig.HIGH :
    			if(this.carry) {
			        int sMin = scale/3 * -1;
			        int sMax = scale/3;
			        applet.pushStyle();
			        applet.ambient(100,255,100);
			        applet.fill(255);
			        applet.stroke(200);
			        applet.pushMatrix();
			        applet.translate(moveX,moveY,scale);
			        applet.rotateZ(this.direction);   
			        //top0 right1 left2 front3
			        applet.beginShape(applet.QUAD);
			        //top
			        applet.vertex(sMin,sMin,sMax,1,0);
			        applet.vertex(sMin,sMax,sMax,0,0);
			        applet.vertex(sMax,sMax,sMax,0,1);
			        applet.vertex(sMax,sMin,sMax,1,1);
			        applet.endShape();
          
			        applet.beginShape(applet.QUAD);
			        //right
			        applet.vertex(sMin,sMin,sMin,0,1);
			        applet.vertex(sMin,sMin,sMax,0,0);
			        applet.vertex(sMax,sMin,sMax,1,0);
			        applet.vertex(sMax,sMin,sMin,1,1);
			        applet.endShape();
          
			        applet.beginShape(applet.QUAD);
			        //left
			        applet.vertex(sMin,sMax,sMin,0,1);
			        applet.vertex(sMin,sMax,sMax,0,0);
			        applet.vertex(sMax,sMax,sMax,1,0);
			        applet.vertex(sMax,sMax,sMin,1,1);
			        applet.endShape();
          
			        applet.beginShape(applet.QUAD);
			        //back
			        applet.vertex(sMin,sMin,sMin,0,0);
			        applet.vertex(sMin,sMin,sMax,0,1);
			        applet.vertex(sMin,sMax,sMax,1,1);
			        applet.vertex(sMin,sMax,sMin,1,0);
			        applet.endShape();
          
			        applet.beginShape(applet.QUAD);
			        //front
			        applet.vertex(sMax,sMin,sMin,0,1);
			        applet.vertex(sMax,sMin,sMax,0,0);
			        applet.vertex(sMax,sMax,sMax,1,0);
			        applet.vertex(sMax,sMax,sMin,1,1);
			        applet.endShape();
			        applet.popMatrix();
			        applet.popStyle();
    			}
    		case ViewerConfig.LOW:
    			applet.pushMatrix();
    			//agent
    			applet.translate(moveX,moveY,scale/2);
    			applet.rotateZ(this.direction);
		        //applet.box(scale);
		        applet.textureMode(applet.NORMAL);    
		        //top0 right1 left2 front3
		        applet.beginShape(applet.QUAD);
		        //top
		        if(images != null) applet.texture(images[0]);
		        applet.vertex(min,min,max,1,0);
		        applet.vertex(min,max,max,0,0);
		        applet.vertex(max,max,max,0,1);
		        applet.vertex(max,min,max,1,1);
		        applet.endShape();
		        
		        applet.beginShape(applet.QUAD);
		        //right
		        if(images != null) applet.texture(images[1]);
		        applet.vertex(min,min,min,0,1);
		        applet.vertex(min,min,max,0,0);
		        applet.vertex(max,min,max,1,0);
		        applet.vertex(max,min,min,1,1);
		        applet.endShape();
		        
		        applet.beginShape(applet.QUAD);
		        //left
		        if(images != null) applet.texture(images[2]);
		        applet.vertex(min,max,min,0,1);
		        applet.vertex(min,max,max,0,0);
		        applet.vertex(max,max,max,1,0);
		        applet.vertex(max,max,min,1,1);
		        applet.endShape();
		        
		        applet.beginShape(applet.QUAD);
		        //back
		        if(images != null) applet.texture(images[4]);
		        applet.vertex(min,min,min,0,0);
		        applet.vertex(min,min,max,0,1);
		        applet.vertex(min,max,max,1,1);
		        applet.vertex(min,max,min,1,0);
		        applet.endShape();
		        
		        applet.beginShape(applet.QUAD);
		        //front
		        if(images != null) applet.texture(images[3]);
		        applet.vertex(max,min,min,0,1);
		        applet.vertex(max,min,max,0,0);
		        applet.vertex(max,max,max,1,0);
		        applet.vertex(max,max,min,1,1);
		        applet.endShape();
		        applet.popMatrix();
        
		        if(config.getFlag("Marker")){
		        	drawMarker(applet);
		        }
		        break;
    		default :
		        applet.pushMatrix();
		        //agent
		        applet.translate(moveX,moveY,scale/2);
		        applet.ellipse(0,0,scale,scale);
		        applet.popMatrix();
		        break;
    	}
    
	    if(config.getFlag("HP")){
	    	applet.pushStyle();
	    	applet.ambient(125,255,125);
	    	applet.noStroke();
	        applet.pushMatrix();
	        int hpBar = (int)applet.map(HP,0,10000,scale*-1,scale);
	        applet.translate(moveX,moveY,scale+50);
	        applet.rotateZ((float)(Math.PI*2-config.getRoll()));
	        applet.rotateX((float)(Math.PI*2-config.getYaw()));
	        applet.beginShape();
	        applet.vertex(scale*-1,scale/4*-1,0);
	        applet.vertex(scale*-1,scale/4,0);
	        applet.vertex(scale,scale/4,0);
	        applet.vertex(scale,scale/4*-1,0);
	        applet.endShape();
	        applet.beginShape();
	        applet.ambient(255,125,125);
	        applet.vertex(hpBar,scale/4*-1,0);
	        applet.vertex(hpBar,scale/4,0);
	        applet.vertex(scale,scale/4,0);
	        applet.vertex(scale,scale/4*-1,0);
	        applet.endShape();
	        applet.popMatrix();
	        applet.popStyle();
	    } 
    }
    public int update(Entity entity, ScreenTransform transform)
    {
    	if(entity == null) return InformationManager.NO_CHANGE;
    	
    	int result = InformationManager.NO_CHANGE;
    	//action reset
    	if(this.carry == false) {
    		this.action = false;
    	}
    	
    	Human h = (Human)entity;
    	if(this.HP != h.getHP()){
    		this.HP = h.getHP();
    		if(this.HP == 0){
    			result = InformationManager.HUMAN_DEAD;
   				dead = true;
    		}
    	}
    
    	int[] humanHis = h.getPositionHistory();
    	if(humanHis==null || humanHis.length <= 0) {
    		try{
    			this.posX = transform.xToScreen(h.getX());
    			this.posY = transform.yToScreen(h.getY());
    		} catch(NullPointerException npe) {
    			//npe.printStackTrace();
    			return InformationManager.NO_CHANGE;
    		}
    		this.history = null;
    		return InformationManager.NO_CHANGE;
    	}

    	ArrayList<Integer> list = new ArrayList<Integer>();
    	list.add(this.posX);
    	list.add(this.posY);
    	int x = transform.xToScreen(humanHis[0]);
    	int y = transform.yToScreen(humanHis[1]);
    	if(!(x == this.posX && y == this.posY)) {
    		list.add(x);
    		list.add(y);
    	}
    
    	for(int i = 2; i < humanHis.length; i+=2){
    		int x1 = transform.xToScreen(humanHis[i-2]);
    		int y1 = transform.yToScreen(humanHis[i-1]);
    		int x2 = transform.xToScreen(humanHis[i]);
    		int y2 = transform.yToScreen(humanHis[i+1]);
      
    		if(!(x1 == x2 && y1 == y2)){
    			list.add(x2);
    			list.add(y2);
    		}
    	}
    	
    	try {
    		this.posX = transform.xToScreen(h.getX());
    		this.posY = transform.yToScreen(h.getY());
    	} catch(NullPointerException npe) {
    		//npe.printStackTrace();
    		return InformationManager.NO_CHANGE;
    	}
      
    	int i = 0;
    	this.history = new int[list.size()];
    	for(Integer next : list){
    		this.history[i] = next;
    		i++;
    	}
    
    	return result;
    }
    public int getID()
    {
    	return id;
    }
    
    public void drawIcon(PApplet applet, ViewerConfig config) 
    {
    	applet.pushStyle();
    	applet.pushMatrix();
    	applet.ambient(255,240,220);
    	applet.stroke(80);
    	applet.strokeWeight(3);
    	applet.translate(moveX, moveY, markHeight + 50);
    	applet.rotateZ((float)(Math.PI*2-config.getRoll()));
    	applet.rotateX((float)(Math.PI*2-config.getYaw()));
    	applet.beginShape();
    	applet.texture(images[0]);
    	applet.vertex(-30,30);
    	applet.vertex(-30,-30);
    	applet.vertex(50,-30);
    	applet.vertex(50,30);
    	applet.endShape();
    	applet.ambient(10,10,10);
    	applet.popMatrix();
    	applet.popStyle();
    }
    public void setMarkHeight(float markerHeight)
    {
    	this.markHeight = markerHeight; 
    }
    protected void drawMarker(PApplet applet){
    	float markerHeight = markHeight;
    	applet.pushStyle();
    	applet.pushMatrix();
    	applet.noStroke();
    	applet.translate(moveX,moveY,markerHeight + 40);
    	applet.beginShape(applet.TRIANGLES);
    	applet.vertex(scale*-1,scale,scale);
    	applet.vertex(scale,scale,scale);
    	applet.vertex(0,0,scale*-1);
    	applet.vertex(scale,scale,scale);
    	applet.vertex(0,scale*-1,scale);
    	applet.vertex(0,0,scale*-1);
    	applet.vertex(scale*-1,scale,scale);
    	applet.vertex(0,scale*-1,scale);
    	applet.vertex(0,0,scale*-1);
    	applet.endShape();
    	applet.popMatrix();
    	applet.popStyle();
    }
}