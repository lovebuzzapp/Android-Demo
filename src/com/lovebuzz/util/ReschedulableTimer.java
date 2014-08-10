package com.lovebuzz.util;

import java.util.Timer;
import java.util.TimerTask;

public class ReschedulableTimer extends Timer {
  private Runnable task;
  private TimerTask timerTask;

  public void scheduleTimer(Runnable runnable, long delay) {
    task = runnable;
    timerTask = new TimerTask() {
      public void run() {
        task.run();
      }
    };
    schedule(timerTask, delay);
  }

  @Override
  public void cancel() {
    if (timerTask != null) {
      timerTask.cancel();
    }
  }

  public void reschedule(long delay) {
    if (timerTask != null) {
      timerTask.cancel();
    }
    timerTask = new TimerTask() {
      public void run() {
        task.run();
      }
    };
    schedule(timerTask, delay);
  }

}
