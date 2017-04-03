package project.bsts.semut.utilities;


import android.os.Handler;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class ScheduleTask {

    public interface TimerFireListener{
        public void onTimerRestart(int periode);
    }

    private static int SCHEDULE_TIMER = 0;
    private static int SCHEDULE_HANDLER = 1;
    private int state = 0;

    Timer timer;
    TimerTask timerTask;
    final Handler handler = new Handler();
    long periode;
    private int counter = 0;

    TimerFireListener listener;

    public ScheduleTask(long _periode, final TimerFireListener listener){
        periode = _periode;
        this.listener = listener;

    }

    public void start() {
        state = SCHEDULE_TIMER;
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                handler.post(() -> {
                    counter++;
                    listener.onTimerRestart(counter);
                });
            }
        };
        timer.schedule(timerTask, 0, periode*1000);
    }

    public void stop() {
        if(state == SCHEDULE_TIMER) {
            if (timer != null) {
                timer.cancel();
                timer = null;
                Log.i(this.getClass().getSimpleName(), "Task Stopped");
            }
        }else {
            handler.removeCallbacks(runnable);
            Log.i(this.getClass().getSimpleName(), "Task Stopped");
        }
    }

    public void startHandler(){
        state = SCHEDULE_HANDLER;
        handler.postDelayed(runnable, periode);

    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            counter++;
            listener.onTimerRestart(counter);
            handler.postDelayed(this, periode*1000);
        }
    };
}
