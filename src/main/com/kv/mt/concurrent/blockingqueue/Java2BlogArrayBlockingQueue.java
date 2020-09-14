package main.com.kv.mt.concurrent.blockingqueue;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * The below code tests where the ArrayBlockingQueue makes the producer threads wait when the queue is full
 * and similarly it makes the consumer threads full if the queue is empty
 */
public class Java2BlogArrayBlockingQueue {
    public static void main(String args[]){
        final BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<Integer>(5);
        final Random random = new Random();

        //producer thread keeps running until its interrupted
        Runnable producer = () -> {
            boolean isInterrupted = false;
            while(!isInterrupted) {
                try {
                    System.out.println(Thread.currentThread().getName() + " adding to queue");
                    blockingQueue.put(random.nextInt());
                    System.out.println(Thread.currentThread().getName() + " finished adding to queue");
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + " interrupted");
                    isInterrupted = true;
                }
            };
        };

        //consumer thread keeps running until its interrupted
        Runnable consumer = () -> {
            boolean isInterrupted = false;
            while(!isInterrupted) {
                try {
                    System.out.println(Thread.currentThread().getName() + " retrieving from queue");
                    System.out.println(Thread.currentThread().getName() + " retrieved " + blockingQueue.take() + " from queue");
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + " interrupted");
                    isInterrupted = true;
                }
            }
        };

        Thread producerThread = new Thread(producer);
        producerThread.setName("MyProducer");

        Thread consumerThread = new Thread(consumer);
        consumerThread.setName("MyConsumer");

        producerThread.start();

        //this code is to wait for the main thread to wait till the producer completely fills the blocking queue
        while(blockingQueue.remainingCapacity()!=0){
            try{
                Thread.sleep(5000);
            }catch (InterruptedException ie){
                System.out.println(" interrupted main thread");
            }
        }
        //This log checks the MyProducer thread state as it should be now in waiting state as the  blocking queue is full
        System.out.println("Queue is full and the MyProducer thread state : "+producerThread.getState());
        assert (Thread.State.WAITING==producerThread.getState());
        assert(producerThread.isAlive());
        // The producer thread is stopped to ensure the blocking queue becomes empty once all integers are consumed
        producerThread.interrupt();

        // now start the consumer threads
        consumerThread.start();

        //wait for the consumer to drain the blocking queue
        while(((ArrayBlockingQueue) blockingQueue).remainingCapacity()!=5){
            try{
                Thread.sleep(5000);
            }catch (InterruptedException ie){
                System.out.println(" interrupted main thread");
            }
        }

        //check the status of the consumer thread once the blocking queue is empty. it should we in waiting state
        System.out.println("Queue is empty and the MyConsumer thread state : "+consumerThread.getState());
        assert(Thread.State.WAITING==consumerThread.getState());
        assert(consumerThread.isAlive());

        //stop the consumer
        consumerThread.interrupt();
    }
}

