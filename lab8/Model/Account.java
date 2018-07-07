
import java.util.*;

/**
 * 
 */
public class Account {

    /**
     * Default constructor
     */
    public Account() {
    }

    /**
     * 
     */
    private String Name;

    /**
     * 
     */
    private String UserId;

    /**
     * 
     */
    private String Password;

    /**
     * 
     */
    private long Money;

    /**
     * 
     */
    private long AccountNum;

    /**
     * 
     */
    private Date Date;

    /**
     * 
     */
    private String WebSite;

    /**
     * @param name 
     * @param number 
     * @param password
     */
    void Account(String name, String number, String password) {
        // TODO implement here
    }

    /**
     * @param Money 
     * @return
     */
    public void Deposit(long Money) {
        // TODO implement here
        return null;
    }

    /**
     * @param Money 
     * @return
     */
    public boolean Withdraw(long Money) {
        // TODO implement here
        return false;
    }

    /**
     * @return
     */
    public long LookUp() {
        // TODO implement here
        return 0;
    }

    /**
     * @param writer 
     * @return
     */
    public void Save2File(FileWriter writer) {
        // TODO implement here
        return null;
    }

    /**
     * @param number 
     * @return
     */
    public boolean IsSame(String number) {
        // TODO implement here
        return false;
    }

    /**
     * @param RelatedCredit 
     * @return
     */
    public void SetCreditAccount(CreditAccount RelatedCredit) {
        // TODO implement here
        return null;
    }

}