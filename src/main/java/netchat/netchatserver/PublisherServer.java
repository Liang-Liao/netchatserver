package netchat.netchatserver;

import java.util.Random;

import org.zeromq.ZMQ;

public class PublisherServer {
	public static ZMQ.Context context;
	public static ZMQ.Socket publisher;
	
	static {
		context = ZMQ.context(1);
		publisher = context.socket(ZMQ.PUB);
		publisher.bind("tcp://*:5556");
	}
	
	public static void publishMsg(String zipcode, String data) {
		String update = String.format("%s %s", zipcode, data);
		publisher.send(update, 0);
	}
}
