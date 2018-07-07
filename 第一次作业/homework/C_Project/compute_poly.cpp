#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<ctype.h>

struct poly{
	int coeff[50];
	int degree[50];
	int f;
}; 

poly add(poly p1,poly p2){
	for(int i=0;i<p2.f;i++){
		for(int j=0;j<50;j++){
			if(p2.degree[i]!=-1&&p1.degree[j]!=-1&&p2.degree[i]==p1.degree[j]){
				p1.coeff[j] += p2.coeff[i];
				break;
			}
			else if(p2.degree[i]!=-1&&p1.degree[j]==-1){
				p1.degree[j] = p2.degree[i];
				p1.coeff[j] = p2.coeff[i];
				p1.f += 1;
				break;
			}
		}
	}
	return p1;
}

poly sub(poly p1,poly p2){
	for(int i=0;i<p2.f;i++){
		for(int j=0;j<50;j++){
			if(p2.degree[i]!=-1&&p1.degree[j]!=-1&&p2.degree[i]==p1.degree[j]){
				p1.coeff[j] -= p2.coeff[i];
				break;
			}
			else if(p2.degree[i]!=-1&&p1.degree[j]==-1){
				p1.degree[j] = p2.degree[i];
				p1.coeff[j] = -p2.coeff[i];
				p1.f += 1;
			}
		}
	}
	return p1;
}

poly polylist[20];
char oplist[20];
int error;
int num;
int l_num;

void report(int d){
	printf("get Error! "); 
	switch(d){
			case 1:
			case 2:
			case 4:
				printf("大括号不存在,或者{多项式}之间存在错误的字符组合或缺少相应字符"); break;
			case 3:
				printf("多项式数量超过20"); break;
			case 5:
				printf("大括号和小括号之间存在错误字符或缺少相应字符"); break;
			case 6:
			case 7:
			case 8:
				printf("{多项式}内部存在错误的字符组合或缺少相应字符"); break;	
			case 9:
				printf("指数n为负数"); break;
			case 10:
			case 11:
			case 13:
			case 17:
				printf("(数对)内部存在错误的字符组合或缺少相应字符"); break;	
			case 12:
				printf("(数对)内系数或指数超出范围"); break;
			case 14:
				printf("多项式中数对超过50"); break;
			case 15:
				printf("指数n发生重复"); break;
			case 16:
				printf("括号匹配不正确"); break;
			case 18:
				printf("存在系数为0 指数不为0的输入"); break;
			default:
				printf("输入存在不合法字符");	
		}
	error = 1;
}

int cal(char str[]){
	char r = '+';
	int r_num = 0;
	int h_num = 0;
	if(str[0]=='\0')	report(17);
	for(int t=0;str[t]!='\0';t++){
		if(error!=0)	return 0;
		if(str[t]=='+'||str[t]=='-'){
			if(t==0)	r = str[t];
			else{
				report(10);
				return 0;
			}	
		}
		else if(str[t]>='0'&&str[t]<='9'){
			h_num = 1;
			int h = 0;
			int x = 0;
			for(h=t;str[h]!='\0';h++){
				if(str[h]>='0'&&str[h]<='9'){
					r_num = r_num*10 + (str[h]-'0');
					x++;
				}
				else	report(11);
			}
			t = h;
			if(str[t]=='\0')
				t = t-1;
			if(x>6)	report(12);
			else{
				r_num = (r=='+')?r_num:(-r_num);
			}
		}
		else	report(13);
	}
	if(h_num==0)	report(17);
	return r_num;
}


int Analyse_l(char data[],int j){
	int k = j+1;
	char str1[500];
	char str2[500];
	if(data[k]=='\0'){
		report(16);
		return 0;
	}
	int t1 = 0;
	int t2 = 0;
	while(data[k]!=','){
		if(data[k]=='\0'){
			report(16);
			return 0;
		}
		str1[t1++] = data[k];
		k++;
	}
	str1[t1] = '\0';
	if(data[k]=='\0'){
		report(16);
		return 0;
	}
	k++;
	while(data[k]!=')'){
		if(data[k]=='\0'){
			report(16);
			return 0;
		}
		str2[t2++] = data[k];
		k++;
	}
	str2[t2] = '\0';
	int c = cal(str1);
	int n = cal(str2);
	if(error!=0)	return 0;
	if(n<0||c==0){
		if(c==0)	report(18);
		else if(n<0)	report(9);
		return 0;
	}
	else{
		if(polylist[num].coeff[l_num]==0){
			polylist[num].coeff[l_num]=c;
			polylist[num].degree[l_num]=n;
		}
		else report(15);
	}
	return k;
}

