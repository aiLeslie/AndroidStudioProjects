package com.example.administrator.flagapitest.mysocket.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import com.example.administrator.flagapitest.mysocket.util.PrintStyle;
import com.example.administrator.flagapitest.mysocket.util.SysConvert;

public class EchoServer {
	public final static String MY_IPAddRESS = "192.168.10.244";// 本机的ip地址
	// STRING_FORMAT and HEX_FORMAT
	public DataFormat TFormat = new DataFormat(DataFormat.HEX_FORMAT);// 发送数据格式
	public DataFormat RFormat = new DataFormat(DataFormat.HEX_FORMAT); // 接收数据格式
	public String encodeMode = "GBK"; // 编码格式
	private ServerSocket mServerSocket; // ServerSocket 服务器对象
	public boolean isListen = true; // 监听状态
	public List<Socket> sockets = new ArrayList<>(); // 客户列表
	private InputStream in; // 输入流
	private OutputStream out; // 输出流
	private ReceiveThread receiveThread; // 接收数据线程
	private LinkedList<Integer> msg = new LinkedList<>(); // 装载接收信息队列
	private int localPort = -1; // 检测端口

	public EchoServer(int port) {
		localPort = port;

	}

	/**
	 * 监听客户的连接
	 */
	public void startListen() {

		new Thread() {
			@Override
			public void run() {
				super.run();
				// 1. 创建一个 ServerSocket 并监听端口 port
				try {
					mServerSocket = new ServerSocket(localPort);
					if (mServerSocket == null) {
						System.out.println("服务器初始化失败");
						return;
					}
					System.out.println("服务器初始化");
					// 开启接收数据线程
					receiveThread = new ReceiveThread();
					receiveThread.start();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				// 2. 开始接受客户连接
				isListen = true;
				
				try {
					// 显示监听端口号
					System.out.println("本服务器的监听端口号: " + localPort);
					// 显示IP地址
					System.out.println("本服务器的IP地址: " + InetAddress.getLocalHost().getHostAddress());
					System.out.println();
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				}
				System.out.println("正在监听客户连接 >>>");

				while (isListen) {
					try {
						Socket client = mServerSocket.accept();
						/**
						 *  遍历客户列表
						 *  如果客户已经连接 移除配置
						 *  否则 不操作
						 */
						Iterator<Socket> iterator = sockets.iterator();
						while (iterator.hasNext()) {
							Socket action = iterator.next();
							if (action.getInetAddress().equals(client.getInetAddress())) {
								sockets.remove(action);
								System.out.println("检测客户重新连接移除配置");
								System.out.println(">>> remove " + action.toString());
								break;

							}
						}
						handleClient(client);
					} catch (IOException e) {
						e.printStackTrace();
					}

				}
				try {
					if (mServerSocket != null) {
						mServerSocket.close();
						mServerSocket = null;
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}.start();

	}

	/**
	 * 处理发出请求连接客户
	 * 
	 * @param socket
	 */
	private void handleClient(Socket socket) {
		if (socket == null)
			return;

		// 3. 使用 socket 进行通信 ...

		// 将socket加入队列中
		sockets.add(socket);
		System.out.println();
		System.out.println("<<< " + socket.toString());

		try {
			if (isListen) {
				in = socket.getInputStream();
				out = socket.getOutputStream();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 向客户发送数据
	 * 
	 * @param content
	 */
	public void sendData(int value) {
		if (out == null)
			return;
		try {
			out.write(value);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("服务端发送数据 >>>");
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
			for (int i = 0; i < sockets.size(); i++) {

				out.write(content, 0, content.length);

			}

			System.out.println("服务端发送数据 >>>");
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
			System.out.println("服务端发送数据 >>>");
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
			for (int i = 0; i < sockets.size(); i++) {

				out.write(content.getBytes(encodeMode));

			}

			System.out.println("服务端发送数据 >>>");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 接收客户发送数据
	 */
	private void receiveData(Socket socket) {

		if (socket == null) {
			return;
		} else if (socket.isClosed()) {
			System.out.println(socket.toString() + " socket close!");
			sockets.remove(socket);
			return;
		}

		try {
			// 获得输入流
			in = socket.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
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
			System.out.println("服务端接收数据 <<<");
			System.out.println(socket.toString());

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
			aswitch = true;
			while (aswitch) {
				System.out.flush();
				try {
					for (int i = 0; i < sockets.size(); i++) {
						if (!isListen)
							break;
						receiveData(sockets.get(i));

					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}

	public void close() {

		// 关闭服务器端的监听
		isListen = false;
		/**
		 * 开启一个线程连接服务端 让serverSocket.accept()返回值,跳出阻塞 关闭服务度接收数据线程 关闭socket资源
		 * 关闭serverSocket
		 */
		new Thread() {
			@SuppressWarnings("resource")
			@Override
			public void run() {
				super.run();
				try {
					new Socket("localhost", localPort);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				// 关闭服务度接收数据线程
				if (receiveThread != null)
					receiveThread.setAswitch(false);
				// 关闭socket资源
				for (Socket action : sockets){
					try {
						if (action != null) {

							if (!action.isClosed())
								action.close();
						}

					} catch (IOException e) {
						e.printStackTrace();
					}
				};

				try {
					if (mServerSocket != null)
						mServerSocket.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("服务器关闭 !!!");

			}
		}.start();

	}

	public static void main(String[] args) {
		EchoServer server = null;
		Scanner console = new Scanner(System.in);
		try {
			// 开启服务器并监听
			server = new EchoServer(8080);
			server.startListen();

			// // 开启客户端连接
			// client = new EchoClient(EchoServer.MY_IPAddRESS, 1000);
			// client.connectServer();

			/**
			 * 检测控制台输入并把信息发给客户端 直到检测控制台输入exit关闭程序 关闭程序系统资源
			 */
			String input = null;

			while (!(input = console.nextLine()).contains("exit")) {

				if ("".equals(input))
					continue;
				if (server.TFormat.isHexFormat()) {
					// 十六进制发送模式
					server.sendData(SysConvert.parseHex(input));
				} else if (server.TFormat.isStringFormat()) {
					// 字符串发送模式
					server.sendData(input);
				}

			}

		} catch (Exception e) {
			System.out.println("系统异常关闭");
		} finally {
			// client.close();// 关闭客户端
			server.close();

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
