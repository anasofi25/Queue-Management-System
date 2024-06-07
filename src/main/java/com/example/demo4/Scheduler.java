package com.example.demo4;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Queue> queues;
    private ConcreteStrategyTime strategy;

    public Scheduler(int numberOfQueues, StrategySelection strategy) {
        queues = new ArrayList<>();
        for (int i = 0; i < numberOfQueues; i++) {
            queues.add(new Queue(i));
        }
        selectStrategy(strategy);
    }

    public List<Queue> getQueues() {
        return queues;
    }
    public void selectStrategy(StrategySelection policy) {
        //select desired strategy
        if (policy == StrategySelection.SHORTEST_TIME_STRATEGY) {
            strategy = new ConcreteStrategyTime();
        } else if (policy == StrategySelection.SHORTEST_QUEUE_STRATEGY) {
            strategy = new ConcreteStrategyTime();
        }
    }
    public void allocateClientToQueue(Client client) {
        strategy.allocateClient(queues, client);
    }
}



