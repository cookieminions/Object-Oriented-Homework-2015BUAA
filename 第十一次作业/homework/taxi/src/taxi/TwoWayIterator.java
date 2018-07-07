package taxi;
/* Overview: 这个接口是双向迭代器接口
 */
public interface TwoWayIterator {
	public Object next ();
	public Object previous ();
	public boolean hasNext();
	public boolean hasPrevious();
}
