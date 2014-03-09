package org.albatrosscad.math

import org.albatrosscad.math.Implicits._

import breeze.linalg._
import scala.math._


case class Sphere(
  val center:DenseVector[Double], 
  val radius:Double
){
  def this(pt:(Double,Double,Double),r:Double) = this(new DenseVector[Double](Array(pt._1, pt._2, pt._3)), r)
  def contains(pt:DenseVector[Double]) = (pt - center).magnitude < radius
}

object Sphere{
  def intersection(s1:Sphere,s2:Sphere,s3:Sphere):List[DenseVector[Double]] = {
    val p1 = s1.center
    val p2 = s2.center
    val p3 = s3.center
    val e_hat_x = (p2 - p1).normalize
    val i = e_hat_x dot (p3 - p1)
    val e_hat_y = (p3 - p1 - e_hat_x*i).normalize
    val d = (p2 - p1).magnitude
    val j = e_hat_y dot (p3 - p1)
    val e_hat_z = cross(e_hat_x,e_hat_y)
    intersectInPlane(s1.radius,s2.radius,s3.radius,d,i,j) map { d:DenseVector[Double] =>
      p1 + e_hat_x*d(0) + e_hat_y*d(1) + e_hat_z*d(2)
    }
  }

  private def intersectInPlane(r1:Double,r2:Double,r3:Double,d:Double,i:Double,j:Double):List[DenseVector[Double]] = {
    if(abs(d-r2)>r1) return List()
    val x = (pow(r1,2) - pow(r2,2) + pow(d,2))/(2*d)
    val y = (pow(r1,2) - pow(r3,2) + pow(i,2) + pow(j,2))/(2*j) - (i*x)/j
    val det = pow(r1,2) - pow(x,2) - pow(y,2)
    if(det < 0) return List()
    if(det == 0) return List((x,y,0.0))
    val z = sqrt(det)
    List((x,y,z),(x,y,-z))
  }
}
