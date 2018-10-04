package com.example.administrator.flagapitest.mysocket.Task;

import java.util.LinkedList;

//通讯协议  {start, lengeth, id, importantdata, checkAnd, end}
public class MsgCounter {
	// 装载信息队列
	public LinkedList<Integer> msg = new LinkedList<>();
	// 执行任务处理器
	private TaskHandler handler;
	// 数据包开头
	private int start = Task.Start_Default;
	// 数据包长度
	private int length = -1;
	// 信息队列长度
	private int count = msg.size();
	// 开始计数标志
	private boolean isCount = false;

	/**
	 * 默认构造器
	 */
	public MsgCounter() {
		super();
	}

	/**
	 * 带有任务处理器的构造器
	 * 
	 * @param taskHandler
	 */
	public MsgCounter(TaskHandler taskHandler) {
		super();
		this.handler = taskHandler;
	}

	/**
	 * 设置数据包开头
	 * 
	 * @param start
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * 数据加载
	 * 
	 * @param value
	 */
	public void add(int value) {
		if (start == value && !isCount) {// 数值等同数据包开头
			// 开始计数
			isCount = true;

		} else if (!isCount) {// 还没有开始计数
			return;
		}
		msg.offer(value); // 装载数据
		// 信息队列长度自加1
		count++;
		if (count == 2) { // 如果信息队列长度为2 该数值为数据包长度
			length = value;
			// 如果该数值为数据包长度<= 5  复位计数
			if (length <= 5)resetCount();
		}
		if (length == count) {// 如果信息队列长度 和 数据包长度 等同
			// 1.处理任务
			if (handler != null) {// 如果有任务处理器
				// 消息队列转换成整形数组
				int[] ints = new int[count];

				for (int i = 0; i < count; i++) {
					ints[i] = msg.get(i);
				}
				// 创建任务 判读通讯是否成功 再执行任务
				Task.communicationIsSuccess(new Task(ints), handler);
			}

			// 2.清空消息队列 (复位计数)
			resetCount();

		}

	}

	/**
	 * 重新复位计数
	 */
	private void resetCount() {
		isCount = false;
		msg.clear();
		length = -1;
		count = 0;

	}

	public static void main(String[] args) {
		System.out.println("验证MsgCounter功能实现");
		Task task = new Task(2, new int[] { 0, 0, 3, 4 });
		// task.getImportanceData()[0] = 2;
		MsgCounter counter = new MsgCounter(new TaskHandler() {

			@Override
			public void handleTask(Task task) {
				switch (task.getId()) {
				case 2:
					int[] data = task.getImportanceData();
					System.out.println("打印传输数据");
					for (int i : data) {
						System.out.print(i);
						System.out.print(" ");
					}
					System.out.println();
					break;
				}

			}
		});
		counter.add(Task.Start_Default);
		for (int i : task.getints()) {
			counter.add(i);
		}
//		counter.add(Task.Start_Default);
		for (int i : task.getints()) {
			counter.add(i);
		}
	}

}
