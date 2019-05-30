package com.esb.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.esb.core.Base;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZookeeperServiceImplTest extends Base {

	@Autowired
	private ZookeeperServiceImpl _zookeeperServiceImpl;
	
	@Test
	public void initCreateZookeeperServiceTest() {
		
		String path = "/root/350000/checkVersion";
		boolean b = _zookeeperServiceImpl.checkExists(path);
		
		if(b) {
			System.out.println(_zookeeperServiceImpl.getData(path));
		}
	}
}
