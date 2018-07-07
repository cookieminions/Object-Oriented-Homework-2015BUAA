package elevator;

import static org.junit.Assert.*;

import org.junit.Test;

public class als_schedulerTest {
	static als_scheduler als_s = new als_scheduler();
	static request_list req_list = new request_list();
	@Test
	public void testSchedule() {
		clear();
		req_list.add_req(new request("(FR,1,UP,0)"));
		req_list.add_req(new request("(ER,4,0)"));
		req_list.add_req(new request("(ER,2,1)"));
		req_list.add_req(new request("(FR,2,UP,4)"));
		req_list.add_req(new request("(FR,3,DOWN,4)"));
		
		als_s.main_req = req_list.get_req(0);
		als_s.schedule(req_list);
		
		assertEquals("(ER,4,0)", als_s.main_req.get_s());
		assertEquals(1,als_s.ele.get_floor());
		assertEquals(1.0, als_s.t, 0);
		
		als_s.schedule(req_list);
		assertEquals("(FR,2,UP,4)", als_s.main_req.get_s());
		assertEquals(4,als_s.ele.get_floor());
		assertEquals(4.5, als_s.t, 0);
		
		als_s.schedule(req_list);
		assertEquals("(FR,2,UP,4)", als_s.main_req.get_s());
		assertEquals(2,als_s.ele.get_floor());
		assertEquals(7.5, als_s.t, 0);
	}

	@Test
	public void testCommand() {
		clear();
		req_list.add_req(new request("(FR,1,UP,0)"));
		req_list.add_req(new request("(ER,4,0)"));
		req_list.add_req(new request("(ER,2,1)"));
		req_list.add_req(new request("(FR,2,UP,4)"));
		req_list.add_req(new request("(FR,3,DOWN,4)"));
		
		als_s.command(req_list);
		assertEquals(0, req_list.left_req());
	}

	@Test
	public void testRepOk() {
		assertTrue(als_s.repOk());
	}
	
	@Test
	public void testRepOkNull() {
		assertTrue(als_s.repOk());
		
		als_s.along_req = null;
		assertFalse(als_s.repOk());
		als_s = new als_scheduler();
		
		als_s.e_direction = null;
		assertFalse(als_s.repOk());
		als_s = new als_scheduler();
		
		als_s.ele = null;
		assertFalse(als_s.repOk());
		als_s = new als_scheduler();
	}
	
	@Test	
	public void testMove2DesMainfloor() {
		clear();
		req_list.add_req(new request("(FR,1,UP,0)"));
		als_s.main_floor = 1;
		
		als_s.Move2Des(req_list);
		assertEquals(als_s.main_floor,als_s.ele.get_floor());
		
		als_s.main_floor = 2;
		als_s.Move2Des(req_list);
		assertEquals(als_s.main_floor,als_s.ele.get_floor());
	}
	
	@Test	
	public void testMove2DesT() {
		clear();
		req_list.add_req(new request("(FR,1,UP,0)"));
		req_list.add_req(new request("(FR,4,UP,0)"));
		req_list.add_req(new request("(ER,2,1)"));
		req_list.add_req(new request("(FR,3,UP,1)"));
		
		//pass first order
		als_s.main_req = req_list.get_req(0);
		als_s.schedule(req_list);
		
		//set env
		als_s.main_floor = als_s.main_req.get_des_floor();
		als_s.e_direction = "STILL";
		als_s.t = als_s.t>als_s.main_req.get_req_t()?als_s.t:als_s.main_req.get_req_t();
		if(als_s.ele.get_floor()<als_s.main_floor){ als_s.e_direction = "UP"; }
		else if(als_s.ele.get_floor()>als_s.main_floor){ als_s.e_direction = "DOWN"; }
		
		//test
		assertEquals(3, req_list.left_req());
		als_s.Move2Des(req_list);
		assertEquals(1, req_list.left_req());
		
		assertEquals(4.5, als_s.t, 0);
	}
	
