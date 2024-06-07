package com.example.demo4;

import java.util.List;

public class ConcreteStrategyQueue implements Strategy{
    @Override
    public void allocateClient(List<Queue> queues, Client client) {
        Queue shortestQueue = null;
        int minSize = Integer.MAX_VALUE;

        for (Queue queue : queues) {
            int size = queue.getClients().size();
            if (size < minSize) {
                minSize = size;
                shortestQueue = queue;
            }
        }

        if (shortestQueue != null) {
            shortestQueue.addClient(client);
        }
    }
}
