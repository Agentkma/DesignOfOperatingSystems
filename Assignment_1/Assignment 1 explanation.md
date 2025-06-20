The `Runnable` interface in Java is part of the `java.lang` package and is used to define a task that can be executed by a thread. It's a core part of Java’s concurrency framework.

Here’s the deal:

- **Runnable has only one method**:

  ```java
  public void run();
  ```

- You implement this interface when you want to define a task (the code inside the `run()` method) that a thread can execute.

### Quick example:

```java
public class MyTask implements Runnable {
    @Override
    public void run() {
        System.out.println("Task is running...");
    }
}

public class Main {
    public static void main(String[] args) {
        Thread thread = new Thread(new MyTask());
        thread.start(); // This triggers run()
    }
}
```

When you pass a `Runnable` to a `Thread`, calling `start()` on that thread will internally call the `run()` method on your task. This is how you can run code concurrently.

It’s especially useful when you want to separate task logic from thread management

Question 1: Rewrite Using the Runnable Interface
Your current code defines two classes, Hi and Hello, both extending Thread. The assignment wants you to refactor this so the classes instead implement the Runnable interface—which separates the task (what the thread does) from the threading itself (how it runs).

Key difference:
With extends Thread, the class is a thread.
With implements Runnable, the class is just a task—you hand it to a Thread instance to run.

Question 2. Explain the memory hierarchy.

### **Memory Hierarchy in Context of the Runnable Thread Example**

In your refactored `Runnable`-based threading example, the **memory hierarchy** plays a vital role behind the scenes. Each component of that hierarchy contributes to how efficiently the threads execute and share resources.

#### Here’s how it maps out:

1. **Registers**:  
   When the CPU begins executing the `run()` method inside `Hi` or `Hello`, it uses its _registers_ to hold immediate values like the loop counter `i`, memory addresses, and intermediate results of expressions. These are lightning-fast and used during every loop iteration.

2. **Cache (L1/L2/L3)**:  
   The CPU’s cache temporarily stores frequently accessed parts of your code (like the loop body and print statements) and data (like `i` and method calls). Because `System.out.println("Hi")` gets executed multiple times, it’s likely to be cached for speed.

3. **Main Memory (RAM)**:  
   This is where the thread objects and their associated data live during runtime. When you call `new Hi()` and `new Hello()`, their instances, along with the `Thread` wrappers, get allocated space in main memory.

4. **Secondary Storage (HDD/SSD)**:  
   Before the program runs, your `.class` files (bytecode) are stored here. When you launch the program, the operating system loads them into memory. Also, if output were redirected to a file, it would go here.

5. **Tertiary/Off-line Storage**:  
   Not directly used in this example, but this would be where you archive source code or compiled artifacts for long-term backup (e.g., USB, cloud).

---

### 🎯 Why This Matters in OS Design

Your `Runnable`-based multithreading example is a hands-on demo of **how the OS manages concurrent execution**, and the memory hierarchy ensures the CPU has rapid access to the most critical data at each stage. Slower parts of memory aren’t ignored—they just serve different roles in the execution pipeline.

Want to take this even deeper? We could explore how **context switching** relies on saving and restoring from different memory layers, or how **cache coherence** affects multithreaded performance. It all links back to how the OS juggles resources to keep threads humming.

Let me know if you want this written up as a class-ready submission or visualized with a memory pyramid linked to the code. 🧠⚙️📝
