package integration;

import logic.BankAccountLogic;
import model.BankAccount;
import model.Currency;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountLogicTest {
    private static final int NUM_THREADS = 20;
    private static final int NUM_ITERATIONS = 10000;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createBankAccount() throws InterruptedException {
        List<Thread> allThreads=new ArrayList<>();
        class TransferThread extends Thread {
            public void run() {
                for (int i=0; i<NUM_ITERATIONS; i++) {
                        BankAccountLogic.getInstance().createBankAccount("Henry",
                                BigDecimal.valueOf(100),Currency.GBP);
                }
            }
        }
        for (int i = 0; i < NUM_THREADS; i++){
            Thread newThread= new TransferThread();
            allThreads.add(newThread);
            newThread.start();
        }

        for(Thread thread : allThreads)
            thread.join();
        List<BankAccount> bankAccounts= BankAccountLogic.getInstance().getAllBankAccounts();
        assertEquals(NUM_ITERATIONS*NUM_THREADS,bankAccounts.size());
    }

    @Test
    void getAllBankAccounts() {
        BankAccountLogic bankAccountLogicInstance= BankAccountLogic.getInstance();
        BankAccount result= bankAccountLogicInstance.createBankAccount("henry",
                BigDecimal.valueOf(100), Currency.GBP);
        assertNotEquals(null,result);
    }
}