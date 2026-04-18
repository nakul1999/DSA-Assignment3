# Ride Matching System — COMP47500 Assignment 4

A ride matching system written in Java that assigns the nearest available driver to a ride request. Built around a custom Min Heap implementation, with a brute-force matcher included for performance comparison.

---

## Table of Contents

- [What this does](#what-this-does)
- [Project Structure](#project-structure)
- [Classes](#classes)
- [Getting Started](#getting-started)
- [Running the Experiments](#running-the-experiments)
- [Experiment Results](#experiment-results)
- [Known Limitations](#known-limitations)
- [References](#references)

---

## What this does

When a ride request comes in, the system finds the nearest available driver and assigns them. The heap keeps drivers ordered by distance from the user, so the closest one is always at the top and can be retrieved in O(log n) time.

A brute-force matcher is also included. It scans every driver on each request and finds the nearest one in O(n) time. It exists purely as a baseline — the experiments show how the two approaches diverge as the number of drivers and requests grows.

---

## Project Structure

```
RideMatchingSystem/
├── Driver.java
├── MinHeap.java
├── RideMatchingSystem.java
├── BruteForceMatcher.java
├── ExperimentRunner.java
└── README.md
```

---

## Classes

### `Driver`
Represents a driver with a 2D coordinate position and an availability flag. Distance from a given user location is calculated using Euclidean distance.

### `MinHeap`
The core data structure. Stores drivers ordered by distance from the current user location. `insert()` adds a driver and sifts up to restore heap order. `extractMin()` removes and returns the nearest driver and sifts down. Both operations run in O(log n).

### `RideMatchingSystem`
The heap-based matcher. Builds a min heap from the available driver pool and uses `extractMin()` to assign the nearest driver per request.

### `BruteForceMatcher`
Scans the full driver list on every request and returns the nearest one. O(n) per request. Used as a performance baseline against the heap.

### `ExperimentRunner`
Runs both matchers across different dataset sizes and request volumes. Reports execution time, average latency per request, throughput, and speedup. See the Experiment Results section below.

---

## Getting Started

Requirements: Java 11 or higher.

```bash
# Compile
javac *.java

# Run
java ExperimentRunner
```

No external dependencies or build tools required.

### Basic usage

```java
List<Driver> drivers = new ArrayList<>();
drivers.add(new Driver("D1", 2.0, 3.0));
drivers.add(new Driver("D2", 5.0, 1.0));
drivers.add(new Driver("D3", 1.0, 4.0));

double userX = 0.0, userY = 0.0;

RideMatchingSystem system = new RideMatchingSystem(drivers, userX, userY);
Driver nearest = system.matchDriver();
System.out.println("Assigned: " + nearest.getId());
```

---

## Running the Experiments

```bash
java ExperimentRunner
```

### Methodology

Each experiment runs 3 warmup iterations before measurement begins. Warmup results are discarded to let the JVM JIT-compile the hot paths before timings are taken. After warmup, 5 measured runs are taken per configuration. Per-request time in microseconds is reported throughout so results across different dataset sizes are directly comparable.

Average, minimum, maximum, and standard deviation are reported for each configuration. A low standard deviation means the result is stable. A high one usually points to GC activity or OS scheduling interference.

---

## Running the Experiments
 
```bash
java ExperimentRunner
```
 
Each experiment runs 3 warmup iterations before measurement begins. Warmup results are discarded to let the JVM JIT-compile the hot paths before timings are taken. After warmup, 5 measured runs are taken per configuration and the average is reported. Per-request time is reported alongside total time so results across different dataset sizes are directly comparable.
 
---
 
## Experiment Results
 
Both approaches were tested across four driver pool sizes: 1,000, 5,000, 10,000, and 50,000 drivers. Each configuration processed 500 ride requests. The table below shows total execution time and average time per request for each approach.
 
| drivers | heap total (ms) | heap avg/req (ms) | brute force total (ms) | brute force avg/req (ms) | speedup |
|---------|-----------------|-------------------|------------------------|--------------------------|---------|
| 1,000   | 0.135           | 0.00027           | 0.660                  | 0.00132                  | 4.9x    |
| 5,000   | 0.198           | 0.000396          | 4.109                  | 0.008218                 | 20.8x   |
| 10,000  | 0.624           | 0.001248          | 7.883                  | 0.015766                 | 12.6x   |
| 50,000  | 1.229           | 0.002458          | 69.471                 | 0.138942                 | 56.5x   |
 
### What the results show
 
At 1,000 drivers the heap is already around 5x faster than brute force. By 50,000 drivers that gap has grown to nearly 57x. This is the expected behaviour — brute force time scales linearly with n since every request triggers a full scan of the driver pool, while the heap only needs O(log n) per extraction.
 
The heap's average time per request grows slowly as n increases: from 0.00027ms at 1,000 drivers to 0.002458ms at 50,000, roughly a 9x increase over a 50x increase in pool size. Brute force average time over the same range goes from 0.00132ms to 0.138942ms, a 105x increase. This confirms the O(log n) vs O(n) difference in practice, not just in theory.
 
The slight dip in speedup at 10,000 drivers (12.6x vs 20.8x at 5,000) is worth noting. This is likely due to the heap build cost — constructing the heap takes O(n log n), and at 10,000 drivers that upfront cost is large enough relative to the per-request savings that the overall speedup temporarily drops before recovering at 50,000. In a real system where the driver pool changes infrequently and request volume is high, this build cost becomes negligible over time.
 
---

## Known Limitations

**Single user location.** The heap is built relative to one fixed point. Supporting multiple concurrent users would require either a separate heap per user or a more general spatial structure like a KD-Tree.

**Euclidean distance only.** Real-world distance depends on road networks, traffic, and one-way streets. Euclidean distance is a reasonable approximation for benchmarking but not for production routing.

**Drivers don't move.** Once the heap is built, driver positions are fixed. A real system would need to handle continuous position updates, which would require efficient heap key updates (decrease-key).

**No concurrency.** Everything runs sequentially. A production system handling simultaneous requests would need thread-safe access to the heap or a per-request copy.

---

## Author

Nakul Agrawal

---

## References

- Cormen, T. et al. *Introduction to Algorithms*, 4th ed., Ch. 6 — Heapsort and Priority Queues
- Sedgewick, R. and Wayne, K. *Algorithms*, 4th ed., Ch. 2.4 — Priority Queues
