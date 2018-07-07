package taxi;

public class Taxi_Main {
	public static void main(String[] args) {
		/*@REQUIRES: 文件(D:\\city_map.txt)存在 && 文件中的点构成连通图;
		  @MODIFIES: None;
		  */
		long startTime = System.currentTimeMillis();
		
		ReadMap city_map = new ReadMap();
		boolean MapCorrect = city_map.readMap();
		int[][] map = city_map.tmpMap;
		
		TaxiGUI gui = new TaxiGUI();
		gui.LoadMap(map, 80);
		
		if(MapCorrect){
			point[][] cityMap = city_map.get_cityMap(); 
			requestList reqlist = new requestList(startTime);
			Taxi_Sys taxi_Sys = new Taxi_Sys(cityMap, reqlist, startTime, gui);
			input_handle iHandle = new input_handle(startTime, reqlist);
			CityMap newMap = new CityMap(cityMap, taxi_Sys.taxis, startTime, gui);
			
			iHandle.start();
			taxi_Sys.start();
			newMap.start();
			
			demothread demot = new demothread(reqlist, taxi_Sys,newMap);
			demot.start();
		}else{
			System.out.println("cityMap error");
		}
	}
}