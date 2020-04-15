package com.me.test;

public class Test {

    public static void main(String[] args) {

        //一个进程可以包含一个或多个线程，但至少会有一个线程。
        //现代os都将线程作为最小调度单位,进程作为资源分配的最小单位
        //实现多任务的方法，有以下几种：
        //    多进程模式（每个进程只有一个线程）
        //    多线程模式（一个进程有多个线程）
        //    多进程＋多线程模式（复杂度最高）

        //多进程稳定性比多线程高，因为在多进程的情况下，一个进程崩溃不会影响其他进程，而在多线程的情况下，任何一个线程崩溃会直接导致整个进程崩溃。
        //多进程的缺点在于：
        //创建进程比创建线程开销大，尤其是在Windows系统上；
        //进程间通信比线程间通信要慢，因为线程间通信就是读写同一个变量，速度很快。

        //Java语言内置了多线程支持：一个Java程序实际上是一个JVM进程，JVM进程用一个主线程来执行main()方法，
        // 在main()方法内部，我们又可以启动多个线程。此外，JVM还有负责垃圾回收的其他工作线程等。
        //对于大多数Java程序来说，我们说多任务，实际上是说如何使用多线程实现多任务。
        //多线程编程的特点在于：多线程经常需要读写共享数据，并且需要同步

        //Java多线程编程的特点又在于：
        //多线程模型是Java程序最基本的并发模型；
        //后续读写网络、数据库、Web开发等都依赖Java多线程模型。

        //要创建一个新线程非常容易，我们需要实例化一个Thread实例，然后调用它的start()方法：
        Thread t=new Thread();
        t.start();

        //我们希望新线程能执行指定的代码，有以下几种方法：
        //方法一：从Thread派生一个自定义类，然后覆写run()方法：
        //注意到start()方法会在内部自动调用实例的run()方法。
        //直接调用Thread实例的run()方法是无效的
        //直接调用run()方法，相当于调用了一个普通的Java方法，当前线程并没有任何改变，也不会启动新线程
        //必须调用Thread实例的start()方法才能启动新线程
        MyThread mt=new MyThread();
        mt.start();

        //方法二：创建Thread实例时，传入一个Runnable实例：
        Thread t1=new Thread(new MyRunnable());
        t1.start();
        //lambda表达式（JDK8+）
        Thread t2=new Thread(()->{
            System.out.println("start new thread3!");
        });
        //优先级高的线程被操作系统调度的优先级较高，操作系统对高优先级线程可能调度更频繁，
        // 但我们决不能通过设置优先级来确保高优先级的线程一定会先执行。
        //可以对线程设定优先级，设定优先级的方法是
        t2.setPriority(10); // 1~10, 1的优先级最低，10的优先级最高，默认值是5
        t2.start();


        System.out.println("main start...");
        Thread tt = new Thread(() -> {
            System.out.println("thread run...");
            try {
                //Thread.sleep(10);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("thread end.");
        });
        tt.start();
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {}
        System.out.println("main end...");





    }

}

//继承类
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("start new thread1!");
    }
}

//实现接口
class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("start new thread2!");
    }
}