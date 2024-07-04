public class LearnThread {

    public static void main(String[] args) throws InterruptedException {

        Thread thread = Thread.currentThread();
        System.out.println(thread.getName());

        Runnable runnable = () -> {

            try {
                Thread.sleep(2_000L);
            System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        Thread thread2 = new Thread(runnable);
//        thread2.setDaemon(true);
        thread2.start();
        System.out.println("Waiting...");
//        thread2.join();
        System.out.println("Done");

//        Runnable runnableInterrupt = () -> {
//            for (int i = 0; i < 10; i++) {
//                System.out.println("Runnable -> " + i);
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
////                throw new RuntimeException(e);
//                    System.out.println(e);
//                }
//            }
//
//
//        };
//
//
//        Thread threadInterrupt = new Thread(runnableInterrupt);
//
//        threadInterrupt.start();
//        System.out.println("Starting thread interrupt");
//        Thread.sleep(5000);
//        threadInterrupt.interrupt();
//        threadInterrupt.join();
//        System.out.println("Done");
    }
}
