import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.Before;
import org.junit.Test;

public class PushBoxTest {
	
	static PushBox pb = new PushBox();

	@Before
	public void setUp() {
		pb = new PushBox();
		Queue<node> q = new LinkedList<node>();
		pb.pre(q);
		pb.m = 5;
		pb.n = 5;
		pb.map[0][1] = 3;
		pb.map[1][0] = 1;
		pb.map[1][2] = 1;
		pb.map[1][3] = 4;
		pb.map[2][2] = 1;
		pb.map[3][0] = 1;
		pb.map[3][2] = 2;
		
		for(int i = 0; i < pb.m; i++)
		{
			for(int j = 0; j < pb.n; j++)
			{
				if(pb.map[i][j] == 2)
				{
					pb.map[i][j] = 0;
					pb.start.bx = i;
					pb.start.by = j;
				}
				else if(pb.map[i][j] == 4)
				{
					pb.map[i][j] = 0;
					pb.start.mx = i;
					pb.start.my = j;
				}
			}
		}
	}

	@Test
	public void testCheck() {
		int x,y;
		
		x = -1;y = 0;
		assertTrue(!pb.check(x, y));
		x = 0;y = -1;
		assertTrue(!pb.check(x, y));
	}
	
	@Test
	public void testCheck2() {
		int x,y;
		x = 11;y = 0;
		assertTrue(!pb.check(x, y));
		x = 0;y = 11;
		assertTrue(!pb.check(x, y));
		x = 1;y = 1;	
	}
	
	@Test
	public void testCheck3() {
		int x,y;

		x = 1;y = 1;
		pb.map[x][y] = 1;
		assertTrue(!pb.check(x, y));
		
	}
	

	@Test
	public void testBfs_man() {
		int sx,sy,ex,ey,bx,by;
		
		pb.q = new LinkedList<node>();
		sx = 3;sy = 1;ex = 1;ey = 1;bx = 2; by = 1;
		assertTrue(pb.bfs_man(sx, sy, ex, ey, bx, by));
		
	}
	@Test
	public void testBfs_man2() {
		int sx,sy,ex,ey,bx,by;
		pb.q = new LinkedList<node>();
		sx = -1;sy = 1;ex = 1;ey = 1;bx = 1; by = 1;
		assertTrue(!pb.bfs_man(sx, sy, ex, ey, bx, by));
	}
	@Test
	public void testBfs_man3() {
		int sx,sy,ex,ey,bx,by;
		
		pb.q = null;
		sx = 1;sy = 1;ex = 1;ey = 1;bx = 1; by = 1;
		assertTrue(!pb.bfs_man(sx, sy, ex, ey, bx, by));
	}

	@Test
	public void testBfs() {
		pb.q = null;
		pb.bfs();
		assertNull(pb.q);
	}
	
	@Test
	public void testBfs2() {
		pb.q = new LinkedList<node>();
		pb.bfs();
		assertNotNull(pb.q);
	}
	
	@Test
	public void testBfs3() {
		pb.bfs();
		assertEquals(8, pb.q.size());
	}

	@Test
	public void testReadmap() {
		pb.map[0][0]=-1;
		pb.readmap();
		assertTrue(pb.map[0][0]!=-1);
	}

}
