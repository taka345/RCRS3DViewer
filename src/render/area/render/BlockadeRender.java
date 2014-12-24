package render.area.render;

import loader.OBJLoader;
import main.ViewerConfig;
import processing.core.PApplet;

public class BlockadeRender {
	public BlockadeRender() {
		
	}
	public  void drawBlockade(PApplet applet,OBJLoader blockades,int[] apexes,int blockadeNum,
            float centerX,float centerY,float rotateY,int blockadeScale,
            int[] sideMaxApexes,ViewerConfig config)
		{
		applet.ambient(50,50,50);
		applet.noStroke();
		switch(config.getDetail()){
		case ViewerConfig.HIGH:
		switch(blockadeNum){
		case 1 :
		blockades.draw(centerX,centerY,applet.PI/2,rotateY,0,blockadeScale);
		break;
		case 2 :
		blockades.draw((centerX+apexes[sideMaxApexes[0]])/2,
		            (centerY+apexes[sideMaxApexes[0]+1])/2,
		            applet.PI/2,rotateY,0,blockadeScale);
		blockades.draw((centerX+apexes[sideMaxApexes[1]])/2,
		            (centerY+apexes[sideMaxApexes[1]+1])/2,
		            applet.PI/2,rotateY,0,blockadeScale);
		break;
		default :
		break;
		}
		break;
		case ViewerConfig.LOW :
		applet.pushMatrix();
		applet.beginShape();
		for(int i = 0 ; i < apexes.length-1; i+=2){
		applet.vertex(apexes[i],apexes[i+1],2);
		}
		applet.endShape();
		applet.popMatrix();
		break;
		default :
		applet.pushMatrix();
		applet.beginShape();
		for(int i = 0 ; i < apexes.length-1; i+=2){
		applet.vertex(apexes[i],apexes[i+1],2);
		}
		applet.endShape();
		applet.popMatrix();
		break;
		}
		}
}
