# Queue Management System 📊

## Description 📖

This project implements a **Queue Management System** designed to simulate the allocation of clients to queues in a way that minimizes the waiting time. It supports multiple strategies for client allocation, such as the **Shortest Time** strategy and the **Shortest Queue** strategy. Additionally, it tracks important statistics such as **Average Waiting Time**, **Average Service Time**, and **Peak Hour**.
![queue](https://github.com/user-attachments/assets/983bb642-ceed-4a07-b37e-92c9e5fad403)

## Features 🛠️

- **Client Management:** Simulate client arrival and service times.
- **Queue Management:** Efficiently assign clients to queues based on two strategies: Shortest Time & Shortest Queue.
- **Multithreading:** The system utilizes multithreading to simulate real-time queue processing.
- **Logging:** All simulation events are logged into a text file for later analysis.
- **Real-time Progress:** The progress of the simulation is displayed live on a GUI interface.
- **Statistical Insights:** Provides insights such as average waiting time, service time, and peak hour.

## Technologies Used 💻

- **Java:** Core programming language
- **JavaFX:** GUI framework
- **Multithreading:** For real-time simulation
- **Object-Oriented Programming:** To structure the application in an organized manner

## Table of Contents 📑

1. [Assignment Objective](#assignment-objective)
2. [Problem Analysis, Modeling, Scenarios, Use Cases](#problem-analysis-modeling-scenarios-use-cases)
3. [Design](#design)
4. [Implementation](#implementation)
5. [Results](#results)
6. [Conclusions](#conclusions)
7. [Bibliography](#bibliography)

---

## Assignment Objective 🎯

### Main Objective:
To design and implement a **queues management application** that minimizes client waiting times by intelligently assigning them to queues.

### Sub-objectives:
- Use object-oriented principles to design classes and methods.
- Implement a random client generator and multithreading.
- Implement two strategies: shortest time and shortest queue.
- Display simulation results both on the GUI and in a text file.
- Calculate and display Average Waiting Time, Average Service Time, and Peak Hour.

---

## Problem Analysis, Modeling, Scenarios, Use Cases 🧠

### Functional Requirements:
- Allow user input for the number of clients, number of queues, and simulation parameters (service and arrival times).
- Validate the input data and display appropriate messages.
- Allow the user to start the simulation and display real-time progress.
- Calculate and display results such as average waiting time, service time, and peak hour.

### Use Cases:
1. **Input Data Validation:**
   - **Actor:** User
   - **Flow:** The user inputs simulation parameters, and the system validates the data.

2. **Start Simulation:**
   - **Actor:** User
   - **Flow:** The user starts the simulation, and the system updates the progress in real-time.

3. **Display Results:**
   - **Actor:** User
   - **Flow:** The system calculates and displays the simulation results.

---

## Design 🖥️

### Object-Oriented Design

The system is designed using object-oriented principles, where key components include:

- **Client:** Represents a client with attributes like arrival and service times.
- **Queue:** Manages a queue of clients and implements the `Runnable` interface for multithreading.
- **Simulation:** Handles the core logic of the simulation, including state progression and logging.
- **Controller:** Manages communication between the application and the GUI.

### Key Classes:
1. **Controller:** Coordinates between the GUI and the core logic.
2. **Simulation:** Manages the queue simulation and logs events.
3. **Client:** Represents a client, including random arrival and service times.
4. **Queue:** Represents a queue and processes clients based on the selected strategy.
5. **Strategy Enum:** Defines two client allocation strategies: Shortest Time and Shortest Queue.
6. **Scheduler:** Allocates clients to queues based on the selected strategy.

---

## Implementation 🛠️

- **Controller Class:** Coordinates the flow between the user interface and the simulation logic. Handles button clicks and text area updates.
  
- **Simulation Class:** Contains the logic for simulating the queue system. It manages the state of the simulation, progression, and logging into a text file.
  
- **Client Class:** Represents a client with an ID, arrival time, and service time. It also has functionality for generating random arrival and service times.

- **Queue Class:** Implements `Runnable` to manage client queues in a multithreaded environment. Each queue processes clients concurrently.

- **Strategy Class:** Provides different strategies for client allocation—either based on the shortest waiting time or the shortest queue.

- **Scheduler Class:** Allocates clients to queues based on the selected strategy, optimizing the system's overall performance.

- **GUI:** A JavaFX-based interface allows users to input data, select strategies, and view real-time progress and results.

---

## Results 📈

The application has been tested for three scenarios, and the results have been logged in text files (`test1.txt`, `test2.txt`, `test3.txt`). These files contain the simulation events and the calculated statistics like average waiting time, average service time, and peak hour.

---

## Conclusions 💡

### Learning:
- Implemented multithreading and managed synchronization.
- Designed a GUI with JavaFX.
- Applied object-oriented principles to solve a real-world problem.

### Future Developments:
- Enhance the GUI with more detailed visualizations of the simulation.
- Implement more advanced queue allocation strategies.
- Optimize performance for larger simulations.

---

## Bibliography 📚

- [Java Concurrency Tutorial](http://docs.oracle.com/javase/tutorial/essential/concurrency/index.html)
- [Java Timer and Scheduling](http://www.tutorialspoint.com/java/util/timer_schedule_period.htm)
- [Java Thread Pool Example](http://www.javacodegeeks.com/2013/01/java-thread-pool-example-using-executorsandthreadpoolexecutor.html)

---
