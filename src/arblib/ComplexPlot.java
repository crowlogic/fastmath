package arblib;

import static arblib.Constants.ARF_RND_DOWN;
import static arblib.arblib.acb_add_ui;
import static arblib.arblib.acb_dirichlet_hardy_z;
import static arblib.arblib.acb_init;
import static arblib.arblib.acb_log;
import static arblib.arblib.acb_pow_ui;
import static arblib.arblib.acb_rel_accuracy_bits;
import static arblib.arblib.acb_tanh;
import static arblib.arblib.arb_get_str;
import static arblib.arblib.acb_get_str;
import static arblib.arblib.arf_add;
import static arblib.arblib.arf_div_ui;
import static arblib.arblib.arf_init;
import static arblib.arblib.arf_mul_ui;
import static arblib.arblib.arf_set_d;
import static arblib.arblib.arf_sub;
import static java.lang.Math.floor;
import static java.lang.Math.min;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class ComplexPlot
{
  static
  {
    System.loadLibrary("arblib");
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
  }

  public static void main(String args[]) throws IOException
  {
    int color_mode = 0;
    int prec = 256;
    long x, y, i;
    double ax, ay, bx, by;

    ax = 14;
    ay = 0;
    bx = 16;
    by = 2;
    color_mode = 0;

    int ynum = 512;
    int xnum = 512;

    acb_struct z = new acb_struct();
    acb_struct w = new acb_struct();
    arf_struct xa = new arf_struct();
    arf_struct xb = new arf_struct();
    arf_struct ya = new arf_struct();
    arf_struct yb = new arf_struct();

    acb_init(z);
    acb_init(w);

    arf_init(xa);
    arf_init(xb);
    arf_init(ya);
    arf_init(yb);

    arf_set_d(xa, ax);
    arf_set_d(xb, bx);
    arf_set_d(ya, ay);
    arf_set_d(yb, by);

    FileOutputStream fos = new FileOutputStream(new File("file.ppm"));
    PrintWriter pw = new PrintWriter(fos);
    pw.format("P6\n%d %d 255\n", xnum, ynum);
    pw.flush();

    for (y = ynum - 1; y >= 0; y--)
    {
      if (y % (ynum / 16) == 0)
        System.out.printf("row %d\n", y);

      for (x = 0; x < xnum; x++)
      {
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
          
          System.err.println("Result :" + acb_get_str(w, 20, 0));

        }

        double R[] = new double[1];
        double G[] = new double[1];
        double B[] = new double[1];

        arblib.color_function(R, G, B, z, color_mode);

        int red = (int) min(255, floor(R[0] * 255));
        int green = (int) min(255, floor(G[0] * 255));
        int blue = (int) min(255, floor(B[0] * 255));
        fos.write(red);
        fos.write(green);
        fos.write(blue);

        // put to file, 0 to 255 representing intensity
      }
    }
  }

  public static arf_struct arb_midref(arb_struct acb_imagref)
  {
    return acb_imagref.getMid();
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
