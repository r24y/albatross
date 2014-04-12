package org.albatrosscad.desktop

import com.jogamp.opengl.util._

import org.albatrosscad._
import org.albatrosscad.atom._
import org.albatrosscad.graphics.Implicits._

import javax.media.opengl._
import javax.media.opengl.glu._
import javax.media.opengl.awt.GLCanvas

import javax.swing._
import java.awt.{List=>_, _}
import java.awt.event._

import scala.math._

class View3D extends JPanel with GLEventListener with Draggable{
  val glp = GLProfile.getDefault
  val caps = new GLCapabilities(glp)
  val canvas = new GLCanvas(caps)
  val timer = new Timer(50, new ActionListener {
    def actionPerformed(e:ActionEvent):Unit = canvas.display()
  })

  var theta = 0f
  var phi = 0f
  val halfEdge:QubicBezierHalfEdge[Double] = HalfEdge.bezier(List(Vector(-1.0,-1.0,-1.0),
                                 Vector(-1.0, 1.0,-1.0),
                                 Vector( 1.0, 1.0, 1.0),
                                 Vector(-1.0, 1.0, 1.0))).asInstanceOf[QubicBezierHalfEdge[Double]]

  // Send our canvas's mouse events to be handled by `Draggable`.
  attachMouseEvents(canvas)

  // Send GL events to ourself.
  canvas.addGLEventListener(this)


  // Layout stuff.
  setLayout(new BorderLayout)
  add(canvas, BorderLayout.CENTER)

  // Set the minimum size properly; if this isn't set then
  // the split pane can't handle the resizing.
  setMinimumSize(new Dimension(1,1))

  def display(drawable:GLAutoDrawable):Unit = {
    update()
    render(drawable)
  }

  private def update():Unit = {
  }

  def positionUpdated(theta:Double, phi:Double):Unit = {
    this.theta = theta.asInstanceOf[Float]
    this.phi = phi.asInstanceOf[Float]
    canvas.display()
  }

  private def render(drawable:GLAutoDrawable):Unit = {
    val gl:GL2 = drawable.getGL().getGL2()
    val ones = scala.List(-1,1)

    def foo(f:((Int,Int)=>Unit)):Unit = {
      ones.foreach{a:Int =>
          ones.foreach{b:Int =>
              f(a,b)
          }
      }
    }

    gl.glClearColor(LAF.primaryColor._1/255f, LAF.primaryColor._2/255f, LAF.primaryColor._3/255f, 1f)
    gl.glClear(GL.GL_COLOR_BUFFER_BIT)

    val glu = new GLU

    // Change to projection matrix.
    gl.glMatrixMode(fixedfunc.GLMatrixFunc.GL_PROJECTION);
    gl.glLoadIdentity();

    // Perspective.
    val widthHeightRatio = (canvas.getWidth.asInstanceOf[Float]/canvas.getHeight.asInstanceOf[Float])
    glu.gluPerspective(45, widthHeightRatio, 1, 1000);
    glu.gluLookAt(0, 0, 4, 0, 0, 0, 0, 1, 0);

    // Change back to model view matrix.
    gl.glMatrixMode(fixedfunc.GLMatrixFunc.GL_MODELVIEW);
    gl.glLoadIdentity();

    gl.glRotatef(phi.asInstanceOf[Float],1,0,0)
    gl.glRotatef(theta.asInstanceOf[Float],0,1,0)

    gl.glBegin(GL.GL_LINES)
    gl.glColor3f(1,1,1)
    foo {(x:Int,y:Int) =>
        gl.glVertex3f(x,y,1)
        gl.glVertex3f(x,y,-1)
    }
    foo {(x:Int,y:Int) =>
        gl.glVertex3f(1,x,y)
        gl.glVertex3f(-1,x,y)
    }
    foo {(x:Int,y:Int) =>
        gl.glVertex3f(x,1,y)
        gl.glVertex3f(x,-1,y)
    }
    halfEdge.render(gl)
    gl.glEnd()


  }

  def init(drawable:GLAutoDrawable):Unit = {
    drawable.getGL.setSwapInterval(1)
  }

  def dispose(drawable:GLAutoDrawable):Unit = {
  }

  def reshape(drawable:GLAutoDrawable, x:Int, y:Int, width:Int, height:Int):Unit = {
  }

  timer.start()
  canvas.display()
}
