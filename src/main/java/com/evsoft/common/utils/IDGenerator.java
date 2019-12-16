package com.evsoft.common.utils;

import java.util.UUID;

/**
 * @ClassName:ID生成器 (IDGenerator.java)
 * 
 * @Description: 1、生成长度为18位的字符串：系统毫秒数+4位顺序数；<br>
 *               2、生成长度为21位的字符串：系统时间字符串+4位顺序数。
 * 
 * @Date: 2014-08-14 15:12:02
 * @author liuwei
 * @Version 1.0
 */
public class IDGenerator {

	/**
	 * 获得ID
	 * 
	 * @return ID
	 */
	public static synchronized String getUid() {
		return new UID().randomUID();
	}

	public static synchronized String getRandomUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public static synchronized String getRandomTUUID() {
		return new UID().randomTID();
	}

	/**
	 * 获得时间字符串ID
	 * 
	 * @return ID
	 */
	@SuppressWarnings("unused")
	private static synchronized String getTid() {
		return new UID().randomTID();
	}

	/**
	 * 获得带有指定前缀的ID
	 * 
	 * @param prefix
	 * @return
	 */
	public static synchronized String getUid(String prefix) {
		return prefix == null ? "" : prefix.toUpperCase() + new UID().randomUID();
	}

	/**
	 * 获得带有指定前缀的时间字符串ID
	 * 
	 * @param prefix
	 * @return
	 */
	@SuppressWarnings("unused")
	private static synchronized String getTid(String prefix) {
		return prefix == null ? "" : prefix.toUpperCase() + new UID().randomTID();
	}

	/**
	 * 获得UUID
	 * 
	 * @return
	 */
	public static String getUUid() {
		return UUID.randomUUID().toString();
	}

	public static void main(String[] agrs){
		for ( int i =0;i<=100;i++){
			System.out.println(IDGenerator.getUUid());
		}

	}
}


