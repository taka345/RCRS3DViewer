package effect;

import processing.core.PApplet;
import processing.core.PImage;

public class Effect extends PApplet{
	private int effectNum = 60;
	
	public int fireCount;
	public int smokeCount;
	  
	public int burning;
	public int smoking;

	public PImage[] fireImage;
	public PImage[] smokeImage;
	
	public Effect(PImage[] fireImage, PImage[] smokeImage) {
	    this.fireCount = 0;
	    this.smokeCount = 0;
	    this.burning = 0;
	    this.smoking = 0;
	    
	    this.fireImage = fireImage;
	    this.smokeImage = smokeImage;
	}
	public void drawFire(float x, float y, float bHeight, int size, float[] nords, int fireCount, float fireInterval, PImage[] fireImage, PApplet applet){      

		  //side
		  float valueX1,valueX2;
		  float valueY1,valueY2;//change coordinate 
		  int picNum;
		  int count = (int)fireCount/2;
		  picNum = selectPic(count);
		  applet.ambient(255,255,255);
		  applet.pushMatrix();
		  applet.textureMode(applet.NORMAL);
		  applet.beginShape(applet.QUADS);
		  applet.noStroke();
		  for(int i = 0; i < nords.length-2; i+=2){
		    valueX1 = inNords(nords[i], x, fireInterval);
		    valueY1 = inNords(nords[i+1], y, fireInterval);
		    valueX2 = inNords(nords[i+2], x, fireInterval);
		    valueY2 = inNords(nords[i+3], y, fireInterval);
		    applet.beginShape(applet.QUAD);
		    applet.texture(fireImage[picNum]);
		    applet.vertex(nords[i] + valueX1,nords[i+1] + valueY1,bHeight / (size+1),0.3f,0.7f);
		    applet.vertex(nords[i+2] + valueX2,nords[i+3] + valueY2,bHeight / (size+1),0.7f,0.7f);
		    applet.vertex(nords[i+2] + valueX2,nords[i+3] + valueY2,bHeight + 60*((size+2)/2),0.7f,0.2f);
		    applet.vertex(nords[i] + valueX1,nords[i+1] + valueY1,bHeight + 60*((size+2)/2),0.3f,0.2f);
		    applet.endShape();
		  }
		  valueX1 = inNords(nords[0], x, fireInterval);
		  valueY1 = inNords(nords[1], y, fireInterval);
		  valueX2 = inNords(nords[nords.length-2], x, fireInterval);
		  valueY2 = inNords(nords[nords.length-1], y, fireInterval);
		  applet.beginShape(applet.QUAD);
		  applet.texture(fireImage[picNum]);
		  applet.vertex(nords[0] + valueX1,nords[1] + valueY1,bHeight / (size+1),0.3f,0.7f);
		  applet.vertex(nords[nords.length-2] + valueX2,nords[nords.length-1] + valueY2,bHeight / (size+1),0.7f,0.7f);
		  applet.vertex(nords[nords.length-2] + valueX2,nords[nords.length-1] + valueY2,bHeight + 60*((size+2)/2),0.7f,0.2f);
		  applet.vertex(nords[0] + valueX1,nords[1] + valueY1,bHeight + 60*((size+2)/2),0.3f,0.2f);
		  applet.endShape();
		  applet.popMatrix();
		}

		public void drawSmokes(float x, float y, float bHeight,int start_point, int size, float[] nords, int smokeCount, float smoke_interval, PImage[] smokeImage, PApplet applet){      

		  //side
		  float valueX1,valueX2;
		  float valueY1,valueY2;//change coordinate 
		  int picNum;
		  int count = (int)smokeCount/2;
		  picNum = selectSmokePic(count);
		  applet.ambient(255,255,255);
		  applet.pushMatrix();
		  applet.textureMode(applet.NORMAL);
		  applet.beginShape(applet.QUADS);
		  applet.noStroke();
		  for(int i = 0; i < nords.length-2; i+=2){
		    valueX1 = inNords(nords[i], x, smoke_interval);
		    valueY1 = inNords(nords[i+1], y, smoke_interval);
		    valueX2 = inNords(nords[i+2], x, smoke_interval);
		    valueY2 = inNords(nords[i+3], y, smoke_interval);
		    /*
		    valueX1 = inNords((nords[i]+nords[i+2])/2, x, smoke_interval);
		    valueY1 = inNords((nords[i+1]+nords[i+3])/2, y, smoke_interval);
		    */
		    applet.beginShape(applet.QUAD);
		    applet.texture(smokeImage[picNum]);
		    applet.vertex(nords[i] + valueX1,nords[i+1] + valueY1,bHeight / (start_point+1),0.2f,0.8f);
		    applet.vertex(nords[i+2] + valueX2,nords[i+3] + valueY2,bHeight / (start_point+1),0.8f,0.8f);
		    applet.vertex(nords[i+2] + valueX2,nords[i+3] + valueY2,bHeight + 60*((size+1)/2),0.8f,0);
		    applet.vertex(nords[i] + valueX1,nords[i+1] + valueY1,bHeight + 60*((size+1)/2),0.2f,0);
		    applet.endShape();
		    applet.beginShape(applet.QUAD);
		  }
		  valueX1 = inNords(nords[0], x, smoke_interval);
		  valueY1 = inNords(nords[1], y, smoke_interval);
		  valueX2 = inNords(nords[nords.length-2], x, smoke_interval);
		  valueY2 = inNords(nords[nords.length-1], y, smoke_interval);
		  applet.beginShape(applet.QUAD);
		  applet.texture(smokeImage[picNum]);
		  applet.vertex(nords[0] + valueX1,nords[1] + valueY1,bHeight / (start_point+1),0.2f,0.8f);
		  applet.vertex(nords[nords.length-2] + valueX2,nords[nords.length-1] + valueY2,bHeight / (start_point+1),0.8f,0.8f);
		  applet.vertex(nords[nords.length-2] + valueX2,nords[nords.length-1] + valueY2,bHeight + 60*((size+1)/2),0.8f,0);
		  applet.vertex(nords[0] + valueX1,nords[1] + valueY1,bHeight + 60*((size+1)/2),0.2f,0);
		  applet.endShape();
		  applet.popMatrix();
		}

