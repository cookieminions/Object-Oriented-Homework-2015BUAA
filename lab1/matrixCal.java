package exp1;

import java.util.*;

class expHandler{
	public static void err(int code){
		System.out.println("err code : " + code);
		System.exit(1);
	}
}

class matrix{
	private int[][] mat;
	
	public matrix(){
		mat = null;
	}
	
	public matrix(int order){
		mat = new int[order][order];
	}
	
	public matrix(String str){
		int order;
    	String [] strs=str.split("[{},]");
    	int i;
    	for(i=2;i<strs.length;i++){
    		if(!strs[i].equals(""))
    			continue;
    		else 
    			break;
    	}
    	order=i-2;
    	
    	if(order==0){
    		expHandler.err(1);
    	}
    	int[][] m=new int[order][order];
    	int j;
    	int record = 0;
    	for(i=0;i<strs.length;i+=2+order){
    		for(j=0;j<order;j++){
    			try{
    				m[i/(2+order)][j]=Integer.parseInt(strs[i+2+j]);
    			}
    			catch(Exception e){
    				expHandler.err(6);
    			}
    			record++;
    		}
    	}
    	if(record!=order*order){
    		expHandler.err(4);
    	}//
    	mat = m;//
    }
	
	public int getOrder(){
		return mat.length;
	}
	
	public matrix add(matrix addThis){
		int i, j, order;
		order = getOrder();
		matrix temp = new matrix(order);
		if(order!=addThis.getOrder()){
			expHandler.err(3);
		}
		for(i = 0; i < order; i++){
			for(j = 0; j < order; j++){
				temp.mat[i][j] = mat[i][j] + addThis.mat[i][j];
			}
		}
		return temp;
	}
	
	public matrix sub(matrix subThis){
		int i, j, order;
		order = getOrder();
		matrix temp = new matrix(order);
		if(order!=subThis.getOrder()){
			expHandler.err(3);
		}//
		for(i = 0; i < order; i++){
			for(j = 0; j < order; j++){
				temp.mat[i][j] = mat[i][j] - subThis.mat[i][j];
			}
		}
		return temp;
	}
	
	public matrix transpose(){
		int order;
		order = getOrder();
		matrix temp = new matrix(order);
		int i, j;
		for(i = 0; i < order; i++){
			for(j = 0; j < order; j++){
				temp.mat[i][j] = mat[j][i];
			}
		}
		return temp;
	}
	
	public matrix multiply(matrix multiplyThis){
		int i, j, k, order, element;
		order = getOrder();
		matrix temp = new matrix(order);
		if(order!=multiplyThis.getOrder()){
			expHandler.err(3);
		}
		for(i = 0; i < order; i++){
			for(j = 0; j < order; j++){
				element = 0;
				for(k = 0; k < order; k++){
					element += mat[i][k] * multiplyThis.mat[k][j];
				}
				temp.mat[i][j] = element;
			}
		}
		return temp;
	}
	
	public String toString(){
		String s = new String();
		int i, j, order;
		order = getOrder();
		for(i = 0; i < order; i++){
			for(j = 0; j < order; j++){
				s += String.valueOf(mat[i][j]);
				s += '\t';
			}
			s = s + '\n';
		}
		return(s);
	}
	
}


public class matrixCal {
	int[][] matrix1, matrix2, answer;
	int dim;
	char operator;
	
	public static void main (String[] args){
		Scanner keyboard = new Scanner(System.in);
		matrix m1 = new matrix(keyboard.nextLine().replaceAll(" ", ""));
		String op;
		char operator = '\0';
		op = keyboard.nextLine().replaceAll(" ", "");
		if(op == null) expHandler.err(1);
		if(op.length() == 1){
			operator = op.charAt(0);
			if(operator!='+'&&operator!='-'&&operator!='*'&&operator!='t')
				expHandler.err(5);
		}
		else expHandler.err(1);
		if(operator == 't'){
			System.out.print(m1.transpose());
		}
		if(operator == '+'){
			matrix m2 = new matrix(keyboard.nextLine().replaceAll(" ", ""));
			System.out.print(m1.add(m2));
		}
		if(operator == '-'){
			matrix m2 = new matrix(keyboard.nextLine().replaceAll(" ", ""));
			System.out.print(m1.sub(m2));
		}
		if(operator == '*'){
			matrix m2 = new matrix(keyboard.nextLine().replaceAll(" ", ""));
			System.out.print(m1.multiply(m2));
		}
		keyboard.close();
	}
	
	
}

