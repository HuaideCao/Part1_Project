package com.utils

import com.models.EnergyData

// DataAnalysis object provides functions for basic statistical analysis of the energy data.
object DataAnalysis {

  // mean function calculates the average energy output from a list of EnergyData.
  //We use Option to handle errors when calculating the average.
  // If outputs.length is greater than 0, use Some to wrap the result as Some(sum / length), indicating that there is a valid average.
  // If outputs.length is 0, indicating that there are no records in the file, it returns None, indicating an invalid average.
  def mean(data: List[EnergyData]): Option[Double] = {
    val outputs = data.map(_.energy)
    val sum = outputs.sum
    val length = outputs.length

    if (length > 0) {
      Some(sum / length)
    } else {
      None
    }
  }


  // median function calculates the middle value of energy output from a sorted list of EnergyData.
  // If the length of the list is even, it takes the average of the two middle values.
  def median(data: List[EnergyData]): Double = {
    val sortedOutputs = data.map(_.energy).sorted
    if (sortedOutputs.length % 2 == 0) {
      (sortedOutputs(sortedOutputs.length / 2 - 1) + sortedOutputs(sortedOutputs.length / 2)) / 2.0
    } else {
      sortedOutputs(sortedOutputs.length / 2)
    }
  }

  // mode function finds the most frequently occurring energy output in a list of EnergyData.
  def mode(data: List[EnergyData]): Double = {
    val outputCounts = data.groupBy(_.energy).mapValues(_.length)
    outputCounts.maxBy(_._2)._1
  }

  // range function calculates the difference between the maximum and minimum energy output in a list of EnergyData.
  def range(data: List[EnergyData]): Double = {
    val outputs = data.map(_.energy)
    outputs.max - outputs.min
  }

  // midrange function calculates the average of the maximum and minimum energy output in a list of EnergyData.
  def midrange(data: List[EnergyData]): Double = {
    val outputs = data.map(_.energy)
    (outputs.min + outputs.max) / 2.0
  }
}
