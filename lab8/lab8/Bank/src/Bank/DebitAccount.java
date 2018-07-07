package Bank;

import java.util.*;

/**
 * 
 */
public class DebitAccount extends Account {

    /**
     * Default constructor
     */
    public DebitAccount() {
    }
    
    public DebitAccount(String name, String number, String password) {
    	super(name,number,password);
    }

    /**
     * 
     */
    private double annualInterestRate;

    /**
     * @param Rate 
     * @return
     */
    public void setRate(double Rate) {
        // TODO implement here
        
    }

    /**
     * @return
     */
    public double getRate() {
        // TODO implement here
        return 0.0d;
    }

}