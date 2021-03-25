package fastmath.visualization;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
//
//import com.jogamp.opengl.GL;
//import com.jogamp.opengl.GL2;
//import com.jogamp.opengl.GLAutoDrawable;
//import com.jogamp.opengl.GLCapabilities;
//import com.jogamp.opengl.GLEventListener;
//import com.jogamp.opengl.awt.GLJPanel;
//
//public class Sphere extends
//                    GLJPanel implements
//                    GLEventListener,
//                    KeyListener
//{
//
//  /**
//   * 
//   */
//  private static final long serialVersionUID = -7419599978736850207L;
//
//  private float rotateX, rotateY, rotateZ;
//
//  public static void main(String[] args)
//  {
//    JFrame window = new JFrame("Cube");
//    GLCapabilities caps = new GLCapabilities(null);
//    Sphere panel = new Sphere(caps);
//    window.setContentPane(panel);
//    window.pack();
//    window.setLocation(50, 50);
//    window.setResizable(false);
//    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    window.setVisible(true);
//    panel.requestFocusInWindow();
//  }
//
//  public Sphere(GLCapabilities capabilities)
//  {
//    // TODO Auto-generated constructor stub
//    super(capabilities);
//    setPreferredSize(new Dimension(500,
//                                   500));
//    addGLEventListener(this);
//    addKeyListener(this);
//    rotateX = 15;
//    rotateY = 15;
//    rotateZ = 0;
//  }
//
//  void drawSphere(double r, int lats, int longs, GL2 gl)
//  {
//    int i, j;
//    for (i = 0; i <= lats; i++)
//    {
//      double lat0 = Math.PI * (-0.5 + (double) (i - 1) / lats);
//      double z0 = Math.sin(lat0);
//      double zr0 = Math.cos(lat0);
//
//      double lat1 = Math.PI * (-0.5 + (double) i / lats);
//      double z1 = Math.sin(lat1);
//      double zr1 = Math.cos(lat1);
//
//      gl.glBegin(gl.GL_QUAD_STRIP);
//      for (j = 0; j <= longs; j++)
//      {
//        double lng = 2 * Math.PI * (double) (j - 1) / longs;
//        double x = Math.cos(lng);
//        double y = Math.sin(lng);
//
//        gl.glNormal3d(x * zr0, y * zr0, z0);
//        gl.glVertex3d(x * zr0, y * zr0, z0);
//        gl.glNormal3d(x * zr1, y * zr1, z1);
//        gl.glVertex3d(x * zr1, y * zr1, z1);
//      }
//      gl.glEnd();
//    }
//  }
//
//  void computeLocation(GL2 gl)
//  {
//    double x = 2 * Math.cos(0); // my x-, y-, and z-coordinates
//    double y = 2 * Math.sin(0);
//    double z = 20;
//    double d = Math.sqrt(x * x + y * y + z * z); // distance to origin
//
//    gl.glMatrixMode(gl.GL_PROJECTION); // Set projection parameters.
//    gl.glLoadIdentity();
//    gl.glFrustum(-d * 0.5, d * 0.5, -d * 0.5, d * 0.5, d - 1.1, d + 1.1);
//  }
//
//  public void display(GLAutoDrawable drawable)
//  {
//
//    GL2 gl = drawable.getGL().getGL2();
//    gl.glClearColor(0, 0, 0, 0);
//    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
//
//    gl.glMatrixMode(GL2.GL_PROJECTION);
//    gl.glLoadIdentity();
//    gl.glMatrixMode(GL2.GL_MODELVIEW);
//
//    gl.glLoadIdentity();
//
//    gl.glRotatef(rotateZ, 0, 0, 1);
//    gl.glRotatef(rotateY, 0, 1, 0);
//    gl.glRotatef(rotateX, 1, 0, 0);
//    gl.glColor3d(1.0, 0.0, 0.0);
//    drawSphere(1, 10, 10, gl);
//
//  }
//
//  public void init(GLAutoDrawable drawable)
//  {
//    // called when the panel is created
//    GL2 gl = drawable.getGL().getGL2();
//    computeLocation(gl);
//    gl.glEnable(GL.GL_DEPTH_TEST);
//  }
//
//  @Override
//  public void dispose(GLAutoDrawable drawable)
//  {
//
//  }
//
//  @Override
//  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
//  {
//
//  }
//
//  @Override
//  public void keyPressed(java.awt.event.KeyEvent e)
//  {
//    int key = e.getKeyCode();
//    if (key == KeyEvent.VK_LEFT)
//      rotateY -= 15;
//    else if (key == KeyEvent.VK_RIGHT)
//      rotateY += 15;
//    else if (key == KeyEvent.VK_DOWN)
//      rotateX += 15;
//    else if (key == KeyEvent.VK_UP)
//      rotateX -= 15;
//    else if (key == KeyEvent.VK_PAGE_UP)
//      rotateZ += 15;
//    else if (key == KeyEvent.VK_PAGE_DOWN)
//      rotateZ -= 15;
//    else if (key == KeyEvent.VK_HOME)
//      rotateX = rotateY = rotateZ = 0;
//    repaint();
//
//  }
//
//  @Override
//  public void keyReleased(java.awt.event.KeyEvent e)
//  {
//    // TODO Auto-generated method stub
//
//  }
//
//  @Override
//  public void keyTyped(java.awt.event.KeyEvent e)
//  {
//    // TODO Auto-generated method stub
//
//  }
//
//}