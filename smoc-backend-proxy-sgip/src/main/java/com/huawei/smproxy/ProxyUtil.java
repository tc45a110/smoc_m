/**
 * @desc
 * 
 */
package com.huawei.smproxy;

public class ProxyUtil {
	  private static int base = 100000000;
	  private static int sequence=(int)(5*base*Math.random()+10*base);

	  static public synchronized int getSequence(){
			if(sequence == Integer.MAX_VALUE){
				return 10*base;
			}
			return sequence++;
	  }
}