	@Test	
	public void testMove2DesEle() {
		clear();
		req_list.add_req(new request("(FR,1,UP,0)"));
		req_list.add_req(new request("(FR,4,UP,0)"));
		req_list.add_req(new request("(ER,2,1)"));
		req_list.add_req(new request("(FR,3,UP,1)"));
		
		//pass first order
		als_s.main_req = req_list.get_req(0);
		als_s.schedule(req_list);
		
		//set env
		als_s.main_floor = als_s.main_req.get_des_floor();
		als_s.e_direction = "STILL";
		als_s.t = als_s.t>als_s.main_req.get_req_t()?als_s.t:als_s.main_req.get_req_t();
		if(als_s.ele.get_floor()<als_s.main_floor){ als_s.e_direction = "UP"; }
		else if(als_s.ele.get_floor()>als_s.main_floor){ als_s.e_direction = "DOWN"; }
		
		//test
		assertEquals(3, req_list.left_req());
		als_s.Move2Des(req_list);
		assertEquals(1, req_list.left_req());
		assertEquals(4, als_s.ele.get_floor());
	}
	
	@Test	
	public void testMove2DesPickupreqFR() {
		clear();
		req_list.add_req(new request("(FR,1,UP,0)"));
		req_list.add_req(new request("(FR,4,UP,0)"));
		req_list.add_req(new request("(ER,2,1)"));
		req_list.add_req(new request("(FR,3,UP,1)"));
		
		//pass first order
		als_s.main_req = req_list.get_req(0);
		als_s.schedule(req_list);
		
		//set env
		als_s.main_floor = als_s.main_req.get_des_floor();
		als_s.e_direction = "STILL";
		als_s.t = als_s.t>als_s.main_req.get_req_t()?als_s.t:als_s.main_req.get_req_t();
		if(als_s.ele.get_floor()<als_s.main_floor){ als_s.e_direction = "UP"; }
		else if(als_s.ele.get_floor()>als_s.main_floor){ als_s.e_direction = "DOWN"; }
		
		//test
		assertEquals(3, req_list.left_req());
		als_s.Move2Des(req_list);
		assertEquals(1, req_list.left_req());
		assertEquals("(FR,4,UP,0)", req_list.get_req(0).get_s());
		assertEquals(4.5, als_s.t, 0);
	}
	
	@Test	
	public void testMove2DesPickupreqER() {
		clear();
		req_list.add_req(new request("(FR,1,UP,0)"));
		req_list.add_req(new request("(ER,4,0)"));
		req_list.add_req(new request("(ER,2,1)"));
		req_list.add_req(new request("(FR,3,UP,1)"));
		
		//pass first order
		als_s.main_req = req_list.get_req(0);
		als_s.schedule(req_list);
		
		//set env
		als_s.main_floor = als_s.main_req.get_des_floor();
		als_s.e_direction = "STILL";
		als_s.t = als_s.t>als_s.main_req.get_req_t()?als_s.t:als_s.main_req.get_req_t();
		if(als_s.ele.get_floor()<als_s.main_floor){ als_s.e_direction = "UP"; }
		else if(als_s.ele.get_floor()>als_s.main_floor){ als_s.e_direction = "DOWN"; }
		
		//test
		assertEquals(3, req_list.left_req());
		als_s.Move2Des(req_list);
		assertEquals(1, req_list.left_req());
		assertEquals("(ER,4,0)", req_list.get_req(0).get_s());
		assertEquals(4.5, als_s.t, 0);
	}
	
	@Test
	public void testMove2DesSamereq(){
		clear();
		req_list.add_req(new request("(FR,1,UP,0)"));
		req_list.add_req(new request("(FR,4,UP,0)"));
		req_list.add_req(new request("(FR,2,UP,1)"));
		req_list.add_req(new request("(FR,3,UP,1)"));
		req_list.add_req(new request("(FR,3,UP,2)"));
		
		//pass first order
		als_s.main_req = req_list.get_req(0);
		als_s.schedule(req_list);
		
		//set env
		als_s.main_floor = als_s.main_req.get_des_floor();
		als_s.e_direction = "STILL";
		als_s.t = als_s.t>als_s.main_req.get_req_t()?als_s.t:als_s.main_req.get_req_t();
		if(als_s.ele.get_floor()<als_s.main_floor){ als_s.e_direction = "UP"; }
		else if(als_s.ele.get_floor()>als_s.main_floor){ als_s.e_direction = "DOWN"; }
		
		//test
		assertEquals(4, req_list.left_req());
		als_s.Move2Des(req_list);
		assertEquals(1, req_list.left_req());
		assertEquals("(FR,4,UP,0)", req_list.get_req(0).get_s());
		assertEquals(4.5, als_s.t, 0);
	}
	
