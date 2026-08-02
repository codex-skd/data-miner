package com.skd.data_miner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataMinerExecutor {

	private static final ExecutorService IO_EXECUTOR = Executors.newFixedThreadPool(2, r -> {
		Thread t = new Thread(r, "dataminer-io");
		t.setDaemon(true);
		return t;
	});

	public static void runAsync(Runnable task) {
		IO_EXECUTOR.execute(task);
	}
}
