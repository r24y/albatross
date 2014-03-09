import com.jogamp.opengl.util._

import breeze.linalg._
import org.albatrosscad.math.Implicits._
import javax.media.opengl._
import javax.media.opengl.awt.GLCanvas

import javax.swing._
import java.awt.event._

import scala.math._


class SimpleScene extends GLEventListener {
  var theta:Float = 0
  var s:Float = 0
  var c:Float = 0

  def display(drawable:GLAutoDrawable):Unit = {
    update()
    render(drawable)
  }

  private def update():Unit = {
    theta += 0.01f
    s = sin(theta).asInstanceOf[Float]
    c = cos(theta).asInstanceOf[Float]
  }

  private def render(drawable:GLAutoDrawable):Unit = {
    val gl:GL2 = drawable.getGL().getGL2();

    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
    // draw a triangle filling the window
    gl.glBegin(GL.GL_TRIANGLES);
    gl.glColor3f(1, 0, 0);
    gl.glVertex2f(-c, -c);
    gl.glColor3f(0, 1, 0);
    gl.glVertex2f(0, c);
    gl.glColor3f(0, 0, 1);
    gl.glVertex2f(s, -s);
    gl.glEnd();
  }

  def init(drawable:GLAutoDrawable):Unit = {
    drawable.getGL.setSwapInterval(1)
  }

  def dispose(drawable:GLAutoDrawable):Unit = {
    // put your cleanup code here
  }

  def reshape(drawable:GLAutoDrawable, x:Int, y:Int, width:Int, height:Int):Unit = {
    // called when user resizes the window
  }
}

object Main {

  def main(args:Array[String]):Unit = {
    val glp = GLProfile.getDefault
    val caps = new GLCapabilities(glp)
    val canvas = new GLCanvas(caps)

    val frame = new JFrame("OpenGL Test")
    frame.setSize(300,300)
    frame.getContentPane.add(canvas)
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

    canvas.addGLEventListener(new SimpleScene)

    frame.setVisible(true)

    val timer = new Timer(50, new ActionListener {
      def actionPerformed(e:ActionEvent):Unit = canvas.display()
    })
    timer.start()
  }
}
