package elevator;

import static org.junit.Assert.*;

import org.junit.Test;

public class schedulerTest {
	static scheduler sche = new scheduler();
	static request_list req_list = new request_list();

	@Test
	public void testRepOk() {
		assertTrue(sche.repOk());
	}
	@Test
	public void testRepOkNull() {
		assertTrue(sche.repOk());
		sche.ele = null;
		assertFalse(sche.repOk());
		sche = new scheduler();
	}
	
	@Test
	public void testSchedule() {
		clear();
		int it;
		req_list.add_req(new request("(FR,1,UP,0)"));
		req_list.add_req(new request("(ER,4,0)"));
		req_list.add_req(new request("(ER,2,1)"));
		req_list.add_req(new request("(FR,2,UP,4)"));
		
		it = sche.iterator;
		sche.schedule(req_list);
		assertEquals(1, sche.ele.get_floor());
		assertEquals(1.0, sche.t, 0);
		assertEquals(sche.iterator, it+1);
		
		it = sche.iterator;
		sche.schedule(req_list);
		assertEquals(4, sche.ele.get_floor());
		assertEquals(3.5, sche.t, 0);
		assertEquals(sche.iterator, it+1);

		it = sche.iterator;
		sche.schedule(req_list);
		assertEquals(2, sche.ele.get_floor());
		assertEquals(5.5, sche.t, 0);
		assertEquals(sche.iterator, it+1);
		
		it = sche.iterator;
		sche.schedule(req_list);
		assertEquals(2, sche.ele.get_floor());
		assertEquals(6.5, sche.t, 0);
		assertEquals(sche.iterator, it+1);
	}
	
	@Test
	public void testScheduleT() {
		clear();
		req_list.add_req(new request("(FR,1,UP,0)"));
		req_list.add_req(new request("(ER,4,0)"));
		req_list.add_req(new request("(ER,2,1)"));
		req_list.add_req(new request("(FR,2,UP,4)"));
		
		sche.schedule(req_list);
		assertEquals(1.0, sche.t, 0);
		
		sche.schedule(req_list);
		assertEquals(3.5, sche.t, 0);

		sche.schedule(req_list);
		assertEquals(5.5, sche.t, 0);

		sche.schedule(req_list);
		assertEquals(6.5, sche.t, 0);
	}
	
	@Test
	public void testScheduleIterator() {
		clear();
		int it;
		req_list.add_req(new request("(FR,1,UP,0)"));
		req_list.add_req(new request("(ER,4,0)"));
		req_list.add_req(new request("(ER,2,1)"));
		req_list.add_req(new request("(FR,2,UP,10)"));
		
		it = sche.iterator;
		sche.schedule(req_list);
		assertEquals(sche.iterator, it+1);
		
		it = sche.iterator;
		sche.schedule(req_list);
		assertEquals(sche.iterator, it+1);

		it = sche.iterator;
		sche.schedule(req_list);
		assertEquals(sche.iterator, it+1);
		
		it = sche.iterator;
		sche.schedule(req_list);
		assertEquals(sche.iterator, it+1);
	}
	
	@Test
	public void testScheduleEle() {
		clear();
		req_list.add_req(new request("(FR,1,UP,0)"));
		req_list.add_req(new request("(ER,4,0)"));
		req_list.add_req(new request("(ER,2,1)"));
		req_list.add_req(new request("(FR,2,UP,4)"));
		
		sche.schedule(req_list);
		assertEquals(1, sche.ele.get_floor());
		
		sche.schedule(req_list);
		assertEquals(4, sche.ele.get_floor());

		sche.schedule(req_list);
		assertEquals(2, sche.ele.get_floor());
		
		sche.schedule(req_list);
		assertEquals(2, sche.ele.get_floor());
	}
	
	@Test
	public void testScheduleSamereq() {
		clear();
		int it;
		req_list.add_req(new request("(FR,1,UP,0)"));
		req_list.add_req(new request("(ER,4,0)"));
		req_list.add_req(new request("(ER,4,0)"));
		req_list.add_req(new request("(ER,4,1)"));
		req_list.add_req(new request("(ER,2,1)"));
		req_list.add_req(new request("(FR,2,UP,4)"));
		req_list.add_req(new request("(FR,2,UP,4)"));
		
		it = sche.iterator;
		sche.schedule(req_list);
		assertEquals(1, sche.ele.get_floor());
		assertEquals(1.0, sche.t, 0);
		assertEquals(sche.iterator, it+1);
		assertEquals(7, req_list.left_req());
		
		it = sche.iterator;
		sche.schedule(req_list);
		assertEquals(4, sche.ele.get_floor());
		assertEquals(3.5, sche.t, 0);
		assertEquals(sche.iterator, it+1);
		assertEquals(7, req_list.left_req());
		
		//same req
		it = sche.iterator;
		sche.schedule(req_list);
		assertEquals(4, sche.ele.get_floor());
		assertEquals(3.5, sche.t, 0);
		assertEquals(sche.iterator, it);
		assertEquals(6, req_list.left_req());
		
		it = sche.iterator;
		sche.schedule(req_list);
		assertEquals(4, sche.ele.get_floor());
		assertEquals(3.5, sche.t, 0);
		assertEquals(sche.iterator, it);
		assertEquals(5, req_list.left_req());

		it = sche.iterator;
		sche.schedule(req_list);
		assertEquals(2, sche.ele.get_floor());
		assertEquals(5.5, sche.t, 0);
		assertEquals(sche.iterator, it+1);
		assertEquals(5, req_list.left_req());
		
		it = sche.iterator;
		sche.schedule(req_list);
		assertEquals(2, sche.ele.get_floor());
		assertEquals(6.5, sche.t, 0);
		assertEquals(sche.iterator, it+1);
		assertEquals(5, req_list.left_req());
		
		it = sche.iterator;
		sche.schedule(req_list);
		assertEquals(2, sche.ele.get_floor());
		assertEquals(6.5, sche.t, 0);
		assertEquals(sche.iterator, it);
		assertEquals(4, req_list.left_req());
	}

	@Test
	public void testCommand() {
		clear();
		req_list.add_req(new request("(FR,1,UP,0)"));
		req_list.add_req(new request("(ER,4,0)"));
		req_list.add_req(new request("(ER,2,1)"));
		req_list.add_req(new request("(FR,2,UP,4)"));
	
		sche.command(req_list);
		
		assertEquals(2, sche.ele.get_floor());
		assertEquals(6.5, sche.t, 0);
		assertEquals(sche.iterator, req_list.left_req());
		
	}
	
	public void clear() {
		while(req_list.left_req()!=0){
			req_list.remove_req(0);
		}
		sche = new scheduler();
	}
}
