package rescue3dviewer;

import java.io.File;

import processing.core.PApplet;
import processing.core.PImage;

import saito.objloader.*;

class ImageLoader{
	private int effectNum = 60;
	
	  static final int AMBULANCE = 0;
	  static final int FIRE = 1;
	  static final int GAS = 2;
	  static final int HYDRANT = 3;
	  static final int POLICE = 4;
	  static final int REFUGE = 5;
	  
	  
	  //top0 right1 left2 front3 back4
	  PImage[] rescue;
	  PImage[] ambulance;
	  PImage[] police;
	  PImage[] firebrigade;
	  PImage[] civilian;
	  PImage[] fires;
	  PImage[] smokes;
	  
	  PImage[] building;
	  PImage road;
	  
	  PImage[] icons;
	  
	  public ImageLoader(PApplet applet)
	  {
	    this.rescue = new PImage[4];
	    this.rescue[0] = applet.loadImage("images"+File.separator+"rescue_top.jpg");
	    this.rescue[1] = applet.loadImage("images"+File.separator+"rescue_right.jpg");
	    this.rescue[2] = applet.loadImage("images"+File.separator+"rescue_left.jpg");
	    this.rescue[3] = applet.loadImage("images"+File.separator+"rescue_front.jpg");
	    
	    this.ambulance = new PImage[5];
	    this.ambulance[0] = applet.loadImage("images"+File.separator+"Ambulance"+File.separator+"top.jpg");
	    this.ambulance[1] = applet.loadImage("images"+File.separator+"Ambulance"+File.separator+"side2.jpg");
	    this.ambulance[2] = applet.loadImage("images"+File.separator+"Ambulance"+File.separator+"side1.jpg");
	    this.ambulance[3] = applet.loadImage("images"+File.separator+"Ambulance"+File.separator+"front.jpg");
	    this.ambulance[4] = applet.loadImage("images"+File.separator+"Ambulance"+File.separator+"back.jpg");
	    
	    this.police = new PImage[5];
	    this.police[0] = applet.loadImage("images"+File.separator+"Police"+File.separator+"top.jpg");
	    this.police[1] = applet.loadImage("images"+File.separator+"Police"+File.separator+"side2.jpg");
	    this.police[2] = applet.loadImage("images"+File.separator+"Police"+File.separator+"side1.jpg");
	    this.police[3] = applet.loadImage("images"+File.separator+"Police"+File.separator+"front.jpg");
	    this.police[4] = applet.loadImage("images"+File.separator+"Police"+File.separator+"back.jpg");
	    
	    this.firebrigade = new PImage[5];
	    this.firebrigade[0] = applet.loadImage("images"+File.separator+"Firebrigade"+File.separator+"top.jpg");
	    this.firebrigade[1] = applet.loadImage("images"+File.separator+"Firebrigade"+File.separator+"side2.jpg");
	    this.firebrigade[2] = applet.loadImage("images"+File.separator+"Firebrigade"+File.separator+"side1.jpg");
	    this.firebrigade[3] = applet.loadImage("images"+File.separator+"Firebrigade"+File.separator+"front.jpg");
	    this.firebrigade[4] = applet.loadImage("images"+File.separator+"Firebrigade"+File.separator+"back.jpg");
	    
	    //---------------------------------------------------------------------------------------------//
	    //2014/11/06 henkou satou 
	    //this.building = new PImage[2];
	    //this.building[0] = applet.loadImage("images"+File.separator+"building.jpg");
	    //this.building[1] = applet.loadImage("images"+File.separator+"entrance.jpg");
	    
	    this.building = new PImage[2];
	    this.building[0] = applet.loadImage("images"+File.separator+"building2.0.png");
	    this.building[1] = applet.loadImage("images"+File.separator+"entrance2.0.png");
	    //---------------------------------------------------------------------------------------------//
	    
	    this.icons = new PImage[6];
	    this.icons[0] = applet.loadImage("images"+File.separator+"AmbulanceCentre-64x64.png");
	    this.icons[1] = applet.loadImage("images"+File.separator+"FireStation-64x64.png");
	    this.icons[2] = applet.loadImage("images"+File.separator+"GasStation-64x64.png");
	    this.icons[3] = applet.loadImage("images"+File.separator+"Hydrant-64x64.png");
	    this.icons[4] = applet.loadImage("images"+File.separator+"PoliceOffice-64x64.png");
	    this.icons[5] = applet.loadImage("images"+File.separator+"Refuge-64x64.png");
	    
	    this.road = applet.loadImage("images"+File.separator+"road.jpg");
	    
	    this.fires = new PImage[effectNum];
	    for(int i = 0; i < this.fires.length; i++){
	      this.fires[i] =  applet.loadImage("images"+File.separator+"Fire"+File.separator+"fire"+i+".png");
	    }
	    
	    this.smokes = new PImage[effectNum];
	    for(int i = 0; i < this.smokes.length; i++){
	      this.smokes[i] =  applet.loadImage("images"+File.separator+"Smoke"+File.separator+"smoke"+i+".png");
	    }
	  }
	  
	  public PImage[] getBuildingImage()
	  {
	    return this.building;
	  }
	  
	  public PImage[] getRescueTeamImage()
	  {
	    return this.rescue;
	  }
	  
	  public PImage[] getIcons()
	  {
	    return this.icons;
	  }
	  
	  public PImage getRoadImage()
	  {
	    return this.road;
	  }
	  
	  public PImage[] getAmbulanceImage()
	  {
	    return this.ambulance;
	  }
	  
	  public PImage[] getPoliceImage()
	  {
	    return this.police;
	  }
	  
	  public PImage[] getFirebrigadeImage()
	  {
	    return this.firebrigade;
	  }
	  
	  public PImage[] getFire()
	  {
	    return this.fires;
	  }
	  
	  public PImage[] getSmoke()
	  {
	    return this.smokes;
	  }
	  
	  public void clear()
	  {
	    PImage[] rescue = null;
	    PImage[] ambulance = null;
	    PImage[] police = null;
	    PImage[] firebrigade = null;
	    PImage[] civilian = null;
	    PImage[] fires = null;
	    PImage[] smokes = null;
	    
	    PImage[] building = null;
	    PImage road = null;
	    
	    PImage[] icons = null;
	  }
	}

