package elevator;

import static org.junit.Assert.*;

import org.junit.Test;

public class request_listTest {
	static request_list reqlist = new request_list();

	@Test
	public void testRepOk() {
		assertTrue(reqlist.repOk());
	}
	
	@Test
	public void testRepOkNull() {
		assertTrue(reqlist.repOk());
		reqlist.req_list = null;
		assertFalse(reqlist.repOk());
		
		reqlist = new request_list();
	}

	@Test
	public void testAdd_reqFirst(){
		clear();
		reqlist.add_req(new request("(FR,1,UP,0)"));
		assertEquals("(FR,1,UP,0)",reqlist.get_req(0).get_s());
		assertEquals(1, reqlist.left_req());
		
		clear();
		assertEquals(0, reqlist.left_req());
		reqlist.add_req(new request("(FR,2,UP,0)"));
		assertEquals(0, reqlist.left_req());
		reqlist.add_req(new request("[FR,1,UP,0)"));
		assertEquals(0, reqlist.left_req());
		
		clear();
		assertEquals(0, reqlist.left_req());
		reqlist.add_req(new request("(FR,1,UP,1)"));
		assertEquals(0, reqlist.left_req());
	}
	
	@Test
	public void testAdd_reqOther(){
		clear();
		reqlist.add_req(new request("(FR,1,UP,0)"));
		reqlist.add_req(new request("abc"));
		assertEquals("(FR,1,UP,0)",reqlist.get_req(0).get_s());
		assertEquals(1, reqlist.left_req());
		reqlist.add_req(new request("(FR,1,UP,0)"));
	}
	
	@Test
	public void testAdd_reqNext(){
		clear();
		reqlist.add_req(new request("(FR,1,UP,0)"));
		reqlist.add_req(new request("(FR,2,UP,1)"));
		reqlist.add_req(new request("(FR,3,UP,0)"));
		assertEquals(2, reqlist.left_req());
		assertEquals("(FR,1,UP,0)",reqlist.get_req(0).get_s());
		assertEquals("(FR,2,UP,1)",reqlist.get_req(1).get_s());
	}
	
	@Test
	public void testAdd_reqSize(){
		clear();
		assertEquals(0, reqlist.left_req());
		reqlist.add_req(new request("(FR,1,UP,0)"));
		assertEquals(1, reqlist.left_req());
		reqlist.add_req(new request("(FR,2,UP,0)"));
		assertEquals(2, reqlist.left_req());
	}

	@Test
	public void testRemove_reqInt() {
		clear();
		reqlist.add_req(new request("(FR,1,UP,0)"));
		reqlist.remove_req(0);
		assertEquals(0, reqlist.left_req());
	}

	@Test
	public void testRemove_reqRequest() {
		clear();
		request req = new request("(FR,1,UP,0)");
		reqlist.add_req(req);
		reqlist.remove_req(req);
		assertEquals(0, reqlist.left_req());
	}

	@Test
	public void testLeft_req() {
		clear();
		reqlist.add_req(new request("(FR,1,UP,0)"));
		reqlist.add_req(new request("(FR,2,UP,0)"));
		assertEquals(2, reqlist.left_req());
	}

	@Test
	public void testGet_req() {
		clear();
		reqlist.add_req(new request("(FR,1,UP,0)"));
		assertEquals("(FR,1,UP,0)",reqlist.get_req(0).get_s());
	}
	
	public void clear() {
		while(reqlist.left_req()!=0){
			reqlist.remove_req(0);
		}
	}

}
