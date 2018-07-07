package taxi;

import java.util.Vector;

public class point {
	int x;
	int y;
	point upPoint;
	point downPoint;
	point leftPoint;
	point rightPoint;
	Vector<point> link;
	
	point(){
		upPoint = null;
		downPoint = null;
		leftPoint = null;
		rightPoint = null;
		link = new Vector<point>();
	}
	
	
	public void setPoint(int signal,int i,int j,point[][] cityMap){
		x = i;
		y = j;
		if(signal==0){
			rightPoint = null;
			downPoint = null;
		}else if(signal==1){
			if(cityMap[i][j+1]==null){
				cityMap[i][j+1] = new point();
			}
			rightPoint = cityMap[i][j+1];
			cityMap[i][j+1].leftPoint = cityMap[i][j];
			downPoint = null;
		}else if(signal==2){
			rightPoint = null;
			if(cityMap[i+1][j]==null){
				cityMap[i+1][j] = new point();
			}
			downPoint = cityMap[i+1][j];
			cityMap[i+1][j].upPoint = cityMap[i][j];
		}else if(signal==3){
			if(cityMap[i][j+1]==null){
				cityMap[i][j+1] = new point();
			}
			rightPoint = cityMap[i][j+1];
			cityMap[i][j+1].leftPoint = cityMap[i][j];
			if(cityMap[i+1][j]==null){
				cityMap[i+1][j] = new point();
			}
			downPoint = cityMap[i+1][j];
			cityMap[i+1][j].upPoint = cityMap[i][j];
		}
		if(upPoint!=null)	link.add(upPoint);
		if(downPoint!=null)	link.add(downPoint);
		if(rightPoint!=null)	link.add(rightPoint);
		if(leftPoint!=null)	link.add(leftPoint);
	}
	public boolean equals(point t){
		if(x==t.x&&y==t.y)	return true;
		return false;
	}
	public int distance(point t){
		if(upPoint.equals(t)||downPoint.equals(t)||rightPoint.equals(t)||leftPoint.equals(t))
			return 1;
		return 9999999;
	}
	public point getNext(int i){
		if(i<link.size())	return link.get(i);
		return null;
	}
	public String toString(){
		return "("+x+","+y+")";
	}
}
