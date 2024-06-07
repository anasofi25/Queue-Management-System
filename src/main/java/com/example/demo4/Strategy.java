package com.example.demo4;

import java.util.List;

public interface Strategy {
    public void allocateClient(List<Queue> queues, Client client);
}
