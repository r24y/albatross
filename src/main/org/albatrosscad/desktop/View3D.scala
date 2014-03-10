package org.albatrosscad.desktop

import com.jogamp.opengl.util._

import javax.media.opengl._
import javax.media.opengl.glu._
import javax.media.opengl.awt.GLCanvas

import javax.swing._
import java.awt._
import java.awt.event._

import scala.math._

class View3D extends JPanel with GLEventListener{
  val glp = GLProfile.getDefault
  val caps = new GLCapabilities(glp)
  val canvas = new GLCanvas(caps)
  val timer = new Timer(50, new ActionListener {
    def actionPerformed(e:ActionEvent):Unit = canvas.display()
  })

  canvas.addGLEventListener(this)
  setLayout(new BorderLayout)
  add(canvas, BorderLayout.CENTER)
  setMinimumSize(new Dimension(1,1))

  def display(drawable:GLAutoDrawable):Unit = {
    update()
    render(drawable)
  }

  private def update():Unit = {
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

    gl.glClear(GL.GL_COLOR_BUFFER_BIT)

    /*
    gl.glBegin(GL.GL_TRIANGLES)
    gl.glColor3f(1, 0, 0)
    gl.glVertex2f(-0.5f,-0.5f)
    gl.glColor3f(0, 1, 0)
    gl.glVertex2f(1,-1)
    gl.glColor3f(0, 0, 1)
    gl.glVertex2f(-1,1)
    gl.glColor3f(1, 1, 1)
    gl.glVertex2f(0.5f,0.5f)
    gl.glColor3f(0, 1, 0)
    gl.glVertex2f(1,-1)
    gl.glColor3f(0, 0, 1)
    gl.glVertex2f(-1,1)
    gl.glEnd()
    */

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

    gl.glRotatef((System.currentTimeMillis/10%360).asInstanceOf[Float],0,1,0)

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
