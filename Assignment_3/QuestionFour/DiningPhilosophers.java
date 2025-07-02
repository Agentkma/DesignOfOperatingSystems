package QuestionFour;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// DiningPhilosophers class solves the classic Dining Philosophers problem
// using an array of fair ReentrantLocks to represent forks.
public class DiningPhilosophers {
    // Each fork is represented by a fair ReentrantLock to prevent starvation.
    // create an array to hold 5 ReentrantLocks references, this does NOT instantiate the locks
    private final Lock[] forks = new ReentrantLock[5];
    // Constructor to initialize the class.
    // Initialize each fork with a fair lock policy.
    public DiningPhilosophers() {
        // loop 5 times to for 5 forks
        for (int i = 0; i < 5; i++) {
            // instantiate each lock in the array
            forks[i] = new ReentrantLock(true); // fair lock ensures FIFO granting of locks
        }
    }

    // wantsToEat is called by each philosopher when they want to eat.
    // The method params are Runnables for picking up/putting down forks and eating.
    public void wantsToEat(int philosopher, 
                           Runnable pickLeftFork, 
                           Runnable pickRightFork, 
                           Runnable eat, 
                           Runnable putLeftFork, 
                           Runnable putRightFork) throws InterruptedException { 
        // Calculate the indices of the left and right forks for this philosopher.
        // since we have 5 indexes, we set the left to the philosopher number
        // and the right to the next index, using modulo to wrap around at 5
        int left = philosopher;
        int right = (philosopher + 1) % 5;

        // To avoid deadlock, always pick up the lower-numbered fork first.
        // this enforces an order on lock acquisition to prevent circular wait
        Lock firstFork = forks[Math.min(left, right)];
        Lock secondFork = forks[Math.max(left, right)];

        // Acquire both forks (locks) before eating.
        firstFork.lock();
        secondFork.lock();
        try {
            // Pick up forks in the correct order, again to avoid deadlock/circular waiting
            // this handles the case for philosopher 4 picking up fork 0
            // that is the last item in the array, the first item is index 0
            // so we need to ensure the lower index is picked up first
            
            if (left < right) {
                // trigger the runnable go synchronously in the current thread
                pickLeftFork.run();
                pickRightFork.run();
            } else {
                pickRightFork.run();
                pickLeftFork.run();
            }
            // Eat.
            eat.run();
            // Put down forks in the correct order, again to avoid deadlock/circular waiting
            if (left < right) {
                putLeftFork.run();
                putRightFork.run();
            } else {
                putRightFork.run();
                putLeftFork.run();
            }
        } finally {
            // Always release the locks in reverse order to avoid deadlock.
            secondFork.unlock();
            firstFork.unlock();
        }
    }
}

 class TestDiningPhilosophers {
    public static void main(String[] args) {
        // create a single instance of DiningPhilosophers
        DiningPhilosophers dp = new DiningPhilosophers();
        // create and start 5 philosopher threads in the loop
        for (int i = 0; i < 5; i++) {
            // need a final variable for the lambda expression
            // the final variable captures the current value of i and cannot be changed
            // and is important for thread safety because each thread gets its own copy of philosopher
            // and may run later, but still has the correct value
            final int philosopher = i;
            // each thread simulates a philosopher repeatedly wanting to eat
            new Thread(() -> {
                for (int j = 0; j < 1; j++) { // Each philosopher eats 3 times
                    try {
                        // pass the args, and print out the formatted string in each runnable
                        dp.wantsToEat(
                            philosopher,
                            () -> System.out.println("[" + philosopher + ",1,1]"),
                            () -> System.out.println("[" + philosopher + ",2,1]"),
                            () -> System.out.println("[" + philosopher + ",0,3]"),
                            () -> System.out.println("[" + philosopher + ",1,2]"),
                            () -> System.out.println("[" + philosopher + ",2,2]")
                        );
                        // Simulate thinking
                        Thread.sleep((long)(Math.random() * 100));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }).start();
        }
    }
}