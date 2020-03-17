# 1.简要说明
1.1.服务总线，基于SOA思想的企业服务总线 
1.2.目前服务总线采用两级的接口即site/service,site code是指站点，service是指某个站点下面的具体服务器，显然site/service路径是唯一的  
1.3.注册服务和调用服务都支持json格式和xml格式的调用,以哪种格式调用就以哪种格式返回 
# 2.已完成功能:    
&nbsp;&nbsp;提供http,webservice,soap三种形式的调用;文件和图片要转为二进制再进行传输  
&nbsp;&nbsp;支持json和xml格式注册,删除服务仅支持json格式调用  
&nbsp;&nbsp;监听zookeeper数据节点  
# 3.下一阶段完成功能:  
&nbsp;&nbsp;消息分发  
&nbsp;&nbsp;负载均衡
