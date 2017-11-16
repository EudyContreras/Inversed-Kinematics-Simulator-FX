package com.eudycontreras.multithreading.tools;

/**
 * Created by Eudy Contreas on 10/14/2016.
 */

/**
 * ThreadPriority levels
 */
public enum TaskPriority {
    /**
     * Lowest priority level. Used for pre-fetches of data.
     */
    LOW,

    /**
     * Medium priority level. Used for warming of data that might soon get visible.
     */
    MEDIUM,

    /**
     * Highest priority level. Used for data that are currently visible on screen.
     */
    HIGH,

    /**
     * Highest priority level. Used for data that are required instantly(mainly for emergency).
     */
    IMMEDIATE;
}
