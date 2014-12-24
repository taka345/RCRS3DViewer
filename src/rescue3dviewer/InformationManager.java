package rescue3dviewer;

import information.DrawLineChart;
import information.graph.CreateLineChart;
import information.graph.createChartImage;
import information.frame.InfomationFrame;

import org.jfree.data.general.DefaultPieDataset;

import processing.core.PApplet;
import processing.core.PImage;

class InformationManager extends PApplet {
	
	public static final int NO_CHANGE = -1;

	public static final int BUILDING_HEATING = 0; 
	public static final int BUILDING_BURNING = 1; 
	public static final int BUILDING_INFERNO = 2;
	public static final int BUILDING_EXTINGUISH = 3;
	public static final int BUILDING_BURNT_OUT = 4;
	public static final int HUMAN_DEAD = 5;

	private double[] score;
	private double[] population;
	private double[] numBurnedBuilding;
	  
	private int livePopulation = 0;
	private int deadPopulation = 0;
	  
	private int heatingBuilding = 0;
	private int burntoutBuilding = 0;
	private int unBurntBuilding = 0;
	  
	private int allPopulation = 0;
	private int refugePopulation = 0;
	private int evacuationPopulation = 0;

	private int currentTime;
	private int maxTime;
	private int startTime;
	  
	private double maxScore = 0;

	private InfomationFrame infoFrame;
	private DrawLineChart scoreChart;
	private DrawLineChart populationChart;
	private DrawLineChart burnedBuildingChart;
	  
	DefaultPieDataset populationData;
	DefaultPieDataset buildingData;
	DefaultPieDataset refugeData;
	    
	PImage chartImage;

	public InformationManager()
	{
	    populationData = new DefaultPieDataset();      
	    buildingData = new DefaultPieDataset();      
	    refugeData = new DefaultPieDataset();
	    
	    this.scoreChart = new DrawLineChart(this, 400, 200);
	    this.scoreChart.setLocation(0, 40);
	    this.scoreChart.setLabelName("Time", "Score");
	    this.scoreChart.setTittle("Score");
	    
	    this.populationChart = new DrawLineChart(this, 400, 200);
	    this.populationChart.setLocation(0, 270);
	    this.populationChart.setLabelName("Time", "populastion");
	    this.populationChart.setTittle("Population");
	    
	    this.burnedBuildingChart = new DrawLineChart(this, 400, 200);
	    this.burnedBuildingChart.setLocation(0, 500);
	    this.burnedBuildingChart.setLabelName("Time", "Burned Building");
	    this.burnedBuildingChart.setTittle("Burned Building");
	    
	    this.infoFrame = new InfomationFrame(0, 0, 225, 30, true);
	    this.infoFrame.setValue("Line", this.scoreChart);
	    this.infoFrame.setValue("Line", this.populationChart);
	    this.infoFrame.setValue("Line", this.burnedBuildingChart);
	}

	public void draw() {
	    background(128);
	    
	    this.scoreChart.setVMax(score);
	    this.scoreChart.setData(score, currentTime);
	    
	    this.populationChart.setVMax(population);
	    this.populationChart.setData(population, currentTime);
	    
	    this.burnedBuildingChart.setVMax(numBurnedBuilding);
	    this.burnedBuildingChart.setData(numBurnedBuilding, currentTime);
	    
	    this.infoFrame.draw(this);
	    
	    //score darw Graph

	    /*
	    switch(button.getFlag()) {
	    case 0 :
	      translate(0,40);
	      gc.setLabelName("Time", "Score");
	      gc.setTittle("Score");
	      gc.setVMax(score);
	      gc.setData(score, currentTime);
	      gc.draw(score, currentTime, 400, 200);
	      
	      translate(0,230);
	      gc.setLabelName("Time", "population");
	      gc.setTittle("Population");
	      gc.setVMax(population);
	      gc.setData(population, currentTime);
	      gc.draw(population, currentTime, 400, 200);
	      
	      translate(0,230);
	      gc.setLabelName("Time", "Burned Building");
	      gc.setTittle("Burned Building");
	      gc.setVMax(numBurnedBuilding);
	      gc.setData(numBurnedBuilding, currentTime);
	      gc.draw(numBurnedBuilding, currentTime, 400, 200);
	      break;
	    case 1 :
	      translate(0,40);
	      refugeData.clear();
	      refugeData.setValue("Refuge\n"+ refugePopulation, refugePopulation);
	      evacuationPopulation = allPopulation - refugePopulation - deadPopulation;
	      refugeData.setValue("Evacuation\n"+ evacuationPopulation, evacuationPopulation);
	      refugeData.setValue("Dead\n"+ deadPopulation, deadPopulation);
	      image(new createChartImage(refugeData).getPieChartPImage(400, 200, "Refuge"),0,30);
	      
	      translate(0,230);
	      buildingData.clear();
	      buildingData.setValue("HeatingBuilding\n"+ heatingBuilding, heatingBuilding);
	      buildingData.setValue("BurntOutBuilding\n"+ burntoutBuilding, burntoutBuilding);
	      buildingData.setValue("unBurntBuilding\n"+ unBurntBuilding, unBurntBuilding);
	      image(new createChartImage(buildingData).getPieChartPImage(400, 200, "BurnedBuilding"),0,30);
	      break;
	    }
	    popStyle();
	    */
	}

	public void mousePressed() {
		infoFrame.push(this.mouseX, this.mouseY);
	}

	public void init(int maxTime, int startTime, int population)
	{
		this.currentTime = 0;
	    this.maxTime = maxTime;
	    this.startTime = startTime;

	    this.score = new double[maxTime-startTime];
	    this.population = new double[maxTime-startTime];
	    this.numBurnedBuilding = new double[maxTime-startTime];

	    this.score[0] = 0;
	    this.population[0] = population;
	    this.numBurnedBuilding[0] = 0;
	    
	    this.livePopulation = 0;
	    this.deadPopulation = 0;
	    
	    this.heatingBuilding = 0;
	    this.burntoutBuilding = 0;
	    this.unBurntBuilding = 0;
	    
	    this.allPopulation = population;
	    this.refugePopulation = 0;

	    for (int i = 1; i < maxTime-startTime; ++i) {
	    	this.score[i] = -1;
	    	this.population[i] = -1;
	    	this.numBurnedBuilding[i] = -1;
	    }
	}
	  
	public void setBurnedBuilding(int time, int fire_count)
	{
		this.numBurnedBuilding[time] = fire_count;
	}
	  
	public void setPopulation(int time, int livePopulation)
	{
	    this.population[time] = livePopulation;
	}
	  
	public void setPopulationData(int live, int dead)
	{
	    this.livePopulation = live;
	    this.deadPopulation = dead;
	}
	  
	public void setBuildingData(int heating, int burnt_out, int unburnt)
	{
	    this.heatingBuilding = heating;
	    this.burntoutBuilding = burnt_out;
	    this.unBurntBuilding = unburnt;
	}
	  
	public void setRefugeData(int refugepopulation)
	{
	    this.refugePopulation = refugepopulation;
	}

	public void nextTime(int t)
	{
	    int time = t - this.startTime;
	    if (time <= 0 || time >= maxTime) return;
	    
	    this.currentTime = time;
	    this.population[time] = this.population[time-1];
	    this.numBurnedBuilding[time] = this.numBurnedBuilding[time-1];
	}

	public void setScore(int t, double score)
	{
	    int time = t-this.startTime;
	    if (time <= 0 || time >= maxTime) return;

	    this.score[time] = score;
	}
}