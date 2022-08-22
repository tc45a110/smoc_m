/**
 * @desc
 * 
 */
package com.protocol.access.smgp.util;

public class SequenceUtil {
	private static int base = 10000;
	private static int sequence=(int)(5*base*Math.random()+10*base);
	
	public static synchronized int getSequence(){
		if(sequence == 999999){
			sequence=(int)(5*base*Math.random()+10*base);
		}
		return sequence++;
	}
	
}


