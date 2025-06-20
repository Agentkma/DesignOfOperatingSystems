CSCI 4200
Design of Operating System
Assignment 1
Question 1: Demonstrate the following code using Runnable interface.

```java
class Hi extends Thread{
public void run()
    {

        for(int i=1;i<=5;i++)
            {
                System.out.println("Hi");
                try {Thread.sleep(1000); } catch(Exception e) {}
            }
    }
}
class Hello extends Thread{
    public void run()
        {
            for(int i=1;i<=5;i++)
                {
                    System.out.println("Hello");
                    try {Thread.sleep(1000); } catch(Exception e) {}
                }
        }
}
public class Main{
    public static void main(String[] args){
        Hi obj1 = new Hi();
        Hello obj2 = new Hello();
        obj1.start();
        try {Thread.sleep(100); } catch(Exception e) {}
        obj2.start();
    }
}
```

Question 2:
Explain the memory hierarchy. 10 Point
