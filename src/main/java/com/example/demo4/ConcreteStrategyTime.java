package com.example.demo4;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcreteStrategyTime implements Strategy{

    @Override
    public void allocateClient(List<Queue> queues, Client client) {

        Queue shortestQueue = null;
        int minTime = Integer.MAX_VALUE;

        for (Queue queue : queues) {

            int totalTime = queue.getSumOfWaitingTimes();
            if (totalTime < minTime) {
                minTime = totalTime;
                shortestQueue = queue;
            }
        }

        if (shortestQueue != null) {
            shortestQueue.addClient(client);
        }
    }

}

