package netchat.netchatserver.utils;

import org.zeromq.ZMQ;

public class PublisherUtil {
	public static ZMQ.Context context = ZMQ.context(1);

	// 转发信息
	public static ZMQ.Socket publisher = context.socket(ZMQ.PUB);
	
	static {
		publisher.bind("tcp://*:5556");
	}
	
}
