package main.com.kv.mt.concurrent.blockingqueue;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ArrayBlockingQueue Implementation
 */
public class ArrayBlockingQueueImpl {
    public static void main(String args[]){
        final BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<Integer>(5);
        final Random random = new Random();
        final AtomicInteger aint = new AtomicInteger(0);

        //producer thread
        Runnable producer = () -> {
            while(aint.get() != 5) {
                try {
                    System.out.println(Thread.currentThread().getName() + " adding to queue");
                    blockingQueue.put(random.nextInt());
                    System.out.println(Thread.currentThread().getName() + " finished adding to queue");
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + " interrupted");
                }
            }
        };

        //consumer thread
        Runnable consumer = () -> {
            while(aint.get() != 10) {
                try {
                    System.out.println(Thread.currentThread().getName() + " retrieving from queue");
                    System.out.println(Thread.currentThread().getName() + " retrieved " + blockingQueue.take() + " from queue");
                } catch (InterruptedException e) {
                    System.out.println(Thread.currentThread().getName() + " interrupted");
                }
            }
        };

        Thread producerThread = new Thread(producer);
        producerThread.setName("MyProducer");

        Thread consumerThread = new Thread(consumer);
        consumerThread.setName("MyConsumer");

        producerThread.start();
         while(blockingQueue.remainingCapacity()!=0){
             try{
                Thread.sleep(5000);
             }catch (InterruptedException ie){
                 System.out.println(" interrupted main thread");
             }
         }

        System.out.println("Queue is full and the MyProducer thread state : "+producerThread.getState());
        assert (Thread.State.WAITING==producerThread.getState());
        assert(producerThread.isAlive());
         // now start the consumer threads
        aint.compareAndSet(0,5);
        consumerThread.start();

        while(blockingQueue.remainingCapacity()!=5){
            try{
                Thread.sleep(5000);
            }catch (InterruptedException ie){
                System.out.println(" interrupted main thread");
            }
        }

        System.out.println("Queue is empty and the MyConsumer thread state : "+consumerThread.getState());
        assert(Thread.State.WAITING==consumerThread.getState());
        assert(consumerThread.isAlive());
        aint.compareAndSet(5,10);
        consumerThread.interrupt();
    }
}


