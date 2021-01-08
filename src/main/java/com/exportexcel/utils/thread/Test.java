package com.exportexcel.utils.thread;

import java.util.concurrent.ExecutionException;

@SuppressWarnings("unchecked")
public class Test {

	/**
	 * 计算圆周率
	 * @return
	 */
	public double kesan(Object arg) throws Exception {
		double k = 0;  // 分母
		int j = 0;  // 计数器
		double sum = 0;  // 总和
		for (int i = 1; i <= 1000; i++) {
			if (i % 2 != 0) {  // 判断是否为基数
				k = i;  // 先获取分母
				j++;
				if (j % 2 == 0) {  // 判断是否为偶数项  如果是 乘-1
					k *= (-1);
				}
			}
			sum =sum +(1/k);  // 计算出总和
		}
		Thread.sleep(1000);
		//if (1 == 1) throw new Exception();
		return 2*sum;
	}
	
	/**
	 * @return
	 * @throws InterruptedException 
	 * @throws ExecutionException 
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Test test = new Test();
		long start_time = System.currentTimeMillis();
		Executer exe = new Executer(3);
		for (int i = 0; i < 5; i++) {
			exe.fork(new Job(i) {
				@Override
				public void execute(Object[] args) throws Exception {
					test.kesan(args[0]);
				}
			});
		}
		long end_time = System.currentTimeMillis();
		exe.close();
	}

}
