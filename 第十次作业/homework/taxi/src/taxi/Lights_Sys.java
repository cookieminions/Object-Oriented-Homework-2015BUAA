package taxi;

import java.awt.Point;
import java.util.Random;

public class Lights_Sys extends Thread{
	/* Overview: 这个类刷新地图上的红绿灯状态
	 */
	
	point[][] cityMap;
	TaxiGUI gui;
	int times;
	
	public boolean repOK(){
		/* 
		  @EFFECTS: (cityMap==null||gui==null||!((Object)times instanceof Integer)) ==> \result == false;
		  			(cityMap.length!=80) ==> \result == false;
		  			(\exist int i;0<=i<80;cityMap[i].length!=80) ==> \result == false;
		  			(\exist int i,j;0<=i<80,0<=j<80,;cityMap[i][j]==null) ==> \result == false;
		  			(其他情况成立) ==> \result == true;
		  */
		if(cityMap==null||gui==null||!((Object)times instanceof Integer))	return false;
		if(cityMap.length!=80)	return false;
		for(int i=0;i<80;i++){
			if(cityMap[i].length!=80)	return false;
			for(int j=0;j<80;j++){
				if(cityMap[i][j]==null)	return false;
			}
		}
		return true;
	}
	
	public Lights_Sys(point[][] tmpMap,TaxiGUI tmpgui) {
		/*
		  @MODIFIES: cityMap, times; 
		  */
		cityMap = tmpMap;
		times = new Random().nextInt(50)+50;
		gui = tmpgui;
	}
	
	public void run(){
		/*
		  @MODIFIES: cityMap; 
		  */
		while(true){
			updateLights();
			yield();
		}
	}
	
	private void updateLights(){
		/*
		  @MODIFIES: cityMap; 
		  */
		try {
			sleep(times);
		} catch (InterruptedException e) {}
		for(int i=0;i<80;i++){
			for(int j=0;j<80;j++){
				if(cityMap[i][j].light.get()!=-1){
					int l = (cityMap[i][j].light.get()+1)%2;
					cityMap[i][j].light.set(l);
					gui.SetLightStatus(new Point(i, j), l+1);
				}
			}
		}
	}
	
}
