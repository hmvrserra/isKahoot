package concurrency;

public class CountDownLatch {

    private final int bonusFactor;
    private int bonusCount;
    private final long waitPeriod;
    private int count;
    private final long startTime;

    public CountDownLatch(int bonusFactor, int bonusCount, int waitPeriod, int count) {
        this.bonusFactor = bonusFactor;
        this.bonusCount = bonusCount;
        this.waitPeriod = waitPeriod;
        this.count = count;
        this.startTime = System.currentTimeMillis();
    }

    public synchronized void await() throws InterruptedException {
        long endTime = startTime + waitPeriod;
        long remainingTime = endTime - System.currentTimeMillis();
        while (count > 0 && remainingTime > 0) {
            wait(remainingTime);
            remainingTime = endTime - System.currentTimeMillis();
        }
    }

    public synchronized int countDown() {
        int bonusMultiplier = 1;
        if(count > 0) {
            count--;
            if(bonusCount > 0) {
                bonusMultiplier = bonusFactor;
                bonusCount--;
            }
            if(count == 0)
                notifyAll();
        }
        return bonusMultiplier;
    }
}
