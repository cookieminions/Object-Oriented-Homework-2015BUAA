package taxi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* Overview: 这个类提供检查请求格式是否正确并构造请求对象的方法
 * Objects: None;
 * ReqCheck类的checkReq方法能够检测请求字符串是否符合格式规范并返回构建的请求
 * invariant: true
 */
public class ReqCheck {
	public boolean repOK() {
		/* 
		  @EFFECTS: \result == true;
		  */
		return true;
	}
	
	public request checkReq(String str,double t){
		/* 
		  @MODIFIES: None;
		  @EFFECTS: (str格式满足指导书输入请求[CR,(x1,y1),(x2,y2)] && str请求中的点在地图内) ==> \result == (依据str构建的request);
		  			(str格式不满足指导书输入请求[CR,(x1,y1),(x2,y2)] || str请求中的点不在地图内) ==> \result == null;
		  */
		request req = null;
		String regEx = "\\[CR,\\((\\+?\\d+),(\\+?\\d+)\\),\\((\\+?\\d+),(\\+?\\d+)\\)\\]";
		if(str.matches(regEx)){
			Pattern pattern = Pattern.compile(regEx);
			Matcher matcher = pattern.matcher(str);
			
			if(matcher.find()){
				try{
					int start_x = Integer.parseInt(matcher.group(1));	int start_y = Integer.parseInt(matcher.group(2));
					int end_x = Integer.parseInt(matcher.group(3));		int end_y = Integer.parseInt(matcher.group(4));
					if(start_x>=0&&start_x<80&&start_y>=0&&start_y<80&&end_x>=0&&end_x<80&&end_y>=0&&end_y<80){
						req = new request(str, start_x, start_y, end_x, end_y, t);
						return req;
					}
				}catch (Exception e){
					req = null;
				}
			}
		}
		return req;
	}
	

}
