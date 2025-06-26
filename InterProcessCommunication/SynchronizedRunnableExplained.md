### Synchronization in SynchronizedRunnable.java

The synchronization in this code is implemented using Java's `synchronized` keyword, `wait()`, and `notify()` methods to coordinate the interaction between the `Producer` and `Consumer` threads.

#### Key Components:

1. **Shared Resource (`Q` Class)**:

   - The `Q` class acts as a shared resource between the `Producer` and `Consumer` threads.
   - It contains a single integer (`num`) and a boolean flag (`valueSet`) to indicate whether a value has been produced and is ready to be consumed.

2. **Synchronization Methods (`put()` and `get()`)**:
   - Both methods are marked as `synchronized`, ensuring that only one thread can execute them at a time.

#### `put(int num)`:

- **Producer's Role**:
  - The `Producer` thread calls `put()` to produce a value.
  - If `valueSet` is `true` (indicating the value has not yet been consumed), the thread enters a `while` loop and calls `wait()`, releasing the lock on the object and pausing until notified by the `Consumer`.
  - Once the value is consumed (`valueSet` becomes `false`), the thread proceeds to set the new value (`num`), marks `valueSet` as `true`, and calls `notify()` to wake up the `Consumer` thread.

#### `get()`:

- **Consumer's Role**:
  - The `Consumer` thread calls `get()` to consume a value.
  - If `valueSet` is `false` (indicating no value has been produced), the thread enters a `while` loop and calls `wait()`, releasing the lock on the object and pausing until notified by the `Producer`.
  - Once a value is produced (`valueSet` becomes `true`), the thread proceeds to consume the value (`num`), marks `valueSet` as `false`, and calls `notify()` to wake up the `Producer` thread.

### Thread Coordination:

- The `Producer` and `Consumer` threads alternate execution:
  - The `Producer` produces a value and notifies the `Consumer`.
  - The `Consumer` consumes the value and notifies the `Producer`.
- This ensures that the `Producer` does not overwrite a value before it is consumed, and the `Consumer` does not attempt to consume a value that has not yet been produced.

### Example Flow:

1. **Initial State**:

   - `valueSet = false`.
   - The `Producer` produces a value (`put()`), sets `valueSet = true`, and calls `notify()`.

2. **Consumer Consumes**:

   - The `Consumer` wakes up, consumes the value (`get()`), sets `valueSet = false`, and calls `notify()`.

3. **Repeat**:
   - The cycle repeats until both threads complete their tasks (producing and consuming 10 items).

### Thread Safety:

- The `synchronized` keyword ensures mutual exclusion, preventing race conditions.
- The `wait()` and `notify()` methods handle thread communication, ensuring proper sequencing between the `Producer` and `Consumer`.

This design is a classic example of the **Producer-Consumer Problem** solved using Java's intrinsic locks and
