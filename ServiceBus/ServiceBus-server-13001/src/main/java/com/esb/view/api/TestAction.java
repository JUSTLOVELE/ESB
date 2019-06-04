package com.esb.view.api;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.esb.core.Base;
import com.esb.util.JSONUtil;

import net.sf.json.JSONObject;

/**
 * @Description:测试类
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-5-16
 * @version 1.00.00
 * @history:
 */
@RequestMapping("/testAction")
@Controller
public class TestAction extends Base{
	
	private final static Log _logger = LogFactory.getLog(TestAction.class);
	
	@RequestMapping(value = "/binaryImgTest", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String binaryImgTest() {
		
		try {
			
			File file = new File("D:/11.jpg");
			byte[] bytes = FileCopyUtils.copyToByteArray(file);
			String s = new String(bytes, "ISO-8859-1");
			String name = file.getName();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", name);
			map.put("binary", s);
			return getJSON(map);
			
			/*JSONObject j = JSONObject.fromObject(json);
			String a = j.getString("binary");
			file = new File("D:/", "44.jpg");
			InputStream in = new ByteArrayInputStream(a.getBytes("ISO-8859-1"));
			FileOutputStream fos = new FileOutputStream(file);
			byte[] b = new byte[1024];
			int nRead = 0;
			
			while ((nRead = in.read(b)) != -1) {
                fos.write(b, 0, nRead);
            }
			
            fos.flush();
            fos.close();
            in.close();*/
			
		} catch (Exception e) {
			_logger.info("", e);
		}
		
		return JSONUtil.errorReturn("error");
	}
	
	@RequestMapping(value = "/threeParam", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String threeParam(String arg0, String arg1, String arg2) {
		
		String s= "arg0=" + arg0 + ";arg1=" + arg1 + ";arg2=" + arg2;
		return s;
	}

	@RequestMapping(value = "/twoParam", produces = "application/json;charset=utf-8")
	@ResponseBody
	public String twoParam(String arg0, String arg1) {
		
		String s= "arg0=" + arg0 + ";arg1=" + arg1;
		return s;
	}
}
