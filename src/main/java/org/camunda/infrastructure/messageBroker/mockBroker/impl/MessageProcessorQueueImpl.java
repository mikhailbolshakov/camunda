package org.camunda.infrastructure.messageBroker.mockBroker.impl;

import org.camunda.repository.messageBroker.Message;
import org.camunda.infrastructure.messageBroker.mockBroker.MockConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;

public class MessageProcessorQueueImpl implements Runnable, MessageProcessor {

    private static Logger logger = LoggerFactory.getLogger(MockConnection.class.getName());

    private final BlockingQueue<Message> queue = new LinkedBlockingDeque<>();
    private final CopyOnWriteArrayList<Subscriber> subscribers = new CopyOnWriteArrayList<>();

    private Thread worker;

    @Override
    public void process(Message message)  {

        try {
            queue.put(message);
        }
        catch(Exception e) {
            String errMsg = String.format("[MockMessageBroker] Process error.\n Error: %s", e.getMessage());
            logger.error(errMsg, e);
        }

    }

    @Override
    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void removeSubscribers() {
        subscribers.clear();
    }

    private void handle(Message message) {

        for(Subscriber sub: subscribers) {

            try {

                if(sub.getTopic().equals(message.getTopic())) {
                    logger.debug(String.format("Handle message.\n Subscriber: %s. Topic: %s. Message: %s", sub.getClass().getSimpleName(), message.getTopic(), message.getPayload()));
                    sub.Consume(message);
                }

            }
            catch(Exception e){
                String errMsg = String.format("[MockMessageBroker] Handle message error.\n Error: %s", e.getMessage());
                logger.error(errMsg, e);
            }

        }

    }

    @Override
    public void run() {
        try {

            while(true) {
                handle(queue.take());
            }

        }
        catch(InterruptedException intE) {
            Thread.currentThread().interrupt();
        }
    }

    public void start() {
        worker = new Thread(this);
        worker.start();
    }

    @Override
    public void stop() {
        if(worker != null) {
            worker.interrupt();
            worker = null;
        }
    }

}
