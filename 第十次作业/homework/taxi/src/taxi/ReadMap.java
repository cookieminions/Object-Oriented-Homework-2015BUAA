package taxi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class ReadMap {
	/* Overview: 这个类提供了读取城市地图信息并构建出程序需要的地图(point数组)的方法
	 */
	
	point [][] cityMap;
	int [][] tmpMap;
	
	public boolean repOK(){
		/* 
		  @EFFECTS: (cityMap==null||tmpMap==null) ==> \result == false;
		  			(cityMap.length!=80||tmpMap.length!=80) ==> \result == false;
		  			(\exist int i;0<=i<80;(cityMap[i].length!=80||tmpMap[i].length!=80)) ==> \result == false;
		  			(文件("D:\\lights_map.txt")不存在) ==> \result == false;
		  			(其他情况成立) ==> \result == true;
		  */
		if(cityMap==null||tmpMap==null)	return false;
		if(cityMap.length!=80||tmpMap.length!=80)	return false;
		for(int i=0;i<80;i++){
			if(cityMap[i].length!=80||tmpMap[i].length!=80)	return false;
		}
		File file = new File("D:\\city_map.txt");
		if(!file.exists())	return false;
		
		return true;
	}
	
	public ReadMap() {
		/*
		  @MODIFIES: cityMap, tmpMap;
		  */
		cityMap = new point[80][80];
		tmpMap = new int[80][80];
		for(int i=0;i<80;i++){
			for(int j=0;j<80;j++){
				cityMap[i][j] = null;
			}
		}
	}
	
	public boolean readMap() {
		/*@REQUIRES: 文件(D:\\city_map.txt)存在 && 文件中的点构成连通图;
		  @MODIFIES: cityMap, tmpMap;
		  @EFFECTS: (文件(D:\\city_map.txt)存在 && 文件中的点符合指导书要求) ==> \result == true;
		  			(文件(D:\\city_map.txt)不存在 || 文件中的点不符合指导书要求) ==> \result == false;
		  */
		String path = "D:\\city_map.txt";
		File file = new File(path);
		try {
			if(file.exists()){
				FileReader fReader = new FileReader(file);
				BufferedReader reader = new BufferedReader(fReader);
				String str = "";	int line = 0;
				while((str = reader.readLine())!=null){
					if(line<80){
						if(!drawMap(str,line))	break;
						line++;
					}else{
						line++;
						if(line>80)	break;
					}
				}if(line!=80){
					reader.close();
					System.out.println("cityMap error");
					return false;
				}
				reader.close();
			}
		} 
		catch (FileNotFoundException e) {return false;} 
		catch (IOException e) {return false;}
		return true;
	}
	
	private boolean drawMap(String str,int line) {
		/*
		  @MODIFIES: cityMap, tmpMap;
		  @EFFECTS: (checkLine()) ==> \result == true;
		  			(!checkLine()) ==> \result == false;
		  */
		str = str.replaceAll(" ", "");
		str = str.replaceAll("\t", "");
		boolean judge = checkLine(str,line);
		if(judge){
			for(int i=0;i<80;i++){
				if(cityMap[line][i]==null){
					cityMap[line][i] = new point();
				}
				int signal = str.charAt(i)-'0';
				cityMap[line][i].setPoint(signal, line, i, cityMap);
				tmpMap[line][i] = signal;
			}
		}
		return judge;
	}
	
	private boolean checkLine(String str,int line) {
		/*
		  @MODIFIES: None;
		  @EFFECTS: (\all int i; 0<=i&&i<str.length(); (str.charAt(i)-'0')满足指导书要求) ==> \result == true;
		  			(\exist int i; 0<=i&&i<str.length(); (str.charAt(i)-'0')不满足指导书要求) ==> \result == false;
		  */
		String regEx = "([0123]){80}";
		if(str.matches(regEx)){
			for(int i=0;i<80;i++){
				int signal = str.charAt(i)-'0';
				if(i==79&&signal==1)	return false;
				if(line==79&&(signal==2||signal==3))	return false;
			}
			return true;
		}
		else
			return false;
	}
	
	public point[][] get_cityMap() {
		/*
		  @MODIFIES: None;
		  @EFFECTS: \result == cityMap;
		  */
		return cityMap;
	}
	
	

}
