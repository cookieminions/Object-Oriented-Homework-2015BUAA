package elevator;

import elevator.Query.Direction;

public class TestQueueList {
	public static void main(String[] args) throws UnsortedException {
		
	QueryList ql=new QueryList(10,1);
	System.out.println("remove from empty queue:"+ql.remove(3));
	
	ql.append(new Query(10,0.0));
	ql.append(new Query(8,1.0,Query.Direction.UP));
	ql.append(new Query(5,1.5,Query.Direction.DOWN));
	
	System.out.println("remove an nonexistent element from the queue:"+ql.remove(-1));
	System.out.println("remove the first element from the queue:"+ql.remove(0));
	System.out.println("remove an exceeded element from the queue:"+ql.remove(4));
	
	}


}
