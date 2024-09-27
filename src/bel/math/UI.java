package bel.math;

import bel.math.data.Exercise;
import bel.math.data.Stat;
import bel.math.types.CategoryLabels;
import javafx.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.text.SimpleDateFormat;
import java.util.Date;

import static bel.math.types.State.*;


public class UI extends JFrame implements Runnable, ActionListener
{
  private final Thread thread = new Thread(this);
  private boolean isAlive = true;

  private final Engine engine = new Engine();

  private final Label timeLbl = new Label();
  private final Label dateLbl = new Label();
  private final Label scoreLbl = new Label();
  private final TextField exNumTF = new TextField();
  private final Button controlBtn = new Button();

  private final Label catLbl = new Label();
  private final Label exLbl = new Label();
  private final TextField ansTF = new TextField();
  private final Label incorrectLbl = new Label();
  private final Label correctAnsLbl = new Label();
  private final TextArea resultTA = new TextArea();


  public UI() throws HeadlessException
  {
    setTitle("Математика");
    setLayout(null);

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int w = 1500;
    int h = 1000;
    int x = screenSize.width / 2 - w / 2;
    int y = screenSize.height / 2 - h / 2;
    setBounds(x, y, w, h);

    Font smallFont = new Font("Arial", Font.PLAIN, 16);
    Font font = new Font("Arial", Font.PLAIN, 20);
    Font exFont = new Font("Monospaced", Font.BOLD, 24);
    Font largeFont = new Font("Arial", Font.PLAIN, 24);

    add(timeLbl);
    timeLbl.setBounds(w - 90, 10, 85, 25);
    timeLbl.setFont(largeFont);
    add(dateLbl);
    dateLbl.setBounds(w - 190, h - 80, 185, 25);
    dateLbl.setFont(font);
    dateLbl.setForeground(Color.blue.darker());

    add(scoreLbl);
    scoreLbl.setBounds(w - 300, 10, 200, 25);
    scoreLbl.setFont(largeFont);
    scoreLbl.setForeground(Color.magenta);

    Label exNum = new Label("Число упражнений:");
    add(exNum);
    exNum.setBounds(10, 10, 150, 20);
    exNum.setFont(smallFont);

    add(exNumTF);
    exNumTF.setText("20");
    exNumTF.setBounds(160, 10, 50, 24);
    exNumTF.setFont(smallFont);

    int dx = 300;
    add(controlBtn);
    controlBtn.setBounds(dx, 10, 150, 40);
    controlBtn.setFont(font);
    controlBtn.setLabel("Старт");
    controlBtn.addActionListener(this);

    int dy = 100;
    add(catLbl);
    catLbl.setBounds(dx, dy, 800, 30);
    catLbl.setFont(font);
    catLbl.setForeground(Color.blue.darker());

    dy += 50;
    add(exLbl);
    exLbl.setVisible(false);
    exLbl.setBounds(dx, dy, 150, 30);
    exLbl.setFont(exFont);

    add(ansTF);
    ansTF.setVisible(false);
    ansTF.setBounds(dx + 170, dy, 100, 30);
    ansTF.setFont(exFont);
    ansTF.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyReleased(KeyEvent e)
      {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
          submit();
      }
    });

    add(incorrectLbl);
    incorrectLbl.setVisible(false);
    incorrectLbl.setText("Не правильно");
    incorrectLbl.setBounds(dx + 300, dy, 200, 30);
    incorrectLbl.setFont(font);
    incorrectLbl.setForeground(Color.red.darker());

    add(correctAnsLbl);
    correctAnsLbl.setVisible(false);
    correctAnsLbl.setBounds(dx + 170, dy, 200, 30);
    correctAnsLbl.setFont(font);
    correctAnsLbl.setForeground(Color.green.darker().darker());

    add(resultTA);
    resultTA.setVisible(false);
    resultTA.setBounds(dx, 200, 1000, 750);
    resultTA.setFont(font);
    resultTA.setForeground(Color.blue.darker());
  }

  public static void main(String[] args)
  {
    final UI ui = new UI();
    ui.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    ui.setVisible(true);
    ui.thread.setDaemon(true);
    ui.thread.start();
  }

  @Override
  public void run()
  {
    try
    {
      SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy hh:mm");
      while (isAlive)
      {
        long time = engine.totalTime + (engine.started > 0 ? (System.currentTimeMillis() - engine.started) : 0);
        timeLbl.setText(format(time / 1000 / 60) + ":" + format(time / 1000 % 60));
        dateLbl.setText(df.format(new Date()));

        sleepMs(100);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == controlBtn && engine.state == NEW)
      start();
    else if (e.getSource() == controlBtn && engine.state == IN_PROGRESS)
      pause();
    else if (e.getSource() == controlBtn && engine.state == PAUSED)
      resume();
  }

  private void start()
  {
    controlBtn.setLabel("Пауза");
    exNumTF.setEnabled(false);
    exLbl.setVisible(true);
    ansTF.setVisible(true);

    engine.start(parse(exNumTF.getText()));
    next();
  }

  private void pause()
  {
    controlBtn.setLabel("Продолжить");
    exLbl.setVisible(false);
    ansTF.setVisible(false);

    engine.pause();
  }

  private void resume()
  {
    controlBtn.setLabel("Пауза");
    exLbl.setVisible(true);
    ansTF.setVisible(true);

    engine.resume();
    next();
  }

  private void next()
  {
    incorrectLbl.setVisible(false);
    correctAnsLbl.setVisible(false);
    ansTF.setForeground(Color.black);
    ansTF.setText("");

    if (engine.exCount <= engine.exNumber)
    {
      engine.generate();
      catLbl.setText(CategoryLabels.labels.get(engine.exercise.category));
      exLbl.setText(engine.exCount + ")  " + engine.exercise.toExpression());
      int w = textWidth(exLbl.getFont(), exLbl.getText());
      exLbl.setSize(w, exLbl.getHeight());
      scoreLbl.setText("Очки: " + engine.score + "  (+" + engine.exercise.complexity() + ")");
      ansTF.setLocation(exLbl.getX() + w + 10, ansTF.getY());
      ansTF.requestFocus();
    }
    else
      finish();
  }

  private void submit()
  {
    boolean correct;
    try
    {
      correct = engine.submit(ansTF.getText().trim().replace(',', '.'));
    }
    catch (Exception e)
    {
      System.out.println("Не распознано: " + ansTF.getText());
      return;
    }

    scoreLbl.setText("Очки: " + engine.score);
    if (correct)
    {
      ansTF.setText("");
      next();
    }
    else
    {
      correctAnsLbl.setLocation(ansTF.getX(), ansTF.getY());
      ansTF.setLocation(ansTF.getX() + 70, ansTF.getY());
      ansTF.setForeground(Color.red.darker());
      incorrectLbl.setLocation(ansTF.getX() + ansTF.getWidth() + 10, ansTF.getY());
      incorrectLbl.setVisible(true);
      correctAnsLbl.setText("" + engine.exercise.r);
      correctAnsLbl.setVisible(true);
      controlBtn.setLabel("Продолжить");
      engine.pause();
      controlBtn.requestFocus();
    }
  }

  private void finish()
  {
    engine.finish();

    catLbl.setVisible(false);
    exLbl.setVisible(false);
    ansTF.setVisible(false);
    controlBtn.setEnabled(false);
    controlBtn.setLabel("Закончили");

    long seconds = engine.totalTime / 1000;
    String text = "закончили, правильно: " + engine.exCorrect + " из " + engine.exNumber;
    text += "\r\nвремя: " + seconds / 60 + " мин. " + seconds % 60 + " сек.";

    double avgTime = Math.round(10.0 * seconds / engine.exNumber) / 10.0;
    String speedMark = "очень быстро";
    if (avgTime > 2)
      speedMark = "быстро";
    if (avgTime > 5)
      speedMark = "нормально";
    if (avgTime > 10)
      speedMark = "медленно";
    if (avgTime > 20)
      speedMark = "ну ты тормоз!";

    text += "\r\nсреднее время на пример: " + avgTime + " сек.   Cкорость: " + speedMark;

    int mark = 5 * engine.exCorrect / engine.exNumber;
    text += "\r\n\r\nОценка за правильность: " + mark;
    if (mark == 5 && avgTime <= 2)
      text += "\r\n\r\nДА ТЫ ХАКЕР !!!???";
    else if (mark == 5 && avgTime <= 3)
      text += "\r\n\r\nПРОСТО КРАСАВА !!!!!!!!!";

    text += "\r\n\r\n";
    for (Pair<Exercise, String> result : engine.incorrectList)
      text += result.getKey().toExpression(true) + "       " + result.getValue() + " - не правильно\r\n";

    text += "\r\nСтатистика по категориям:\r\n";
    for (Stat stat : engine.userStat.values())
      text += stat.accuracyPercentage() + "  -  " + CategoryLabels.labels.get(stat.category) + "\r\n";

    int result = 100 * engine.exCorrect / engine.exNumber;
    if (result == 100)
    {
      text += "\r\nАбсолютная правильность!!! Супер бонус - все очки [" + engine.score + "] х2 ";
      engine.score *= 2;
    }
    else if (result >= 90)
    {
      int bonus = engine.score / 4;
      engine.score += bonus;
      text += "\r\n Правильность 90% или более, бонус +25%: +" + bonus;
    }

    scoreLbl.setText("Очки: " + engine.score);
    resultTA.setText(text);
    resultTA.setVisible(true);
  }

  private void sleepMs(long ms)
  {
    try
    {
      long sleepBy = System.currentTimeMillis() + ms;
      while (isAlive && System.currentTimeMillis() < sleepBy)    //noinspection BusyWait
        Thread.sleep(5);
    }
    catch (InterruptedException e)
    {
    }
  }

  private String format(long num)
  {
    String str = "0" + num;
    return str.substring(str.length() - 2);
  }

  private int parse(String str)
  {
    try
    {
      return Integer.parseInt(str);
    }
    catch (NumberFormatException e)
    {
      return 0;
    }
  }

  private int textWidth(Font font, String text)
  {
    AffineTransform affinetransform = new AffineTransform();
    FontRenderContext frc = new FontRenderContext(affinetransform, true, true);
    return (int) (font.getStringBounds(text, frc).getWidth());
  }
}