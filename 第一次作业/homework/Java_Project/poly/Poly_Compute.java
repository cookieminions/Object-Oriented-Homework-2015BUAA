package poly;

import java.util.Scanner;

public class Poly_Compute {
	private Poly[] PolyList;
	private char[] OpList;
	
	private int num = 0;
	private int pair_num = 0;
	private Poly p;
	
	public int error;
	
	Poly_Compute(){//构造函数
		p = new Poly(1000000);
		PolyList = new Poly[20];
		OpList = new char[20];
		error = 0;
		num = 0;
		pair_num = 0;
	}
	
	private void compute(){//计算
		Poly q;
		char op;
		for(int i=0;i<num;i++){
			q = PolyList[i];
			op = OpList[i];
			if(op=='+')			
				p.add(q);
			else if(op=='-')	
				p.sub(q);
		}
		PolyList[0] = p;
	}
	
	private void output(){//输出
		if(error!=0){
			//System.out.println("get error");
			error_throw();
		}
		else{
			int commal_flag = 1;
			System.out.print("{");
			for(int i=0;i<1000000;i++){
				if(p.get_Coeff(i)!=0){
					if(commal_flag==0){
						System.out.print(",");
						commal_flag = 1;
					}
					System.out.print("("+p.get_Coeff(i)+","+i+")");
					commal_flag = 0;
				}
			}
			System.out.print("}");
		}
	}
	
	private void Analyse_Data(String data){//处理字符串
		int flag = 0;//确定op和{}的位置关系
		int has_brackets = 0;
		for(int i=0;i<data.length();i++){
			if(error!=0)	return;
			char ch = data.charAt(i);
			if(ch=='{'){
				has_brackets = 1;
				if(flag==0)	report(1);//{}前面没有op
				else{//flag = 1
					PolyList[num] = new Poly();
					i = Analyse_big(data,i);
					num++;
					pair_num = 0;
					flag = 0;
				}
			}
			else if(ch=='+'||ch=='-'){
				if(flag==1)	report(2);//op前面没有{}
				else{//flag = 0
					flag = 1;
					if(num>=20)	report(3);//多项式超过了20
					else{
						OpList[num] = ch;
					}
				}
			}
			else{
				report(4);//多余的}
			}
		}
		if(has_brackets!=1)	report(1);
	}
	private int Analyse_big(String data,int i){//处理大括号内的内容
		int j = i+1;
		int flag = 0;//确定,和()的位置关系
		if(j>=data.length()-1){
			report(16);
			return 0;
		}
		if(data.charAt(i+1)!='(')	report(5);
		while(data.charAt(j)!='}'){
			if(error!=0)	return 0;
			if(j>=data.length()-1){
				report(16);
				return 0;
			}
			if(data.charAt(j)=='('){
				if(flag==1)	report(6);//括号中间没有,
				else{//flag = 0
					if(pair_num>=50){//数对超过50
						//System.out.println(pair_num);
						report(14);
					}
					else{
						j = Analyse_l(data,j);
						pair_num++;
						flag = 1;
					}
				}
			}
			else if(data.charAt(j)==','){
				if(flag==0) report(7);//,前面没有紧接着括号
				else{//flag = 1
					flag = 0;
				}
			}
			else{
				report(8);//多余的)
			}
			if(j>=data.length()-1){
				report(16);
				return 0;
			}
			j++;
		}
		if(flag == 0)	report(5);
		if(error!=0)	return 0;
		return j;
	}
	
	private int Analyse_l(String data,int j){//处理小括号内的内容
		int k = j+1;
		String str1 = "";
		String str2 = "";
		
		if(k>=data.length()-1){
			report(16);
			return 0;
		}
		
		while(data.charAt(k)!=','){
			if(k>=data.length()-1){
				report(16);
				return 0;
			}
			str1 += data.charAt(k);
			k++;
		}
		if(k>=data.length()-1){
			report(16);
			return 0;
		}
		k++;
		while(data.charAt(k)!=')'){
			if(k>=data.length()-1){
				report(16);
				return 0;
			}
			str2 += data.charAt(k);
			k++;
		}
		
		int c = cal(str1);
		int n = cal(str2);
		
		if(error!=0)	return 0;
		
		if(n<0){
			report(9);//幂不能为负数
			return 0;
		}
		else if(c==0){
			report(18);
			return 0;
		}
		else{
			//设置c和n
			if(PolyList[num].get_Coeff(n)==0){
				PolyList[num].set_Coeff(c,n);
			}
			else report(15);//n不能重复
			
			if(n>PolyList[num].get_Degree())
				PolyList[num].set_Degree(n);
		}		
		return k;
	}
	
