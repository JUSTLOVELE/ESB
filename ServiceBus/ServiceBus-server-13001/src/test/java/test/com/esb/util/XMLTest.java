package test.com.esb.util;

import java.io.StringReader;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.junit.Test;

public class XMLTest {
	
	@Test
	public void jdom() {
		
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("<root>");
			sb.append("<orgCode>机构编码</orgCode>");
			sb.append("<serviceCode>服务号</serviceCode>");
			sb.append("<offline>1</offline>");
			sb.append("<param>");
			sb.append("<value>1</value>");
			sb.append("<key>key1</key>");
			sb.append("</param>");
			sb.append("<param>");
			sb.append("<value>2</value>");
			sb.append("<key>key2</key>");
			sb.append("</param>");
			sb.append("<param>");
			sb.append("<value>3</value>");
			sb.append("<key>key3</key>");
			sb.append("</param>");
			sb.append("</root>");
			SAXBuilder sax = new SAXBuilder();
			Document doc = sax.build(new StringReader(sb.toString()));
			Element root = doc.getRootElement();
			Element orgcode = root.getChild("orgCode");
			List<Element> es = root.getChildren("param");
			
			for(Element e: es) {
				System.out.println(e.getChild("value").getValue());
				System.out.println(e.getChild("key").getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void DOM4J() {
		
		try {
			
			StringBuffer sb = new StringBuffer();
			sb.append("<root>");
			sb.append("<orgCode>机构编码</orgCode>");
			sb.append("<serviceCode>服务号</serviceCode>");
			sb.append("<offline>1</offline>");
			sb.append("<param>");
			sb.append("</param>");
			sb.append("</root>");
//			Document registerXML = DocumentHelper.parseText(sb.toString());
//			Element root = registerXML.getRootElement();
//			System.out.println(root.toString());
//			Iterator iterator = root.elementIterator();
//			
//			while(iterator.hasNext()) {
//				
//				Element e = (Element) iterator.next();
//				System.out.println(e.toString());
//			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
