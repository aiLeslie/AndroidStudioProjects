package com.example.administrator.bluetoothtest.mysocket.socket;

import com.example.administrator.bluetoothtest.mysocket.util.SysConvert;

import java.util.Scanner;

public class Test {
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

			while (!(input = console.nextLine()).contains("eixt")) {

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