	private int cal(String str){
		char r = '+';
		int record_num = 0;
		int has_num = 0;
		
		if(str.length()==0) report(17);//数字为空
		
		for(int t=0;t<str.length();t++){
			if(error!=0)	return 0;
			if((str.charAt(t)=='+'||str.charAt(t)=='-')){
				if(t==0)//-1,+2可以
					r = str.charAt(t);
				else{
					report(10);//1+2不行
					return 0;
				}
			}
			else if(Character.isDigit(str.charAt(t))){
				has_num = 1;
				int h = 0;
				String tmp_str = "";
				for(h=t;h<str.length();h++){
					if(Character.isDigit(str.charAt(h))){
						tmp_str += str.charAt(h);
					}
					else{
						report(11);//出现非数字
					}	
				}
				t = h;
				/*此处原本为了处理前导0使其不占位置*/
				/*int splice_0 = -1;
				for(int s=0;s<tmp_str.length();s++){
					if(tmp_str.charAt(s)!='0'){
						splice_0 = s;
						break;
					}
				}
				if(splice_0<0)
					splice_0 = tmp_str.length()-2;
				tmp_str = tmp_str.substring(splice_0);*/
				if(tmp_str.length()>6)	report(12);//超出1000000
				else{
					record_num = Integer.parseInt(tmp_str);
					record_num = r=='+'?record_num:(-record_num);
				}
			}
			else{
				report(13);//出现非数字
			}
		}
		if(has_num==0)	report(17);//缺少数字
		return record_num;
	}
	
	private void report(int d){
		//System.out.println("error"+d);
		error = d;
	}
	
	private void error_throw(){
		System.out.print("get Error! ");
		switch(error){
			case 1:
			case 2:
			case 4:
				System.out.print("大括号不存在,或者{多项式}之间存在错误的字符组合或缺少相应字符, "); break;
			case 3:
				System.out.print("多项式数量超过20, "); break;
			case 5:
				System.out.print("大括号和小括号之间存在错误字符或缺少相应字符, "); break;
			case 6:
			case 7:
			case 8:
				System.out.print("{多项式}内部存在错误的字符组合或缺少相应字符, "); break;	
			case 9:
				System.out.print("指数n为负数, "); break;
			case 10:
			case 11:
			case 13:
			case 17:
				System.out.print("(数对)内部存在错误的字符组合或缺少相应字符, "); break;	
			case 12:
				System.out.print("(数对)内系数或指数超出范围, "); break;
			case 14:
				System.out.print("多项式中数对超过50, "); break;
			case 15:
				System.out.print("指数n发生重复, "); break;
			case 16:
				System.out.print("括号匹配不正确, "); break;
			case 18:
				System.out.print("存在系数为0 指数不为0的输入, "); break;
			default:
				System.out.print("输入存在不合法字符, ");
				
		}
		System.out.println("请重新输入:");
	}

	public static void main(String[] args) {	
		//String line = "+{(-12,2),(2,3)}-{(-2,2),(1,4)}+{(3,2)}+{(121,0),(666,999999),(-999999,12)}";
		int global_error = -1;
		Scanner s_in = new Scanner(System.in);
		
		while(global_error!=0){
			global_error = -1;
			String line = "";
			try{
				line = s_in.nextLine();
			}
			catch(Exception e){
				//global_error = 1;
				s_in.close();
				System.out.println("get Error! 输入存在异常, 请重新运行程序:");
				//continue;
				return;
			}
			
			line = line.replaceAll(" ","");
			if(line.length()==0){
				global_error = 1;
				System.out.println("get Error! 输入为空, 请重新输入:");
			}
			
			for(int i=0;i<line.length();i++){
				if(line.charAt(i)!='+'&&line.charAt(i)!='-'&&line.charAt(i)!=','
						&&line.charAt(i)!='{'&&line.charAt(i)!='}'
						&&line.charAt(i)!='('&&line.charAt(i)!=')'
						&&!Character.isDigit(line.charAt(i))){
					global_error = 1;
					System.out.println("get Error! 输入存在不合法字符, 请重新输入:");
					break;
				}
			}
			
			if(global_error==-1){
				if(line.charAt(0)!='+'&&line.charAt(0)!='-')
					line = "+"+line;
				
				Poly_Compute cp = new Poly_Compute();
				
				cp.Analyse_Data(line);
				if(cp.error!=1)
					cp.compute();
				
				cp.output();
				
				global_error = cp.error;
			}
		}
		s_in.close();	
	}
}


