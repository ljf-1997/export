package com.exportexcel.export.server;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

class myData {
    //没有加volatile之前，mian线程没有被通知到
    int number = 0;

    public void addNumber() {
        this.number = 60;
    }

    public void numberaddadd() {
        number++;
    }
}

/**
 * @author hjh
 * @Description: 验证volatile特性
 * @date 2021/2/6 下午 12:30
 */
public class VolatileDemo {

    public static void main(String[] args) {

        int i = timerTest();
        System.out.println(i);
    }

    private static int timerTest() {
        AtomicInteger i = new AtomicInteger();

        //线程1 查询数据，休眠x秒，修改i值为99
        Thread t1 = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " 线程启动");
                Thread.sleep(15 * 1000);
                System.out.println(Thread.currentThread().getName() + " 线程休眠5s");
                if (Thread.activeCount()>3) {
                    i.set(99);
                    System.out.println(Thread.currentThread().getName() + " 线程赋值i==" + i);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t1");
        t1.start();

        //定时器超过x秒，停止线程1，修改i值为88
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " 定时器启动");
                //当前线程数>3，代表t1正在运行
                if (Thread.activeCount()>3) {
                    //结束t1
                    t1.interrupt();
                    System.out.println(Thread.currentThread().getName() + " 关闭t1");
                    i.set(88);
                    System.out.println(Thread.currentThread().getName() + "赋值i==" + i);
                }
                System.out.println(Thread.currentThread().getName() +  "关闭");
                this.cancel();
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 5 * 1000);

        //当线程数>2时，主线程等待
        while (Thread.activeCount() > 3) {
            Thread.yield();
        }
        System.out.println(Thread.currentThread().getName() + " 最终结果为：" + i);
        return i.get();
    }


    private static void testAc() {
        //验证volatile的原子性
        myData myData = new myData();
        for (int i = 1; i <= 20; i++) {
            new Thread(() -> {
                for (int j = 1; j <= 1000; j++) {
                    myData.numberaddadd();
                }
            }, String.valueOf(i)).start();
        }
        //当线程数>2时，主线程等待（主线程+JVM守护线程）
        while (Thread.activeCount() > 2) {
            Thread.yield();
        }
        //main线程获取最新值
        System.out.println(Thread.currentThread().getName() + "线程 获取最新值：" + myData.number);
    }

    /**
     * @Description: 验证volatile的可见性
     * @author hjh
     * @date 2021/2/6 下午 12:58
     */
    private static void testVolatileSeeAble() {
        myData myData = new myData();

        //线程AAA修改变量
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " 线程启动");
            try {
                Thread.sleep(3 * 1000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            System.out.println(Thread.currentThread().getName() + " 线程休眠3s");
            myData.addNumber();
            System.out.println(Thread.currentThread().getName() + "修改结果:" + myData.number);
        }, "AAA").start();

/*
        //验证主线程是否被通知
        while(myData.number == 0){

        }
        System.out.println(Thread.currentThread().getName()+"线程获取值："+myData.number);*/

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "线程启动");
            while (myData.number == 0) {

            }
            System.out.println(Thread.currentThread().getName() + "线程收到了通知:" + myData.number);
        }, "BBB").start();
    }


}
