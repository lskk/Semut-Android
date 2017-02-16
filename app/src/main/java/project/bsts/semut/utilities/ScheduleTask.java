package project.bsts.semut.utilities;


import android.os.Handler;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class ScheduleTask {

    public interface TimerFireListener{
        public void onTimerRestart(int periode);
    }

    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();
    long periode;
    private int counter = 0;

    public ScheduleTask(long _periode){
        periode = _periode;

    }

    public void start(final TimerFireListener listener) {
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        counter++;
                        listener.onTimerRestart(counter);
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, periode*1000);
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
            Log.i(this.getClass().getSimpleName(), "Task Stopped");
        }
    }
}
