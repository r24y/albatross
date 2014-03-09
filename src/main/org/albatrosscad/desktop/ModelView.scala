package org.albatrosscad.desktop

import com.sun.j3d.utils.universe.SimpleUniverse
import com.sun.j3d.utils.geometry.{Sphere, ColorCube}
import javax.media.j3d.{DirectionalLight, BoundingSphere, BranchGroup, Canvas3D}
import java.awt.GraphicsConfiguration
import java.awt.BorderLayout
import java.awt.Label
import javax.swing.JPanel
import com.sun.j3d.utils.applet.MainFrame
import javax.vecmath.{Vector3f, Point3d, Color3f}

/**
 * Created with IntelliJ IDEA.
 * User: ryan
 * Date: 2/17/14
 * Time: 7:58 PM
 * To change this template use File | Settings | File Templates.
 */
class ModelView extends JPanel {
  setLayout(new BorderLayout)
  val config = SimpleUniverse.getPreferredConfiguration
  val canvas = new Canvas3D(config)
  add("Center",canvas)
  val contents = new BranchGroup()
  val sphere = new Sphere(0.5f)
  contents.addChild(sphere)
  val light1Color = new Color3f(1.8f,0.1f,0.1f)
  val boundsph = new BoundingSphere(new Point3d(0.0,0.0,0.0),100.0)
  val lightDir = new Vector3f(4.0f,-7.0f,-12.0f)
  val light1 = new DirectionalLight(light1Color,lightDir)
  light1.setInfluencingBounds(boundsph)
  contents.addChild(light1)
  val universe = new SimpleUniverse(canvas)
  universe.getViewingPlatform.setNominalViewingTransform()
  universe.addBranchGraph(contents)
}
