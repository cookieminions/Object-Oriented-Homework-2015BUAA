package exp1;

import java.util.*;

class expHandler{
	public static void err(int code){
		System.out.println("err code : " + code);
		if (code == 1) System.out.println("error: matrix is empty!");
		else if (code == 2) System.out.println("error: two matrices are not corresponding!");
		else if (code == 3) System.out.println("error: it's not a square matrix!");
		else if (code == 4) System.out.println("error£ºMultiplicative overflow!");
		else if (code == 5) System.out.println("error: elemens are illegal or too large or too small!");
		else if (code == 6) System.out.println("error: Addition overflow!");
		else if (code == 7) System.out.println("error: Subraction overflow!");
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
		int order=-1;
		str.replace(" ", "");
		int len=str.length();
    	int i,j,k;
    	for (i=0;i<len;i++)
    		if (str.charAt(i) == '{') order++;
    	if(order==0){
    		expHandler.err(1);
    	}
    	String [] strs=str.split("[{},]");
    	mat = new int[order][order];
    	for(i=0,j=0,k=0;i<strs.length;i++){
    		if(strs[i].equals(""))
    			continue;
    		else{
    			try {
    			    mat[j][k] = Integer.valueOf(strs[i]).intValue();
    			    k++;
    			    if (k == order) {
    			    	k = 0;
    			    	j++;
    			    }
    			} catch (NumberFormatException e) {
    			    expHandler.err(5);
    			}
    		}
    	}
    	if (j != order || k != 0) expHandler.err(3);
    }
	
	public int getOrder(){
		return mat.length;
	}
	
	public matrix add(matrix addThis){
		int i, j, order;
		order = getOrder();
		if (addThis.getOrder() != order) expHandler.err(2);
		matrix temp = new matrix(order);
		for(i = 0; i < order; i++){
			for(j = 0; j < order; j++){
				if (addThis.mat[i][j] > 2147483647 - mat[i][j]) expHandler.err(6);
				if (addThis.mat[i][j] < -2147483647 - mat[i][j]) expHandler.err(6);
				temp.mat[i][j] = mat[i][j] + addThis.mat[i][j];
			}
		}
		return temp;
	}
	
	public matrix sub(matrix subThis){
		int i, j, order;
		order = getOrder();
		if (subThis.getOrder() != order) expHandler.err(2);
		matrix temp = new matrix(order);
		for(i = 0; i < order; i++){
			for(j = 0; j < order; j++){
				if (subThis.mat[i][j] > 2147483647 + mat[i][j]) expHandler.err(7);
				if (subThis.mat[i][j] < -2147483647 + mat[i][j]) expHandler.err(7);
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
		if (multiplyThis.getOrder() != order) expHandler.err(2);
		matrix temp = new matrix(order);
		for(i = 0; i < order; i++){
			for(j = 0; j < order; j++){
				element = 0;
				for(k = 0; k < order; k++){
					if (2147483647 / Math.abs(mat[i][k]) < Math.abs(multiplyThis.mat[k][j])) expHandler.err(4);
					if (element > 2147483647 - mat[i][k] * multiplyThis.mat[k][j]) expHandler.err(4);
					if (element < -2147483647 - mat[i][k] * multiplyThis.mat[k][j]) expHandler.err(4);
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
		matrix m1 = new matrix(keyboard.nextLine());
		String op;
		char operator = '\0';
		op = keyboard.nextLine();
		if(op == null) expHandler.err(1);
		if(op.length() == 1) operator = op.charAt(0);
		else expHandler.err(1);
		if(operator == 't'){
			System.out.print(m1.transpose());
		}
		if(operator == '+'){
			matrix m2 = new matrix(keyboard.nextLine());
			System.out.print(m1.add(m2));
		}
		if(operator == '-'){
			matrix m2 = new matrix(keyboard.nextLine());
			System.out.print(m1.sub(m2));
		}
		if(operator == '*'){
			matrix m2 = new matrix(keyboard.nextLine());
			System.out.print(m1.multiply(m2));
		}
		keyboard.close();
	}
	
	
}

