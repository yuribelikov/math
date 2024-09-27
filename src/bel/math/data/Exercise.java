package bel.math.data;

import bel.math.types.Category;
import bel.math.types.Operation;

import java.util.HashMap;
import java.util.Map;

import static bel.math.types.Operation.*;

public class Exercise
{
  protected final static Map<Operation, Character> operations = new HashMap<>();

  static
  {
    operations.put(ADD, '+');
    operations.put(SUB, '-');
    operations.put(MUL, 'x');
    operations.put(DIV, ':');
  }

  public Category category;
  protected Operation o;
  public String r;


  public static Exercise create(Category category)
  {
    Exercise exercise;

    String type = category.name().substring(0, 3);
    if (type.equals("INT"))
      exercise = new ExerciseInt();
    else if (type.equals("DFR"))
      exercise = new ExerciseDecFrac();
    else if (type.equals("EQ1"))
      exercise = new ExerciseEq();
    else
      throw new RuntimeException("Unknown exercise type");

    exercise.category = category;
    if (category.name().length() >= 7)
      exercise.o = Operation.valueOf(category.name().substring(4, 7));
    else
      exercise.o = Math.random() > 0.5 ? ADD : SUB;

    exercise.generate();

    return exercise;
  }

  protected int ng(double prob)
  {
    return Math.random() < prob ? -1 : 1;
  }

  protected void generate()
  {
    System.out.println("generate, exercise: " + this);
  }

  public String toExpression(boolean... wr)
  {
    return "";
  }

  public int complexity()
  {
    String exp = toExpression().replaceAll(" ", "").replaceAll("=", "");
//    String exp = "13.0 : 5.0 = ".replaceAll(" ", "").replaceAll("=", "");
//    System.out.println(exp + " --> " + exp.length());
    int complexity = exp.length() / 2 + 2 * r.length() / 3 + (exp.startsWith("-") ? 1 : 0) + has(operations.get(MUL)) + 2 * has(operations.get(DIV)) + 3 * has('.') + (category.name().endsWith("S2") ? 5 : 0) + (category == Category.DFR_MUL_S1 ? 10 : 0) +
        (category == Category.EQ1 ? 8 : 0) + (exp.endsWith("X") ? 4 : 0);
    if (r.equals("0"))
      complexity /= 2;
    else if (r.endsWith("0"))
      complexity = 2 * complexity / 3;
    else if (!category.name().startsWith("DFR") && (exp.startsWith("0") || exp.endsWith("0")))
      complexity /= 2;

    return complexity / 2;
  }

  protected int has(Character c)
  {
    return toExpression().indexOf(c) != -1 ? 1 : 0;
  }

  @Override
  public String toString()
  {
    return "[" + category + " x" + complexity() + "]   " + toExpression();
  }
}
