package com.esb.util;
import java.util.UUID;
/**
 * @Description:uuid工具類
 * @Copyright: Copyright (c) 2017 HYKJ All Rights Reserved
 * @Company: 
 * @author yangzl 2019-6-1
 * @version 1.00.00
 * @history:
 */
public class UUIDUtil {

	public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
   }

   public static void main(String[] args) {
       System.out.println("格式前的UUID ： " + UUID.randomUUID().toString());
       System.out.println("格式化后的UUID ：" + getUUID());
   }
}
