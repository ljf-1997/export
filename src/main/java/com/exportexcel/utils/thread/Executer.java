package com.exportexcel.utils.thread;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 并行框架
 * 
 * @author hunter.han
 * 
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class Executer {
	// 条件队列锁,以及线程计数器
	public final Lock lock = new Lock();
	// 线程池
	private ExecutorService pool = null;
	//默认初始值
	private int threadPoolSize = 1;
	//异常数目
	public int errNum = 0;

	Exception exception;

	public Executer(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
		pool = Executors.newFixedThreadPool(threadPoolSize);
	}

	/**
	 * 任务派发
	 * @param job
	 */
	public void fork(Job job) throws Exception {
		synchronized (lock) {
			while (lock.thread_count >= threadPoolSize) {// 检查线程数
				try {
					lock.wait();// 满了就挂起。等待完成的任务给予通知
				} catch (InterruptedException e) {
					catchPocess(e);
				}
			}
		}
		if (errNum > 0) {
			this.close();
			throw exception;
		}
		//设置Executer容器
		job.setExecuter(this);
		// 设置同步锁
		job.setLock(lock);
		// 将任务派发给线程池去异步执行
		pool.submit(job);
		// 增加线程数
		synchronized (lock) {
			lock.thread_count++;
		}
	}

	public void fork(Collection<Job> jobs) throws Exception {
		for(Job job : jobs) {
			fork(job);
		}
	}

	public void fork(Job[] jobs) throws Exception {
		for(Job job : jobs) {
			fork(job);
		}
	}

	/**
	 * 等待锁
	 * 阻塞上一批线程,上一批线程全部执行完毕后再继续下一批次线程
	 */
	public void waitlock() {
		do {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (this.lock.thread_count > 0);
	}
	
	public void close() {
		this.pool.shutdown();
		this.lock.thread_count = 0;
	}

	void catchPocess(Exception e) {
		synchronized (this) {
			if (++errNum == 1) {
				exception = e;
				e.printStackTrace();
			}
		}
	}
}
