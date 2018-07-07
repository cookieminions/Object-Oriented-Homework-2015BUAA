package Bank;

import java.util.*;

/**
 * 
 */
public class CreditAccount extends Account {

    /**
     * Default constructor
     */
    public CreditAccount() {
    }

    /**
     * 
     */
    private double overdraftLimit;

    /**
     * 
     */
    private String creditRank;

    /**
     * @param name 
     * @param number 
     * @param password
     */
    CreditAccount(String name, String number, String password) {
        // TODO implement here
    	super(name,number,password);
    }

    /**
     * @param Money 
     * @return
     */
    public void setMax(double Money) {
        // TODO implement here
        
    }

    /**
     * @return
     */
    public double getMax() {
        // TODO implement here
        return 0.0d;
    }

    /**
     * @param String Rank 
     * @return
     */
    public void setRank(String Rank) {
        // TODO implement here
        
    }

    /**
     * @return
     */
    public String getRank() {
        // TODO implement here
        return "";
    }

    /**
     * @param Money 
     * @return
     */
    public boolean drawMoney(double Money) {
        // TODO implement here
        return false;
    }

}