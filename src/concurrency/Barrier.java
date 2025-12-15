package concurrency;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Barrier {

    private final int totalPlayers;
    private int count;
    private final Runnable barrierAction;

    private final Lock lock =  new ReentrantLock();
    private final Condition condition = lock.newCondition();

    private final long startTime;
    private final int waitPeriod;

    public Barrier(int totalPlayers, int waitPeriod, Runnable barrierAction) {
        this.totalPlayers = totalPlayers;
        this.waitPeriod = waitPeriod;
        this.count = 0;
        this.barrierAction = barrierAction;
        this.startTime = System.currentTimeMillis();
    }

    public void await() throws InterruptedException {
        lock.lock();
        try {
            count++;
            if(count >= totalPlayers) {
                triggerBarrier();
            } else{
                try{
                    long endTime = startTime + waitPeriod;
                    long remainingTime = endTime - System.currentTimeMillis();
                    if(remainingTime <= 0){
                        if(count < totalPlayers)
                            triggerBarrier();
                    } else {
                        boolean arrivedOnTime = condition.await(remainingTime, TimeUnit.MILLISECONDS);
                        if(!arrivedOnTime){
                            if(count < totalPlayers)
                                triggerBarrier();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void reset(){
        lock.lock();
        try{
            count = 0;
            condition.signalAll();
        } finally{
            lock.unlock();
        }
    }

    public void triggerBarrier(){
        if(count == 0)
            return;
        try{
            barrierAction.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
        condition.signalAll();
        count = 0;
    }
}
