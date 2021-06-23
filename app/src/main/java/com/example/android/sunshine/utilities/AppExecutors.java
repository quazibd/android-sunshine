package com.example.android.sunshine.utilities;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AppExecutors {
    // Sets the amount of time an idle thread waits before terminating
    private static final int KEEP_ALIVE_TIME = 1;
    // Sets the Time Unit to seconds
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    /*
     * Gets the number of available cores
     * (not always the same as the maximum number of cores)
     */
    private static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private static final AppExecutors staticInstance;

    static {
        staticInstance = new AppExecutors();
    }

    // Creates a thread pool manager
    private ThreadPoolExecutor networkIOExecutor;

    private AppExecutors() {
        this.networkIOExecutor = new ThreadPoolExecutor(
                NUMBER_OF_CORES,       // Initial pool size
                NUMBER_OF_CORES,       // Max pool size
                KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT,
                new LinkedBlockingQueue<>()
        );
    }

    public static AppExecutors getInstance() {
        return staticInstance;
    }

    public Executor getNetworkIOExecutor() {
        return networkIOExecutor;
    }
}
