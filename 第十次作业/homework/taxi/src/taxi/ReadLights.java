package taxi;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class ReadLights {
	/* Overview: 这个类提供读取红绿灯文件并将红绿灯信息保存到地图中的方法
	 */
	
	point[][] cityMap;
	TaxiGUI gui;
	
	public boolean repOK(){
		/* 
		  @EFFECTS: (cityMap==null||gui==null) ==> \result == false;
		  			(cityMap.length!=80) ==> \result == false;
		  			(\exist int i;0<=i<80;cityMap[i].length!=80) ==> \result == false;
		  			(文件("D:\\lights_map.txt")不存在) ==> \result == false;
		  			(其他情况成立) ==> \result == true;
		  */
		if(cityMap==null||gui==null)	return false;
		if(cityMap.length!=80)	return false;
		for(int i=0;i<80;i++){
			if(cityMap[i].length!=80)	return false;
		}
		File file = new File("D:\\lights_map.txt");
		if(!file.exists())	return false;
		
		return true;
	}
	
	public ReadLights(point[][] tmpMap,TaxiGUI tmpgui) {
		/*
		  @MODIFIES: cityMap;
		  */
		cityMap = tmpMap;
		gui = tmpgui;
	}
	
	public boolean readLights() {
		/*@REQUIRES: 文件(D:\\lights_map.txt)存在 && 文件中的点为0或1;
		  @MODIFIES: cityMap;
		  @EFFECTS: (文件(D:\\lights_map.txt)存在 && 文件中的点符合指导书要求) ==> \result == true;
		  			(文件(D:\\lights_map.txt)不存在 || 文件中的点不符合指导书要求) ==> \result == false;
		  */
		String path = "D:\\lights_map.txt";
		File file = new File(path);
		try {
			if(file.exists()){
				FileReader fReader = new FileReader(file);
				BufferedReader reader = new BufferedReader(fReader);
				String str = "";	int line = 0;
				while((str = reader.readLine())!=null){
					if(line<80){
						if(!setLights(str,line))	break;
						line++;
					}else{
						line++;
						if(line>80)	break;
					}
				}if(line!=80){
					reader.close();
					System.out.println("lightsMap error");
					return false;
				}
				reader.close();
			}
		} 
		catch (FileNotFoundException e) {return false;} 
		catch (IOException e) {return false;}
		return true;
	}
	
	private boolean setLights(String str,int line){
		/*
		  @MODIFIES: cityMap, tmpMap;
		  @EFFECTS: (checkLights()) ==> \result == true;
		  			(!checkLights()) ==> \result == false;
		  */
		str = str.replaceAll(" ", "");
		str = str.replaceAll("\t", "");
		boolean judge = checkLights(str,line);
		if(judge){
			for(int i=0;i<80;i++){
				int light = str.charAt(i)-'0';
				if(light == 0){
					cityMap[line][i].light.set(-1);
					gui.SetLightStatus(new Point(line, i), 0);
				}
				else{
					Random r = new Random();
					cityMap[line][i].light.set(r.nextInt(2));
				}
			}
		}
		return judge;
	}
	private boolean checkLights(String str,int line){
		/*
		  @MODIFIES: None;
		  @EFFECTS: (\all int i; 0<=i&&i<str.length(); (str.charAt(i)-'0')==0||(str.charAt(i)-'0')==1) ==> \result == true;
		  			(\exist int i; 0<=i&&i<str.length(); (str.charAt(i)-'0')!=0&&(str.charAt(i)-'0')!=1) ==> \result == false;
		  */
		String regEx = "([01]){80}";
		if(str.matches(regEx))
			return true;
		else
			return false;
	}
}
