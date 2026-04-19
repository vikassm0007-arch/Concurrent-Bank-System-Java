import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Shared Resource
class BankAccount {
    private int balance = 1000;

    // Lock for thread safety
    private final Lock lock = new ReentrantLock();

    // Deposit Method
    public void deposit(int amount) {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " depositing: " + amount);
            balance += amount;
            System.out.println("Balance after deposit: " + balance);
        } finally {
            lock.unlock();
        }
    }

    // Withdraw Method
    public void withdraw(int amount) {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " withdrawing: " + amount);

            if (balance >= amount) {
                balance -= amount;
                System.out.println("Balance after withdrawal: " + balance);
            } else {
                System.out.println("Insufficient balance!");
            }
        } finally {
            lock.unlock();
        }
    }

    public int getBalance() {
        return balance;
    }
}

// Thread Class
class TransactionThread extends Thread {
    private BankAccount account;
    private int amount;
    private boolean isDeposit;

    public TransactionThread(BankAccount account, int amount, boolean isDeposit) {
        this.account = account;
        this.amount = amount;
        this.isDeposit = isDeposit;
    }

    @Override
    public void run() {
        if (isDeposit) {
            account.deposit(amount);
        } else {
            account.withdraw(amount);
        }
    }
}

// Main Class
public class MultiThreadedBankApp {
    public static void main(String[] args) {

        BankAccount account = new BankAccount();

        // Creating multiple threads
        Thread t1 = new TransactionThread(account, 500, true);
        Thread t2 = new TransactionThread(account, 300, false);
        Thread t3 = new TransactionThread(account, 700, true);
        Thread t4 = new TransactionThread(account, 400, false);

        // Naming threads
        t1.setName("User-1");
        t2.setName("User-2");
        t3.setName("User-3");
        t4.setName("User-4");

        // Start threads
        t1.start();
        t2.start();
        t3.start();
        t4.start();

        // Wait for threads to finish
        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nFinal Balance: " + account.getBalance());
    }
}