package bel.math.data;

import static bel.math.types.Operation.*;

public class ExerciseDecFrac extends Exercise
{
  private double x;
  private double y;
  private double dr;


  protected void generate()
  {
    boolean s2 = category.name().endsWith("S2");

    if (o == ADD || o == SUB)
    {
      double max = s2 ? 19.0 : 9.0;
      double dec = s2 ? 100.0 : 10.0;
      x = rnd(max, dec) * ng(0.2);
      if (s2 && Math.random() < 0.5)    // 15.34 + 8.3
        max = dec = 10.0;
      y = rnd(max, dec) * ng(0.2);
    }
    else if (o == MUL)
    {
      x = rnd(5.0, 10.0) * ng(0.25);
      y = rnd(3.0, 10.0) * ng(0.25);
    }
    else    // DIV
    {
      dr = rnd(3.0, 10.0) * ng(0.25);
      y = (1.0 + rnd(5.0, 1.0)) * ng(0.25);
      x = norm(dr * y);
      eval();
    }

    eval();
    r = "" + dr;
    if (dr == Math.round(dr))
      r = r.substring(0, r.indexOf('.'));

    super.generate();
  }

  public void eval()
  {
    if (o == ADD)
      dr = norm(x + y);
    else if (o == SUB)
      dr = norm(x - y);
    else if (o == MUL)
      dr = norm(x * y);
    else if (o == DIV)
      dr = norm(x / y);
  }

  private double rnd(double max, double dec)
  {
    return norm((Math.round(dec * max * Math.random())) / dec);
  }

  private double norm(double v)
  {
    return Math.round(v * 100.0) / 100.0;
  }

  public String toExpression(boolean... wr)
  {
    return x + " " + operations.get(o) + (y < 0 ? " (" : " ") + y + (y < 0 ? ")" : "") + " = " + ((wr != null && wr.length > 0 && wr[0]) ? dr : "");
  }

}
