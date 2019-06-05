package test.camel.cxf.soap;

import javax.jws.WebService;

@WebService
public class QueryService implements QueryServiceInter{
	public String queryPersonnelInformation(String peopleParam) {
        String returnRusult = "queryPersonnelInformation";
        System.out.println(returnRusult);
        return returnRusult;
    }
  
    public String queryCarInfomation(String carParam) {
        String returnRusult = "queryCarInfomation:"+carParam;
        System.out.println(returnRusult);
        return returnRusult;
    }
}
