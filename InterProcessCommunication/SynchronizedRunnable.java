package InterProcessCommunication;

class Q {
    int num;
    boolean valueSet = false;

    // Putting the value
    public synchronized void put(int num) {
        while (valueSet) {
            try {
                wait();
            } catch (Exception e) {
                // Exception ignored because thread is waiting for notification
            }
        }
        System.out.println("Put : " + num);
        this.num = num;
        valueSet = true;
        notify();
    }

    public synchronized void get() {
        while (!valueSet) {
            try {
                wait();
            } catch (Exception e) {
                // Exception ignored because thread is waiting for notification
            }
        }
        System.out.println("Get : " + num);
        valueSet = false;
        notify();
    }
}

// Producer putting
class Producer implements Runnable {
    Q q;

    public Producer(Q q) {
        this.q = q;
        Thread t1 = new Thread(this, "Producer");
        t1.start();
    }

    public void run() {
        int i = 0;
        while (i < 10) { // End condition: produce 10 items
            q.put(i);
            i++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupt status
                break; // Exit loop if interrupted
            }
        }
    }
}

// Consumer consumes
class Consumer implements Runnable {
    Q q;

    public Consumer(Q q) {
        this.q = q;
        Thread t2 = new Thread(this, "Consumer");
        t2.start();
    }

    public void run() {
        int i = 0;
        while (i < 10) { // End condition: consume 10 items
            q.get();
            i++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupt status
                break; // Exit loop if interrupted
            }
        }
    }
}

public class SynchronizedRunnable {
    public static void main(String[] args) {
        Q q = new Q();
        // Creating Objects
        new Producer(q);
        new Consumer(q);
    }
}