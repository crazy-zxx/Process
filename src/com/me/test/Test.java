package com.me.test;

public class Test {

    public static void main(String[] args) throws Exception {
        System.out.println("main start...");


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

        //要创建一个新线程非常容易，我们需要实例化一个Thread实例，然后调用它的start()方法
        //创建的新线程与其父线程 并行执行
        Thread t = new Thread();
        t.start();

        /**
         * 我们希望新线程能执行指定的代码，有以下几种方法：
         * 方法一：从Thread派生一个自定义类，然后覆写run()方法
         */
        //注意到start()方法会在内部自动调用实例的run()方法。
        //直接调用Thread实例的run()方法是无效的
        //直接调用run()方法，相当于调用了一个普通的Java方法，当前线程并没有任何改变，也不会启动新线程
        //必须调用Thread实例的start()方法才能启动新线程
        MyThread mt = new MyThread();
        mt.start();

        /**
         * 方法二：创建Thread实例时，传入一个Runnable实例
         */
        Thread t1 = new Thread(new MyRunnable());
        t1.start();
        //lambda表达式（JDK8+）
        Thread t2 = new Thread(() -> {
            System.out.println("start new thread3!");
        });
        //优先级高的线程被操作系统调度的优先级较高，操作系统对高优先级线程可能调度更频繁，
        // 但我们决不能通过设置优先级来确保高优先级的线程一定会先执行。
        //可以对线程设定优先级，设定优先级的方法是
        t2.setPriority(10); // 1~10, 1的优先级最低，10的优先级最高，默认值是5
        t2.start();


        Thread tt = new Thread(() -> {
            System.out.println("thread run...");
            try {
                //Thread.sleep(10);
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("thread end.");
        });
        tt.start();
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
        }


        //Java线程的状态有以下几种：
        //New：新创建的线程，尚未执行；
        //Runnable：运行中的线程，正在执行run()方法的Java代码；
        //Blocked：运行中的线程，因为某些操作被阻塞而挂起；
        //Waiting：运行中的线程，因为某些操作在等待中；
        //Timed Waiting：运行中的线程，因为执行sleep()方法正在计时等待；
        //Terminated：线程已终止，因为run()方法执行完毕。

        //一个线程还可以 等待 另一个线程 直到其运行结束。
        //join就是指等待调用该方法的线程结束，然后才继续往下执行自身线程
        //join(long)的重载方法也可以指定一个等待时间，超过等待时间后就不再继续等待
        // 例如，main线程在启动t线程后，可以通过t.join()等待t线程结束后再继续运行
        Thread tj = new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("hello");
        });
        System.out.println("start");
        tj.start();
        tj.join();
        System.out.println("end");


        //中断线程就是其他线程给该线程发一个信号，该线程收到信号后结束执行run()方法，使得自身线程能立刻结束运行。
        /**
         * 中断线程
         * 对目标线程调用interrupt()方法可以请求中断一个线程，目标线程通过检测isInterrupted()标志获取自身是否已中断。
         * 如果目标线程处于等待状态，该线程会捕获到InterruptedException；
         * 目标线程检测到isInterrupted()为true或者捕获了InterruptedException都应该立刻结束自身线程；
         */
        Thread mti = new MyThreadInterrupt();
        mti.start();
        Thread.sleep(10);
        //interrupt()方法仅仅向t线程发出了“中断请求”，至于t线程是否能立刻响应，要看具体代码
        mti.interrupt();    //中断
        mti.join();
        System.out.println("mti end");


        Thread mtih = new MyThreadInterruptHello();
        mtih.start();
        Thread.sleep(1000);
        mtih.interrupt(); // 中断mtih线程
        mtih.join(); // 等待mtih线程结束
        System.out.println("mtih end");

        //另一个常用的中断线程的方法是设置标志位。
        // 我们通常会用一个running标志位来标识线程是否应该继续运行，
        // 在外部线程中，通过把HelloThread.running置为false，就可以让线程结束
        HelloThreadRunningFlag htrf = new HelloThreadRunningFlag();
        htrf.start();
        Thread.sleep(1);
        htrf.running = false;     //标志位置为false


        //守护线程是指为其他线程服务的线程。
        //在JVM中，所有非守护线程都执行完毕后，无论有没有守护线程，虚拟机都会自动退出。
        //守护线程不能持有任何需要关闭的资源,因为虚拟机退出时，守护线程没有任何机会来关闭文件，这会导致数据丢失。
        //创建守护线程方法和普通线程一样，只是在调用start()方法前，
        // 调用setDaemon(true)把该线程标记为守护线程：
        ThreadGuard td = new ThreadGuard();
        td.setDaemon(true); //标记为守护线程
        td.start();
        System.out.println("main: wait 3 sec...");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //线程模型下，要保证逻辑正确，对共享变量进行读写时，必须保证一组指令以原子方式执行
        //通过加锁和解锁的操作，就能保证3条指令总是在一个线程执行期间，不会有其他线程会进入此指令区间。
        // 即使在执行期线程被操作系统中断执行，其他线程也会因为无法获得锁导致无法进入此指令区间。
        // 只有执行线程将锁释放后，其他线程才有机会获得锁并执行。
        // 这种加锁和解锁之间的代码块我们称之为临界区（Critical Section），任何时候临界区最多只有一个线程能执行。

        //Java程序使用synchronized关键字对一个对象进行加锁
        //使用synchronized解决了多线程同步访问共享变量的正确性问题。
        // 但是，它的缺点是带来了性能下降。因为synchronized代码块无法并发执行。
        // 此外，加锁和解锁需要消耗一定的时间，所以，synchronized会降低程序的执行效率。
        var add = new AddThread();
        var dec = new DecThread();
        add.start();
        dec.start();
        add.join();
        dec.join();
        System.out.println(Counter.count);

        //JVM规范定义了几种原子操作：
        //基本类型（long和double除外）赋值，例如：int n = m；
        //引用类型赋值，例如：List<String> list = anotherList。
        //在x64平台的JVM是把long和double的赋值作为原子操作实现的。

        //让线程自己选择锁对象往往会使得代码逻辑混乱，也不利于封装。
        // 更好的方法是把synchronized逻辑封装起来。
        //并且，我们注意到，synchronized锁住的对象是this，即当前实例，
        // 这又使得创建多个Counter实例的时候，它们之间互不影响，可以并发执行
        //用synchronized修饰的方法就是同步方法，它表示整个方法都必须用 this实例 加锁。
        var c1 = new Counter1();
        var c2 = new Counter1();
        // 对c1进行操作的线程:
        new Thread(() -> {
            c1.add(10);
        }).start();
        new Thread(() -> {
            c1.dec(10);
        }).start();
        // 对c2进行操作的线程:
        new Thread(() -> {
            c2.add(20);
        }).start();
        new Thread(() -> {
            c2.dec(20);
        }).start();
        Thread.sleep(1000); //等待上述线程结束
        System.out.println(c1.get());
        System.out.println(c2.get());

        //Java标准库的java.lang.StringBuffer也是线程安全的。
        //还有一些不变类，例如String，Integer，LocalDate，它们的所有成员变量都是final，多线程同时访问时只能读不能写，这些不变类也是线程安全的。
        //类似Math这些只提供静态方法，没有成员变量的类，也是线程安全的。
        //没有特殊说明时，一个类默认是非线程安全的。


        //Java的线程锁是可重入的锁。
        //JVM允许同一个线程重复获取同一个锁，这种能被同一个线程反复获取的锁，就叫做可重入锁。
        //所以，获取锁的时候，不但要判断是否是第一次获取，还要记录这是第几次获取。
        // 每获取一次锁，记录+1，每退出synchronized块，记录-1，减到0的时候，才会真正释放锁
        //两个线程各自持有不同的锁，然后各自试图获取对方手里的锁，造成了双方无限等待下去，这就是死锁。
        //死锁发生后，没有任何机制能解除死锁，只能强制结束JVM进程。
        //如何避免死锁呢？答案是：线程获取锁的 顺序要一致。


        System.out.println("main end...");
    }

}

