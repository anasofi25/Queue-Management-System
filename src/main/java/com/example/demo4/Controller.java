package com.example.demo4;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;

import java.io.IOException;
import java.util.Arrays;

public class Controller {

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
    private ChoiceBox<String> strategyChoiceBox;
    @FXML
    private Button startSimulation;
    @FXML
    private Button validate;

    private Simulation simulation;

    @FXML
    private void initialize() {
        simulation = new Simulation(textArea);

        if (strategyChoiceBox != null) {

            strategyChoiceBox.getItems().addAll("SHORTEST_TIME_STRATEGY", "SHORTEST_QUEUE_STRATEGY");
            strategyChoiceBox.setValue(String.valueOf(StrategySelection.SHORTEST_TIME_STRATEGY));
        } else {
            System.out.println("strategyChoiceBox is null.");
        }
    }


    @FXML
    private void onEvaluateClicked() {
        if (areAllInputsValid()) {
            setupSimulation();
            textArea.setText("The values are valid. Continue with the simulation.");
        } else {
            textArea.setText("Error. Cannot start simulation. Make sure all input values are integers.");
        }
    }

    @FXML
    private void startSimulation() throws IOException {
        simulation.start();
    }

    private boolean areAllInputsValid() {
        try {
            parseInputValues();
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void setupSimulation() {
        int numberOfClients = Integer.parseInt(clientNr.getText());
        int numberOfQueues = Integer.parseInt(queueNr.getText());
        int simTime = Integer.parseInt(simulationTime.getText());
        int minArrival = Integer.parseInt(minArrivalTime.getText());
        int maxArrival = Integer.parseInt(maxArrivalTime.getText());
        int minService = Integer.parseInt(minServiceTime.getText());
        int maxService = Integer.parseInt(maxServiceTime.getText());
        StrategySelection strategy = StrategySelection.valueOf(strategyChoiceBox.getValue());

        simulation.setup(numberOfClients, numberOfQueues, simTime, minArrival, maxArrival, minService, maxService, strategy);
    }

    private void parseInputValues() {
        Integer.parseInt(clientNr.getText());
        Integer.parseInt(queueNr.getText());
        Integer.parseInt(simulationTime.getText());
        Integer.parseInt(minArrivalTime.getText());
        Integer.parseInt(maxArrivalTime.getText());
        Integer.parseInt(minServiceTime.getText());
        Integer.parseInt(maxServiceTime.getText());
    }
}
