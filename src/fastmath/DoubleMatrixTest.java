
package fastmath;

import fastmath.exceptions.IllegalValueError;
import fastmath.exceptions.SingularFactorException;
import junit.framework.TestCase;

public class DoubleMatrixTest extends
                              TestCase
{
  public void testAppendColumn()
  {
    DoubleColMatrix a = new DoubleColMatrix(new double[][]
    {
        { 2.0, -1.0, 0.0 },
        { -1.0, 2.0, -1.0 },
        { 0.0, -1.0, 2.0 } });
    Vector newCol = a.appendColumn();
    newCol.assign(9.0, 4.0, 0.0);
    DoubleColMatrix b = new DoubleColMatrix(new double[][]
    {
        { 2.0, -1.0, 0.0, 9.0 },
        { -1.0, 2.0, -1.0, 4.0 },
        { 0.0, -1.0, 2.0, 0.0 } });
    DoubleMatrixTest.assertEquals((Object) b, (Object) a);
  }

  public void testSum()
  {
    DoubleColMatrix a = new DoubleColMatrix(new double[][]
    {
        { 1.0, 2.0, 3.0, 4.0, 5.0 },
        { 6.0, 7.0, 8.0, 9.0, 10.0 } });
    Vector asum = a.sum();
    DoubleMatrixTest.assertEquals((Object) asum, (Object) new Vector(new double[]
    { 7.0, 9.0, 11.0, 13.0, 15.0 }));
  }

  public void testColMatrix()
  {
    DoubleColMatrix matrix = new DoubleColMatrix(2,
                                                 2);
    matrix.set(0, 0, 1.0);
    matrix.set(0, 1, 2.0);
    matrix.set(1, 0, 3.0);
    matrix.set(1, 1, 4.0);
    Vector diag = matrix.diag();
    DoubleMatrixTest.assertEquals((Object) 5.0, (Object) diag.sum());
  }

  public void testAsVector()
  {
    DoubleColMatrix a = new DoubleColMatrix(new double[][]
    {
        { 1.0, 2.0, 3.0, 4.0, 5.0 },
        { 6.0, 7.0, 8.0, 9.0, 10.0 } });
    Vector avec = a.asVector();
    DoubleMatrixTest.assertEquals((Object) avec, (Object) new Vector(new double[]
    { 1.0, 6.0, 2.0, 7.0, 3.0, 8.0, 4.0, 9.0, 5.0, 10.0 }));
  }

  public void testLdivideSquare() throws SingularFactorException, IllegalValueError
  {
    DoubleColMatrix a = new DoubleColMatrix(new double[][]
    {
        { 2.0, -1.0, 0.0 },
        { -1.0, 2.0, -1.0 },
        { 0.0, -1.0, 2.0 } });
    DoubleColMatrix b = new DoubleColMatrix(new double[][]
    {
        { 1.0, 2.0 },
        { 3.0, 4.0 },
        { 5.0, 6.0 } });
    DoubleColMatrix eq = new DoubleColMatrix(new double[][]
    {
        { 3.5, 5.0 },
        { 6.0, 8.0 },
        { 5.5, 7.0 } });
    DoubleMatrix res = a.ldivide(b);
    DoubleMatrixTest.assertTrue((boolean) res.equals(eq, Math.pow(0.1, 10.0)));
  }

  public void testLdivide() throws SingularFactorException, IllegalValueError
  {
    DoubleColMatrix a = new DoubleColMatrix(new double[][]
    {
        { 1.0, 4.0 },
        { 2.0, 5.0 },
        { 3.0, 6.0 } });
    DoubleColMatrix b = new DoubleColMatrix(new double[][]
    {
        { 7.0, 10.0 },
        { 8.0, 11.0 },
        { 9.0, 12.0 } });
    DoubleColMatrix eq = new DoubleColMatrix(new double[][]
    {
        { -1.0, -2.0 },
        { 2.0, 3.0 } });
    DoubleMatrix res = a.ldivide(b);
    DoubleMatrixTest.assertTrue((boolean) res.equals(eq, Math.pow(0.1, 14.0)));
  }

  public void testProd()
  {
    DoubleColMatrix b = new DoubleColMatrix(new double[][]
    {
        { 7.0, 10.0 },
        { 8.0, 11.0 },
        { 9.0, 12.0 } });
    DoubleColMatrix a = new DoubleColMatrix(new double[][]
    {
        { 2.0, -1.0, 0.0 },
        { -1.0, 2.0, -1.0 },
        { 0.0, -1.0, 2.0 } });
    DoubleColMatrix c = a.prod(b);
    DoubleColMatrix shouldBe = new DoubleColMatrix(new double[][]
    {
        { 6.0, 9.0 },
        { 0.0, 0.0 },
        { 10.0, 13.0 } });
    DoubleMatrixTest.assertEquals((Object) shouldBe, (Object) c);
  }

  public void testProdTransA()
  {
    DoubleColMatrix b = new DoubleColMatrix(new double[][]
    {
        { 7.0, 10.0 },
        { 8.0, 11.0 },
        { 9.0, 12.0 } });
    DoubleColMatrix a = new DoubleColMatrix(new double[][]
    {
        { 2.0, -1.0, 0.0 },
        { -1.0, 2.0, -1.0 },
        { 0.0, -1.0, 2.0 } });
    DoubleColMatrix c = a.trans().prod(b);
    DoubleColMatrix shouldBe = new DoubleColMatrix(new double[][]
    {
        { 6.0, 9.0 },
        { 0.0, 0.0 },
        { 10.0, 13.0 } });
    DoubleMatrixTest.assertEquals((Object) shouldBe, (Object) c);
  }

  public void testProdTransB()
  {
    DoubleColMatrix b = new DoubleColMatrix(new double[][]
    {
        { 7.0, 8.0, 9.0 },
        { 10.0, 11.0, 12.0 } });
    DoubleColMatrix a = new DoubleColMatrix(new double[][]
    {
        { 2.0, -1.0, 0.0 },
        { -1.0, 2.0, -1.0 },
        { 0.0, -1.0, 2.0 } });
    DoubleColMatrix c = a.prod(b.trans());
    DoubleColMatrix shouldBe = new DoubleColMatrix(new double[][]
    {
        { 6.0, 9.0 },
        { 0.0, 0.0 },
        { 10.0, 13.0 } });
    DoubleMatrixTest.assertEquals((Object) shouldBe, (Object) c);
  }

  public void testDoubleRowMatrixAppend()
  {
    DoubleRowMatrix a = new DoubleRowMatrix(0,
                                            5);
    Vector newRow = a.appendRow();
    newRow.set(2, 6.9);
    DoubleMatrixTest.assertEquals((Object) 6.9, (Object) a.get(0, 2));
  }

  public void testResizeSmaller()
  {
    DoubleColMatrix x = new DoubleColMatrix(new double[][]
    {
        { 1.0, 2.0, 3.0, 4.0 },
        { 5.0, 6.0, 7.0, 8.0 },
        { 9.0, 10.0, 11.0, 12.0 },
        { 13.0, 14.0, 15.0, 16.0 } });
    x.resize(2, 2);
    System.out.println(x);
  }

  public void testResizeLarger()
  {
    DoubleColMatrix x = new DoubleColMatrix(new double[][]
    {
        { 1.0, 2.0, 3.0, 4.0 },
        { 5.0, 6.0, 7.0, 8.0 },
        { 9.0, 10.0, 11.0, 12.0 },
        { 13.0, 14.0, 15.0, 16.0 } });
    x.resize(5, 5);
    System.out.println(x);
  }

  public void testRowsAndColumnsFromOffset()
  {
    for (int numRows = 1; numRows < 10; ++numRows)
    {
      for (int numCols = 1; numCols < 10; ++numCols)
      {
        DoubleColMatrix dcm = new DoubleColMatrix(numRows,
                                                  numCols);
        for (int i = 0; i < dcm.numRows; ++i)
        {
          for (int j = 0; j < dcm.numCols; ++j)
          {
            int offset = dcm.getOffset(i, j) / 8;
            int col = dcm.getOffsetCol(offset);
            int row = dcm.getOffsetRow(offset);
            DoubleMatrixTest.assertEquals((String) Integer.toString(offset), (int) i, (int) col);
            DoubleMatrixTest.assertEquals((String) Integer.toString(offset), (int) j, (int) row);
          }
        }
      }
    }
  }
}
