package org.albatrosscad.device

import org.albatrosscad.math._
import org.albatrosscad.math.Implicits._

import breeze.linalg._
import scala.math._

/** Represents a delta printer whose print head is moved by
 carriages sliding vertically along towers. */
class DeltaPrinter(
                   /** The printer's print bed. */
                   override val printBed:PrintBed,
                   /** The printer's extruders. */
                   override val extruders: List[Extruder],
                   /** The length of the printer's delta arms, in mm. 
                    In Marlin, this is equal to (DELTA_DIAGONAL_ROD).*/
                   val diagonalRod:Double,
                   /** The distance from the print bed center to each tower, in mm. 
                    In Marlin, this is equal to (DELTA_SMOOTH_ROD_OFFSET - DELTA_CARRIAGE_OFFSET) .*/
                   val towerDistance:Double,
                   /** The distance from the print head center to the arm attachment point, in mm. 
                    In Marlin, this is equal to (DELTA_EFFECTOR_OFFSET).*/
                   val effectorOffset: Double
                    ) extends Printer(printBed,extruders){

  /** Tower indices. */
  val Tower = new {val A=0; val B=1; val C=2}

  /** Calculate the carriage positions that would place the print head at the desired location. */
  def getCarriagePositionsForHeadAt(head:DenseVector[Double]): (Double,Double,Double) = {
    val headFlat = head.withZ(0)
    val A = (headFlat+effectorVector(Tower.A)) - tower(Tower.A,0) 
    val B = (headFlat+effectorVector(Tower.B)) - tower(Tower.B,0)
    val C = (headFlat+effectorVector(Tower.C)) - tower(Tower.C,0)
    val ha = head(Axis.Z) + sqrt(pow(diagonalRod,2)-pow(A.magnitude,2))
    val hb = head(Axis.Z) + sqrt(pow(diagonalRod,2)-pow(B.magnitude,2))
    val hc = head(Axis.Z) + sqrt(pow(diagonalRod,2)-pow(C.magnitude,2))
    (ha,hb,hc)
  }
  
  def getCarriagePositionsForHeadAt(a:Double,b:Double,c:Double):(Double,Double,Double) = getCarriagePositionsForHeadAt(new DenseVector[Double](Array(a,b,c)))

  /** Get a vector representing the given height on the given tower. */
  private def tower(n:Int, h:Double):DenseVector[Double] = n match {
    case Tower.A => (-towerDistance*DeltaPrinter.SIN_60,-towerDistance*DeltaPrinter.COS_60,h)
    case Tower.B => (towerDistance*DeltaPrinter.SIN_60,-towerDistance*DeltaPrinter.COS_60,h)
    case Tower.C => (0.0, towerDistance, h)
    case _ => throw new IllegalTowerException(n)
  }

  private def effectorVector(n:Int):DenseVector[Double] = n match {
    case Tower.A => (-effectorOffset*DeltaPrinter.SIN_60,-effectorOffset*DeltaPrinter.COS_60,0.0)
    case Tower.B => (effectorOffset*DeltaPrinter.SIN_60,-effectorOffset*DeltaPrinter.COS_60,0.0)
    case Tower.C => (0.0, effectorOffset, 0.0)
    case _ => throw new IllegalTowerException(n)
  }

  /**
   Calculate a print head position given the positions of the carriages.
   */
  def getHeadPositionForCarriagesAt(ha:Double,hb:Double,hc:Double): DenseVector[Double] = {
    // locations of carriages A, B, C
    val Jp1 = tower(Tower.A,ha) - effectorVector(Tower.A)
    val Jp2 = tower(Tower.B,hb) - effectorVector(Tower.B)
    val Jp3 = tower(Tower.C,hc) - effectorVector(Tower.C)

    val intersections = Sphere.intersection(
      new Sphere(Jp1, diagonalRod),
      new Sphere(Jp2, diagonalRod),
      new Sphere(Jp3, diagonalRod)
    )

    if(intersections.length==0) return (Double.NaN,Double.NaN,Double.NaN)
    if(intersections.length==1) return intersections(0)
    intersections(1)
  }

  def getHeadPositionForCarriagesAt(c:(Double,Double,Double)):DenseVector[Double] = c match {
    case (ha:Double,hb:Double,hc:Double) => getHeadPositionForCarriagesAt(ha,hb,hc)
  }

  def calibrate(diagonalRodRange:Double,
                diagonalRodStep: Double,
                towerDistanceRange: Double,
                towerDistanceStep: Double,
                effectorOffsetRange: Double,
                effectorOffsetStep: Double,
                heightsAndMeasuredPositions: List[((Double,Double,Double),DenseVector[Double])]) = {
                  var minSigma = 1e20
                  var bestPrinter = this
                  for(dRod <- (diagonalRod - diagonalRodRange/2) to (diagonalRod + diagonalRodRange/2) by diagonalRodStep){
                    for(tDis <- (towerDistance - towerDistanceRange/2) to (towerDistance + towerDistanceRange/2) by towerDistanceStep){
                      for(eOff <- (effectorOffset - effectorOffsetRange/2) to (effectorOffset + effectorOffsetRange/2) by effectorOffsetStep){
                        val printer = this.withDiagonalRodLength(dRod).withTowerDistance(tDis).withEffectorOffset(eOff)
                        val distances = heightsAndMeasuredPositions map { 
                          case ((ha:Double,hb:Double,hc:Double),v:DenseVector[Double]) => (v - printer.getHeadPositionForCarriagesAt(ha,hb,hc)).magnitude 
                        }
                        val sigma = distances.reduce((a,b)=>a+b)/heightsAndMeasuredPositions.length
                        if(sigma<minSigma){
                          minSigma = sigma
                          bestPrinter = printer
                        }
                      }
                    }
                  }
                  bestPrinter
  }

  /** Create a new DeltaPrinter with the same characteristics as this one but different arm length. */
  def withDiagonalRodLength(l:Double) = new DeltaPrinter(printBed, extruders, l, towerDistance, effectorOffset)
  /** Create a new DeltaPrinter with the same characteristics as this one but different tower distance. */
  def withTowerDistance(d:Double) = new DeltaPrinter(printBed, extruders, diagonalRod, d, effectorOffset)
  /** Create a new DeltaPrinter with the same characteristics as this one but different effector offset. */
  def withEffectorOffset(o:Double) = new DeltaPrinter(printBed, extruders, diagonalRod, towerDistance, o)
}

object DeltaPrinter{
  val SIN_60 = 0.8660254037844386
  val COS_60 = 0.5

  /** Create a new DeltaPrinter with default values.

   Values taken from default Marlin Delta config. */
  def default = new DeltaPrinter(null, null, 250, 175-18, 33)
}

class IllegalTowerException(n:Int) extends IllegalArgumentException(s"Tower index must be between 0 and 2, inclusive (found $n). Try using DeltaPrinter.Tower constants."){
}
