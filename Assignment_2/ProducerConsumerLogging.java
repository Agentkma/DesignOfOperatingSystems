import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.*;

//shutdown hook  will be triggered when the application receives a SIGINT (Ctrl+C)

public class ProducerConsumerLogging {

    private static final int BUFFER_CAPACITY = 100;
    private static final BlockingQueue<String> logQueue = new LinkedBlockingQueue<>(BUFFER_CAPACITY);

    private static final String LOG_FILE_PATH = "/Users/Kevin/Code/Design of Operating Systems/Assignment_2/system_logs.txt";

    // Assignment_2/system_logs.txt
    public static void main(String[] args) {
        // Start producer and consumer threads
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            executor.execute(new LogProducer());
            executor.execute(new LogConsumer());

            // Optional: Shut down gracefully after some time (e.g., for demo purposes)
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                executor.shutdownNow();
                System.err.println("Shutdown initiated. Logs flushed.");
            }));
        } finally {
            executor.shutdown();
        }
    }

    static class LogProducer implements Runnable {
        @Override
        public void run() {
            try {
                int logCounter = 1;
                while (!Thread.currentThread().isInterrupted()) {
                    String logMessage = String.format(
                            "[%s] INFO: Log message #%d",
                            LocalDateTime.now(), logCounter++);
                    logQueue.put(logMessage); // blocks if queue is full
                    Thread.sleep(500); // simulate log generation rate
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("LogProducer interrupted");
            }
        }
    }

    static class LogConsumer implements Runnable {
        @Override
        public void run() {

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH, true))) {
                while (!Thread.currentThread().isInterrupted()) {
                    String log = logQueue.take(); // blocks if queue is empty
                    writer.write(log);
                    writer.newLine();
                    writer.flush(); // force disk write for every line (could be buffered in practice)
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("LogConsumer interrupted");
            } catch (IOException e) {
                System.err.println("Error writing logs to file: " + e.getMessage());
            }
        }
    }
}

/*
 * What are the components of OS? The OS is made up of the kernel and system
 * programs. The OS provides services via system calls, command line
 * interpreter, and gui interfaces
 * Why are system calls in OS? To complete some operation that the operating
 * system controls. To provide an interface to the services made available by an
 * operating system.
 */