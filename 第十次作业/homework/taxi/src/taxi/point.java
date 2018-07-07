package taxi;

import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class point {
	/* Overview: 这个类表示了地图中的点, 存储了点在地图中的位置和连通情况以及连通边的流量, 存储了点上红绿灯的状态; 还提供一系列查询状态和设置状态的方法
	 */
	
	int x;
	int y;
	point upPoint;
	point downPoint;
	point leftPoint;
	point rightPoint;
	Vector<point> link;
	int[] flow;
	
	AtomicInteger light;
	
	public boolean repOK(){
		/* 
		  @EFFECTS: (flow==null||link==null) ==> \result == false;
		  			(flow.length!=4||link.size()==0) ==> \result == false;
		  			(x<0||x>=80||y<0||y>=80) ==> \result == false;
		  			(light.get()!=0&&light.get()!=1&&light.get()!=-1) ==> \result == false;
		  			(其他情况成立) ==> \result == true;
		  */
		if(flow==null||link==null)	return false;
		if(flow.length!=4||link.size()==0)	return false;
		if(x<0||x>=80||y<0||y>=80)	return false;
		if(light.get()!=0&&light.get()!=1&&light.get()!=-1)	return false;
		
		return true;
	}
	
	point(){
		/*
		  @MODIFIES: upPoint, downPoint, leftPoint, rightPoint, link, flow, linkPoints;
		  */
		upPoint = null;
		downPoint = null;
		leftPoint = null;
		rightPoint = null;
		link = new Vector<point>();
		flow = new int[4];
		light = new AtomicInteger(-1);
	}
	
	
	public void setPoint(int signal,int i,int j,point[][] cityMap){
		/*
		  @MODIFIES: x, y, upPoint, downPoint, leftPoint, rightPoint, link;
		  */
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
		if(rightPoint!=null)link.add(rightPoint);
		if(leftPoint!=null)	link.add(leftPoint);
		
		if(upPoint!=null)	flow[0] = 0;else	flow[0] = 999999;
		if(downPoint!=null)	flow[1] = 0;else	flow[1] = 999999;
		if(leftPoint!=null)	flow[2] = 0;else	flow[2] = 999999;
		if(rightPoint!=null)flow[3] = 0;else	flow[3] = 999999;
	}
	
	public synchronized void setOpen(point p,int i){
		/* 
		  @MODIFIES: link, pPoint, downPoint, leftPoint, rightPoint, flow;
		  @EFFECTS: (i==1) ==> link.add(p) && upPoint = p && this.setFlow(0, 0);
		  			(i==-1) ==> link.add(p) && downPoint = p && this.setFlow(1, 0);
		  			(i==2) ==> link.add(p) && leftPoint = p && this.setFlow(2, 0);
		  			(i==-2) ==> link.add(p) && rightPoint = p && this.setFlow(3, 0);
		  @THREAD_EFFECTS: \locked();
		  */
		link.add(p);
		if(i==1)	{upPoint = p; this.setFlow(0, 0);}
		else if(i==-1)	{downPoint = p; this.setFlow(1, 0);}
		else if(i==2)	{leftPoint = p; this.setFlow(2, 0);}
		else if(i==-2)	{rightPoint = p; this.setFlow(3, 0);}
	}
	
	public synchronized void setClose(point p,int i){
		/* 
		  @MODIFIES: link, pPoint, downPoint, leftPoint, rightPoint, flow;
		  @EFFECTS: (i==1) ==> link.remove(p) && upPoint = null && this.setFlow(0, 999999);
		  			(i==-1) ==> link.remove(p) && downPoint = null && this.setFlow(1, 999999);
		  			(i==2) ==> link.remove(p) && leftPoint = null && this.setFlow(2, 999999);
		  			(i==-2) ==> link.remove(p) && rightPoint = null && this.setFlow(3, 999999);
		  @THREAD_EFFECTS: \locked();
		  */
		link.remove(p);
		if(i==1)	{upPoint = null; this.setFlow(0, 999999);}
		else if(i==-1)	{downPoint = null; this.setFlow(1, 999999);}
		else if(i==2)	{leftPoint = null; this.setFlow(2, 999999);}
		else if(i==-2)	{rightPoint = null; this.setFlow(3, 999999);}
	}
	
	public boolean equals(point t){
		/* 
		  @MODIFIES: None; 
		  @EFFECTS: (t!=null&&x==t.x&&y==t.y) ==> \result == true;
		  			(!(t!=null&&x==t.x&&y==t.y)) ==> \result == false;
		  */
		if(t!=null&&x==t.x&&y==t.y)	return true;
		return false;
	}
	//link
	public synchronized point getNext(int i){
		/* 
		  @MODIFIES: None; 
		  @EFFECTS: (i<link.size()) ==> \result == link.get(i);
		  			(i>=link.size()) ==> \result == null;
		  @THREAD_EFFECTS: \locked();
		  */
		if(i<link.size())	return link.get(i);
		return null;
	}
	public synchronized int getLinkSize() {
		/* 
		  @MODIFIES: None; 
		  @EFFECTS: \result == link.size();
		  @THREAD_EFFECTS: \locked();
		  */
		return link.size();
	}
	//flow
	public synchronized void addFlow(int i,int d) {
		/* 
		  @MODIFIES: flow; 
		  @THREAD_EFFECTS: \locked();
		  */
		flow[i] += d;
	}
	public synchronized void setFlow(int i,int d) {
		/* 
		  @MODIFIES: flow;
		  @THREAD_EFFECTS: \locked();
		  */
		flow[i] = d;
	}
	public synchronized int getFlow(int i){
		/* 
		  @MODIFIES: None; 
		  @EFFECTS: \result == flow[i];
		  @THREAD_EFFECTS: \locked();
		  */
		return flow[i];
	}
	public synchronized int getFlow(point p){
		/* 
		  @MODIFIES: None; 
		  @EFFECTS: (p.equals(upPoint)||其他情况) ==> \result = flow[0];
		  			(p.equals(downPoint)) ==> \result = flow[1];
		  			(p.equals(leftPoint)) ==> \result = flow[2];
		  			(p.equals(rightPoint)) ==> \result = flow[3];
		  @THREAD_EFFECTS: \locked();
		  */
		if(p.equals(upPoint))			return flow[0];
		else if(p.equals(downPoint))	return flow[1];
		else if(p.equals(leftPoint))	return flow[2];
		else if(p.equals(rightPoint))	return flow[3];
		else return flow[0];
	}
	public synchronized void refreshFlow() {
		/* 
		  @MODIFIES: flow; 
		  @THREAD_EFFECTS: \locked();
		  */
		if(upPoint!=null)	flow[0] = 0;else	flow[0] = 999999;
		if(downPoint!=null)	flow[1] = 0;else	flow[1] = 999999;
		if(leftPoint!=null)	flow[2] = 0;else	flow[2] = 999999;
		if(rightPoint!=null)flow[3] = 0;else	flow[3] = 999999;
	}
	//getPoint
	public synchronized point getMinFlow(int i){
		/* 
		  @MODIFIES: None; 
		  @EFFECTS: (i==0) ==> \result = upPoint;
		  			(i==1) ==> \result = downPoint;
		  			(i==2) ==> \result = leftPoint;
		  			(i==3) ==> \result = rightPoint;
		  			(其他情况) ==> \result = null;
		  @THREAD_EFFECTS: \locked();
		  */
		if(i==0)		return upPoint;
		else if(i==1)	return downPoint;
		else if(i==2)	return leftPoint;
		else if(i==3)	return rightPoint;
		else return null;
	}
	public synchronized void calFlow(point nextPoint){
		/* 
		  @MODIFIES: flow;
		  @THREAD_EFFECTS: \locked();
		  */
		if(nextPoint.equals(upPoint)){
			this.addFlow(0, 1);
			nextPoint.addFlow(1, 1);
		}else if(nextPoint.equals(downPoint)){
			this.addFlow(1, 1);
			nextPoint.addFlow(0, 1);
		}else if(nextPoint.equals(leftPoint)){
			this.addFlow(2, 1);
			nextPoint.addFlow(3, 1);
		}else if(nextPoint.equals(rightPoint)){
			this.addFlow(3, 1);
			nextPoint.addFlow(2, 1);
		}
	}
	
	public String toString(){
		/* 
		  @MODIFIES: None;
		  @EFFECTS: \result == "("+x+","+y+")";
		  */
		return "("+x+","+y+")";
	}
}
