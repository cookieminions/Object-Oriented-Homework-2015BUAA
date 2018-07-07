package taxi;

import java.util.Vector;

public class RouteBFS {
	private point[][] cityMap;
	
	public RouteBFS(point[][] map) {
		cityMap = map;
	}
	
	public void BFS(point a,point b,Vector<point> visited,point[][] tmpMap){
		Vector<point> tmpqueue = new Vector<point>();
		tmpqueue.add(a);
		while(tmpqueue.size()!=0){
			int i=0;
			point vPoint = tmpqueue.remove(0);
			point wPoint = vPoint.getNext(i++);
			
			while(wPoint!=null){
				if(visited.indexOf(wPoint)==-1){//wPoint not visited
					tmpqueue.add(wPoint);
					visited.add(wPoint);
					tmpMap[wPoint.x][wPoint.y] = vPoint;
					if(vPoint.equals(b))	return;
				}
				wPoint = vPoint.getNext(i++);
			}
		}
	}
	
	public Vector<point> findroute(point a,point b){
		Vector<point> visited = new Vector<point>();
		point[][] tmpMap = new point[80][80];
		Vector<point> route = new Vector<point>();
		point aNew = cityMap[a.x][a.y];
		point bNew = cityMap[b.x][b.y];
		
		BFS(aNew, bNew, visited, tmpMap);
		point lastPoint = bNew;
		while(!lastPoint.equals(aNew)){
			route.add(0,lastPoint);
			lastPoint = tmpMap[lastPoint.x][lastPoint.y];
		}
		route.add(0,aNew);
		return route;
	}
	

}