int Analyse_big(char data[],int i){
	int j = i+1;
	int flag = 0;
	if(data[j]=='\0'){
		report(16);
		return 0;
	}
	if(data[i+1]!='(')	report(14);
	while(data[j]!='}'){
		if(error!=0)	return 0;
		if(data[j]=='\0'){
			report(16);
			return 0;
		}
		if(data[j]=='('){
			if(flag==1)	report(6);
			else{
				if(l_num>=50)	report(14);
				else{
					j = Analyse_l(data,j);
					l_num++;
					flag = 1;
				}
			}
		}
		else if(data[j]==','){
			if(flag==0)	report(7);
			else	flag = 0;
		}
		else	report(8);
		if(data[j]=='\0'){
			report(16);
			return 0;
		}
		j++;
	}
	if(error!=0)	return 0;
	return j;
}

int main(){
	error = 0;
	num = 0;
	l_num = 0;
	
	char data[1000000];
    char s;
    int t = 0;
    while((s=getchar())!='\n'){
    	if(s==','||s=='+'||s=='-'||s=='{'||s=='}'||s=='('||s==')'||(s>='0'&&s<='9')){
    		if(t==0&&(s!='+'&&s!='-')){
    			data[0] = '+';
    			t = 1;
				data[t++] = s;
			}
			else{
				data[t++] = s;
			}
        }
        else{
            if(s!=' '){
            	printf("get Error! 输入存在不合法字符\n");
            	error = 1;
				return 0;
			}
        }
	}
	data[t] = '\0';
    
    if(t==0){
    	printf("get Error! 输入为空");
    	error = 1;
	}
	
	if(error!=1){
		for(int i=0;i<20;i++){
			for(int j=0;j<50;j++){
				polylist[i].coeff[j] = 0;
				polylist[i].degree[j] = -1;
			}
		}
		int flag = 0;
		int has_b = 0;
		for(int i=0;data[i]!='\0';i++){	
			if(error!=0)	break;
			if(data[i]=='{'){
				has_b = 1;
				if(flag==0)	report(1);
				else{
					i = Analyse_big(data,i);
					polylist[num].f = l_num;
					num++;
					l_num = 0;
					flag = 0;
				}
			}
			else if(data[i]=='+'||data[i]=='-'){
				if(flag==1)	report(2);
				else{
					flag = 1;
					if(num>=20)	report(3);
					else{
						oplist[num] = data[i];
					}
				}
			}
			else	report(4);
		}
		if(has_b!=1)	report(1);
		poly p;
		char op;
	
		for(int i=1;i<num;i++){
			op = oplist[i];
			if(op=='+'){
				polylist[0] = add(polylist[0],polylist[i]);
			}
			else if(op=='-'){
				polylist[0] = sub(polylist[0],polylist[i]);
			}
		}	
		if(error!=0){
			//printf("get error\n");
		}
		else{
			for(int i=0;i<polylist[0].f-1;i++){
				for(int j=i+1;j<polylist[0].f;j++){
					int tmp_c;
					int tmp_n;
					if(polylist[0].degree[i]>polylist[0].degree[j]){
						tmp_n = polylist[0].degree[i];
						tmp_c = polylist[0].coeff[i];
						polylist[0].degree[i] = polylist[0].degree[j];
						polylist[0].degree[j] = tmp_n;
						polylist[0].coeff[i] = polylist[0].coeff[j];
						polylist[0].coeff[j] = tmp_c;
					}
				}
			}


			printf("{");
			int c_flag = 1;
			for(int i=0;i<50;i++){
				if(polylist[0].coeff[i]!=0){
					if(c_flag==0){
						printf(",");
						c_flag = 1;
					}
					printf("(%d,%d)",polylist[0].coeff[i],polylist[0].degree[i]);
					c_flag = 0;
				}
			}
			printf("}");
		}
	}
}