	@Test	
	public void testArriveAtDesPickupreqFR4ER() {
		clear();
		req_list.add_req(new request("(FR,1,UP,0)"));
		req_list.add_req(new request("(FR,4,UP,0)"));
		req_list.add_req(new request("(ER,4,0)"));
		req_list.add_req(new request("(FR,2,UP,1)"));
		req_list.add_req(new request("(FR,3,UP,1)"));
		
		//pass first order
		als_s.main_req = req_list.get_req(0);
		als_s.schedule(req_list);
		
		//set env
		als_s.main_floor = als_s.main_req.get_des_floor();
		als_s.e_direction = "STILL";
		als_s.t = als_s.t>als_s.main_req.get_req_t()?als_s.t:als_s.main_req.get_req_t();
		if(als_s.ele.get_floor()<als_s.main_floor){ als_s.e_direction = "UP"; }
		else if(als_s.ele.get_floor()>als_s.main_floor){ als_s.e_direction = "DOWN"; }
		
		//test
		assertEquals(4, req_list.left_req());
		als_s.Move2Des(req_list);
		
		assertEquals(2, req_list.left_req());
		als_s.ArriveAtDes(req_list);
		
		assertEquals(0, req_list.left_req());
		assertEquals(5.5, als_s.t, 0);
	}
	
	@Test	
	public void testArriveAtDesPickupreqER4FR() {
		clear();
		req_list.add_req(new request("(FR,1,UP,0)"));
		req_list.add_req(new request("(FR,4,UP,0)"));
		req_list.add_req(new request("(ER,4,0)"));
		req_list.add_req(new request("(FR,2,UP,1)"));
		req_list.add_req(new request("(FR,3,UP,1)"));
		
		//pass first order
		als_s.main_req = req_list.get_req(0);
		als_s.schedule(req_list);
		
		//set env
		als_s.main_floor = als_s.main_req.get_des_floor();
		als_s.e_direction = "STILL";
		als_s.t = als_s.t>als_s.main_req.get_req_t()?als_s.t:als_s.main_req.get_req_t();
		if(als_s.ele.get_floor()<als_s.main_floor){ als_s.e_direction = "UP"; }
		else if(als_s.ele.get_floor()>als_s.main_floor){ als_s.e_direction = "DOWN"; }
		
		//test
		assertEquals(4, req_list.left_req());
		als_s.Move2Des(req_list);
		
		assertEquals(2, req_list.left_req());
		als_s.ArriveAtDes(req_list);
		
		assertEquals(0, req_list.left_req());
		assertEquals(5.5, als_s.t, 0);
	}
	
	@Test	
	public void testArriveAtDesMainreq() {
		clear();
		req_list.add_req(new request("(FR,1,UP,0)"));
		req_list.add_req(new request("(FR,4,UP,0)"));
		req_list.add_req(new request("(FR,2,UP,1)"));
		req_list.add_req(new request("(FR,3,UP,1)"));
		
		//pass first order
		als_s.main_req = req_list.get_req(0);
		als_s.schedule(req_list);
		
		//set env
		als_s.main_floor = als_s.main_req.get_des_floor();
		als_s.e_direction = "STILL";
		als_s.t = als_s.t>als_s.main_req.get_req_t()?als_s.t:als_s.main_req.get_req_t();
		if(als_s.ele.get_floor()<als_s.main_floor){ als_s.e_direction = "UP"; }
		else if(als_s.ele.get_floor()>als_s.main_floor){ als_s.e_direction = "DOWN"; }
		
		//test
		assertEquals(3, req_list.left_req());
		als_s.Move2Des(req_list);
		
		assertEquals(1, req_list.left_req());
		als_s.ArriveAtDes(req_list);
		assertEquals(0, req_list.left_req());
		assertEquals(5.5, als_s.t, 0);
	}
	
