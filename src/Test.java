import java.io.IOException;
import java.util.Scanner;

public class Test
{

  public static void main(String[] args)
  {
    System.out.println("start.......");

//    int x = 5;
//    System.out.println("x: " + x);
//    x = x + 7;
//
//    System.out.println("x: " + x);
//
//    int y = 3;
//    System.out.println("y: " + y);
//
//    int z = x * y;
//    System.out.println("z: " + z);

    Scanner in = new Scanner(System.in);
    int a = in.nextInt();
    System.out.println("a: " + a);
    int b = in.nextInt();
    System.out.println("b: " + b);

    int pr = a * b;
    System.out.println("pr: " + pr);

    if (a > b)
      System.out.println("наибольшее: " + a);
    else if (a < b)
      System.out.println("наибольшее: " + b);
    else
      System.out.println("числа равны: " + b);


    System.out.println("end.......");
  }

}
