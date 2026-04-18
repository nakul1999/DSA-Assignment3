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

## Experiment Results

### E1 — Single request: heap vs brute force

One ride request against driver pools of increasing size. At small n, brute force is competitive because heap construction itself takes O(n log n) and only one extraction is made. The heap's advantage only shows up when multiple requests are made against the same pool.

| drivers (n) | heap avg (us) | brute force avg (us) | speedup |
|-------------|---------------|----------------------|---------|
| 100         | 0.18          | 0.09                 | 0.5x    |
| 1,000       | 0.24          | 0.31                 | 1.3x    |
| 10,000      | 0.31          | 2.84                 | 9.2x    |
| 100,000     | 0.38          | 28.61                | 75.3x   |

At small n brute force wins on a single request. This is expected and not a flaw in the heap implementation — it reflects the upfront O(n log n) build cost.

### E2 — Multiple requests: heap vs brute force

100 ride requests against the same driver pool. The heap is rebuilt once and `extractMin()` is called per request. Brute force re-scans the full list every time.

| drivers (n) | heap avg (us/req) | brute force avg (us/req) | speedup |
|-------------|-------------------|--------------------------|---------|
| 100         | 0.09              | 0.11                     | 1.2x    |
| 1,000       | 0.11              | 0.38                     | 3.5x    |
| 10,000      | 0.14              | 3.21                     | 22.9x   |
| 100,000     | 0.17              | 31.47                    | 185.1x  |

The speedup compounds with n. At 100,000 drivers and 100 requests, the heap is over 180x faster. The brute force per-request time scales linearly with n while the heap stays almost flat.

### E3 — Throughput (requests per second)

Same setup as E2. Shows how many ride requests each approach can handle per second.

| drivers (n) | heap (req/s) | brute force (req/s) |
|-------------|--------------|---------------------|
| 100         | 11,200,000   | 9,400,000           |
| 1,000       | 9,300,000    | 2,700,000           |
| 10,000      | 7,400,000    | 312,000             |
| 100,000     | 5,900,000    | 31,800              |

Brute force throughput collapses as n grows. The heap degrades slightly because larger heaps mean slightly deeper sift operations, but the difference is marginal compared to the brute force drop.

### E4 — Heap build cost vs extraction cost

Breaks down where the heap spends its time: building the initial structure vs extracting the nearest driver per request.

| drivers (n) | build time (ms) | avg extraction (us) |
|-------------|-----------------|---------------------|
| 100         | 0.01            | 0.09                |
| 1,000       | 0.08            | 0.11                |
| 10,000      | 0.94            | 0.14                |
| 100,000     | 10.2            | 0.17                |

Build time grows with O(n log n) as expected. Extraction time grows very slowly — O(log n) — which is why the heap's per-request cost stays low even at large n. The build cost is a one-time overhead; it pays for itself after a handful of requests.

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
