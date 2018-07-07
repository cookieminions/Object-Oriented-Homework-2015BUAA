package taxi;

import java.util.Vector;

public class RouteBFS {
	private point[][] cityMap;
	
	public RouteBFS(point[][] map) {
		/*
		  @MODIFIES: cityMap;
		  */
		cityMap = map;
	}
	
	public void BFS(point a,point b,Vector<point> visited,point[][] tmpMap,Vector<point> currPoints,int[][] routeFlow){
		/* 
		  @MODIFIES: None; 
		  */
		Vector<point> tmpqueue = new Vector<point>();
		tmpqueue.add(a);
		while(tmpqueue.size()!=0){
			int i=0;
			point vPoint = tmpqueue.remove(0);
			point wPoint = vPoint.getNext(i++);
			
			while(wPoint!=null){
				if(visited.indexOf(wPoint)==-1){//wPoint not visited
					if(!currPoints.contains(wPoint))	currPoints.add(wPoint);
					if(tmpMap[wPoint.x][wPoint.y]!=null){
						if(routeFlow[wPoint.x][wPoint.y]>routeFlow[vPoint.x][vPoint.y]+vPoint.getFlow(wPoint)){//cal Flow
							tmpMap[wPoint.x][wPoint.y] = vPoint;
							routeFlow[wPoint.x][wPoint.y] = routeFlow[vPoint.x][vPoint.y]+vPoint.getFlow(wPoint);
						}
					}else{
						tmpMap[wPoint.x][wPoint.y] = vPoint;
						routeFlow[wPoint.x][wPoint.y] = vPoint.getFlow(wPoint);
					}
				}
				wPoint = vPoint.getNext(i++);
			}
			if(tmpqueue.size()==0){
				while(currPoints.size()!=0){
					point tmpPoint = currPoints.remove(0);
					if(tmpPoint.equals(b))	return;
					visited.add(tmpPoint);
					tmpqueue.add(tmpPoint);
				}
			}
		}
	}
	
	public Vector<point> findroute(point a,point b){
		/* 
		  @MODIFIES: None; 
		  @EFFECTS: (a与b存在最短路径) ==> \result = (流量最少的最短路径上的点组成的Vector);
		  */
		Vector<point> visited = new Vector<point>();
		Vector<point> currPoints = new Vector<point>();
		point[][] tmpMap = new point[80][80];
		int[][] routeFlow = new int[80][80];
		Vector<point> route = new Vector<point>();
		point aNew = cityMap[a.x][a.y];
		point bNew = cityMap[b.x][b.y];
		
		BFS(aNew, bNew, visited, tmpMap, currPoints, routeFlow);
		point lastPoint = bNew;
		while(!lastPoint.equals(aNew)){
			route.add(0,lastPoint);
			lastPoint = tmpMap[lastPoint.x][lastPoint.y];
		}
		route.add(0,aNew);
		
		return route;
	}
}
