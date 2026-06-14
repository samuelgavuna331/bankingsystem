package Transactionalsystem.bank;

import java.util.Calendar;

/*
 * ADVANCED PROGRAMMING ASSIGNMENT
 * Single Program Demonstrating:
 * 1. Interface
 * 2. Inheritance
 * 3. Polymorphism
 * 4. Method Overriding
 * 5. Exception Handling
 * 6. Type Casting
 * 7. Transaction Reversal
 */

// ==================== INTERFACE ====================
interface TransactionInterface {

    double getAmount();

    Calendar getDate();

    String getTransactionID();

    void printTransactionDetails();

    void apply(BankAccount ba);
}

// ==================== BANK ACCOUNT ====================
class BankAccount {

    private String accountNumber;
    private double balance;

    public BankAccount(String accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public void withdraw(double amount) {
        balance -= amount;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void displayBalance() {
        System.out.println("Balance = Ksh " + balance);
    }
}

// ==================== CUSTOM EXCEPTION ====================
class InsufficientFundsException extends Exception {

    public InsufficientFundsException(String message) {
        super(message);
    }
}

// ==================== BASE TRANSACTION ====================
class BaseTransaction implements TransactionInterface {

    protected double amount;
    protected Calendar date;
    protected String transactionID;

    public BaseTransaction(double amount, String transactionID) {
        this.amount = amount;
        this.transactionID = transactionID;
        this.date = Calendar.getInstance();
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public Calendar getDate() {
        return date;
    }

    @Override
    public String getTransactionID() {
        return transactionID;
    }

    @Override
    public void printTransactionDetails() {
        System.out.println("\nTransaction Details");
        System.out.println("--------------------");
        System.out.println("Transaction ID : " + transactionID);
        System.out.println("Amount         : " + amount);
        System.out.println("Date           : " + date.getTime());
    }

    @Override
    public void apply(BankAccount ba) {
        System.out.println("Generic transaction applied.");
    }
}

// ==================== DEPOSIT TRANSACTION ====================
class DepositTransaction extends BaseTransaction {

    public DepositTransaction(double amount, String transactionID) {
        super(amount, transactionID);
    }

    @Override
    public void apply(BankAccount ba) {

        ba.deposit(amount);

        System.out.println("\nDeposit Successful");
        System.out.println("Deposited Ksh " + amount);
    }
}

// ==================== WITHDRAWAL TRANSACTION ====================
class WithdrawalTransaction extends BaseTransaction {

    private BankAccount accountUsed;
    private double remainingAmount;

    public WithdrawalTransaction(double amount, String transactionID) {
        super(amount, transactionID);
    }

    // Normal Withdrawal
    @Override
    public void apply(BankAccount ba) {

        try {

            if (ba.getBalance() < amount) {
                throw new InsufficientFundsException(
                        "Insufficient Funds! Balance = "
                                + ba.getBalance());
            }

            ba.withdraw(amount);
            accountUsed = ba;

            System.out.println("\nWithdrawal Successful");
            System.out.println("Withdrawn Ksh " + amount);

        } catch (InsufficientFundsException e) {

            System.out.println("Exception: "
                    + e.getMessage());
        }
    }

    // Overloaded Apply Method
    public void apply(BankAccount ba,
                      boolean availableBalanceWithdrawal) {

        try {

            if (ba.getBalance() <= 0) {

                throw new InsufficientFundsException(
                        "Account balance is zero.");
            }

            if (ba.getBalance() < amount) {

                remainingAmount =
                        amount - ba.getBalance();

                System.out.println(
                        "\nPartial Withdrawal Performed");

                System.out.println(
                        "Amount Withdrawn = "
                                + ba.getBalance());

                System.out.println(
                        "Amount Remaining = "
                                + remainingAmount);

                ba.withdraw(ba.getBalance());
            }

            else {

                ba.withdraw(amount);

                System.out.println(
                        "\nFull Withdrawal Successful");
            }

        } catch (InsufficientFundsException e) {

            System.out.println(
                    "Exception: "
                            + e.getMessage());

        } finally {

            System.out.println(
                    "Transaction Completed.");
        }
    }

    // Reverse Withdrawal
    public boolean reverse() {

        if (accountUsed != null) {

            accountUsed.deposit(amount);

            System.out.println(
                    "\nWithdrawal Reversed.");

            return true;
        }

        return false;
    }
}

// ==================== MAIN CLASS ====================
public class Main {

    public static void main(String[] args) {

        System.out.println("BANK TRANSACTION SYSTEM");
        System.out.println("========================");

        // Create Bank Account
        BankAccount account =
                new BankAccount("ACC1001", 1000);

        System.out.println("\nInitial Account");
        account.displayBalance();

        // =====================================
        // DEPOSIT TEST
        // =====================================

        DepositTransaction deposit =
                new DepositTransaction(
                        500,
                        "DEP001");

        deposit.printTransactionDetails();

        deposit.apply(account);

        account.displayBalance();

        // =====================================
        // WITHDRAWAL TEST
        // =====================================

        WithdrawalTransaction withdrawal =
                new WithdrawalTransaction(
                        300,
                        "WIT001");

        withdrawal.printTransactionDetails();

        withdrawal.apply(account);

        account.displayBalance();

        // =====================================
        // REVERSE WITHDRAWAL
        // =====================================

        withdrawal.reverse();

        account.displayBalance();

        // =====================================
        // POLYMORPHISM TEST
        // =====================================

        System.out.println(
                "\nPOLYMORPHISM TEST");
        System.out.println(
                "-------------------");

        BaseTransaction transaction;

        transaction =
                new DepositTransaction(
                        200,
                        "DEP002");

        transaction.apply(account);

        transaction =
                new WithdrawalTransaction(
                        100,
                        "WIT002");

        transaction.apply(account);

        account.displayBalance();

        // =====================================
        // TYPE CASTING TEST
        // =====================================

        System.out.println(
                "\nTYPE CASTING TEST");
        System.out.println(
                "-------------------");

        BaseTransaction bt =
                new WithdrawalTransaction(
                        50,
                        "WIT003");

        bt.apply(account);

        WithdrawalTransaction wt =
                (WithdrawalTransaction) bt;

        wt.reverse();

        account.displayBalance();

        // =====================================
        // EXCEPTION TEST
        // =====================================

        System.out.println(
                "\nEXCEPTION TEST");
        System.out.println(
                "-------------------");

        WithdrawalTransaction bigWithdrawal =
                new WithdrawalTransaction(
                        10000,
                        "WIT004");

        bigWithdrawal.apply(account);

        // =====================================
        // OVERLOADED APPLY TEST
        // =====================================

        System.out.println(
                "\nOVERLOADED APPLY TEST");
        System.out.println(
                "------------------------");

        WithdrawalTransaction partialWithdrawal =
                new WithdrawalTransaction(
                        5000,
                        "WIT005");

        partialWithdrawal.apply(
                account,
                true);

        account.displayBalance();

        System.out.println(
                "\nPROGRAM COMPLETED SUCCESSFULLY");
    }
}
