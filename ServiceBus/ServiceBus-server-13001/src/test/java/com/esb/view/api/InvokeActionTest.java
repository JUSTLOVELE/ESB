package com.esb.view.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.esb.core.Base;
import com.esb.util.Constant;

@RunWith(SpringRunner.class)
@SpringBootTest
public class InvokeActionTest extends Base{
	
	@Autowired
	private InvokeAction _invokeAction;

	@Test
	public void registerWithJsonTest() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put(Constant.Key.SITE_CODE, "350000");
		map.put(Constant.Key.SERVICE_CODE, "checkVersion");
		map.put(Constant.Key.URL, "http://192.168.1.151/HY-GS/mobileSystemAction/api/checkVersion?source=1");
		map.put(Constant.Key.TYPE, 1);
		List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
		
		map.put(Constant.Key.PARAMS, params);
		String param = getJSON(map);
		String s = _invokeAction.registerWithJson(param);
		System.out.println(s);
	}
}
