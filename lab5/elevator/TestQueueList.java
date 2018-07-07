package elevator;

import elevator.Query.Direction;

public class TestQueueList {
	public static void main(String[] args) throws Exception {
		
	System.out.println("-------test remove()----------");
	QueryList ql=new QueryList(10,1);
	try {
		System.out.println("remove from empty queue:"+ql.remove(3));
	} catch (EmptyQueueException | InvalidIndexException e1) {
		e1.printStackTrace();
	}
	
	ql.append(new Query(10,0.0));
	ql.append(new Query(8,1.0,Query.Direction.UP));
	ql.append(new Query(5,1.5,Query.Direction.DOWN));
	
	try {
		System.out.println("remove an nonexistent element from the queue:"+ql.remove(-1));
		System.out.println("remove the first element from the queue:"+ql.remove(0));
		System.out.println("remove an exceeded element from the queue:"+ql.remove(4));
	} catch (EmptyQueueException e) {
		e.printStackTrace();
	} catch (InvalidIndexException e) {
		e.printStackTrace();
	}
	
	//test append()
	System.out.println("-------test append()----------");
	boolean test = false;
	test = ql.append(new Query(6,1.5,Query.Direction.UP));
	test = ql.append(new Query(9,2.5,Query.Direction.UP));
	
	System.out.println("Element 0 in Query is "+ql.getQuery(0).toString());
	System.out.println("Element 1 in Query is "+ql.getQuery(1).toString());
	System.out.println("Element 2 in Query is "+ql.getQuery(2).toString());
	
	test = ql.append(new Query(8,1.0));
	if(!test){
		System.out.println("lastTime>req.queryTime cannot append ");
	}
	test = ql.append(new Query(10,2.0,Query.Direction.UP));
	if(!test){
		System.out.println("targetFloor==high&&req.queryDirection==Direction.UP cannot append ");
	}
	test = ql.append(new Query(1,2.0,Query.Direction.DOWN));
	if(!test){
		System.out.println("targetFloor==low&&req.queryDirection==Direction.DOWN cannot append ");
	}
	
	//test pickupQuery()
	System.out.println("-------test pickupQuery()----------");
	Elevator ele = new Elevator(10, 1);
	
	for(int i=0;i<ql.getSize();i++){
		ele.pickupQuery(ql.getQuery(i));
	}
	
	System.out.println(ele.getCurQuery().toString());
	if(!ele.emptyQuery()){
		System.out.println("not empty");
	}
	
	System.out.println("-------test moveForQuery() && test checkFinishedQuery()----------");
	while(!ele.emptyQuery()){
		ele.moveForQuery();
	}
	
	System.out.println("-------test moveUP()----------");
	System.out.println("before moveUP");
	System.out.println(ele.getCurFloor());
	System.out.println(ele.getCurStatus());
	System.out.println("after moveDOWN");
	if(ele.moveUP()){
		System.out.println(ele.getCurFloor());
		System.out.println(ele.getCurStatus());
	}
	
	
	}
	
	


}
