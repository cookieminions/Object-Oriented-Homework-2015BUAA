package taxi;

import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

/* Overview: 这个类表示了地图中的点, 存储了点在地图中的位置和连通情况以及连通边的流量, 存储了点上红绿灯的状态; 还提供一系列查询状态和设置状态的方法
 * Objects: upPoint, downPoint, leftPoint, rightPoint, link, linkPoints, origin_link, origin_upPoint, origin_downPoint, origin_leftPoint, origin_rightPoint;
 * point类通过setPoint方法完成对类周围连通情况的初始化
 * point类的setClose和setOpen方法完成对连通情况的修改
 * point类通过一系列flow的操作完成流量的更改和选择
 * invariant: !((nflow==null||link==null)||(nflow.length()!=4||link.size()==0)||
 * 				(x<0||x>=80||y<0||y>=80)||(light.get()!=0&&light.get()!=1&&light.get()!=-1))
 */
public class point {	
	int x;
	int y;
	point upPoint; point downPoint; point leftPoint; point rightPoint;
	point origin_upPoint; point origin_downPoint; point origin_leftPoint; point origin_rightPoint;
	Vector<point> link;
	Vector<point> origin_link;
	
	AtomicInteger light;
	AtomicIntegerArray nflow;
	
	public boolean repOK(){
		/* 
		  @EFFECTS: (nflow==null||link==null) ==> \result == false;
		  			(nflow.length()!=4||link.size()==0) ==> \result == false;
		  			(x<0||x>=80||y<0||y>=80) ==> \result == false;
		  			(light.get()!=0&&light.get()!=1&&light.get()!=-1) ==> \result == false;
		  			(其他情况成立) ==> \result == true;
		  */
		if(nflow==null||link==null)	return false;
		if(nflow.length()!=4||link.size()==0)	return false;
		if(x<0||x>=80||y<0||y>=80)	return false;
		if(light.get()!=0&&light.get()!=1&&light.get()!=-1)	return false;
		
		return true;
	}
	
	point(){
		/*
		  @MODIFIES: upPoint, downPoint, leftPoint, rightPoint, link, linkPoints, origin_link, origin_upPoint, origin_downPoint, origin_leftPoint, origin_rightPoint;
		  */
		upPoint = null;	downPoint = null; leftPoint = null;	rightPoint = null;
		origin_upPoint = null; origin_downPoint = null; origin_leftPoint = null; origin_rightPoint = null;
		link = new Vector<point>();		origin_link = new Vector<point>();
		light = new AtomicInteger(-1);
		nflow = new AtomicIntegerArray(4);
	}
	
	
	public void setPoint(int signal,int i,int j,point[][] cityMap){
		/*
		  @MODIFIES: x, y, upPoint, downPoint, leftPoint, rightPoint, link, origin_link, origin_upPoint, origin_downPoint, origin_leftPoint, origin_rightPoint;
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
			rightPoint = cityMap[i][j+1];	/*origin_upPoint=cityMap[i][j+1];*/
			cityMap[i][j+1].leftPoint = cityMap[i][j];	/*cityMap[i][j+1].origin_leftPoint = cityMap[i][j];*/
			downPoint = null;
		}else if(signal==2){
			rightPoint = null;
			if(cityMap[i+1][j]==null){
				cityMap[i+1][j] = new point();
			}
			downPoint = cityMap[i+1][j];	/*origin_downPoint = cityMap[i+1][j];*/
			cityMap[i+1][j].upPoint = cityMap[i][j];	/*cityMap[i+1][j].origin_upPoint = cityMap[i][j];*/
		}else if(signal==3){
			if(cityMap[i][j+1]==null){
				cityMap[i][j+1] = new point();
			}
			rightPoint = cityMap[i][j+1];	/*origin_rightPoint = cityMap[i][j+1];*/
			cityMap[i][j+1].leftPoint = cityMap[i][j];	/*cityMap[i][j+1].origin_leftPoint = cityMap[i][j];*/
			if(cityMap[i+1][j]==null){
				cityMap[i+1][j] = new point();
			}
			downPoint = cityMap[i+1][j];	/*origin_downPoint = cityMap[i+1][j];*/
			cityMap[i+1][j].upPoint = cityMap[i][j];	/*cityMap[i+1][j].origin_upPoint = cityMap[i][j];*/
		}
		if(upPoint!=null)	{link.add(upPoint);origin_link.add(upPoint);origin_upPoint=upPoint;}
		if(downPoint!=null)	{link.add(downPoint);origin_link.add(downPoint);origin_downPoint=downPoint;}
		if(rightPoint!=null){link.add(rightPoint);origin_link.add(rightPoint);origin_rightPoint=rightPoint;}
		if(leftPoint!=null)	{link.add(leftPoint);origin_link.add(leftPoint);origin_leftPoint=leftPoint;}
		
