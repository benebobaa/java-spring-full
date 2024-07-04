package test;

import org.junit.jupiter.api.Test;

public class ThreadTest {

    private String message = null;

    @Test
    public void testNewThread() throws InterruptedException {


        Thread thread1 = new Thread(() -> {
            while (message == null) {

            }

            System.out.println(message);
        });

        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            message = "Hello Thread1";
        });

        thread2.start();
        thread1.start();

        thread1.join();
        thread2.join();

    }
}
