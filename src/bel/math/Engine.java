package bel.math;

import bel.math.data.Exercise;
import bel.math.data.Stat;
import bel.math.types.Category;
import bel.math.types.State;
import javafx.util.Pair;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;

import static bel.math.types.State.*;

class Engine
{
  private final static Path USER_STAT_PATH = Paths.get("user.stat");

  State state = NEW;

  long started = 0;
  long totalTime = 0;

  int exNumber = 0;
  int exCount = 1;
  int exCorrect = 0;
  int score = 0;

  Map<Category, Stat> userStat = new LinkedHashMap<>();

  List<Pair<Exercise, String>> incorrectList = new ArrayList<>();

  Exercise exercise = null;


  public Engine()
  {
    loadUserStat();
  }

  void start(int exNumber)
  {
    backupUserStat();

    this.exNumber = exNumber;
    started = System.currentTimeMillis();
    state = IN_PROGRESS;
  }

  void pause()
  {
    totalTime += (System.currentTimeMillis() - started);
    started = 0;
    state = PAUSED;
  }

  void resume()
  {
    started = System.currentTimeMillis();
    state = IN_PROGRESS;
  }

  void finish()
  {
    totalTime += (System.currentTimeMillis() - started);
    started = 0;
    state = FINISHED;
  }

  boolean submit(String answer)
  {
    boolean correct = Double.parseDouble(exercise.r) == Double.parseDouble(answer);
    exCount++;
    if (correct)
    {
      exCorrect++;
      score += exercise.complexity();
    }
    else
      incorrectList.add(new Pair<>(exercise, answer));

    userStat.get(exercise.category).add(correct);
    saveUserStat();

    return correct;
  }

  private Category chooseWorstCategory()
  {
    Stat worst = null;
    for (Stat stat : userStat.values())
    {
      System.out.println("stat: " + stat);
      if (worst == null || stat.level() < worst.level())
        worst = stat;
    }

    System.out.println("worst stat: " + worst);
    assert worst != null;
//    return Category.EQ1;
    return worst.category;
  }

  void generate()
  {
    Category category = chooseWorstCategory();
    exercise = Exercise.create(category);
  }

  private void loadUserStat()
  {
    try
    {
      List<String> lines = Files.readAllLines(USER_STAT_PATH);
      for (String line : lines)
      {
        Stat stat = new Stat(line);
        userStat.put(stat.category, stat);
      }

    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private void saveUserStat()
  {
    try
    {
      StringBuilder text = new StringBuilder();
      for (Stat stat : userStat.values())
        text.append(stat.toRecord()).append(System.lineSeparator());

      Files.write(USER_STAT_PATH, text.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.WRITE);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private void backupUserStat()
  {
    try
    {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      Path backup = Paths.get("user.stat.backup." + sdf.format(new Date()));
      Files.copy(USER_STAT_PATH, backup);
    }
    catch (IOException ignored)
    {
    }
  }
}