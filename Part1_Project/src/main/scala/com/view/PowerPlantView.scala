package com.view
import com.models.{EnergyData, EnergyDataView, Issue}
import com.utils.DataStorage

import scala.io.StdIn
import java.time.{LocalDateTime, ZoneId, ZoneOffset}
import java.time.format.DateTimeFormatter
import java.time.Instant
import java.time.LocalDateTime

class PowerPlantView {
  // Converts hour to LocalDateTime
  def hourToLocalDateTime(hour: Long): LocalDateTime = {
    LocalDateTime.ofEpochSecond(hour / 1000, 0, ZoneOffset.UTC)
  }

  // Displays energy data search results
  def showSearchResults(energyData: List[EnergyData]): Unit = {
    energyData.foreach { data =>
      val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
      val instant = Instant.ofEpochMilli(data.hour)
      val dateTime = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"))
      val formattedDateTime = dateTime.format(formatter)

      println(s"Equipment ID: ${data.equipmentId}, Hour: $formattedDateTime, Energy Type: ${data.energyType}, Energy: ${data.energy}, Equipment Status: ${data.equipmentStatus}")
    }
  }

  // Displays energy data
  def showEnergyData(energyData: List[EnergyData]): Unit = {
    energyData.foreach { data =>
      val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
      val instant = Instant.ofEpochMilli(data.hour)
      val dateTime = LocalDateTime.ofInstant(instant, ZoneId.of("UTC"))
      val formattedDateTime = dateTime.format(formatter)

      println(s"Equipment ID: ${data.equipmentId}, Hour: $formattedDateTime, Energy Type: ${data.energyType}, Energy: ${data.energy}, Equipment Status: ${data.equipmentStatus}")
    }
  }


  // Adds new energy view data
  def addEnergyViewData(energyData: List[EnergyDataView], newData: EnergyDataView): List[EnergyDataView] = {
    energyData :+ newData
  }

  // Displays total energy generation by source
  def showEnergyGeneration(energyData: List[EnergyData]): Unit = {
    val totalOutputBySource = energyData.groupBy(_.energyType).mapValues(data => data.map(_.energy).sum)
    totalOutputBySource.foreach { case (source, totalOutput) =>
      println(s"Total energy for $source: $totalOutput")
    }
  }

  // Displays the menu
  def showMenu(): Unit = {
    println("\nWelcome to the Renewable Energy System. Please select an option:")
    println("1. Show all energy data")
    println("2. Add new energy data")
    println("3. Remove energy data")
    println("4. Search energy data")
    println("5. Show total energy generation by source")
  }

  // Handles user input for menu selection
  def handleUserInput(energyData: List[EnergyData], energyDataView: List[EnergyDataView]): (List[EnergyData], List[EnergyDataView]) = {
    val userInput = StdIn.readInt()
    userInput match {
      case 1 =>
        showAllEnergyData(energyDataView)
        (energyData, energyDataView)
      case 2 =>
        println("Enter equipment id, date, energy type, energy amount and status (separated by commas)")
        val dataInput = StdIn.readLine().split(",")
        val newData = EnergyDataView(dataInput(0), dataInput(1), dataInput(2), dataInput(3).toDouble, dataInput(4))
        val updatedviewData = addEnergyViewData(energyDataView, newData)
        DataStorage.storeViewData(updatedviewData, "src/main/scala/com/energy_data_view.csv")
        (energyData, updatedviewData)
      case 3 =>
        println("Enter equipment id, hour, energy type, energy amount and status of the data you want to remove")
        val dataInput = StdIn.readLine().split(",")
        val updatedviewData = removeEnergyData(energyData, dataInput(0), dataInput(1))
        DataStorage.storeData(updatedviewData, "src/main/scala/com/energy_data_view.csv")
        (updatedviewData, energyDataView)
      case 4 =>
        println("Enter keyword to search")
        val keyword = StdIn.readLine()
        searchEnergyData(energyDataView, keyword)
        (energyData, energyDataView)
      case 5 =>
        showTotalEnergyGenerationBySource(energyData)
        (energyData, energyDataView)
      case _ =>
        println("Invalid input. Please enter a number from 1 to 5.")
        (energyData, energyDataView)
    }
  }

  // Filters energy data by date part (Month, Day, Hour)
  def filterEnergyDataByDate(energyData: List[EnergyData], datePart: Char, range: String): List[EnergyData] = {
    datePart match {
      case 'M' => energyData.filter(data => hourToLocalDateTime(data.hour).format(DateTimeFormatter.ofPattern("MM")) == range)
      case 'D' => energyData.filter(data => hourToLocalDateTime(data.hour).format(DateTimeFormatter.ofPattern("dd")) == range)
      case 'H' => energyData.filter(data => hourToLocalDateTime(data.hour).format(DateTimeFormatter.ofPattern("HH")) == range)
      case _ => energyData
    }
  }

  // Displays all energy data
  def showAllEnergyData(energyData: List[EnergyDataView]): Unit = {
    energyData.foreach(data => println(s"Equipment ID: ${data.equipmentId}, Hour: ${data.hour}, Energy Type: ${data.energyType}, Energy: ${data.energy}, Equipment Status: ${data.equipmentStatus}"))
  }

  // Adds new energy data
  def addEnergyData(energyData: List[EnergyDataView], newData: EnergyDataView): List[EnergyDataView] = {
    energyData :+ newData
  }

  // Removes energy data
  def removeEnergyData(energyData: List[EnergyData], equipmentId: String, hour: String): List[EnergyData] = {
    energyData.filterNot(data => data.equipmentId == equipmentId && data.hour == hour)
  }

  // Searches for energy data
  def searchEnergyData(energyData: List[EnergyDataView], keyword: String): Unit = {
    val results = energyData.filter(data => data.energyType.contains(keyword) || data.equipmentId.contains(keyword))
    showAllEnergyData(results)
  }

  // Displays total energy generation by source
  def showTotalEnergyGenerationBySource(energyData: List[EnergyData]): Unit = {
    val totalOutputBySource = energyData.groupBy(_.energyType).mapValues(data => data.map(_.energy).sum)
    totalOutputBySource.foreach { case (source, totalOutput) =>
      println(s"Total energy for $source: $totalOutput")
    }
  }
}
