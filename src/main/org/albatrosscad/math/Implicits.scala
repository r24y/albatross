package org.albatrosscad.math

import breeze.linalg._ 
import scala.math._

object Implicits{
  class FancyVec(val v:DenseVector[Double]){
    def magnitude = sqrt(v dot v)
  }

  implicit def tuple3ToVec(t:(Double,Double,Double)) = new DenseVector[Double](Array(t._1,t._2,t._3))

  implicit def denseVecToVec(v:DenseVector[Double]) = new {
    def magnitude = sqrt(v dot v)
    def normalize = v / sqrt(v dot v)
    def withX(x:Double):DenseVector[Double] = (x,v(1),v(2))
    def withY(y:Double):DenseVector[Double] = (v(0),y,v(2))
    def withZ(z:Double):DenseVector[Double] = (v(0),v(1),z)
  }

  lazy val Origin:DenseVector[Double] = (0.0,0.0,0.0)
}
