import breeze.linalg._
import org.albatrosscad.math.Implicits._

object Main {
  def main(args:Array[String]) = {
    println(new DenseVector(args.map(_.toDouble)).magnitude)
  }
}