	@Test	
	public void testArriveAtDesSamereq() {
		clear();
		req_list.add_req(new request("(FR,1,UP,0)"));
		req_list.add_req(new request("(FR,10,DOWN,1)"));
		req_list.add_req(new request("(FR,10,DOWN,3)"));
		req_list.add_req(new request("(FR,10,DOWN,6)"));
		
		//pass first order
		als_s.main_req = req_list.get_req(0);
		als_s.schedule(req_list);
		
		//set env
		als_s.main_floor = als_s.main_req.get_des_floor();
		als_s.e_direction = "STILL";
		als_s.t = als_s.t>als_s.main_req.get_req_t()?als_s.t:als_s.main_req.get_req_t();
		if(als_s.ele.get_floor()<als_s.main_floor){ als_s.e_direction = "UP"; }
		else if(als_s.ele.get_floor()>als_s.main_floor){ als_s.e_direction = "DOWN"; }
		
		//test
		assertEquals(3, req_list.left_req());
		als_s.Move2Des(req_list);
		
		assertEquals(3, req_list.left_req());
		als_s.ArriveAtDes(req_list);
		
		assertEquals(0, req_list.left_req());
		assertEquals(6.5, als_s.t, 0);
	}
	
	@Test	
	public void testArriveAtDesSamereq2() {
		clear();
		req_list.add_req(new request("(FR,1,UP,0)"));
		req_list.add_req(new request("(FR,10,DOWN,1)"));
		req_list.add_req(new request("(ER,10,1)"));
		req_list.add_req(new request("(ER,10,3)"));
		req_list.add_req(new request("(FR,10,DOWN,3)"));
		req_list.add_req(new request("(FR,10,DOWN,6)"));
		
		//pass first order
		als_s.main_req = req_list.get_req(0);
		als_s.schedule(req_list);
		
		//set env
		als_s.main_floor = als_s.main_req.get_des_floor();
		als_s.e_direction = "STILL";
		als_s.t = als_s.t>als_s.main_req.get_req_t()?als_s.t:als_s.main_req.get_req_t();
		if(als_s.ele.get_floor()<als_s.main_floor){ als_s.e_direction = "UP"; }
		else if(als_s.ele.get_floor()>als_s.main_floor){ als_s.e_direction = "DOWN"; }
		
		//test
		assertEquals(5, req_list.left_req());
		als_s.Move2Des(req_list);
		
		assertEquals(5, req_list.left_req());
		als_s.ArriveAtDes(req_list);
		
		assertEquals(0, req_list.left_req());
		assertEquals(6.5, als_s.t, 0);
	}
	
	@Test	
	public void testArriveAtDesSamereq3() {
		clear();
		req_list.add_req(new request("(FR,1,UP,0)"));
		req_list.add_req(new request("(FR,9,DOWN,1)"));
		req_list.add_req(new request("(ER,9,1)"));
		req_list.add_req(new request("(ER,9,3)"));
		req_list.add_req(new request("(FR,9,UP,3)"));
		req_list.add_req(new request("(FR,9,UP,3)"));
		
		//pass first order
		als_s.main_req = req_list.get_req(0);
		als_s.schedule(req_list);
		
		//set env
		als_s.main_floor = als_s.main_req.get_des_floor();
		als_s.e_direction = "STILL";
		als_s.t = als_s.t>als_s.main_req.get_req_t()?als_s.t:als_s.main_req.get_req_t();
		if(als_s.ele.get_floor()<als_s.main_floor){ als_s.e_direction = "UP"; }
		else if(als_s.ele.get_floor()>als_s.main_floor){ als_s.e_direction = "DOWN"; }
		
		//test
		assertEquals(5, req_list.left_req());
		als_s.Move2Des(req_list);
		
		assertEquals(5, req_list.left_req());
		als_s.ArriveAtDes(req_list);
		
		assertEquals(0, req_list.left_req());
		assertEquals(6.0, als_s.t, 0);
	}
	@Test	
	public void testArriveAtDesStill() {
		clear();
		req_list.add_req(new request("(FR,1,UP,0)"));
		req_list.add_req(new request("(FR,9,DOWN,1)"));
		req_list.add_req(new request("(ER,9,1)"));
		req_list.add_req(new request("(ER,9,3)"));
		req_list.add_req(new request("(FR,9,UP,3)"));
		
		//pass first order
		als_s.main_req = req_list.get_req(0);
		als_s.schedule(req_list);
		
		//set env
		als_s.main_floor = als_s.main_req.get_des_floor();
		als_s.e_direction = "STILL";
		als_s.t = als_s.t>als_s.main_req.get_req_t()?als_s.t:als_s.main_req.get_req_t();
		if(als_s.ele.get_floor()<als_s.main_floor){ als_s.e_direction = "UP"; }
		else if(als_s.ele.get_floor()>als_s.main_floor){ als_s.e_direction = "DOWN"; }
		
		//test
		assertEquals(4, req_list.left_req());
		als_s.Move2Des(req_list);
		
		als_s.e_direction = "STILL";
		assertEquals(4, req_list.left_req());
		als_s.ArriveAtDes(req_list);
		
		assertEquals(1, req_list.left_req());
		assertEquals(6.0, als_s.t, 0);
	}
	
