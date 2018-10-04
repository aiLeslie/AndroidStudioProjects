package com.example.administrator.flagapitest.mysocket.socket;

import com.example.administrator.flagapitest.mysocket.util.PrintStyle;
import com.example.administrator.flagapitest.mysocket.util.SysConvert;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;



public class EchoClient {
	public DataFormat TFormat = new DataFormat(DataFormat.HEX_FORMAT);// 发送数据格式
	public DataFormat RFormat = new DataFormat(DataFormat.HEX_FORMAT);// 接收数据格式
	public String encodeMode = "GBK"; // 编码格式
	private String mHost; // 远程主机ip地址
	private int mPort; // 连接端口号
	public Socket mSocket; // Socket通讯对象
	private InputStream in; // 输入流
	private OutputStream out; // 输出流
	public ReceiveThread receiveThread; // 接收数据线程
	private LinkedList<Integer> msg = new LinkedList<>(); // 装载接收信息队列
	private static EchoClient nowClient = new EchoClient(null, -1); // 现在连接服务器的客户

	public EchoClient(String host, int port) {
		// 创建 socket 并连接服务器
		mHost = host;
		mPort = port;
	}

	/**
	 * 连接服务器
	 */

	public void connectServer() {
		synchronized (nowClient) {
			if (nowClient.isConnect()) {
				System.out.println("X<X 已经连接服务器");
				return;
			}
			new Thread() {
				@Override
				public void run() {
					super.run();
					try {
						System.out.println("客户端初始化");
						System.out.println("正在连接服务器  >>>");
						mSocket = new Socket(mHost, mPort);

						// 和服务端进行通信
						in = mSocket.getInputStream();
						out = mSocket.getOutputStream();
						receiveThread = new ReceiveThread();
						receiveThread.start();
						System.out.println("连接成功  <<< " + mSocket.toString());
						nowClient = EchoClient.this;

					} catch (IOException e) {
						e.printStackTrace();

					}
				}
			}.start();

		}
	}

	/**
	 * 向客户发送数据
	 *
	 * @param content
	 */
	public void sendData(byte[] content) {
		if (out == null)
			return;
		try {
			out.write(content, 0, content.length);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendData(int[] content) {
		if (out == null)
			return;
		try {
			for (int i = 0; i < content.length; i++)
				out.write(content[i]);
			System.out.println("客户端发送数据 >>>");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendData(String content) {
		if (out == null)
			return;
		if (TFormat.isHexFormat()) {
			sendData(content);
			return;
		}
		try {

			out.write(content.getBytes(encodeMode));

			System.out.println("客户端发送数据 >>>");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 接收客户发送数据
	 */
	private void receiveData() {
		if (mSocket == null) {
			return;
		} else if (mSocket.isClosed()) {
			System.out.println(mSocket.toString() + " socket close!");
			return;
		}

		if (in == null) {
			return;
		}

		try {
			// 输入流中没有数据
			if (in.available() == 0) {
				return;
			}

			PrintStyle.printLine();
			System.out.println("客户端接收数据 <<<");
			System.out.println(mSocket.toString());

			if (RFormat.isHexFormat()) {
				int value = -1;
				while (in.available() > 0) {
					value = in.read();

					msg.offer(value);

					if (msg.size() % 10 == 0)
						System.out.println();
					System.out.print(Integer.toHexString(value));
					System.out.print(" ");

					if (in.available() == 0)
						System.out.println();
				}
			} else if (RFormat.isStringFormat()) {
				byte[] bytes = new byte[in.available()];
				in.read(bytes, 0, bytes.length);
				System.out.println(new String(bytes, encodeMode));
			}

			PrintStyle.printLine();
			System.out.println();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 项目名称：SocketProject 类名称：ReceiveThread 类描述：接收数据线程 创建人：李玮斌 创建时间：2018年7月6日
	 * 下午12:55:30
	 * 
	 * @version 1.0
	 */
	public class ReceiveThread extends Thread {
		private boolean aswitch = true;

		public boolean isAswitch() {
			return aswitch;
		}

		public void setAswitch(boolean aswitch) {
			this.aswitch = aswitch;
		}

		@Override
		public void run() {
			super.run();
			while (aswitch) {
				System.out.flush();

				receiveData();
			}
		}
	}

	/**
	 * 判断连接状态
	 * 
	 * @return 当前客户连接状态
	 */
	public boolean isConnect() {
		if (mSocket != null && mSocket.isClosed() || mSocket == null) {
			return false;
		} else {
			return true;
		}

	}

	/**
	 * 关闭socket资源
	 */
	public void close() {
		// 关闭服务度接收数据线程
		if (receiveThread != null)
			receiveThread.setAswitch(false);
		// 关闭socket资源
		try {

			if (mSocket != null) {

				if (!mSocket.isClosed())
					mSocket.close();
			}
			System.out.println("客户端关闭 !!!");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] argv) {
		// 由于服务端运行在同一主机，这里我们使用 localhost
		EchoClient client = null;
		Scanner console = new Scanner(System.in);
		try {

			// // 开启客户端连接 localhost 192.168.10.193
			client = new EchoClient("192.168.10.193", 8080);
			client.connectServer();

			/**
			 * 检测控制台输入并把信息发给服务器 直到检测控制台输入exit关闭程序 关闭程序系统资源
			 */
			String input = null;

			while (!(input = console.nextLine()).contains("exit")) {

				if ("".equals(input))
					continue;
				if (client.TFormat.isHexFormat()) {
					// 十六进制发送模式
					client.sendData(SysConvert.parseHex(input));
				} else if (client.TFormat.isStringFormat()) {
					// 字符串发送模式
					client.sendData(input);
				}

			}

		} catch (Exception e) {
			System.out.println("系统异常关闭");
		} finally {
			client.close();// 关闭客户端

			console.close();

			new Thread() {
				public void run() {
					try {
						sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println("系统关闭");
					System.exit(0);

				};

			}.start();
		}
	}
}
