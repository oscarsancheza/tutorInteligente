package tutorInteligente.Utils;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

  private DateTimeFormatter formatter;
  Timeline clock;

  public DateTimeUtil() {}

  public void initClock(Label label) {
    this.clock =
        new Timeline(
            new KeyFrame(
                Duration.ZERO,
                e -> {
                  formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");
                  label.setText(LocalDateTime.now().format(formatter));
                }),
            new KeyFrame(Duration.seconds(1)));
    clock.setCycleCount(Animation.INDEFINITE);
    clock.play();
  }
}
