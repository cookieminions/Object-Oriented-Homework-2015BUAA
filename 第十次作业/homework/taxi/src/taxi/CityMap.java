package taxi;

import java.awt.Point;
import java.text.DecimalFormat;

public class CityMap extends Thread{
	/* Overview: 这个类能够刷新地图上边的流量 提供测试者打开和关闭地图上边的方法
	 */
	
	private point[][] cityMap;
	private taxi[] taxis;
	private long startTime;
	private TaxiGUI gui;
	
	public boolean repOK(){
		/* 
		  @EFFECTS: (cityMap==null||taxis==null||(gui==null)) ==> \result == false;
		  			(cityMap.length!=80||taxis.length!=100) ==> \result == false;
		  			(\exist int i;0<=i<80;cityMap[i].length!=80) ==> \result == false;
		  			(\exist int i,j;0<=i<80,0<=j<80,;cityMap[i][j]==null) ==> \result == false;
		  			(\exist int i;0<=i<100;taxis[i]==null) ==> \result == false;
		  			(其他情况成立) ==> \result == true;
		  */
		if(cityMap==null||taxis==null||(gui==null))
			return false;
		if(cityMap.length!=80||taxis.length!=100)	return false;
		for(int i=0;i<80;i++){
			if(cityMap[i].length!=80)	return false;
			for(int j=0;j<80;j++){
				if(cityMap[i][j]==null)	return false;
			}
		}
		for(int i=0;i<100;i++){
			if(taxis[i]==null)	return false;
		}
		return true;
	}
	
	public CityMap(point[][] tmp_map,taxi[] tmp_taxis,long t,TaxiGUI taxigui){
		/* 
		  @MODIFIES: cityMap, taxis, startTime, gui;
		  */
		cityMap = tmp_map;
		taxis = tmp_taxis;
		startTime = t;
		gui = taxigui;
	}
	
	public void run(){
		/*
		  @MODIFIES: taxis, lastTime;
		  */
		while(true){
			updateFlow();
			yield();
		}
	}
	
	public void setOpen(int i1,int j1,int i2,int j2) {
		/*
		  @MODIFIES: cityMap;
		  @EFFECTS: ((i1,j1)和(i2,j2)都在地图内 && (i1,j1)和(i2,j2)相邻 && (i1,j1)和(i2,j2)之间是断开的) ==> Open((i1,j1),(i2,j2));
		  */
		point P1 = cityMap[i1][j1];
		point P2 = cityMap[i2][j2];
		
		int dir = judgeSet(i1, j1, i2, j2);
		if(dir == 0){	
			System.out.println("("+i1+","+j1+") and ("+i2+","+j2+") setOpen wrong");
			return;
		}
		
		for(int i=0;i<P1.getLinkSize();i++){
			if(P2.equals(P1.link.get(i))){
				System.out.println("("+i1+","+j1+") and ("+i2+","+j2+") already linked");
				return;
			}
		}
		P1.setOpen(P2, dir);
		P2.setOpen(P1, -dir);
		gui.SetRoadStatus(new Point(i1, j1), new Point(i2, j2), 1);
	}
	
	public void setClose(int i1,int j1,int i2,int j2) {
		/*
		  @MODIFIES: cityMap;
		  @EFFECTS: ((i1,j1)和(i2,j2)都在地图内 && (i1,j1)和(i2,j2)相邻 && (i1,j1)和(i2,j2)之间是连通的) ==> Close((i1,j1),(i2,j2));
		  */
		point P1 = cityMap[i1][j1];
		point P2 = cityMap[i2][j2];
		
		int dir = judgeSet(i1, j1, i2, j2);
		if(dir == 0){	
			System.out.println("("+i1+","+j1+") and ("+i2+","+j2+") setClose wrong");
			return;
		}
		
		for(int i=0;i<P1.getLinkSize();i++){
			if(P2.equals(P1.link.get(i))){
				for(int j=0;j<100;j++){
					if(P1.equals(taxis[i].getPosition())&&P2.equals(taxis[i].getnextPoint())){
						System.out.println("There are taxis on ("+i1+","+j1+") to ("+i2+","+j2+")");
						return;
					}
				}
				P1.setClose(P2, dir);
				P2.setClose(P1, -dir);
				gui.SetRoadStatus(new Point(i1, j1), new Point(i2, j2), 0);
				return;
			}
		}
		System.out.println("("+i1+","+j1+") and ("+i2+","+j2+") already closed");
	}
	
	private int judgeSet(int i1,int j1,int i2,int j2){
		/*
		  @MODIFIES: None;
		  @EFFECTS: (i1<0||i1>=80||j1<0||j1>=80||i2<0||i2>=80||j2<0||j2>=80) ==> \result = 0;
		  			((i1+1==i2)&&(j1==j2))	==> \result = -1;
		  			((i1==i2+1)&&(j1==j2)) ==> \result = 1;
		  			((i1==i2)&&(j1+1==j2)) ==> \result = -2;
		  			((i1==i2)&&(j1==j2+1)) ==> \result = 2;
		  */
		if(i1<0||i1>=80||j1<0||j1>=80||i2<0||i2>=80||j2<0||j2>=80)
			return 0;
		if((i1+1==i2)&&(j1==j2))	return -1;
		if((i1==i2+1)&&(j1==j2))	return 1;
		if((i1==i2)&&(j1+1==j2))	return -2;
		if((i1==i2)&&(j1==j2+1))	return 2;
		return 0;
	}
	
	private void updateFlow(){
		/*
		  @MODIFIES: cityMap;
		  */
		//System.out.println(getTime()+" refresh");
		for(int i=0;i<80;i++){
			for(int j=0;j<80;j++){
				cityMap[i][j].refreshFlow();
			}
		}
		try {
			sleep(200);
		} catch (InterruptedException e) {}
	}
	

	
	public double getTime() {
		/* 
		  @MODIFIES: None;
		  @EFFECTS: \result == Double((System.currentTimeMillis()-startTime)/100);
		  */
		double t = (System.currentTimeMillis() - startTime)/100;
		t = Double.parseDouble(new DecimalFormat("#0.0").format(t));
		return t;
	}
	
}
