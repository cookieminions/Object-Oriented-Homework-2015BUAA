package Bank;

import java.util.Iterator;

public class AccountSet {
//OVERVIEW:帐户的集合，不区别帐户类别
//
   //constructors
   public AccoutSet(){
   /*@EFFECTS： (\result = this) &&(this.isEmpty())
   */
   }
   
   //methods
   public void insert(Account a){
   /*@ REQUIRES: a!= null ;
     @ MODIFIES: this;
     @ EFFECTS:     
         (this.size == \old(this).size+1) && (this.contains(a)==true)&&(\result==true); 
       */
	//请根据规格补充代码
   }   
   
   public void removeByAccountId(String id) throws InvalidAccountException{
    /*@MODIFIES:this
      @EFFECTS:
           normal_behavior
           (\old(this).queryByAccountId(id) !=null) ==> (this.size == \old(this).size-1) && (this.contains(\old(this).queryByAccountId(id))==false) && (\result==true);            (\old(this).queryByAccountId(id) ==null)==>exceptional_behavior(InvalidAccountException)                   
    */
   }


   public void removeByUserId(String id)throws InvalidUserException{
    /*@MODIFIES:this
      @EFFECTS:
           normal_behavior
           (\old(this).queryByUserId(id) !=null) ==> (this.size == \old(this).size-\old(this).queryByUserId(id).size()) && (this.contains(\old(this).queryByUserId(id))==false) && (\result==true);           
          (\old(this).queryByUserId(id) ==null)==>exceptional_behavior(InvalidUserException)                   
    */
   }
   
  
   public boolean isIn(Account a)
   { /*@ REQUIRES: a!= null ;
       @ EFFECTS:     
         (this.contains(a)==true)==> (\result==true);
         (this.contains(a)==false)==>(\result==false);
       */
	//请根据规格补充代码
   }   

   public long size()
   { /* @ EFFECTS: 
        (\result==this.size());
     */
	//请根据规格补充代码
   }   
  
   
   public Iterator elements()
 { /* @ REQUIRES:this!=null
      @ EFFECTS: 
        (\result==this.size());
     */
	//请根据规格补充代码
   }   
   


   public Account queryByAccountId(String accountId)
   { /*@ REQUIRES: accountId!= null ;
        
       @ EFFECTS: 
         normal_behavior    
        (\all Account a;a.getAccountId()==accountId && (this.contains(a)==true))==>(\result==a);
        (\all Account a;a.getAccountId()==accountId && (this.contains(a)==false))==>exceptional_behavior(InvalidAccountException) ;   

       */
	//请根据规格补充代码
   }   
  
   
      
   public AccountSet queryByUserId(String userId)
   { /*@ REQUIRES: userId!= null ;        
       @ EFFECTS: 
         normal_behavior
        (AccountSet as;as.size()==0)    
        (\all Account a;a.getUserId()==userId && (this.contains(a)==true))==>(\result==as.insert(a));

        (\all Account a;a.getUserId()==userId && (this.contains(a)==false))==>exceptional_behavior(InvalidUserException) ;   

       */
	//请根据规格补充代码
   }   
  
   
   public reqOK()
   
}

