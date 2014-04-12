package org.albatrosscad.desktop

import java.awt._
import java.awt.event._

trait Draggable extends MouseListener with MouseMotionListener{

  var _theta = 0.0
  var _phi = 0.0
  var scale = 0.2f

  var dTheta = 0.0
  var dPhi = 0.0

  var dragStart = new Point(0,0)

  override def mousePressed(e: MouseEvent) = {
    dragStart = e.getPoint
  }
  override def mouseReleased(e: MouseEvent) = {
    _theta += dTheta
    _phi += dPhi
    dTheta = 0.0
    dPhi = 0.0
    positionUpdated(_theta, _phi)
  }
  override def mouseEntered(e: MouseEvent) = {}
  override def mouseExited(e: MouseEvent) = {}
  override def mouseClicked(e: MouseEvent) = {}
  override def mouseMoved(e: MouseEvent) = {}
  override def mouseDragged(e: MouseEvent) = {
    dTheta = (e.getX - dragStart.getX)*scale
    dPhi = (e.getY - dragStart.getY)*scale
    positionUpdated(_theta+dTheta, _phi+dPhi)
  }

  def attachMouseEvents(c:Component) = {
    c.addMouseListener(this)
    c.addMouseMotionListener(this)
  }

  def setDragScale(f:Float) = {this.scale = f}

  def positionUpdated(theta:Double, phi:Double):Unit
}
