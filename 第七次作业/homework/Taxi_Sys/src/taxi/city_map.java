package taxi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class city_map {
	point [][] cityMap;
	int [][] tmpMap;
	
	public city_map() {
		cityMap = new point[80][80];
		tmpMap = new int[80][80];
		for(int i=0;i<80;i++){
			for(int j=0;j<80;j++){
				cityMap[i][j] = null;
			}
		}
	}
	
	public boolean readMap() {
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
		return cityMap;
	}

}
