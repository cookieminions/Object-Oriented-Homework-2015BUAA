package elevator;

import static org.junit.Assert.*;

import org.junit.*;

public class elevatorTest {
	static elevator  ele = new elevator();

	@Test
	public void testRepOk() {
		assertTrue(ele.repOk());
	}
	
	@Test
	public void testRepOkDirection() {
		assertTrue(ele.repOk());
		
		ele.direction = null;
		assertTrue(!ele.repOk());
		
		ele = new elevator();
	}
	
	@Test
	public void testRepOkFloor() {
		assertTrue(ele.repOk());
		
		ele.direction = "STILL";
		ele.floor = 0;
		assertTrue(!ele.repOk());
		ele.floor = 11;
		assertTrue(!ele.repOk());
		ele = new elevator();
	}
	
	@Test
	public void testSet_floor() {
		ele.set_floor(1);
		ele.set_floor(5);
		assertEquals("UP", ele.get_direction());
		assertEquals(5, ele.get_floor());	
	}

	@Test
	public void testGet_floor() {
		ele.set_floor(5);
		assertEquals(5, ele.get_floor());
	}

	@Test
	public void testGet_directionUp() {
		ele.set_floor(1);
		ele.set_floor(5);
		assertEquals("UP", ele.get_direction());
	}
	
	@Test
	public void testGet_directionDown() {
		ele.set_floor(5);
		ele.set_floor(1);
		assertEquals("DOWN", ele.get_direction());
	}
	
	@Test
	public void testGet_directionStill() {
		ele.set_floor(5);
		ele.set_floor(5);
		assertEquals("STILL", ele.get_direction());
	}

	@Test
	public void testToStringDouble() {
		ele.set_floor(1);
		ele.set_floor(5);
		assertEquals("(5,UP,1.0)", ele.toString(1.0));
	}

}
