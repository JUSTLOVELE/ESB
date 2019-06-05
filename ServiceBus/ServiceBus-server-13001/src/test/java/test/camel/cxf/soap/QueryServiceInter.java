package test.camel.cxf.soap;
import javax.jws.WebParam;  
import javax.jws.WebService;

public interface QueryServiceInter {
	/** 
     * 查询人员信息 
     *  
     * @param queryInfo 
     * @return 
     */  
    String queryPersonnelInformation(@WebParam(name = "peopleParam") String peopleParam);  
  
    /** 
     * 查询车辆信息 
     *  
     * @param carInfo 
     * @return 
     */  
    String queryCarInfomation(@WebParam(name = "carParam") String carParam);  
}
