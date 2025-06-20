class Hi implements Runnable {
    public void run() {
        for(int i = 1; i <= 5; i++) {
            System.out.println("Hi");
            try { Thread.sleep(1000); } catch (Exception e) {}
        }
    }
}

class Hello implements Runnable {
    public void run() {
        for(int i = 1; i <= 5; i++) {
            System.out.println("Hello");
            try { Thread.sleep(1000); } catch (Exception e) {}
        }
    }
}

class Main {
    public static void main(String[] args) {
        Thread t1 = new Thread(new Hi());
        Thread t2 = new Thread(new Hello());

        t1.start();
        try { Thread.sleep(100); } catch (Exception e) {}
        t2.start();
    }
}

/* Explain the memory hierarchy
 * The program is run in Main Memory. However, the loop body may be stored in the Cache.  
 * The Thread objects and associated data will be in Main memory during runtime.
 * The file itself is stored in Secondary Storage (like a hard drive or SSD).
 */