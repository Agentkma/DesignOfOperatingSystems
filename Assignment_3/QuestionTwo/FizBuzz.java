package QuestionTwo;

import java.util.function.IntConsumer;

public class FizBuzz {
    private int n; // number up to which to print to
    private int current = 1; // current number to print
    private final Object lock = new Object(); // lock object for synchronization

    // constructor to initialize n
    public FizBuzz(int n) {
        this.n = n;
    }
    // abstraction to avoid code duplication, pass a condition to check and an action to perform
    private void runForCondition(java.util.function.BooleanSupplier condition, Runnable action) throws InterruptedException {
        // loop until lock is acquired
        while (true) {
            synchronized (lock) {
                // wait until the condition is met 
                while (current <= n && !condition.getAsBoolean()) {
                    lock.wait();
                }
                // break the loop if current exceeds n
                if (current > n) break;
                // perform the action
                action.run();
                // increment the current number 
                current++;
                // notify all waiting threads that are waiting on the lock
                lock.notifyAll();
            }
        }
    }

    // fiz method to print "fizz" for multiples of 3
    public void fizz(Runnable printFizz) throws InterruptedException {
        runForCondition(() -> current % 3 == 0 && current % 5 != 0, printFizz);
    }

    // buzz method to print "buzz" for multiples of 5
    public void buzz(Runnable printBuzz) throws InterruptedException {
        // use abstraction to DRY up : condition is the lambda expression that checks if current is a multiple of 5 but not 3
        runForCondition(() -> current % 5 == 0 && current % 3 != 0, printBuzz);
    }

    // fizzbuzz method to print "fizzbuzz" for multiples of both 3 and 5
    public void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException {
        runForCondition(() -> current % 15 == 0, printFizzBuzz);
    }

    // number method to print numbers that are not multiples of 3 or 5
    public void number(IntConsumer printNumber) throws InterruptedException {
        runForCondition(() -> current % 3 != 0 && current % 5 != 0, () -> printNumber.accept(current));
    }
}


class TestFizBuzz {
    public static void main(String[] args) throws InterruptedException {
        // create an instance of FizBuzz to print numbers from 1 to 15
        FizBuzz fizzBuzz = new FizBuzz(15);
        // create four threads to call fizz(), buzz(), fizzbuzz(), and number() methods
        // all Runnables need to handle InterruptedException
        Thread threadA = new Thread(() -> {
            try {
                // pass the action as a lambda expression that prints "fizz"
                fizzBuzz.fizz(() -> System.out.print("fizz,"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread threadB = new Thread(() -> {
            try {
                // pass the action as a lambda expression that prints "buzz"
                fizzBuzz.buzz(() -> System.out.print("buzz,"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread threadC = new Thread(() -> {
            try {
                // pass the action as a lambda expression that prints "fizzbuzz"
                fizzBuzz.fizzbuzz(() -> System.out.print("fizzbuzz,"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread threadD = new Thread(() -> {
            try {
                // pass the action as a lambda expression that prints the current number
                fizzBuzz.number((num) -> System.out.print(num + ","));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // start all four threads
        threadA.start();
        threadB.start();
        threadC.start();
        threadD.start();
// thread join() can throw InterruptedException, so we need to handle it
        try {
            // wait for all threads to finish
            threadA.join();
            threadB.join();
            threadC.join();
            threadD.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}