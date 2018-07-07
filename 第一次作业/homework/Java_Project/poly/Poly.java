package poly;

public class Poly {
	private int[] terms;
	private int deg;
	//private int MAX_N = 1000000;
	public Poly(int d){
		terms = new int[1000000];
		deg = d;
		for(int i=0;i<1000000;i++){
			terms[i] = 0;
		}
	}
	public Poly(){
		terms = new int[1000000];
		for(int i=0;i<1000000;i++){
			terms[i] = 0;
		}
	}
	public void set_Degree(int d){
		deg = d;
	}
	public void set_Coeff(int c,int n){
		if(c==0){
			terms[n] = 1000000;
		}
		else{
			terms[n] = c;
		}
	}
	
	public int get_Degree(){
		return deg;
	}
	public int get_Coeff(int d){
		return terms[d];
	}
	
	public void add(Poly q){
		int max_deg = deg>q.get_Degree()?deg:q.get_Degree();
		for(int i=0;i<max_deg;i++){
			if(q.get_Coeff(i)==1000000){
				terms[i] += 0;
			}
			else{
				terms[i] += q.get_Coeff(i);
			}
		}
		deg = max_deg;
	}
	
	public void sub(Poly q){
		int max_deg = deg>q.get_Degree()?deg:q.get_Degree();
		for(int i=0;i<max_deg;i++){
			if(q.get_Coeff(i)==1000000){
				terms[i] -= 0;
			}
			else{
				terms[i] -= q.get_Coeff(i);
			}
		}
		deg = max_deg;
	}
	
	

}
