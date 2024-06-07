/*
package com.example.demo4;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QueueController {

    @FXML
    private TextField clientNr;

    @FXML
    private TextField queueNr;

    @FXML
    private TextField simulationTime;

    @FXML
    private TextField minArrivalTime;

    @FXML
    private TextField maxArrivalTime;

    @FXML
    private TextField minServiceTime;

    @FXML
    private TextField maxServiceTime;

    @FXML
    private TextArea textArea;

    @FXML
    private Button startSimulation;

    @FXML
    private Button validate;

    private List<Client> clients;
    private List<Client> originalClients;
    private SchedulerTime scheduler;
    private int currentTime;
    private int simulationDuration;
    private Logger logger = new Logger();
    private double totalServiceTime = 0.0;
    private double totalWaitingTime = 0.0;
    private Map<Integer, List<Integer>> clientsInQueues = new HashMap<>();

    private boolean areAllIntegers() {
        try {
            Integer.parseInt(clientNr.getText());
            Integer.parseInt(queueNr.getText());
            Integer.parseInt(simulationTime.getText());
            Integer.parseInt(minArrivalTime.getText());
            Integer.parseInt(maxArrivalTime.getText());
            Integer.parseInt(minServiceTime.getText());
            Integer.parseInt(maxServiceTime.getText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private List<Client> generateRandomClients(int nrClients, int minArrival, int maxArrival, int minService, int maxService) {
        List<Client> clientList = new ArrayList<>();
        Random random = new Random();
        for (int i = 1; i <= nrClients; i++) {
            int arrivalTime = random.nextInt(maxArrival - minArrival + 1) + minArrival;
            int serviceTime = random.nextInt(maxService - minService + 1) + minService;
            clientList.add(new Client(i, arrivalTime, serviceTime));
        }
        return clientList;
    }

    @FXML
    private void onEvaluateClicked() {
        if (areAllIntegers()) {
            int numberOfClients = Integer.parseInt(clientNr.getText());
            int numberOfQueues = Integer.parseInt(queueNr.getText());
            int simTime = Integer.parseInt(simulationTime.getText());
            int minArrival = Integer.parseInt(minArrivalTime.getText());
            int maxArrival = Integer.parseInt(maxArrivalTime.getText());
            int minService = Integer.parseInt(minServiceTime.getText());
            int maxService = Integer.parseInt(maxServiceTime.getText());

            clients = generateRandomClients(numberOfClients, minArrival, maxArrival, minService, maxService);
            originalClients = new ArrayList<>(clients);
            scheduler = new SchedulerTime(numberOfQueues);

            simulationDuration = simTime;

            textArea.setText("The values are valid. Continue with the simulation.");
        } else {
            textArea.setText("Error. Cannot start simulation. Make sure all input values are integers.");
        }
    }
    private ExecutorService executorService;
    @FXML
    private void startSimulation() {
        if (clients == null || scheduler == null) {
            textArea.setText("Error. Please validate the inputs before starting the simulation.");
            return;
        }

        currentTime = 0;
        List<Queue> queues = scheduler.getQueues();
        executorService = Executors.newFixedThreadPool(queues.size());
        for (Queue queue : queues) {
           executorService.submit(queue);
        }
        new Thread(() -> {
            try (BufferedWriter logWriter = new BufferedWriter(new FileWriter("simulation_log.txt"))) {
                logInitialSimulationState();
                while (currentTime <= simulationDuration) {
                    simulateOneTimeUnit();
                   logSimulation(logWriter);
                    currentTime++;
                    Thread.sleep(1000);
                }
                logAverages();
                stopQueues();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void logAverages(){
        double averageServiceTime = calculateAverageServiceTime();
        int peakHour = findPeakHour();
        double averageWaitingTime = calculateAverageWaitingTime();

        logEvent("Average Waiting Time: " + averageWaitingTime);
        logEvent("Average Service Time: " + averageServiceTime);
        logEvent("Peak Hour: " + peakHour);

        logger.writeLogToFile("simulation.txt");
        logger.logEvent("Average Waiting Time: " + averageWaitingTime);
        logger.logEvent("Average Service Time: " + averageServiceTime);
        logger.logEvent("Peak Hour: " + peakHour);
    }
    private void logInitialSimulationState() {
        logEvent("Simulation started.");
    }

    private void simulateOneTimeUnit() {
        List<Client> clientsToRemove = new ArrayList<>();
        for (Client client : clients) {
            if (client.getArrivalTime() == currentTime) {
                scheduler.allocateClientToQueue(client);
                totalServiceTime+=client.getServiceTime();
                clientsToRemove.add(client);
                clientsToRemove.remove(client);
            }
        }
    }

    private void logEvent(String event) {
        Platform.runLater(() -> {
            if (event.matches(".*Time \\d+.*")) {
                textArea.clear();
            }
            textArea.appendText(event + "\n");
        });
        logger.logEvent(event);
    }

    private void logSimulation(BufferedWriter logWriter) throws IOException {
        logEvent("Time " + currentTime);
        logWaitingClients();
        logQueueStatus();

        logWriter.write("Time " + currentTime + "\n");
        logWriter.write("Waiting clients: ");
        for (Client client : clients) {
            logWriter.write(client.toString() + "; ");
        }
        logWriter.write("\n");

        for (Queue queue : scheduler.getQueues()) {
            logWriter.write(queue.toString() + "\n");
        }
        logWriter.write("\n");
    }

    private void logWaitingClients() {
        StringBuilder waitingClients = new StringBuilder("Waiting clients: ");
        for (Client client : clients) {
            waitingClients.append(client.toString()).append("; ");
        }
        logEvent(waitingClients.toString());
    }

    private void logQueueStatus() {
        for (Queue queue : scheduler.getQueues()) {
            int queueId = queue.getQueueID();
            List<Client> clientsInQueue = queue.getClients();
            int currentCount = clientsInQueue.size();
            List<Integer> clientsCountList = clientsInQueues.getOrDefault(queueId, new ArrayList<>());
            clientsCountList.add(currentTime, currentCount);
            clientsInQueues.put(queueId, clientsCountList);

            StringBuilder queueStatus = new StringBuilder("Queue " + queueId + ": ");
            for (Client client : clientsInQueue) {

                queueStatus.append(client.toString()).append("; ");
            }
            logEvent(queueStatus.toString());
        }
    }

    private double calculateAverageServiceTime() {
        return totalServiceTime / originalClients.size();
    }

    private double calculateAverageWaitingTime() {
        return totalWaitingTime / originalClients.size();
    }

    private int findPeakHour() {
        int peakHour = 0;
        int maxClientsInQueues = 0;

        for (Map.Entry<Integer, List<Integer>> entry : clientsInQueues.entrySet()) {
            for (int time = 0; time < entry.getValue().size(); time++) {
                int currentClients = entry.getValue().get(time);
                if (currentClients > maxClientsInQueues) {
                    maxClientsInQueues = currentClients;
                    peakHour = time;
                }
            }
        }

        return peakHour;
    }

    private void stopQueues() {
        for (Queue queue : scheduler.getQueues()) {
            queue.stop();
        }
    }
}

 */