		if(upPoint!=null)	nflow.getAndSet(0, 0);else	nflow.getAndSet(0, 999999);
		if(downPoint!=null)	nflow.getAndSet(1, 0);else	nflow.getAndSet(1, 999999);
		if(leftPoint!=null)	nflow.getAndSet(2, 0);else	nflow.getAndSet(2, 999999);
		if(rightPoint!=null)nflow.getAndSet(3, 0);else	nflow.getAndSet(3, 999999);
	}
	
	public synchronized void setOpen(point p,int i){
		/* 
		  @MODIFIES: link, pPoint, downPoint, leftPoint, rightPoint, nflow;
		  @EFFECTS: (i==1) ==> link.add(p) && upPoint = p && nflow.setFlow(0, 0);
		  			(i==-1) ==> link.add(p) && downPoint = p && nflow.setFlow(1, 0);
		  			(i==2) ==> link.add(p) && leftPoint = p && nflow.setFlow(2, 0);
		  			(i==-2) ==> link.add(p) && rightPoint = p && nflow.setFlow(3, 0);
		  @THREAD_EFFECTS: \locked();
		  */
		link.add(p);
		if(i==1)		{upPoint = p; 	nflow.getAndSet(0, 0);}
		else if(i==-1)	{downPoint = p; nflow.getAndSet(1, 0);}
		else if(i==2)	{leftPoint = p; nflow.getAndSet(2, 0);}
		else if(i==-2)	{rightPoint = p;nflow.getAndSet(3, 0);}
	}
	
	public synchronized void setClose(point p,int i){
		/* 
		  @MODIFIES: link, pPoint, downPoint, leftPoint, rightPoint, nflow;
		  @EFFECTS: (i==1) ==> link.remove(p) && upPoint = null && nflow.setFlow(0, 666666);
		  			(i==-1) ==> link.remove(p) && downPoint = null && nflow.setFlow(1, 666666);
		  			(i==2) ==> link.remove(p) && leftPoint = null && nflow.setFlow(2, 666666);
		  			(i==-2) ==> link.remove(p) && rightPoint = null && nflow.setFlow(3, 666666);
		  @THREAD_EFFECTS: \locked();
		  */
		link.remove(p);
		if(i==1)		{upPoint = null; 	nflow.getAndSet(0, 666666);}
		else if(i==-1)	{downPoint = null;	nflow.getAndSet(1, 666666);}
		else if(i==2)	{leftPoint = null; 	nflow.getAndSet(2, 666666);}
		else if(i==-2)	{rightPoint = null; nflow.getAndSet(3, 666666);}
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
	//origin_link
	public point getNext4N(int i){
		/* 
		  @MODIFIES: None; 
		  @EFFECTS: (i<origin_link.size()) ==> \result == origin_link.get(i);
		  			(i>=origin_link.size()) ==> \result == null;
		  */
		if(i<origin_link.size())	return origin_link.get(i);
		return null;
	}
	//flow
	public int getFlow(point p){
		/* 
		  @MODIFIES: None; 
		  @EFFECTS: (p.equals(upPoint)||其他情况) ==> \result = nflow[0];
		  			(p.equals(downPoint)) ==> \result = nflow[1];
		  			(p.equals(leftPoint)) ==> \result = nflow[2];
		  			(p.equals(rightPoint)) ==> \result = nflow[3];
		  @THREAD_EFFECTS: \locked();
		  */
		if(p.equals(upPoint))			return nflow.get(0);
		else if(p.equals(downPoint))	return nflow.get(1);
		else if(p.equals(leftPoint))	return nflow.get(2);
		else if(p.equals(rightPoint))	return nflow.get(3);
		else return nflow.get(0);
	}
	public void refreshFlow() {
		/* 
		  @MODIFIES: nflow; 
		  @THREAD_EFFECTS: \locked();
		  */
		if(upPoint!=null)	nflow.getAndSet(0, 0);else	nflow.getAndSet(0, nflow.get(0));
		if(downPoint!=null)	nflow.getAndSet(1, 0);else	nflow.getAndSet(1, nflow.get(1));
		if(leftPoint!=null)	nflow.getAndSet(2, 0);else	nflow.getAndSet(2, nflow.get(2));
		if(rightPoint!=null)nflow.getAndSet(3, 0);else	nflow.getAndSet(3, nflow.get(3));
	}
	//getPoint
	public point getMinFlow(int i,int f){
		/* 
		  @MODIFIES: None; 
		  @EFFECTS: (i==0) ==> \result = f==1?upPoint:origin_upPoint;
		  			(i==1) ==> \result = f==1?downPoint:origin_downPoint;
		  			(i==2) ==> \result = f==1?leftPoint:origin_downPoint;
		  			(i==3) ==> \result = f==1?rightPoint:origin_downPoint;
		  			(其他情况) ==> \result = null;
		  @THREAD_EFFECTS: \locked();
		  */
		if(i==0)		return f==1?upPoint:origin_upPoint;
		else if(i==1)	return f==1?downPoint:origin_downPoint;
		else if(i==2)	return f==1?leftPoint:origin_leftPoint;
		else if(i==3)	return f==1?rightPoint:origin_rightPoint;
		else return null;
		/*if(i==0)		return upPoint;
		else if(i==1)	return downPoint;
		else if(i==2)	return leftPoint;
		else if(i==3)	return rightPoint;
		else return null;*/
	}
	public void calFlow(point nextPoint){
		/* 
		  @MODIFIES: nflow;
		  @THREAD_EFFECTS: \locked();
		  */
		if(nextPoint.equals(upPoint)){
			nflow.getAndIncrement(0);
			nextPoint.nflow.getAndIncrement(1);
		}else if(nextPoint.equals(downPoint)){
			nflow.getAndIncrement(1);
			nextPoint.nflow.getAndIncrement(0);
		}else if(nextPoint.equals(leftPoint)){
			nflow.getAndIncrement(2);
			nextPoint.nflow.getAndIncrement(3);
		}else if(nextPoint.equals(rightPoint)){
			nflow.getAndIncrement(3);
			nextPoint.nflow.getAndIncrement(2);
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
