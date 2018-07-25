package netchat.netchatserver;

import org.zeromq.ZMQ;

/**
 * Hello world! 这是服务端的测试类
 */
public class Server {

	//请求应答模式的服务线程
	public static class rrThread implements Runnable {
		@Override
		public void run() {
			ZMQ.Context context = ZMQ.context(1);

			// Socket to talk to clients
			ZMQ.Socket responder = null;
			responder = context.socket(ZMQ.REP);
			responder.bind("tcp://*:5555");

			System.out.println("服务器启动");
			while (!Thread.currentThread().isInterrupted()) {
				// 等待信息
				byte[] request = responder.recv(0);
				System.out.println("接受信息： " + new String(request));

				// 工作
				System.out.println("正在处理信息：" + new String(request));

				// 发送回复信息
				String reply = "已接收： " + new String(request);
				responder.send(reply.getBytes(), 0);
			}
			responder.close();
			context.term();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		new Thread(new rrThread()).start();
	}
}
