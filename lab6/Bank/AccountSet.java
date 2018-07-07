package Bank;

import java.util.Iterator;
import java.util.Vector;

public class AccountSet {
//OVERVIEW:帐户的集合，不区别帐户类别
//
   private Vector<Account> Accounts;
	//constructors
   public AccountSet(){
   /*@EFFECTS： (\result = this) &&(this.isEmpty())
   */ 
	   Accounts = new Vector<Account>();
   }
   
   //methods
   public void insert(Account a){
   /*@ REQUIRES: a!= null ;
     @ MODIFIES: this;
     @ EFFECTS:     
         (this.size == \old(this).size+1) && (this.contains(a)==true)&&(\result==true); 
       */
	   if(queryByAccountId(a.getAccountId())!=null)	return;
	   Accounts.add(a);
   }   
   
   public void removeByAccountId(String id) throws InvalidAccountException{
    /*@MODIFIES:this
      @EFFECTS:
           normal_behavior
           (\old(this).queryByAccountId(id) !=null) ==> (this.size == \old(this).size-1) && (this.contains(\old(this).queryByAccountId(id))==false) && (\result==true);
           (\old(this).queryByAccountId(id) ==null)==>exceptional_behavior(InvalidAccountException)                   
    */
	   Account tmp = queryByAccountId(id);
	   if(tmp!=null){
		   Accounts.remove(tmp);
	   }
	   else throw new InvalidAccountException();
   }


   public void removeByUserId(String id)throws InvalidUserException{
    /*@MODIFIES:this
      @EFFECTS:
           normal_behavior
           (\old(this).queryByUserId(id) !=null) ==> (this.size == \old(this).size-\old(this).queryByUserId(id).size()) && (this.contains(\old(this).queryByUserId(id))==false) && (\result==true);           
          (\old(this).queryByUserId(id) ==null)==>exceptional_behavior(InvalidUserException)                   
    */
	   AccountSet tmp = queryByUserId(id);
	   if(tmp!=null){
		   for(int i=0;i<Accounts.size();i++){
			   if(tmp.isIn(Accounts.get(i)))
				   Accounts.remove(i--);
		   }
	   }
	   else throw new InvalidUserException();
   }
   
  
   public boolean isIn(Account a)
   { /*@ REQUIRES: a!= null ;
       @ EFFECTS:     
         (this.contains(a)==true)==> (\result==true);
         (this.contains(a)==false)==>(\result==false);
       */
	   return Accounts.contains(a)?true:false;
   }   

   public long size()
   { /* @ EFFECTS: 
        (\result==this.size());
     */
	   return (long)Accounts.size();
   }   
  
   
   public Iterator elements()
 { /* @ REQUIRES:this!=null
      @ EFFECTS: 
        (\result==this.size());
     */
	  return Accounts.iterator();
   }   
   


   public Account queryByAccountId(String accountId)
   { /*@ REQUIRES: accountId!= null ;
        
       @ EFFECTS: 
         normal_behavior    
        (\all Account a;a.getAccountId()==accountId && (this.contains(a)==true))==>(\result==a);
        (\all Account a;a.getAccountId()==accountId && (this.contains(a)==false))==>exceptional_behavior(InvalidAccountException) ;   

       */
	   for(int i=0;i<Accounts.size();i++){
		   if(Accounts.get(i).getAccountId().equals(accountId)){
			   return Accounts.get(i);
		   }
	   }
	   return null;
	   //throw new InvalidAccountException();
   }   
  
   
      
   public AccountSet queryByUserId(String userId)
   { /*@ REQUIRES: userId!= null ;        
       @ EFFECTS: 
         normal_behavior
        (AccountSet as;as.size()==0)    
        (\all Account a;a.getUserId()==userId && (this.contains(a)==true))==>(\result==as.insert(a));

        (\all Account a;a.getUserId()==userId && (this.contains(a)==false))==>exceptional_behavior(InvalidUserException) ;   

       */
	   int flag = 0;
	   AccountSet as = new AccountSet();
	   
	   for(int i=0;i<Accounts.size();i++){
		   if(Accounts.get(i).getUserId().equals(userId)){
			   flag = 1;
			   as.insert(Accounts.get(i));
		   }
	   }
	   if(flag==1)	return as;
	   else			return null;
	   //else	throw new InvalidUserException();
   }   
  
   
   public boolean reqOK(){
	   if(Accounts==null)	return false;
	   return true;
   }
   
}

