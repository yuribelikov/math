package counting;

import java.util.Scanner;

public class SimpleCounting
{
  final static int COUNT = 50;
  final static boolean NEGATIVE = true;
  int correct = 0;


  public static void main(String[] args)
  {
    SimpleCounting p = new SimpleCounting();
    p.run();
    System.exit(0);
  }


  void run()
  {
    long started = System.currentTimeMillis();

    int count = 0;
    while (count++ < COUNT)
    {
      Exercise ex = generate();
      System.out.print(count + ")  " + ex);
      int a = read();
      if (a == ex.r)
        correct++;
      else
        System.out.println("    " + ex + ex.r);
    }

    long seconds = (System.currentTimeMillis() - started) / 1000;
    System.out.println("");
    System.out.println("закончили, правильно: " + correct + " из " + COUNT);
    System.out.println("время: " + seconds / 60 + " мин. " + seconds % 60 + " сек.");
    double avgTime = Math.round(10.0 * seconds / COUNT) / 10.0;
    String speedMark = "очень быстро";
    if (avgTime > 2)
      speedMark = "быстро";
    if (avgTime > 5)
      speedMark = "нормально";
    if (avgTime > 10)
      speedMark = "медленно";
    if (avgTime > 20)
      speedMark = "ну ты тормоз!";

    System.out.println();
    System.out.println("среднее время на пример: " + avgTime + " сек.   Cкорость: " + speedMark);
    System.out.println("");
    int mark = 5 * correct / COUNT;
    System.out.println("Оценка за правильность: " + mark);
    System.out.println("");
    if (mark == 5 && avgTime <= 2)
      System.out.println("ДА ТЫ ХАКЕР !!!???");
    else if (mark == 5 && avgTime <= 3)
      System.out.println("ПРОСТО КРАСАВА !!!!!!!!!");
  }

  int read()
  {
    Scanner in = new Scanner(System.in);

    try
    {
      return in.nextInt();
    }
    catch (Exception e)
    {
    }

    return Integer.MIN_VALUE;
  }

  Exercise generate()
  {
    Exercise ex = new Exercise();
    if (ex.o == '+')
    {
      ex.x = rnd(90);
      ex.y = rnd(10);
    }
    else if (ex.o == '-')
    {
      ex.x = 10 + rnd(90);
      ex.y = rnd(10);
    }
    else if (ex.o == 'x')
    {
      ex.x = rnd(10);
      ex.y = rnd(10);
    }
    else if (ex.o == ':')
    {
      ex.y = 1 + rnd(9);
      ex.x = rnd(10) * ex.y;
    }

    if (NEGATIVE)
    {
      ex.x = ex.x * (Math.random() > 0.5 ? -1 : 1);
      ex.y = ex.y * (Math.random() > 0.5 ? -1 : 1);
    }

    ex.eval();

    return ex;
  }

  int rnd(int max)
  {
    return (int) Math.round(max * Math.random());
  }
}
