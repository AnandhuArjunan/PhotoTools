package com.anandhuarjunan.imagetools.helper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;

public class ThreadPoolHelper {
	private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	
	private ThreadPoolHelper(){}
	
	public static void execute(Runnable runnable) {
		executorService.execute(runnable);
	}
	
	public static void shutdownPool() {
		executorService.shutdown();
	}
	
	public void executeOnUIThread(Runnable runnable) {
		Platform.runLater(runnable);
	}
	
}
