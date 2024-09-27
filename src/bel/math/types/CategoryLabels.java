package bel.math.types;

import java.util.HashMap;
import java.util.Map;

import static bel.math.types.Category.*;

public class CategoryLabels
{
  public final static Map<Category, String> labels = new HashMap<>();

  static
  {
    labels.put(INT_ADD_IN_10, "Сложение в пределах 10");
    labels.put(INT_ADD_OVER_10, "Сложение через 10");
    labels.put(INT_ADD_IN_100, "Сложение в пределах 100");
    labels.put(INT_ADD_NEG_IN_10, "Сложение отрицательных чисел в пределах 10");
    labels.put(INT_ADD_NEG_OVER_10, "Сложение отрицательных чисел через 10");
    labels.put(INT_ADD_NEG_IN_100, "Сложение отрицательных чисел в пределах 100");

    labels.put(INT_SUB_IN_10, "Вычитание в пределах 10");
    labels.put(INT_SUB_OVER_10, "Вычитание через 10");
    labels.put(INT_SUB_IN_100, "Вычитание в пределах 100");
    labels.put(INT_SUB_NEG_IN_10, "Вычитание отрицательных чисел в пределах 10");
    labels.put(INT_SUB_NEG_OVER_10, "Вычитание отрицательных чисел через 10");
    labels.put(INT_SUB_NEG_IN_100, "Вычитание отрицательных чисел в пределах 100");

    labels.put(INT_MUL_IN_100, "Умножение в пределах 100");
    labels.put(INT_DIV_IN_100, "Деление в пределах 100");
    labels.put(INT_MUL_NEG_IN_100, "Умножение отрицательных чисел в пределах 100");
    labels.put(INT_DIV_NEG_IN_100, "Деление отрицательных чисел в пределах 100");

    labels.put(DFR_ADD_S1, "Сложение десячных дробей с одним знаком");
    labels.put(DFR_ADD_S2, "Сложение десячных дробей с двумя знаками");
    labels.put(DFR_SUB_S1, "Вычитание десячных дробей с одним знаком");
    labels.put(DFR_SUB_S2, "Вычитание десячных дробей с двумя знаками");
    labels.put(DFR_MUL_S1, "Умножение десячных дробей с одним знаком");
    labels.put(DFR_DIV_S1, "Деление десячных дробей с одним знаком");

    labels.put(EQ1, "Простые уравнения");

  }
}
