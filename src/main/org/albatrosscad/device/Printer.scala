package org.albatrosscad.device

/**
 * Represents a particular printer's characteristics.
 *
 * For example, this could represent a delta robot with 0.5-meter towers
 * and a 0.25-meter circular print bed, or a Mendel with 0.3-meter Z-travel
 * and a 0.3x0.3-meter bed.
 */
abstract class Printer (val printBed:PrintBed,val extruders: List[Extruder]) {
  /** Constants for easily referring to axes. */
  def Axis = new {
    val X = 0;
    val Y = 1;
    val Z = 2;
  }
}
