package org.albatrosscad.graphics

import org.albatrosscad.atom._

import javax.media.opengl._
import javax.media.opengl.glu._
import javax.media.opengl.awt.GLCanvas


object Implicits{

  class DrawableHalfEdge(h: QubicBezierHalfEdge[Double]){
    // Render this half-edge into a GL context.
    def render(gl:GL2, n:Int = 30):Unit = {
      // Define the stepping increment.
      val incr = 1.0/n

      // Evaluate the half-edge at its start point.
      var vx = h(0.0)

      gl.glBegin(GL.GL_LINE_STRIP)

      for(t <- 0.0 to 1.0 by incr){
        gl.glVertex3d(vx.x, vx.y, vx.z)
        vx = h(t)
        gl.glVertex3d(vx.x, vx.y, vx.z)
      }
      gl.glEnd()
    }
  }

  implicit def halfEdgeToDrawable(h: QubicBezierHalfEdge[Double]):DrawableHalfEdge = new DrawableHalfEdge(h)
}

