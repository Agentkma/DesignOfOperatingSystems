class Foo {
    private boolean firstDone = false; // variable to track if first() has been called
    private boolean secondDone = false; // variable to track if second() has been called
    private final Object lock = new Object();//In Java, any object can be used as a lock for synchronization
    
    public Foo() {
        // no initialization needed
    }

    public void first(Runnable printFirst) throws InterruptedException {
        // use synchronized block to protect code inside it
        synchronized (lock) { // Acquire the lock to ensure exclusive access, thread will wait until the lock is released and then acquire
            // printFirst.run() outputs "first". Do not change or remove this line.
            printFirst.run(); // Execute the Runnable to print "first"
            firstDone = true; // Mark that the first method has completed
            lock.notifyAll(); // Notify any waiting threads that they can proceed
        }
    }

    public void second(Runnable printSecond) throws InterruptedException {
        synchronized (lock) {// Acquire the lock to ensure exclusive access, thread will wait until the lock is released and then acquire
            while (!firstDone) // check if first() has been called
                lock.wait();// wait until first() has been called
            }// this while loop will continue unit firstDone is true
            
            // printSecond.run() outputs "second". Do not change or remove this line.
            printSecond.run();
            secondDone = true;// mark that the second method has completed
            lock.notifyAll();// Notify any waiting threads that they can proceed to try to acquire lock
        }
    }

    public void third(Runnable printThird) throws InterruptedException {
        synchronized (lock) {
            while (!secondDone) {// check if second() has been called
                lock.wait();// wait until second() has been called
            }// this while loop will continue until secondDone is true
            
            // printThird.run() outputs "third". Do not change or remove this line.
            printThird.run();
        }
    }
}


class FooTest {
    public static void main(String[] args) {
        Foo foo = new Foo(); // create an instance of Foo
        // create three threads to call first(), second(), and third() methods in order
        // Thread constructor expects a Runnable. A lambda matches a functional interface if its parameters and 
        // return type match the interface’s single abstract method. 
        // Here, the lambda matches Runnable because it has the same signature as void run()
        Thread threadA = new Thread(() -> {
            // the Foo methods are declared with throws InterruptedException, so we need to handle it
            try { 
                foo.first(() -> System.out.print("first")); //() -> System.out.print("first") is a lambda expression that implements the Runnable interface's run() method.
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        // The second thread will call the second() method of Foo
        Thread threadB = new Thread(() -> {
            try {
                foo.second(() -> System.out.print("second"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        // The third thread will call the third() method of Foo
        Thread threadC = new Thread(() -> {
            try {
                foo.third(() -> System.out.print("third"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        //The start() calls are sequential.
        //The threads themselves may run in parallel (or concurrently),
        // meaning their execution overlaps in time, and you cannot predict the order 
        // in which their code will run unless you synchronize them which we do in Foo class.
        threadC.start(); // Start threadC (calls foo.third()), thread begins execution in parallel
        threadB.start(); // Start threadB (calls foo.second()), thread begins execution in parallel
        threadA.start(); // Start threadA (calls foo.first()), thread begins execution in parallel
        // If the current thread (the one calling join()) is interrupted while waiting for threadA to finish, 
        //an InterruptedException is thrown and we must handle it.
        try {
            threadA.join(); // Wait for threadA to finish before continuing
            threadB.join(); // Wait for threadB to finish before continuing
            threadC.join(); // Wait for threadC to finish before continuing
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
