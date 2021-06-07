package fastmath.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipWriter
{
  public final int BUF_SIZE = 16384;
  ZipOutputStream zipOut;

  public ZipWriter(File file) throws FileNotFoundException
  {
    this.zipOut = new ZipOutputStream(new FileOutputStream(file));
  }

  public void close() throws IOException
  {
    this.zipOut.close();
  }

  public void add(String name, InputStream inputStream) throws IOException
  {
    this.zipOut.putNextEntry(new ZipEntry(name));
    BufferTools.fastChannelCopy(inputStream, this.zipOut);
  }

  /*
   * WARNING - Removed try catching itself - possible behaviour change.
   */
  public void addDir(String dirName) throws FileNotFoundException, IOException
  {
    File[] files;
    File dir = new File(dirName);
    for (File file : files = dir.listFiles(new FilenameFilter()
    {

      @Override
      public boolean accept(File dir, String name)
      {
        File file = new File(dir,
                             name);
        return file.isFile() && file.canRead();
      }
    }))
    {
      try (FileInputStream fis = new FileInputStream(file);)
      {
        this.add(file.getName(), fis);
      }
    }
  }

}
