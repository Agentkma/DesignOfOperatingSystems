# ProducerConsumerLogging

## Overview

The `ProducerConsumerLogging` program demonstrates a solution to the Producer-Consumer problem using Java's `BlockingQueue`. It simulates a logging system where log messages are produced by one thread and consumed (written to a file) by another thread.

## Producer-Consumer Problem

The Producer-Consumer problem is a classic synchronization problem where:

- **Producers** generate data and place it into a shared buffer.
- **Consumers** retrieve data from the shared buffer for processing.
  The challenge lies in ensuring that producers do not overwrite data in a full buffer and consumers do not read from an empty buffer, while maintaining thread safety.

## Solution in `ProducerConsumerLogging`

This program solves the problem using the following components:

1. **Shared Buffer**: A `BlockingQueue` is used as the shared buffer. It provides thread-safe operations for adding and removing elements, blocking producers when the buffer is full and consumers when the buffer is empty.
2. **Producer**: The `LogProducer` class generates log messages and places them into the `BlockingQueue`. It simulates log generation at regular intervals.
3. **Consumer**: The `LogConsumer` class retrieves log messages from the `BlockingQueue` and writes them to a file. It ensures logs are flushed to disk immediately.
4. **Graceful Shutdown**: A shutdown hook ensures that the program terminates gracefully when interrupted (e.g., via Ctrl+C), allowing threads to stop and logs to be flushed.

## Key Features

- **Thread Safety**: The `BlockingQueue` ensures safe concurrent access by producer and consumer threads.
- **Blocking Operations**: Producers wait when the buffer is full, and consumers wait when the buffer is empty, avoiding busy-waiting.
- **Graceful Termination**: The shutdown hook ensures proper cleanup when the application is interrupted.
