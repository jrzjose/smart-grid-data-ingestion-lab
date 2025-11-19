package org.lab;

import java.util.concurrent.TimeUnit;

public class TimerUtil {

    private long currentTime = 0L;
    private TimeUnit timeUnit;
    private int lapsed;

    public TimerUtil(TimeUnit timeUnit, int lapsed) {
        this.timeUnit = timeUnit;
        this.lapsed = lapsed;
        currentTime = System.currentTimeMillis();
    }

    public boolean elapsed() {
        if (timeUnit.equals(TimeUnit.SECONDS))
            return !(System.currentTimeMillis() - currentTime > lapsed * 1000);
        else if (timeUnit.equals(TimeUnit.MINUTES))
            return !(System.currentTimeMillis() - currentTime > lapsed * 60 * 1000);
        else if (timeUnit.equals(TimeUnit.HOURS))
            return !(System.currentTimeMillis() - currentTime > lapsed * 60 * 60 * 1000);
            
        return false;
    }

    public static void block(TimeUnit timeUnit, int period) {
        try {
            if (timeUnit.equals(TimeUnit.SECONDS))
                Thread.sleep(period * 1000);
            else if (timeUnit.equals(TimeUnit.MINUTES))
                Thread.sleep(period * 60 * 1000);
            else if (timeUnit.equals(TimeUnit.HOURS))
                Thread.sleep(period * 60 * 60 * 1000);
        }
        catch (Exception e)
        {
            //
        }
    }
}
