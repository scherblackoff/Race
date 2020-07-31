package java3;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;

public class Car implements Runnable {
    private static int carsCount;
    private static CountDownLatch cdl;
    private static CountDownLatch cdlForStart;
    private static boolean winnerFound;
    private CyclicBarrier cb;
    private Lock lock;

    static {
        carsCount = 0;
    }

    private Race race;
    private int speed;
    private String name;

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed, CyclicBarrier cb, CountDownLatch cdl, CountDownLatch cdlForStart) {
        this.cdlForStart = cdlForStart;
        this.cb = cb;
        this.cdl = cdl;
        this.race = race;
        this.speed = speed;
        carsCount++;
        this.name = "Участник #" + carsCount;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            System.out.println(this.name + " готов");
            cb.await();
            cdlForStart.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
        cdl.countDown();
        checkWinner(this);
    }

    private static synchronized void checkWinner(Car c) {
        if (!winnerFound) {
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>>" + c.name + " - WIN");
            winnerFound = true;
        }
    }
}


