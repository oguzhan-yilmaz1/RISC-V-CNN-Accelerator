package examples

//import scala.collection.mutable.ArrayBuffer
import chisel3.iotesters.{ChiselFlatSpec, Driver, PeekPokeTester}
import chisel3._
import chisel3.util._

class FullyConnectedTests (c: FullyConnected) extends PeekPokeTester(c) {
	val vectorInputs1 = Vector(BigInt(5), BigInt(4), BigInt(4), BigInt(2), BigInt(15), BigInt(15))
	val vectorInputs2 = Vector(BigInt(4), BigInt(7), BigInt(2), BigInt(1), BigInt(5), BigInt(8))
	poke(c.io.in1, vectorInputs1)
	poke(c.io.in2, vectorInputs2)
	step(1)
	expect(c.io.out, 253)
	//expect(c.io.out, vectorInputs1)
}

class FullyConnectedTester extends ChiselFlatSpec {
  behavior of "FullyConnected"
  backends foreach {backend =>
    it should s"mutiply two matrices, fully-connected" in {
      Driver(() => new FullyConnected(6), backend)(c => new FullyConnectedTests(c)) should be (true)
    }
  }
}