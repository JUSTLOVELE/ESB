package test.camel.cxf.soap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.Builder;

public class HttpGetProcessor implements Processor {
	
	public void process(Exchange exchange) throws Exception {
		// 因为很明确消息格式是http的，所以才使用这个类
		// 否则还是建议使用org.apache.camel.Message这个抽象接口
		Message message = (Message)exchange.getIn();
		InputStream bodyStream =  (InputStream)message.getBody();
		String inputContext = analysisMessage(bodyStream);
		bodyStream.close();
		
		// 存入到exchange的out区域
		Message outMessage = exchange.getOut();
		outMessage.setHeader(Exchange.HTTP_QUERY, Builder.constant("clientName=110")) ;
		
		Message message1 = (Message)exchange.getIn();
		InputStream bodyStream1 =  (InputStream)message.getBody();
		String inputContext1 = analysisMessage(bodyStream);
		System.out.println(inputContext1);
	}
	
	/**
	 * 从stream中分析字符串内容
	 * @param bodyStream
	 * @return
	 */
    private String analysisMessage(InputStream bodyStream) throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] contextBytes = new byte[4096];
		int realLen;
		while((realLen = bodyStream.read(contextBytes , 0 ,4096)) != -1) {
			outStream.write(contextBytes, 0, realLen);
		}
		
		// 返回从Stream中读取的字串
		try {
			return new String(outStream.toByteArray() , "UTF-8");
		} finally {
			outStream.close();
		}
	}
}