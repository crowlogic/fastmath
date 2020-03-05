/*
 * Decompiled with CFR 0.144.
 */
package fastmath.matfile;

import static java.lang.System.out;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import fastmath.AbstractBufferedObject;
import fastmath.BufferUtils;
import fastmath.DoubleColMatrix;
import fastmath.DoubleMatrix;

public class MatFile implements
                     Iterable<MiElement>
{
  protected static final int HEADER_OFFSET = 128;
  public static final short BIG_ENDIAN = 18765;
  public static final short LITTLE_ENDIAN = 19785;
  public static final short READ_MODE = 1;
  private static final int SIXTY_FOUR_BITS = 8;
  public static final short VERSION = 256;
  public static final short WRITE_MODE = 2;
  private transient FileChannel fileChannel;
  private Header header;
  private transient File file;
  private boolean readOnly;
  private final TreeSet<String> writtenNames = new TreeSet<>();
  private int elementsWritten = 0;
  private transient File backupFile;

  public MatFile()
  {

  }

  public static void main(String[] args) throws IOException
  {
    MatFile matFile = new MatFile(new File(args[0]));
    System.out.println("Parsing " + matFile);
    matFile.forEach(x -> System.out.println(x));
  }

  public String toString()
  {
    return String.format("MatFile[file=%s]", this.file);
  }

  public static long pad(SeekableByteChannel channel) throws IOException
  {
    long skip = 8L - channel.position() % 8L;
    if (skip < 8L)
    {
      channel.write(ByteBuffer.allocateDirect((int) skip));
    }
    return channel.position();
  }

  public static long pad(long pos)
  {
    int skip = (int) pos % 8;
    return pos + (long) (skip > 0 ? 8 - skip : 0);
  }

  /**
   * Open an existing file
   * @param file
   */
  public MatFile(File file)
  {
    try
    {
      if ( !file.canRead() )
      {
        throw new NoSuchFileException(file.getAbsolutePath());
      }
      RandomAccessFile raf = new RandomAccessFile(file,
                                                  "rw");
      this.file = file;
      this.setFileChannel(raf.getChannel());
      this.header = new Header(this.getFileChannel());
      this.readOnly = false;
    }
    catch (IOException e)
    {
      throw new RuntimeException(file.getAbsolutePath() + ": " + e.getMessage(),
                                 e);
    }
  }

  public MatFile(final File file, String headerText) throws IOException
  {
    if (file.exists())
    {
      this.backupFile = new File(file.getAbsolutePath() + ".bak");
      out.println( "Renaming " + file.getAbsolutePath() + " to " + backupFile.getAbsolutePath() );
      file.renameTo(this.backupFile);
//			Runtime.getRuntime().addShutdownHook(new Thread() {
//
//				@Override
//				public void run() {
//					if (MatFile.this.backupFile != null) {
//						System.err.println("Restoring " + MatFile.this.backupFile + " to " + file);
//						file.delete();
//						MatFile.this.backupFile.renameTo(file);
//					}
//				}
//			});
    }
    RandomAccessFile raf = new RandomAccessFile(file,
                                                "rw");
    this.file = file;
    raf.setLength(0L);
    this.setFileChannel(raf.getChannel());
    this.header = new Header(this.getFileChannel(),
                             headerText);
    this.readOnly = false;
  }

  public MatFile(String filename)
  {
    this(new File(filename));
  }

  public boolean close()
  {
    try
    {
      this.getFileChannel().close();
      this.backupFile = null;
    }
    catch (IOException e)
    {
      throw new RuntimeException(this.file.getName() + ": " + e.getMessage(),
                                 e);
    }
    return true;
  }

  public FileChannel getFileChannel()
  {
    return this.fileChannel;
  }

  public Header getHeader()
  {
    return this.header;
  }

  public MiIterator iterator()
  {
    try
    {
      long fileSize = this.getFileChannel().size();
      if (fileSize < 128L)
      {
        throw new IOException(this.file.getName() + " is not a .mat file");
      }
      MappedByteBuffer buffer = this.getFileChannel().map(FileChannel.MapMode.READ_WRITE, 128L, fileSize - 128L);
      return new MiIterator(this,
                            buffer);
    }
    catch (Exception e)
    {
      throw new IllegalArgumentException("unable to read file header: " + e.getMessage(),
                                         e);
    }
  }

  public Map<String, MiMatrix> readVariables()
  {
    HashMap<String, MiMatrix> vars = new HashMap<String, MiMatrix>();
    for (AbstractBufferedObject obj : this)
    {
      if (obj instanceof MiMatrix)
      {
        MiMatrix matrix = (MiMatrix) obj;
        vars.put(matrix.getName(), matrix);
        continue;
      }
      throw new UnsupportedOperationException("Invalid element " + obj.getClass().getName());
    }
    return vars;
  }

  public Map<String, MiMatrix> readVariables(String regularExpression)
  {
    Pattern pattern = Pattern.compile(regularExpression);
    HashMap<String, MiMatrix> vars = new HashMap<String, MiMatrix>();
    for (AbstractBufferedObject obj : this)
    {
      if (obj instanceof MiMatrix)
      {
        MiMatrix matrix = (MiMatrix) obj;
        String variableName = matrix.getName();
        if (!pattern.matcher(variableName).matches())
          continue;
        vars.put(variableName, matrix);
        continue;
      }
      throw new UnsupportedOperationException("Invalid element " + obj.getClass().getName());
    }
    return vars;
  }

  public void setFileChannel(FileChannel fileChannel)
  {
    this.fileChannel = fileChannel;
  }

  public void setHeader(Header header)
  {
    this.header = header;
  }

  public synchronized void write(MiElement element) throws IOException
  {
    if (element == null)
    {
      return;
    }
    element.write(this.fileChannel);
    ++this.elementsWritten;
  }

  public static void write(String filename, MiElement... writables)
  {
    File file = new File(filename);
    try
    {
      MatFile.write(file, writables);
    }
    catch (IOException e)
    {
      throw new RuntimeException(e.getMessage(),
                                 e);
    }
  }

  public static void write(File file, MiElement... writables) throws IOException
  {
    MatFile matFile = new MatFile(file,
                                  MatFile.class.getName());
    for (MiElement writable : writables)
    {
      matFile.write(writable);
    }
    matFile.close();
  }

  public TreeSet<String> getWrittenNames()
  {
    return this.writtenNames;
  }

  public File getFile()
  {
    return this.file;
  }

  public static DoubleColMatrix loadMatrix(String filename, String variableName) throws IOException
  {
    MatFile matFile = new MatFile(new File(filename));
    MiMatrix vars = matFile.readVariables(variableName).get(variableName);
    DoubleColMatrix doubleColMatrix = vars != null ? (DoubleColMatrix) vars.toDenseDoubleMatrix() : null;
    return doubleColMatrix;
  }

  @SuppressWarnings("unchecked")
  public <S extends MiElement> Stream<S> stream()
  {
    return (Stream<S>) StreamSupport.stream(this.spliterator(), false);
  }

  public Stream<DoubleMatrix> matrixStream()
  {
    Stream<MiElement> variables = this.stream();
    variables = variables.filter(x -> x instanceof MiMatrix);
    Stream<DoubleMatrix> matrices = variables.map(x -> ((MiMatrix) x).toDenseDoubleMatrix());
    return matrices;
  }

  public int getElementsWritten()
  {
    return this.elementsWritten;
  }

  public Map<String, DoubleColMatrix> matrixMap() throws IOException
  {
    HashMap<String, DoubleColMatrix> map = new HashMap<String, DoubleColMatrix>();
    this.loadMatrices().forEach(matrix -> map.put(matrix.getName(), (DoubleColMatrix) matrix));
    return map;
  }

  public Map<String, DoubleColMatrix> matrixMap(Predicate<String> predicate)
  {
    TreeMap<String, DoubleColMatrix> map = new TreeMap<String, DoubleColMatrix>();
    this.loadMatrices().forEach(matrix ->
    {
      if (predicate.test(matrix.getName()))
      {
        map.put(matrix.getName(), (DoubleColMatrix) matrix);
      }
    });
    return map;
  }

  public static Map<String, DoubleColMatrix> matrixMap(File file) throws IOException
  {
    TreeMap<String, DoubleColMatrix> map = new TreeMap<String, DoubleColMatrix>();
    MatFile.loadMatrices(file).forEach(matrix -> map.put(matrix.getName(), (DoubleColMatrix) matrix));
    return map;
  }

  public static List<DoubleColMatrix> loadMatrices(File file) throws IOException
  {
    return new MatFile(file).loadMatrices();
  }

  public static List<DoubleColMatrix> loadMatrices(String filename) throws IOException
  {
    return new MatFile(new File(filename)).loadMatrices();
  }

  public List<DoubleColMatrix> loadMatrices()
  {
    try
    {
      Stream<DoubleColMatrix> matrixStream = this.readVariables()
                                                 .values()
                                                 .stream()
                                                 .map(miMatrix -> (DoubleColMatrix) miMatrix.toDenseDoubleMatrix());
      List<DoubleColMatrix> list = matrixStream.collect(Collectors.toList());
      return list;
    }
    finally
    {
      this.close();
    }
  }

  public static class Header extends
                             AbstractBufferedObject
  {
    private static final long serialVersionUID = 1L;
    public static final int HEADER_TEXT_LEN = 116;
    public static final int SUBSYS_SPECIFIC_HEADER_LEN = 8;
    public static final int HEADER_LEN = 128;

    public Header()
    {
      super(128);
    }

    public Header(ByteBuffer byteBuffer)
    {
      super(byteBuffer);
    }

    public Header(ByteBuffer buffer, String header)
    {
      this(buffer);
      BufferUtils.copy(header, buffer);
    }

    public Header(FileChannel filechannel, String header) throws IOException
    {
      this(header);
      filechannel.write(this.buffer);
    }

    public Header(FileChannel filechannel) throws IOException
    {
      this();
      filechannel.read(this.buffer);
    }

    public Header(String header)
    {
      this();
      int i;
      for (i = 0; i < header.length() && i < 116; ++i)
      {
        this.buffer.put((byte) header.charAt(i));
      }
      while (i < 116)
      {
        this.buffer.put((byte) 32);
        ++i;
      }
      for (i = 0; i < 8; ++i)
      {
        this.buffer.put((byte) 0);
      }
      this.buffer.putShort((short) 256);
      this.buffer.putShort((short) 19785);
      this.buffer.flip();
    }

    @Override
    public ByteBuffer getBuffer()
    {
      return this.buffer;
    }
  }

}
