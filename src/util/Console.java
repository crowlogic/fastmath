/*
 * Decompiled with CFR 0.144.
 */
package util;

public class Console
{
  public static void println(Object str)
  {
    System.out.println(str);
  }

  public static void printf(String format, Object... args)
  {
    System.out.format(format, args);
  }
}
