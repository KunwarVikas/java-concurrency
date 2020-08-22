package main.com.kv.mt;

public class WaitNotifyExample {
    /**
     *
     */
    int amount=10000;

    /**
     *
     * @param amount
     */
    synchronized void withdraw(int amount){
            System.out.println("going to withdraw...");
            if(this.amount<amount){
                System.out.println("Less balance; waiting for deposit...");
                try{
                    wait();
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
            this.amount-=amount;
            System.out.println("withdraw completed...");
        }

    /**
     *
     * @param amount
     */
    synchronized void deposit(int amount){
            System.out.println("going to deposit...");
            this.amount+=amount;
            System.out.println("deposit completed... ");
            notify();
        }
    }

/**
 *
 */
class Main{
        public static void main(String args[]){
            final WaitNotifyExample c = new WaitNotifyExample();
            new Thread(){
                public void run(){c.withdraw(15000);}
            }.start();
            new Thread(){
                public void run(){c.deposit(10000);}
            }.start();
        }}
