package bel.math.data;

import static bel.math.types.Category.*;
import static bel.math.types.Category.INT_DIV_NEG_IN_100;
import static bel.math.types.Operation.*;

public class ExerciseInt extends Exercise
{
  private int x;
  private int y;
  private int ir;


  protected void generate()
  {
    int limit = 1000;
    while (limit-- > 0)
    {
      if (category == INT_ADD_IN_10 || category == INT_SUB_IN_10)
      {
        set(90, 10);
        if (ir / 10 == x / 10 && ir >= 0)
          break;
      }
      else if (category == INT_ADD_OVER_10 || category == INT_SUB_OVER_10)
      {
        set(90, 10);
        if (ir / 10 != x / 10 || ir < 0)
          break;
      }
      else if (category == INT_ADD_IN_100 || category == INT_SUB_IN_100)
      {
        set(90, 90);
        if (ir < 110)
          break;
      }
      else if (category == INT_ADD_NEG_IN_10 || category == INT_SUB_NEG_IN_10)
      {
        set(90, 10, true);
        if (ir / 10 == x / 10)
          break;
      }
      else if (category == INT_ADD_NEG_OVER_10 || category == INT_SUB_NEG_OVER_10)
      {
        set(90, 10, true);
        if (ir / 10 != x / 10)
          break;
      }
      else if (category == INT_ADD_NEG_IN_100 || category == INT_SUB_NEG_IN_100)
      {
        set(90, 90, true);
        if (Math.abs(x) > 10 && Math.abs(y) > 10 && Math.abs(ir) < 110)
          break;
      }
      else if (category == INT_MUL_IN_100)
      {
        set(10, 10);
        break;
      }
      else if (category == INT_DIV_IN_100)
      {
        y = 1 + rnd(9);
        x = rnd(10) * y;
        eval();
        break;
      }
      else if (category == INT_MUL_NEG_IN_100)
      {
        set(10, 10, true);
        break;
      }
      else if (category == INT_DIV_NEG_IN_100)
      {
        y = 1 + rnd(9);
        x = rnd(10) * y;
        makeNg();
        eval();
        break;
      }
    }

    r = "" + ir;
    super.generate();
  }

  private void makeNg()
  {
    x *= ng(0.5);
    y *= ng(0.5);
    if (x >= 0 && y >= 0)
      y *= -1;
  }

  private void set(int maxX, int maxY, boolean... ng)
  {
    x = rnd(maxX);
    y = rnd(maxY);
    if (ng != null && ng.length > 0 && ng[0])
      makeNg();

    eval();
  }

  private void eval()
  {
    if (o == ADD)
      ir = x + y;
    else if (o == SUB)
      ir = x - y;
    else if (o == MUL)
      ir = x * y;
    else if (o == DIV)
      ir = x / y;
  }

  private int rnd(int max)
  {
    return (int) Math.round(max * Math.random());
  }

  public String toExpression(boolean... wr)
  {
    return x + " " + operations.get(o) + (y < 0 ? " (" : " ") + y + (y < 0 ? ")" : "") + " = " + ((wr != null && wr.length > 0 && wr[0]) ? ir : "");
  }

}
