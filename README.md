1.服务总线，基于SOA思想的企业服务总线 
2.目前服务总线采用两级的接口即site/service,site code是指站点，service是指某个站点下面的具体服务器，显然site/service路径是唯一的。  
3.注册服务和调用服务都支持json格式和xml格式的调用,以哪种格式调用就以哪种格式返回  
4.已完成功能:    
	提供http,webservice,soap三种形式的调用;文件和图片要转为二进制再进行传输  
	支持json和xml格式注册,删除服务仅支持json格式调用  
	监听zookeeper数据节点  
5.下一阶段完成功能:  
	消息分发  
	负载均衡
