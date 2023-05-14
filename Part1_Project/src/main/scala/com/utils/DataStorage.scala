package com.utils

import com.models.{EnergyData, EnergyDataView}

import java.time.format.DateTimeFormatter
import com.models.EnergyData

import scala.io.Source
import java.io.{BufferedWriter, File, FileWriter, PrintWriter}
import java.time.{LocalDateTime, ZoneOffset}

// The DataStorage object provides functions for loading and storing energy data to/from files.
object DataStorage {

  // loadData function reads data from a CSV file and returns a list of EnergyData.
  def loadData(csvFilename: String): List[EnergyData] = {
    val bufferedSource = io.Source.fromFile(csvFilename)
    val energyData = (for (line <- bufferedSource.getLines.drop(1)) yield {
      val cols = line.split(",").map(_.trim)
      EnergyData(cols(0), cols(1).toLong, cols(2), cols(3).toDouble, cols(4))
    }).toList
    bufferedSource.close
    energyData
  }

  // loadViewData function reads data from a file and returns a list of EnergyData.
  def loadViewData(filename: String): List[EnergyData] = {
    val source = Source.fromFile(filename)
    val data = source.getLines().drop(1).map { line =>
      val columns = line.split(",")
      val timestamp = columns(1).toLong
      val dateTime = LocalDateTime.ofEpochSecond(timestamp / 1000, 0, ZoneOffset.UTC)
      EnergyData(columns(0), dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")).toLong, columns(2), columns(3).toDouble, columns(4))
    }.toList
    source.close()
    data
  }

  // writeViewDataToCSV function writes a list of EnergyDataView to a CSV file.
  def writeViewDataToCSV(data: List[EnergyDataView], filename: String): Unit = {
    val file = new File(filename)
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write("equipmentId,hour,energyType,energy,equipmentStatus\n")
    for (record <- data) {
      bw.write(s"${record.equipmentId},${record.hour},${record.energyType},${record.energy},${record.equipmentStatus}\n")
    }
    bw.close()
  }

  // writeDataToCSV function writes a list of EnergyData to a CSV file.
  def writeDataToCSV(energyData: List[EnergyData], csvFilename: String): Unit = {
    val writer = new PrintWriter(new File(csvFilename))
    writer.write("equipment_id,hour,energy_type,energy,equipment_status\n")
    for (data <- energyData) {
      writer.write(s"${data.equipmentId},${data.hour},${data.energyType},${data.energy},${data.equipmentStatus}\n")
    }
    writer.close()
  }

  // readDataFromCSV function reads data from a CSV file and returns a list of EnergyData.
  def readDataFromCSV(csvFilename: String, isViewData: Boolean = false): List[EnergyData] = {
    if (isViewData) {
      loadViewData(csvFilename)
    } else {
      loadData(csvFilename)
    }
  }

  // readDataViewFromCSV function reads data from a CSV file and returns a list of EnergyDataView.
  def readDataViewFromCSV(filePath: String): List[EnergyDataView] = {
    val bufferedSource = io.Source.fromFile(filePath)
    val data = (for (line <- bufferedSource.getLines().drop(1) if line.nonEmpty) yield {
      val columns = line.split(",").map(_.trim)
      // assuming that the columns are in the order of equipmentId, hour, energyType, energy, equipmentStatus
      if (columns.length < 5) {
        println(s"Error: line '$line' does not contain enough columns")
        null
      } else {
        try {
          EnergyDataView(columns(0), columns(1), columns(2), columns(3).toDouble, columns(4))
        } catch {
          case e: NumberFormatException =>
            println(s"Error parsing line '$line'")
            null
        }
      }
    }).toList
    bufferedSource.close()
    data.filter(_ != null)
  }

  // storeData function writes a list of EnergyData to a CSV file.
  def storeData(energyData: List[EnergyData], csvFilename: String): Unit = {
    val writer = new PrintWriter(csvFilename)
    writer.println("equipment_id,hour,energy_type,energy,equipment_status")
    energyData.foreach(data => writer.println(s"${data.equipmentId},${data.hour},${data.energyType},${data.energy},${data.equipmentStatus}"))
    writer.close()
  }

  // storeViewData function writes a list of EnergyDataView to a file.
  def storeViewData(energyData: List[EnergyDataView], filePath: String): Unit = {
    val file = new File(filePath)
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write("equipmentId,date,energyType,energy,equipmentStatus\n")
    for (data <- energyData) {
      bw.write(s"${data.equipmentId},${data.hour},${data.energyType},${data.energy},${data.equipmentStatus}\n")
    }
    bw.close()
  }

  // storeviewData function writes a list of EnergyData to a file with timestamp as readable format.
  def storeviewData(energyData: List[EnergyData], filePath: String): Unit = {
    val writer = new BufferedWriter(new FileWriter(filePath))
    writer.write("equipmentId,hour,energyType,energy,equipmentStatus\n") // write header
    energyData.foreach { data =>
      val hourAsString = LocalDateTime.ofEpochSecond(data.hour / 1000, 0, ZoneOffset.UTC)
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
      writer.write(s"${data.equipmentId},$hourAsString,${data.energyType},${data.energy},${data.equipmentStatus}\n")
    }
    writer.close()
  }
}

