package bel.math.data;

import bel.math.types.Category;

public class Stat
{
  public final Category category;
  private int correct;
  private int total;
  private long updated;


  public Stat(String record)
  {
    String[] sa = record.split(",");
    category = Category.valueOf(sa[0]);
    correct = Integer.parseInt(sa[1]);
    total = Integer.parseInt(sa[2]);
    updated = Long.parseLong(sa[3]);
  }

  public void add(boolean correct)
  {
    total++;
    if (correct)
      this.correct++;

    updated = System.currentTimeMillis();
  }

  public double level()
  {
    return 0.7 * accuracy() + Math.min(0.3 * total / 500, 0.3) - 0.01 * age();
  }

  private int age()   // minutes
  {
    return (int) ((System.currentTimeMillis() - updated) / 60000);
  }

  private double accuracy()
  {
    return total > 0 ? (1.0 * correct / total) : 0;
  }

  public String accuracyPercentage()
  {
    return Math.round(100 * accuracy()) + "%";
  }

  public String toRecord()
  {
    return category.name() + "," + correct + "," + total + "," + updated;
  }

  @Override
  public String toString()
  {
    return correct + "/" + total + " => " + (Math.round(100 * accuracy()) / 100.0) + "  age=" + age() + "  ---> level=" + (Math.round(1000 * level()) / 1000.0) + "   category=" + category;
  }
}
