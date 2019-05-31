package com.esb.service.route;

import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.camel.RuntimeCamelException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.esb.util.Constant;


/**
 * @Description:动态路由
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @author yangzl 2019-5-31
 * @version 1.00.00
 * @history:
 */
public class DynamicRouter {
	
	private final static Log _logger = LogFactory.getLog(DynamicRouter.class);
	
	public String selectRoute(Exchange exchange) {
		
		_logger.info("selectRoute");
		return "direct:esb_350000_checkVersion";
	}

	/**
	 * 选择优先级路由
	 * 	根据当前登录用户的等级来进行修正
	 * @param exchange
	 * @return
	 */
	//通过@Header(SystemConstant.HEADER_NAME_INVOKE_PRIORITY)Integer invokePriority 可以访问头参数的数据
	public String routeByPriority(Exchange exchange, @Header(Constant.HeadParam.INVOKEPRIORITY) Integer invokePriority){
		
		if(invokePriority==null||invokePriority<0){
			throw new RuntimeCamelException("invokePriority=0或空");
			//return null;
		}else if(invokePriority<5){
			return RouteUtil.Direct.DIRECT_PRODUCENORMAL;
		}else{
			return RouteUtil.Direct.DIRECT_PRODUCEHIGH;
		}
	}
}
