namespace IntroEF.Utils;

public static class PrintUtil
{
  public static void PrintTitle(string text = "", int length = 60, char character = '-')
  {
    int preLen = (length - (text.Length + 2)) / 2;
    int postLen = length - (preLen + text.Length + 2);
    Console.WriteLine($"{new string(character, preLen)} {text} {new string(character, postLen)}");
  }
}
