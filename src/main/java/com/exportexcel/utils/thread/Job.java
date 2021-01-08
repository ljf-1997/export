package com.exportexcel.utils.thread;

import java.util.concurrent.Callable;

/**
 * 抽象任务
 *
 * @author hunter.han
 */
@SuppressWarnings({"rawtypes"})
public abstract class Job<T> implements Callable<T>, Cloneable {
    //锁
    private Lock lock = null;

    private Object[] args;

    private Executer executer;

    public Job() {
    }

    public Job(Object... args) {
        this.args = args;
    }

    public Object[] getArgs() {
        return args;
    }

    void setLock(Lock lock) {
        this.lock = lock;
    }

    void setExecuter(Executer executer) {
        this.executer = executer;
    }

	public T call() throws Exception {
        if (executer != null && executer.errNum > 0) {
            synchronized (lock) {
                lock.thread_count--;
                lock.notifyAll();
            }
            return null;
        }
        T result = null;
        try {
            this.execute(args);//执行子类具体任务
        } catch (Exception e) {
            if (executer != null) {
            	executer.catchPocess(e);
            }
        }
        //处理完业务后，任务结束，递减线程数，同时唤醒主线程
        synchronized (lock) {
            lock.thread_count--;
            lock.notifyAll();
        }
        return result;
    }

	@Override
    public Job clone() {
        try {
            return (Job) super.clone();
        } catch (CloneNotSupportedException e) {

        }
        return null;
    }

    /**
     * 业务处理函数
     */
    public abstract void execute(Object[] args) throws Exception;
}
