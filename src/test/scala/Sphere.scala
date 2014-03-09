package org.albatrosscad.math

import org.albatrosscad.math._
import org.albatrosscad.math.Implicits._

import breeze.linalg._

import org.scalatest._

// NOTE: This code should check to make sure that three spheres
// with collinear centers are correctly computed. I'm leaving out
// the test case for now because I won't run into that case, but
// in the future I definitely want to see it added in.
class SphereSpec extends FlatSpec {
  val s = new Sphere((0.0,0.0,0.0),1)
  "Sphere [(0,0,0), 1]" should "contain (0.2,0.2,0.2)" in {
    assert(s contains (0.2,0.2,0.2))
  }
  it should "not contain (0,0,5)" in {
    assert(!(s contains (0.0,0.0,5.0)))
  }

  "Three intersecting spheres" should "intersect in two places" in {
    val s1 = new Sphere((0.0,0.0,0.0),1.0)
    val s2 = new Sphere((1.0,0.0,0.0),1.0)
    val s3 = new Sphere((0.0,1.0,0.0),1.0)
    val intersections = Sphere.intersection(s1,s2,s3)
    assert(intersections.length == 2)
  }
  "Three spheres with no common point" should "intersect in no places" in {
    val s1 = new Sphere((0.0,0.0,0.0),0.5)
    val s2 = new Sphere((1.0,0.0,0.0),0.5)
    val s3 = new Sphere((2.0,1.0,0.0),0.5)
    val intersections = Sphere.intersection(s1,s2,s3)
    intersections foreach {v:DenseVector[Double] => println(v(0)+","+v(1)+","+v(2))}
    assert(intersections.length == 0)
  }
}
