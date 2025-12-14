package concurrency;

public class CountDownLatch {

    private int count;
    private int bonusCounter;

    public CountDownLatch(int count) {
        this.count = count;
        this.bonusCounter = 2;
    }

    public synchronized void await(long timeout) throws InterruptedException {
        long endTime = System.currentTimeMillis() + timeout;
        long remainingTime = timeout;
        while (count > 0 && remainingTime > 0) {
            wait(remainingTime);
            remainingTime = endTime - System.currentTimeMillis();
        }
    }

    public synchronized void await() throws InterruptedException {
        await(30000); // 30 seconds timeout
    }

    public synchronized int countDown() {
        int bonusMultiplier = 1;
        if(count > 0) {
            count--;
            if(bonusCounter > 0) {
                bonusMultiplier = 2;
                bonusCounter--;
            }
            if(count == 0)
                notifyAll();
        }
        return bonusMultiplier;
    }
}
