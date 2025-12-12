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

    public Barrier(int totalPlayers, Runnable barrierAction) {
        this.totalPlayers = totalPlayers;
        this.count = 0;
        this.barrierAction = barrierAction;
    }

    public int getTotalPlayers() {
        return totalPlayers;
    }
    public int getCount() {
        return count;
    }

    public void await(long timeout) throws InterruptedException {
        lock.lock();
        try {
            count++;
            if(count >= totalPlayers) {
                triggerBarrier();
            } else{
                try{
                    boolean arrivedOnTime = condition.await(timeout, TimeUnit.MILLISECONDS);
                    if(!arrivedOnTime){
                        if(count < totalPlayers)
                            triggerBarrier();
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