		public void drawSmoke(float x, float y, float bHeight,int start_point, int size, float[] nords, int smokeCount, float smoke_interval, PImage[] smokeImage, PApplet applet){      

		  //side
		  float valueX1,valueX2;
		  float valueY1,valueY2;//change coordinate 
		  float valueNX,valueNX2;
		  float valueNY,valueNY2;
		  int picNum;
		  int count = (int)smokeCount/2;
		  picNum = selectSmokePic(count);
		  applet.ambient(255,255,255);
		  applet.pushMatrix();
		  applet.textureMode(applet.NORMAL);
		  applet.beginShape(applet.QUADS);
		  applet.noStroke();
		  for(int i = 0; i < nords.length-2; i+=2){
		    valueX1 = inNords(nords[i], x, smoke_interval);
		    valueY1 = inNords(nords[i+1], y, smoke_interval);
		    valueX2 = inNords(nords[i+2], x, smoke_interval);
		    valueY2 = inNords(nords[i+3], y, smoke_interval);
		    valueNX = onNords(nords[i], x, 2);
		    valueNY = onNords(nords[i+1], y, 2);
		    valueNX2 = onNords(nords[i+2], x, 2);
		    valueNY2 = onNords(nords[i+3], y, 2);
		    applet.beginShape(applet.QUAD);
		    applet.texture(smokeImage[picNum]);
		    applet.vertex(nords[i] + valueX1,nords[i+1] + valueY1,bHeight,0.2f,0.8f);
		    applet.vertex(nords[i+2] + valueX2,nords[i+3] + valueY2,bHeight,0.8f,0.8f);
		    applet.vertex(nords[i+2] + valueNX2,nords[i+3] + valueNY2,bHeight + 60*((size+1)/2),0.8f,0);
		    applet.vertex(nords[i] + valueNX,nords[i+1] + valueNY,bHeight + 60*((size+1)/2),0.2f,0);
		    applet.endShape();
		    applet.beginShape(applet.QUAD);
		  }
		  valueX1 = inNords(nords[0], x, smoke_interval);
		  valueY1 = inNords(nords[1], y, smoke_interval);
		  valueX2 = inNords(nords[nords.length-2], x, smoke_interval);
		  valueY2 = inNords(nords[nords.length-1], y, smoke_interval);
		  valueNX = onNords(nords[0], x, 2);
		  valueNY = onNords(nords[1], y, 2);
		  valueNX2 = onNords(nords[nords.length-2], x, 2);
		  valueNY2 = onNords(nords[nords.length-1], y, 2);
		  applet.beginShape(applet.QUAD);
		  applet.texture(smokeImage[picNum]);
		  applet.vertex(nords[0] + valueX1,nords[1] + valueY1,bHeight,0.2f,0.8f);
		  applet.vertex(nords[nords.length-2] + valueX2,nords[nords.length-1] + valueY2,bHeight,0.8f,0.8f);
		  applet.vertex(nords[nords.length-2] + valueNX2,nords[nords.length-1] + valueNY2,bHeight + 60*((size+1)/2),0.8f,0);
		  applet.vertex(nords[0] + valueNX,nords[1] + valueNY,bHeight + 60*((size+1)/2),0.2f,0);
		  applet.endShape();
		  applet.popMatrix();
		}

		int selectPic(int num){
		  if(num < 20){
		    return num;
		  }
		  else{
		    int i = 0;
		    i = (num - 20) % 40;
		    i = i + 20;
		    return i;
		  }
		}

		int selectSmokePic(int num){
		  if(num < 30){
		    return num;
		  }
		  else{
		    int i = 0;
		    i = (num - 30) % 30;
		    i = i + 30;
		    return i;
		  }
		}

		float inNords(float buildingPoint, float nordsPoint, float distance){
		  float i = distance;
		  if(buildingPoint > nordsPoint){
		    return i;
		  }else if(buildingPoint < nordsPoint){
		    return -i;
		  }else{
		    return 0;
		  }
		}

		float onNords(float buildingPoint, float nordsPoint, int distance){
		  float dis;
		  if(buildingPoint > nordsPoint){
		    dis = (buildingPoint - nordsPoint)/distance;
		    return -dis;
		  }else if(buildingPoint < nordsPoint){
		    dis = (nordsPoint - buildingPoint)/distance;
		    return dis;
		  }else{
		    return 0;
		  }
		}
		
		public void buildingPre(int firelevel,int smokelevel,int count){
		    burning = firelevel;
		    smoking = smokelevel;
		    if(burning > 0){
		    	fireCount++;
		    }else{
		    	fireCount = 0;
		    }
		    if(smoking > 0){
		      if(count%2 == 0){
		    	  smokeCount++;
		      }
		    }else{
		      smokeCount = 0;
		    }
		}
}
