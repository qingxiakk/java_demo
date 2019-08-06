package com.lcc.juc.producerconsumer;


import java.util.concurrent.locks.ReentrantLock;

class Cake2 implements Cake{

	private volatile int cakeNum;
	ReentrantLock reentrantLock = new ReentrantLock();

	@Override
	public synchronized void  produce() {
		while (cakeNum > 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		cakeNum++;
		System.out.println(Thread.currentThread().getName() + " " + cakeNum);
		// 使用 this.notify() 只会唤醒一个线程,多个线程情况容易造成相互等待没人唤醒
		this.notifyAll();
	}

	@Override
	public synchronized void consumer() {
		while (cakeNum <= 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		cakeNum--;
		System.out.println(Thread.currentThread().getName() + " " + cakeNum);

		// 使用 this.notify() 只会唤醒一个线程,多个线程情况容易造成相互等待没人唤醒
		this.notifyAll();
	}


}


public class CakeLockDemo {

	public static void main(String[] args) {
		Cake cake = new Cake2();

		new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				cake.produce();
			}
		}, "produce AA: ").start();

		new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				cake.consumer();
			}
		}, "consumer CC: ").start();


		new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				cake.produce();
			}
		}, "produce BB: ").start();

		new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				cake.consumer();
			}
		}, "consumer DD: ").start();

	}

}