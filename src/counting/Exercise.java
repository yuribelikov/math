package counting;

public class Exercise
{
  int x;
  int y;
  char o;
  int r;

  public Exercise()
  {
    char[] opers = {'+', '-', 'x', ':'};
    int i = (int) (opers.length * Math.random());
    o = opers[i];
  }

  void eval()
  {
    switch (o)
    {
      case '+':
        r = x + y;
        break;
      case '-':
        r = x - y;
        break;
      case 'x':
        r = x * y;
        break;
      case ':':
        r = x / y;
        break;
    }
  }

  @Override
  public String toString()
  {
    return x + " " + o + (y < 0 ? " (" : " ") + y + (y < 0 ? ")" : "") + " = ";
  }
}
