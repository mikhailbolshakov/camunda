package org.camunda.infrastructure.messageBroker.mockBroker.impl;

import org.camunda.repository.messageBroker.Message;
import org.camunda.infrastructure.messageBroker.mockBroker.MockConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class MessageProcessor implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(MockConnection.class.getName());

    private final BlockingQueue<Message> queue;
    private final CopyOnWriteArrayList<Subscriber> subscribers;
    private Thread worker;

    public MessageProcessor(BlockingQueue<Message> queue, CopyOnWriteArrayList<Subscriber> subscribers) {
        this.queue = queue;
        this.subscribers = subscribers;
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

    public Thread start() {
        worker = new Thread(this);
        worker.start();
        return worker;
    }

    public void interrupt() {

        if(worker != null) {
            worker.interrupt();
            worker = null;
        }
    }

    public boolean isQueueEmpty() {
        return queue.isEmpty();
    }

}
