package tutorInteligente.Utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class Chronometer {

  private Timeline timeline;
  private long timeCounter;
  private Label label;

  public Chronometer(Label label) {
    this.timeCounter = 0L;
    this.label = label;

    this.timeline =
        new Timeline(
            new KeyFrame(
                Duration.millis(1), arg -> this.label.setText(updateLabels(timeCounter++))));

    this.timeline.setCycleCount(Timeline.INDEFINITE);
  }

  protected String updateLabels(long timeCounter) {
    long seconds = (timeCounter / 1000) % 60;
    long minutes = (timeCounter / 60000) % 60;
    String sSec = seconds < 10 ? ("0" + seconds) : ("" + seconds);
    String sMin = minutes < 10 ? ("0" + minutes) : ("" + minutes);

    return sMin + ":" + sSec;
  }

  public void play() {
    timeline.play();
  }

  public void stop() {
    this.timeline.stop();
  }

  public void pause() {
    this.timeline.pause();
  }

  public void reset() {
    this.stop();
    this.timeCounter = 0L;
    this.label.setText(this.updateLabels(0L));
  }
}
