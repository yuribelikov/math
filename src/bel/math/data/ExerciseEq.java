package bel.math.data;

import bel.math.types.Operation;

import static bel.math.types.Operation.*;

public class ExerciseEq extends Exercise
{
  private int a;
  private int b;
  private int c;
  private int d;
  private int x;
  private Type type;

  enum Type
  {
    axb0, axbc, bcax, axbcdx;
  }


  protected void generate()
  {
    type = Type.values()[(int) (Math.random() * Type.values().length)];
//    type = Type.axbcdx;
    System.out.println("type: " + type);
    System.out.println("o: " + o);

    a = (1 + rnd(5)) * ng(0.2);
    x = rnd(5) * ng(0.4);
    c = rnd(10) * ng(0.2);

    if (type == Type.axb0)  // ax + b = 0   ->  3x + 6 = 0
    {
      c = 0;
      b = a * x * (o == ADD ? -1 : 1);
    }
    else if (type == Type.axbc)      // ax + b = c  ->  3x + 5 = 17
    {
      if (o == ADD)   // ax + b = c
        b = c - a * x;
      else                  // ax - b = c
        b = a * x - c;
    }
    else if (type == Type.bcax)   // b = c + ax
    {
      b = c + a * x * (o == ADD ? 1 : -1);
    }
    else if (type == Type.axbcdx)   // ax + b = c + dx
    {
      d = (1 + rnd(3)) * ng(0.5);
      if (o == ADD)   // ax + b = c + dx
        b = c + d * x - a * x;
      else                  // ax - b = c + dx
        b = a * x - c - d * x;
    }
    else
      throw new RuntimeException("wrong eq type");

    System.out.println("a: " + a);
    System.out.println("x: " + x);
    System.out.println("b: " + b);
    System.out.println("c: " + c);
    System.out.println("d: " + d);

    r = "" + x;
    super.generate();
  }

  private int rnd(int max)
  {
    return (int) Math.round(max * Math.random());
  }

  public String toExpression(boolean... wr)
  {
    String expr;
    if (type == Type.bcax)   // b = c + ax
      expr = b + " = " + c + " " + operations.get(o) + (a < 0 ? " (" : " ") + printAX() + (a < 0 ? ")" : "") + "   X = ";
    else    // ax + b = c + dx
      expr = printAX() + operations.get(o) + (b < 0 ? " (" : " ") + b + (b < 0 ? ")" : "") + " = " + c + (d != 0 ? printDX() : "") + "   X = ";

    return expr + ((wr != null && wr.length > 0 && wr[0]) ? x : "");
  }

  private String printAX()
  {
    if (a == 1)
      return "X ";
    else if (a == -1)
      return "-X ";
    else
      return a + "X ";
  }

  private String printDX()
  {
    if (d == 1)
      return " + X ";
    else if (d == -1)
      return " - X ";
    else if (d > 0)
      return " + " + d + "X ";
    else
      return " - " + Math.abs(d) + "X ";
  }

}