class Counter1 {
    private int count = 0;

    public void add(int n) {
        synchronized (this) {   //同步代码块，以当前对象为锁
            count += n;
        }
    }

    public synchronized void dec(int n) {   //同步方法，表示整个方法都必须用this实例加锁
        count -= n;
    }

    public int get() {
        return count;
    }

    //对static方法添加synchronized，锁住的是 该类的class实例
    public synchronized static void test(){
        System.out.println("lock is Counter1.class");
    }
}


class Counter {
    public static final Object lock = new Object(); //用Counter.lock实例作为锁,唯一
    public static int count = 0;
}

class AddThread extends Thread {
    public void run() {
        for (int i = 0; i < 10000; i++) {
            synchronized (Counter.lock) {    // 获取锁,它表示用Counter.lock实例作为锁
                Counter.count += 1;
            }   // 无论有无异常，都会在此释放锁
        }
    }
}

class DecThread extends Thread {
    public void run() {
        for (int i = 0; i < 10000; i++) {
            synchronized (Counter.lock) {
                Counter.count -= 1;
            }
        }
    }
}


class ThreadGuard extends Thread {
    @Override
    public void run() {
        int n = 0;
        while (++n > 0) {
            System.out.println(n + ":guard");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class HelloThreadRunningFlag extends Thread {
    /**
     * 线程间共享变量需要使用volatile关键字标记，确保每个线程都能读取到更新后的变量值。
     */
    //在Java虚拟机中，变量的值保存在主内存中，但是，当线程访问变量时，它会先获取一个副本，并保存在自己的工作内存中。
    // 如果线程修改了变量的值，虚拟机会在某个时刻把修改后的值回写到主内存，但是，这个时间是不确定的！
    //volatile关键字解决的是可见性问题：当一个线程修改了某个共享变量的值，其他线程能够立刻看到修改后的值。
    public volatile boolean running = true;

    public void run() {
        int n = 0;
        while (running) {
            n++;
            System.out.println(n + " hello!");
        }
        System.out.println("end!");
    }
}

class MyThreadInterruptHello extends Thread {
    public void run() {
        Thread hello = new HelloThread();
        hello.start(); // 启动hello线程
        try {
            hello.join(); // 等待hello线程结束
        } catch (InterruptedException e) {  //等待时被中断将会捕获该异常
            System.out.println("MyThreadInterruptHello interrupted!");
        }
        //需要将中断传递给子线程
        hello.interrupt();
    }
}

class HelloThread extends Thread {
    public void run() {
        int n = 0;
        while (!isInterrupted()) {
            n++;
            System.out.println(n + " hello!");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("hello interrupted");
                break;
            }
        }
    }
}


class MyThreadInterrupt extends Thread {
    public void run() {
        int n = 0;
        //循环检检查中断
        while (!isInterrupted()) {
            n++;
            System.out.println(n + " hello!");
        }
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