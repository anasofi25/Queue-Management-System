package com.example.demo4;

public class Client {
    private int clientId;
    private int arrivalTime;
    private int serviceTime;
    private int initialServiceTime;

    public Client(int clientId, int arrivalTime, int serviceTime) {
        this.clientId = clientId;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.initialServiceTime=0;
    }

    public int getClientId() {
        return clientId;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public int getInitialServiceTime() {
        return initialServiceTime;
    }



    public void decreaseServiceTime() {
        if (serviceTime > 0) {
            serviceTime--;
        }
    }

    @Override
    public String toString() {
        return "Client{id=" + clientId + ", arrivalTime=" + arrivalTime + ", serviceTime=" + serviceTime + '}';
    }
}
