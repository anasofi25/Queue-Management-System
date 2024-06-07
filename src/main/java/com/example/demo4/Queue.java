package com.example.demo4;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Queue implements Runnable{

    private LinkedBlockingQueue<Client> clients;
    private AtomicInteger waitingTime;
    private AtomicInteger sumOfTimes;
    private int queueID;

    public Queue() {
        clients = new LinkedBlockingQueue<>();
        waitingTime = new AtomicInteger(0);
        running = true;
        this.sumOfTimes= new AtomicInteger(0);
    }
    public int getQueueSize() {
        return clients.size();
    }
    public int getSumOfWaitingTimes() {
        return clients.stream().mapToInt(Client::getServiceTime).sum();
    }

    public void addWaitingTime(int time) {
        waitingTime.addAndGet(time);
    }

    public AtomicInteger getWaitingTime() {
        return waitingTime;
    }
    public int getQueueID(){
        return queueID;
    }

    public void addClient (Client newClient)
    {
        clients.add(newClient);
        waitingTime.addAndGet(newClient.getServiceTime());
    }
    public synchronized void processClients() {

        if (!clients.isEmpty()) {
            Client client = clients.peek();
            if (client != null) {
                client.decreaseServiceTime();
                if (client.getServiceTime() <= 0) {
                    clients.poll(); // remove when serviceTime==0
                    waitingTime.addAndGet(-client.getInitialServiceTime()); // adjust wait
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Queue ").append(queueID).append(": ");

        if(this.clients.isEmpty()){
            sb.append("closed\n");
        }

        for (Client client : clients) {
            sb.append("(").append(client.getClientId()).append(",").append(client.getArrivalTime()).append(",").append(client.getServiceTime()).append("); ");
        }

        return sb.toString();
    }
    @Override
    public void run() {
        while ( running) {
            processClients();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public List<Client> getClients() {
        List<Client> clientsList=new ArrayList<>();
        clients.forEach(clientsList::add);
        return clientsList;
    }
    public Queue(int id) {
        this.queueID = id;
        clients = new LinkedBlockingQueue<>();
        waitingTime=new AtomicInteger(0);
        running = true;
        this.sumOfTimes= new AtomicInteger(0);
    }
    private volatile boolean running;
    public void stop() {
        running = false;
    }


}