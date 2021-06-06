package arblib;

import static arblib.Constants.ARF_RND_DOWN;
import static arblib.arblib.*;
import static java.lang.Math.floor;
import static java.lang.Math.min;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.IntStream;

public class ComplexPlot
{
  static
  {
    System.loadLibrary("arblib");
  }

  int color_mode = 0;
  int prec = 256;

  double ax, ay, bx, by;

  ThreadLocal<double[]> R = new ThreadLocal<>()
  {

    @Override
    protected double[] initialValue()
    {
      return new double[1];
    }
  };

  ThreadLocal<double[]> G = new ThreadLocal<>()
  {

    @Override
    protected double[] initialValue()
    {
      return new double[1];
    }
  };

  ThreadLocal<double[]> B = new ThreadLocal<>()
  {

    @Override
    protected double[] initialValue()
    {
      return new double[1];
    }
  };

  int ynum = 2000;
  int xnum = 2000;

  arf_struct xa = new arf_struct();
  arf_struct xb = new arf_struct();
  arf_struct ya = new arf_struct();
  arf_struct yb = new arf_struct();

  public ComplexPlot()
  {

    ax = 13;
    ay = -3;
    bx = 55;
    by = 3;
    color_mode = 0;

    arf_init(xa);
    arf_init(xb);
    arf_init(ya);
    arf_init(yb);

    arf_set_d(xa, ax);
    arf_set_d(xb, bx);
    arf_set_d(ya, ay);
    arf_set_d(yb, by);
  }

  /**
   * tanh(ln(1+Z(t)^2))
   */
  static void Y(acb_struct res, acb_struct z, int prec)
  {
    acb_dirichlet_hardy_z(res, z, null, null, 1, prec);
    acb_pow_ui(res, res, 2, prec);
    acb_add_ui(res, res, 1, prec);
    acb_log(res, res, prec);
    acb_tanh(res, res, prec);
    arb_set_d(res.getImag(), 0);
  }

  public static void main(String args[]) throws IOException
  {
    ComplexPlot plotter = new ComplexPlot();
    plotter.plot();
  }

  private void plot() throws IOException
  {

    FileOutputStream fos = new FileOutputStream(new File("file.ppm"));
    PrintWriter pw = new PrintWriter(fos);
    pw.format("P6\n%d %d 255\n", xnum, ynum);
    pw.flush();

    IntStream.range(0, ynum).parallel().forEach(y ->
    {
      if (y % (ynum / 16) == 0)
        System.out.printf("row %d\n", y);

      for (int x = 0; x < xnum; x++)
      {
        acb_struct w = evaluateFunction(x, y);

        colorizeAndRecordPoint(x, y, w);
      }
    });

    for (int y = ynum - 1; y >= 0; y--)
    {

      for (int x = 0; x < xnum; x++)
      {
        writeToFile(x, y, fos);
      }
    }
    pw.close();
  }

  int grid[][] = new int[xnum][ynum];

  private void colorizeAndRecordPoint(int x, int y, acb_struct w)
  {
    double R[] = this.R.get();
    double G[] = this.G.get();
    double B[] = this.B.get();

    arblib.color_function(R, G, B, w, color_mode);

    int red = (int) min(255, floor(R[0] * 255));
    int green = (int) min(255, floor(G[0] * 255));
    int blue = (int) min(255, floor(B[0] * 255));
    grid[x][y] = red | (green << 8) | (blue << 16);
    // System.out.println( "grid[" + x + "][" + y + "]=" + grid[x][y] );
//    fos.write(red);
//    fos.write(green);
//    fos.write(blue);
  }

  private void writeToFile(int x, int y, FileOutputStream fos) throws IOException
  {
    int val = grid[x][y];
    int red = val & 255;
    int green = (val >> 8) & 255;
    int blue = (val >> 16) & 255;

    fos.write(red);
    fos.write(green);
    fos.write(blue);
  }

  ThreadLocal<acb_struct> z = new ThreadLocal<>()
  {

    @Override
    protected acb_struct initialValue()
    {
      acb_struct a = new acb_struct();
      acb_init(a);
      return a;
    }
  };
  ThreadLocal<acb_struct> w = new ThreadLocal<>()
  {

    @Override
    protected acb_struct initialValue()
    {
      acb_struct a = new acb_struct();
      acb_init(a);
      return a;
    }
  };

  private acb_struct evaluateFunction(long x, long y)
  {
    acb_struct z = this.z.get();
    acb_struct w = this.w.get();

    for (prec = 30; prec < 500; prec *= 2)
    {
      arf_struct zr = arb_midref(acb_realref(z));
      arf_struct zi = arb_midref(acb_imagref(z));
      arf_sub(zi, yb, ya, prec, ARF_RND_DOWN);
      arf_mul_ui(zi, zi, y, prec, ARF_RND_DOWN);
      arf_div_ui(zi, zi, ynum - 1, prec, ARF_RND_DOWN);
      arf_add(zi, zi, ya, prec, ARF_RND_DOWN);

      arf_sub(zr, xb, xa, prec, ARF_RND_DOWN);
      arf_mul_ui(zr, zr, x, prec, ARF_RND_DOWN);
      arf_div_ui(zr, zr, xnum - 1, prec, ARF_RND_DOWN);

      arf_add(zr, zr, xa, prec, ARF_RND_DOWN);

      Y(w, z, prec);

      if (acb_rel_accuracy_bits(w) > 4)
        break;

    }

    return w;
  }

  public static arf_struct arb_midref(arb_struct z)
  {
    return z.getMid();
  }

  public static arb_struct acb_realref(acb_struct z)
  {
    return z.getReal();
  }

  public static arb_struct acb_imagref(acb_struct z)
  {
    return z.getImag();
  }
}
