package com.example.administrator.bluetoothtest.mysocket.Task;
/**
 * 
* 项目名称：SocketProject   
* 类名称：Task   
* 类描述：
* 创建人：李玮斌   
* 创建时间：2018年7月2日 上午11:14:32   
* @version 1.0
 */
// 通讯协议  {start, lengeth, id, importantdata, checkAnd, end}
public class Task {
	public static final int Start_Default = 0xff;
	public static final int End_Default = 0xfe;

	private int start = Start_Default;//数据包开头
	private int length;//数据包长度
	private int id;//任务id
	private int[] importanceData;//重要的传输数据
	private int checkAnd;//校验和
	private int end = End_Default;//数据包结尾
	private int[] ints;//表示任务的所有属性的数组
	
	

	/**
	 * 通用任务构造方法
	 * @param id
	 * @param importanceData
	 */
	public Task(int start,int id, int[] importanceData, int end) {
		super();
		if (importanceData == null)throw new IllegalArgumentException();
		this.start = start;
		this.end = end;
		this.id = id;
		this.importanceData = importanceData;
		this.length = (int) (importanceData.length + 5);
		updateCheckAnd();
	}

	/**
	 * 发送任务构造方法
	 * @param id
	 * @param importanceData
	 */
	public Task(int id, int[] importanceData) {
		super();
		if (importanceData == null)throw new IllegalArgumentException();
		this.id = id;
		this.importanceData = importanceData;
		updateCheckAnd();
	}
	public Task(int start, int length, int id, int[] importanceData, int checkAnd, int end) {
		this.start = start;
		this.length = length;
		this.id = id;
		this.importanceData = importanceData;
		this.checkAnd = checkAnd;
		this.end = end;
		updateCheckAnd();
	}


	/**
	 * 把接收的数据构建Rtask实例
	 * @param ints
	 * @return
	 */
	public Task(int[] ints){
		if (ints.length <= 5) {
			throw new IllegalArgumentException();
		}
		this.ints = ints;
		this.start = ints[0];
		this.length = ints[1];
		this.id = ints[2];

			this.importanceData = new int[ints.length - 3 - 2];
			for (int i = 0 ; i < importanceData.length ; i++){
				importanceData[i] = ints[3 + i];
			}

		
		this.checkAnd = ints[ints.length - 2];
		this.end = ints[ints.length - 1];
	}


	/**
	 *
	 * @return 表示任务的所有属性的字节数组
	 */
	public int[] getints() {
		this.ints = new int[this.length];
		ints[0] = this.start;
		ints[1] = this.length;
		ints[2] = this.id;
		for (int i = 0 ; i < importanceData.length;i++) {
			ints[3 + i] = importanceData[i];
		}
		ints[length - 2] = this.checkAnd;
		ints[length - 1] = this.end;
		return ints;
	}


	public int[] getImportanceData() {
		return importanceData;
	}
	public void setImportanceData(int[] importanceData) {
		this.importanceData = importanceData;
		updateCheckAnd();
	}
	public  void updateCheckAnd() {
		//更新任务长度
		this.length = (int) (importanceData.length + 5);
		//更新任务校验和
		this.checkAnd = (int)(this.id + this.length + this.getImportanceDataSum());
	}
	/**
	 * 接收任务
	 * 判断通讯是否成功
	 * @param task
	 * @return
	 */
	public static boolean communicationIsSuccess(Task task) {
		if (task.checkAnd == task.id + task.length + task.getImportanceDataSum() && task.start == Start_Default && task.end == End_Default) {
			return true;
		}else if (task.checkAnd == task.id + task.length && task.start == Start_Default && task.end == End_Default) {
			return true;
		}else {
			return false;
		}
	}
	/**
	 * 接收任务
	 * 判断通讯是否成功
	 * 如果通讯成功就执行任务
	 * @param task
	 * @return
	 */
	public static boolean communicationIsSuccess(Task task, TaskHandler handler) {
		/**
		 * 数据头相同
		 * 数据尾相同
		 * 校验和 = 任务号 + 任务长度 + 有效数据的总和
		 */
		if (task.checkAnd == task.id + task.length + task.getImportanceDataSum() && task.start == Start_Default && task.end == End_Default) {
			handler.handleTask(task);
			return true;
			/**
			 * 数据头相同
			 * 数据尾相同
			 * 校验和 = 任务号 + 任务长度
			 */
		}else if (task.checkAnd == task.id + task.length && task.start == Start_Default && task.end == End_Default) {
			handler.handleTask(task);
			return true;
		}else {
			return false;
		}
	}
	
	public int getStart() {
		return start;
	}
	public int getId() {
		return id;
	}
	public int getLength() {
		return length;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int b :getints()){
			builder.append(b);
			builder.append(" ");
		}
		return builder.toString();
	}
	/**
	 * 计算任务中的重要传输数据的和
	 * @return
	 */
	private int getImportanceDataSum() {
		int sum = 0;
		for (int b : importanceData) {
			sum += b;
		}
		return sum;
	}



    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Task)){
            return false;
        }else {
            int[] ints = ((Task)obj).getints();
            if (ints.length != this.getints().length){
                return false;
            }else {
                for (int i = 0 ; i < ints.length ; i++){
                    if (ints[i] != this.getints()[i]){
                        return false;
                    }
                }
                return true;
            }

        }

    }
    
}