	@Test	
	public void testArriveAtDesT() {
		clear();
		req_list.add_req(new request("(FR,1,UP,0)"));
		req_list.add_req(new request("(FR,4,UP,0)"));
		req_list.add_req(new request("(FR,2,UP,1)"));
		req_list.add_req(new request("(FR,3,UP,1)"));
		
		//pass first order
		als_s.main_req = req_list.get_req(0);
		als_s.schedule(req_list);
		
		//set env
		als_s.main_floor = als_s.main_req.get_des_floor();
		als_s.e_direction = "STILL";
		als_s.t = als_s.t>als_s.main_req.get_req_t()?als_s.t:als_s.main_req.get_req_t();
		if(als_s.ele.get_floor()<als_s.main_floor){ als_s.e_direction = "UP"; }
		else if(als_s.ele.get_floor()>als_s.main_floor){ als_s.e_direction = "DOWN"; }
		
		//test
		assertEquals(3, req_list.left_req());
		als_s.Move2Des(req_list);
		double tmp = als_s.t;
		assertEquals(1, req_list.left_req());
		als_s.ArriveAtDes(req_list);
		assertEquals(tmp+1.0, als_s.t, 0);
	}

	@Test
	public void testResetMainReq() {
		clear();
		req_list.add_req(new request("(FR,1,UP,0)"));
		req_list.add_req(new request("(ER,4,0)"));
		req_list.add_req(new request("(FR,2,UP,1)"));
		req_list.add_req(new request("(ER,5,2)"));
		req_list.add_req(new request("(FR,3,UP,4)"));
		req_list.add_req(new request("(ER,1,5)"));
		req_list.add_req(new request("(FR,2,DOWN,5)"));
		//pass first order
		als_s.main_req = req_list.get_req(0);
		als_s.schedule(req_list);
		
		assertEquals("(ER,4,0)", als_s.main_req.get_s());
		
		als_s.main_floor = als_s.main_req.get_des_floor();
		als_s.e_direction = "STILL";
		als_s.t = als_s.t>als_s.main_req.get_req_t()?als_s.t:als_s.main_req.get_req_t();
		if(als_s.ele.get_floor()<als_s.main_floor){ als_s.e_direction = "UP"; }
		else if(als_s.ele.get_floor()>als_s.main_floor){ als_s.e_direction = "DOWN"; }
		
		als_s.Move2Des(req_list);
		als_s.ArriveAtDes(req_list);
		als_s.ResetMainReq(req_list);
		assertEquals("(ER,5,2)", als_s.main_req.get_s());
		
		
		als_s.schedule(req_list);
		assertEquals("(FR,3,UP,4)", als_s.main_req.get_s());
		als_s.schedule(req_list);
		assertEquals("(ER,1,5)", als_s.main_req.get_s());
		als_s.schedule(req_list);
	}
	
	public void clear() {
		while(req_list.left_req()!=0){
			req_list.remove_req(0);
		}
		als_s = new als_scheduler();
		als_s.e_direction = "STILL";
	}

}
