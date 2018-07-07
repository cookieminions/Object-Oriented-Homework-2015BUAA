package taxi;

/* Overview: 这个类包括了main方法 是程序入口
 */
public class Taxi_Main {
	public boolean repOK(){
		/* 
		  @EFFECTS: \result == true;
		  */
		return true;
	}
	
	public static void main(String[] args) {
		/*@REQUIRES: 文件(D:\\city_map.txt)存在 && 文件中的点构成连通图;
		  @MODIFIES: None;
		  */
		long startTime = System.currentTimeMillis();
		
		ReadMap city_map = new ReadMap();
		boolean MapCorrect = city_map.readMap();
		
		if(MapCorrect){
			int[][] map = city_map.tmpMap;
			TaxiGUI gui = new TaxiGUI();
			gui.LoadMap(map, 80);
			point[][] cityMap = city_map.get_cityMap(); 
			ReadLights city_lights = new ReadLights(cityMap,gui);
			boolean LightsCorrect = city_lights.readLights();
			if(LightsCorrect){
				requestList reqlist = new requestList(startTime);
				Taxi_Sys taxi_Sys = new Taxi_Sys(cityMap, reqlist, startTime, gui);
				input_handle iHandle = new input_handle(startTime, reqlist);
				CityMap newMap = new CityMap(cityMap, taxi_Sys.taxis, startTime, gui);
				Lights_Sys lights_Sys = new Lights_Sys(cityMap,gui);
				
				iHandle.start();
				taxi_Sys.start();
				newMap.start();
				lights_Sys.start();
				
				demothread demot = new demothread(reqlist, taxi_Sys,newMap);
				demot.start();
			}
		}else{
			System.out.println("cityMap error");
		}
	}
}