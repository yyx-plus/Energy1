package com.thread;

/**
 * 线程执行方法（做一些项目启动后 一直要执行的操作，比如根据时间自动更改订单状态，比如订单签收30天自动收货功能，比如根据时间来更改状态）
 */
public class MyThreadMethod extends Thread  {
    public void run() {
        while (!this.isInterrupted()) {// 线程未中断执行循环
            try {
                Thread.sleep(5000); //每隔5000ms执行一次
            } catch (InterruptedException e) {
                // 重新设置中断状态，因为InterruptedException会清除中断状态
                Thread.currentThread().interrupt();
                break; // 收到中断信号时退出循环
            }

//			 ------------------ 开始执行 ---------------------------
//            System.out.println("线程执行中:" + System.currentTimeMillis());
        }
    }
}
