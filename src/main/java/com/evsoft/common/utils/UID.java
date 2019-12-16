package com.evsoft.common.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class UID {
    private static final String mask = "yyyyMMddHHmmssSSS";
	private final DecimalFormat df = new DecimalFormat("0000");

	private static final long ONE_STEP = 10000;
	private static final Lock LOCK = new ReentrantLock();
	private static long lastTime = System.currentTimeMillis();
	private static short lastCount = 0;

	private static long time;
	private static short count;

	public UID() {
		LOCK.lock();
		try {
			if (lastCount == ONE_STEP) {
				boolean done = false;
				while (!done) {
					long now = System.currentTimeMillis();
					if (now == lastTime) {
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
						} // ignore exception
						continue;
					} else {
						lastTime = now;
						lastCount = 0;
						done = true;
					}
				}
			}
			time = lastTime;
			count = lastCount++;
		} finally {
			LOCK.unlock();
		}
	}

	/**
	 * 生成规则：时间毫秒数 + 0-9999的顺序数
	 * 
	 * @return UID
	 */
	protected String randomUID() {
		return time + df.format(count);
	}

	/**
	 * 生成规则：时间毫秒字符串 + 0-9999的顺序数
	 * 
	 * @return UID
	 */
	protected String randomTID() {
		return new SimpleDateFormat(mask).format(new Date(time)) + df.format(count);
	}

}