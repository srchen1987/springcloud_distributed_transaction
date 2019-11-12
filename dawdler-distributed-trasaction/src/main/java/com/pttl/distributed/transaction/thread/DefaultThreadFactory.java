package com.pttl.distributed.transaction.thread;

import java.util.Locale;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @Title: DefaultThreadFactory.java
 * @Description: netty中copy的线程工厂
 * @author: jackson.song
 * @date: 2015年04月21日
 * @version V1.0
 * @email: suxuan696@gmail.com
 */
public class DefaultThreadFactory implements ThreadFactory {

	private static final AtomicInteger poolId = new AtomicInteger();

	private final AtomicInteger nextId = new AtomicInteger();
	private final String prefix;
	private final boolean daemon;
	private final int priority;
	private final ThreadGroup threadGroup;

	public DefaultThreadFactory(Class<?> poolType) {
		this(poolType, false, Thread.NORM_PRIORITY);
	}

	public DefaultThreadFactory(String poolName) {
		this(poolName, false, Thread.NORM_PRIORITY);
	}

	public DefaultThreadFactory(Class<?> poolType, boolean daemon) {
		this(poolType, daemon, Thread.NORM_PRIORITY);
	}

	public DefaultThreadFactory(String poolName, boolean daemon) {
		this(poolName, daemon, Thread.NORM_PRIORITY);
	}

	public DefaultThreadFactory(Class<?> poolType, int priority) {
		this(poolType, false, priority);
	}

	public DefaultThreadFactory(String poolName, int priority) {
		this(poolName, false, priority);
	}

	public DefaultThreadFactory(Class<?> poolType, boolean daemon, int priority) {
		this(toPoolName(poolType), daemon, priority);
	}

	private static String toPoolName(Class<?> poolType) {
		if (poolType == null) {
			throw new NullPointerException("poolType");
		}
		String poolName;
		Package pkg = poolType.getPackage();
		if (pkg != null) {
			poolName = poolType.getName().substring(pkg.getName().length() + 1);
		} else {
			poolName = poolType.getName();
		}

		switch (poolName.length()) {
		case 0:
			return "unknown";
		case 1:
			return poolName.toLowerCase(Locale.US);
		default:
			if (Character.isUpperCase(poolName.charAt(0)) && Character.isLowerCase(poolName.charAt(1))) {
				return Character.toLowerCase(poolName.charAt(0)) + poolName.substring(1);
			} else {
				return poolName;
			}
		}
	}

	public DefaultThreadFactory(String poolName, boolean daemon, int priority) {
		if (poolName == null) {
			throw new NullPointerException("poolName is null!");
		}
		if (priority < Thread.MIN_PRIORITY || priority > Thread.MAX_PRIORITY) {
			throw new IllegalArgumentException(
					"priority: " + priority + " (expected: Thread.MIN_PRIORITY <= priority <= Thread.MAX_PRIORITY)");
		}
		prefix = poolName + '-' + poolId.incrementAndGet() + '-';
		this.daemon = daemon;
		this.priority = priority;
		SecurityManager s = System.getSecurityManager();
		threadGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread t = new Thread(threadGroup, r, prefix + nextId.incrementAndGet(), 0);
		try {
			if (t.isDaemon()) {
				if (!daemon) {
					t.setDaemon(false);
				}
			} else {
				if (daemon) {
					t.setDaemon(true);
				}
			}

			if (t.getPriority() != priority) {
				t.setPriority(priority);
			}
		} catch (Exception ignored) {
		}
		return t;
	}
}
