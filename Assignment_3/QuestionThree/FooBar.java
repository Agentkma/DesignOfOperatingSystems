package QuestionThree;

class FooBar {
    private int n; // number of times to print "foo" and "bar"
    private boolean fooTurn = true; // true if it's foo's turn, false for bar
    private final Object lock = new Object();// create a lock object for synchronization

    // constructor to initialize n
    public FooBar(int n) {
        this.n = n;
    }
// foo method to print "foo"
    public void foo() throws InterruptedException {
        // loop n times
        for (int i = 0; i < n; i++) {
            // acquire the lock
            synchronized (lock) {
                // wait until it's foo's turn
                while (!fooTurn) {
                    lock.wait();
                }
                // print "foo"
                System.out.print("foo");
                // switch turn to bar
                fooTurn = false;
                // notify all waiting threads so they can try to acquire the lock
                lock.notifyAll();
            }
        }
    }
// bar method to print "bar"
    public void bar() throws InterruptedException {
        // loop n times
        for (int i = 0; i < n; i++) {
            // acquire the lock
            synchronized (lock) {
                // wait until it's bar's turn
                while (fooTurn) {
                    lock.wait();
                }
                // print "bar"
                System.out.print("bar");
                // switch turn to foo
                fooTurn = true;
                // notify all waiting threads so they can try to acquire the lock
                lock.notifyAll();
            }
        }
    }
}

class TestFooBar {
    public static void main(String[] args) {
        FooBar fooBar = new FooBar(3); // create an instance of FooBar with n=10
        // create a new thread for foo
        Thread threadFoo = new Thread(() -> {
            // try/catch for InterruptedException
            try { 
                fooBar.foo();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        // create a new thread for bar
        Thread threadBar = new Thread(() -> {
            // try/catch for InterruptedException
            try {
                fooBar.bar();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
  
        //The start() calls are sequential.
        //The threads themselves may run in parallel (or concurrently)
        // start the Bar thread first to demonstrate that it waits for foo to print first
        threadBar.start(); // Start threadBar,thread begins execution in parallel
        threadFoo.start(); // Start threadFoo, thread begins execution in parallel
  
        try {
            threadFoo.join(); // Wait for threadFoo to finish before continuing
            threadBar.join(); // Wait for threadBar to finish before continuing
          
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
