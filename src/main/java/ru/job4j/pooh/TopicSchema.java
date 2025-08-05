package ru.job4j.pooh;

import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class TopicSchema implements Schema {
    private final ConcurrentHashMap<String, CopyOnWriteArrayList<Receiver>> receivers = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, BlockingQueue<String>> data = new ConcurrentHashMap<>();
    private final Condition condition = new Condition();

    @Override
    public void addReceiver(Receiver receiver) {
        receivers.putIfAbsent(receiver.name(), new CopyOnWriteArrayList<>());
        receivers.get(receiver.name()).add(receiver);
        condition.on();
    }

    @Override
    public void publish(Message message) {
        data.putIfAbsent(message.name(), new LinkedBlockingQueue<>());
        data.get(message.name()).add(message.text());
        condition.on();

    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            for (var dataKey : data.keySet()) {
                BlockingQueue<String> messages = new LinkedBlockingQueue<>();
                data.get(dataKey).drainTo(messages);
                var receiverByTopic = receivers.getOrDefault(dataKey, new CopyOnWriteArrayList<>());
                messages.forEach(m -> receiverByTopic.forEach(r -> r.receive(m)));
            }
        }
    }
}
