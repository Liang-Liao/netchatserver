package netchat.netchatserver;

import org.zeromq.ZMQ;

/**
 * Hello world! 这是服务端的测试类
 */
public class Server {
	public static void main(String[] args) throws InterruptedException {
		ZMQ.Context context = ZMQ.context(1);

        //  Socket to talk to clients
        ZMQ.Socket responder = context.socket(ZMQ.REP);
        responder.bind("tcp://*:5555");

        while (!Thread.currentThread().isInterrupted()) {
            // Wait for next request from the client
            byte[] request = responder.recv(0);
            System.out.println("Received " + new String (request));

            // Do some 'work'
            Thread.sleep(1000);

            // Send reply back to client
            String reply = "World";
            responder.send(reply.getBytes(), 0);
        }
        responder.close();
        context.term();
		
		System.out.println("服务端修改");
		System.out.println("Hello World!");
	}
}