package com.example.demo4;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Simulation {

    private List<Client> clients;
    private List<Client> originalClients;
    private Scheduler scheduler;
    private int currentTime;
    private int simulationDuration;
    private double totalServiceTime = 0.0;
    private double totalWaitingTime = 0.0;
    private final Map<Integer, List<Integer>> clientsInQueues = new HashMap<>();
    private ExecutorService executorService;
    private final TextArea textArea;
    private final List<String> logEvents = new ArrayList<>();
    public Simulation(TextArea textArea) {
        this.textArea = textArea;
    }

    public void setup(int numberOfClients, int numberOfQueues, int simTime, int minArrival, int maxArrival, int minService, int maxService, StrategySelection strategy) {
        this.simulationDuration = simTime;
        this.clients = generateRandomClients(numberOfClients, minArrival, maxArrival, minService, maxService);
        this.originalClients = new ArrayList<>(clients);
        this.scheduler = new Scheduler(numberOfQueues, strategy);
    }

    private List<Client> generateRandomClients(int nrClients, int minArrival, int maxArrival, int minService, int maxService) {
        List<Client> clientList = new ArrayList<>();
        Random random = new Random();
        for (int i = 1; i <= nrClients; i++) {
            int arrivalTime = random.nextInt(maxArrival - minArrival + 1) + minArrival;
            int serviceTime = random.nextInt(maxService - minService + 1) + minService;
            clientList.add(new Client(i, arrivalTime, serviceTime));
        }
        return clientList;
    }

    public void start() throws IOException {

        resetSimulationState();
        startQueueThreads();
        runSimulation();
    }

    private void resetSimulationState() {
        currentTime = 0;
        clientsInQueues.clear();
        totalServiceTime = 0.0;
        totalWaitingTime = 0.0;
    }

    private void startQueueThreads() {
        executorService = Executors.newFixedThreadPool(scheduler.getQueues().size());
        for (Queue queue : scheduler.getQueues()) {
            executorService.submit(queue);
        }
    }

    private void runSimulation() {
        new Thread(() -> {
            try (BufferedWriter logWriter = new BufferedWriter(new FileWriter("simulation_log.txt"))) {
                logEvent("Simulation started.");
                while (currentTime <= simulationDuration) {
                    simulateOneTimeUnit();
                    logSimulationState(logWriter);
                    currentTime++;
                    Thread.sleep(1000);
                }
                logFinalStatistics();
                writeLogToFile("simulation_log.txt");
                stopQueueThreads();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    private void simulateOneTimeUnit() {
        List<Client> clientsToRemove = new ArrayList<>();
        int waitingTime=0;
        for (Client client : clients) {
            if (client.getArrivalTime() == currentTime) {
                scheduler.allocateClientToQueue(client);
                totalServiceTime += client.getServiceTime();

                int queuePosition=clients.indexOf(client);
                if(queuePosition!=0){
                    waitingTime++;
                }
                totalWaitingTime+=waitingTime;
                clientsToRemove.add(client);
            }
        }
        clients.removeAll(clientsToRemove);
    }
    
    private void logEvent(String event) {
        Platform.runLater(() -> {
            if (event.matches(".*Time \\d+.*")) {
                textArea.clear();
            }
            textArea.appendText(event + "\n");
        });
        logEvents.add(event);
    }
    private void logSimulationState(BufferedWriter logWriter) throws IOException {
        logEvent("Time " + currentTime);
        logClientStatus(logWriter);
        logQueueStatus(logWriter);
    }
    private void logClientStatus(BufferedWriter logWriter) throws IOException {
        StringBuilder waitingClients = new StringBuilder("Waiting clients: ");

        for (Client client : clients) {
            waitingClients.append("(").append(client.getClientId()).append(",").append(client.getArrivalTime()).append(",").append(client.getServiceTime()).append("); ");
        }
        logEvent(waitingClients.toString());

        String waitingClientsStr = waitingClients.toString();

        logWriter.write("Time " + currentTime + "\n");
        logWriter.write(waitingClientsStr + "\n");
    }

    private void logQueueStatus(BufferedWriter logWriter) throws IOException {
        for (Queue queue : scheduler.getQueues()) {
            logQueue(queue, logWriter);
        }
        logWriter.write("\n");
    }

    private void logQueue(Queue queue, BufferedWriter logWriter) throws IOException {
        int queueId = queue.getQueueID();
        List<Client> clientsInQueue = queue.getClients();
        int currentCount = clientsInQueue.size();
        clientsInQueues.computeIfAbsent(queueId, k -> new ArrayList<>()).add(currentTime, currentCount);

        StringBuilder queueStatus = new StringBuilder("Queue " + queueId + ": ");
        for (Client client : clientsInQueue) {
            queueStatus.append("(").append(client.getClientId()).append(",").append(client.getArrivalTime()).append(",").append(client.getServiceTime()).append(") ");
        }
        logEvent(queueStatus.toString());
        logWriter.write(queueStatus.toString() + "\n");
    }

    private void logFinalStatistics() {
        double averageServiceTime = calculateAverageServiceTime();
        double averageWaitingTime = calculateAverageWaitingTime();
        int peakHour = findPeakHour();

        logEvent("Average Waiting Time: " + averageWaitingTime);
        logEvent("Average Service Time: " + averageServiceTime);
        logEvent("Peak Hour: " + peakHour);
    }
    private int getNrClients(){
        return originalClients.size();
    }
    private double calculateAverageServiceTime() {

        return totalServiceTime / getNrClients();
    }

    private double calculateAverageWaitingTime() {
        return totalWaitingTime / getNrClients();
    }

    private int findPeakHour() {
        int peakHour = 0;
        int maxClientsInQueues = 0;

        for (List<Integer> clientsCountList : clientsInQueues.values()) {
            for (int time = 0; time < clientsCountList.size(); time++) {
                int currentClients = clientsCountList.get(time);
                if (currentClients > maxClientsInQueues) {
                    maxClientsInQueues = currentClients;
                    peakHour = time;
                }
            }
        }

        return peakHour;
    }

    private void stopQueueThreads() {
        for (Queue queue : scheduler.getQueues()) {
            queue.stop();
        }
        executorService.shutdown();
    }

    private void writeLogToFile(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String event : logEvents) {
                writer.write(event);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
