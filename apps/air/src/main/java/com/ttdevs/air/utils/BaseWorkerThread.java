package com.ttdevs.air.utils;

/**
 * Created by ttdevs
 * 2017-01-22 (android)
 * https://github.com/ttdevs
 */
public abstract class BaseWorkerThread extends Thread {

    private boolean isRunning = true;

    @Override
    public void run() {
        super.run();
        isRunning = workerBefore();

        while (isRunning) {
            workerCycle();
        }

        workerAfter();
    }

    /**
     * 提前执行 true: 继续 false: 结束
     *
     * @return
     */
    public boolean workerBefore() {
        return true;
    }

    /**
     * 工作方法，被循环调用
     *
     * @return true: 继续 false: 结束
     */
    public abstract void workerCycle();


    /**
     * 结束执行
     */
    public void workerAfter() {

    }

    /**
     * 开始线程
     */
    public void startThread() {
        isRunning = true;
        try {
            start();
        } catch (Exception e) {
            e.printStackTrace();
            isRunning = false;
        }
    }

    /**
     * 结束线程
     */
    public void stopThread() {
        isRunning = false;
    }
}
