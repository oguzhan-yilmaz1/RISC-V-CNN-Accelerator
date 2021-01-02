package examples

import chisel3._
import chisel3.util._
//import scala.collection.mutable.ArrayBuffer

// takes 2 vectors of equal length n, multiplies element-wise and adds the results up using n-1 adders
// elements of each vector are 4-bit integers. I tried using 8-bit, 16-bit, and 32-bit but even the 8-bit
// inputs take forever and give and error: throwable GC overhead limit exceeded

// param: n  -> size of the input vectors, change it accordingly in the tester file(FullyConnectedTests)
// and in Launcher.scala
class FullyConnected(val n:Int) extends Module{
	val io = IO(new Bundle {
    	val in1 = Input(Vec(n, UInt(4.W)))
    	val in2 = Input(Vec(n, UInt(4.W)))
   		val out = Output(UInt(8.W))
   	})
    val As   = Array.fill(n-1)(Module(new Adder(8)).io)   //n-1 adders
    val mulvals = Reg(Vec(n, UInt()))
    val mul1 = Array.fill(n)(Module(new Mul()))
    for (i <- 0 until n) {
    	mul1(i).io.x := io.in1(i)
    	mul1(i).io.y := io.in2(i)
    	mulvals(i) := mul1(i).io.z
    }
    As(0).A := mulvals(0)
    As(0).B := mulvals(1)
    for (j <- 0 until n-2) {
    	As(j+1).A := As(j).Sum
    	As(j+1).B := mulvals(j+2)
    }
    io.out := As(n-2).Sum
}