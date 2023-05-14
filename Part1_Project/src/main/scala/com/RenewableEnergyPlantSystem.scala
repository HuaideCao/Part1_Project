package com

// Import necessary models, utilities, and services
import models.{EnergyData, EnergyDataView, HydroPower, SolarPanel, WindTurbine}
import view.PowerPlantView
import utils.{AlertGenerator, DataAnalysis, DataFilter, DataSearcher, DataSorter, DataStorage}
import services.IssueDetection

import java.time.format.DateTimeFormatter
import scala.io.StdIn
import scala.io.StdIn.readLine

// This is the main object for the renewable energy plant system
object RenewableEnergyPlantSystem {
  // Main method
  def main(args: Array[String]): Unit = {
    // Create instances of renewable energy sources.
    // Read data from CSV file and filter according to the energy type
    val energyData = DataStorage.readDataFromCSV("src/main/scala/com/energy_data.csv")
    val solarData = energyData.filter(_.energyType == "solar")
    val windData = energyData.filter(_.energyType == "wind")
    val hydroData = energyData.filter(_.energyType == "hydro")

    // Create instances of each energy source with corresponding data
    val solarPanel = new SolarPanel(solarData)
    val windTurbine = new WindTurbine(windData)
    val hydroPower = new HydroPower(hydroData)

    // Create a PowerPlantView instance
    val powerPlantView = new PowerPlantView()

    // Start running the system
    var running = true
    while (running) {
      // Display options to user
      println("Welcome to the Renewable Energy System. Please select an option:")
      println("1. Check the status of energy sources")
      println("2. Create new data")
      println("3. View energy data")
      println("4. Analyze energy data")
      println("5. Detect and handle issues")
      println("6. View")
      println("0. Exit")
      print("Enter your option:")

      // Wait for user input
      val option = readLine()
      // Handle user input
      option match {
        // Option 1: Check the status of energy sources
        case "1" =>
          val energyData = List(solarPanel, windTurbine, hydroPower).flatMap(_.getEnergyData)
          powerPlantView.showEnergyGeneration(energyData)

        // Option 2: Create new data
        case "2" =>
          println("Enter the equipment ID, hour in timestamp, energy type, energy, and equipment status (separated by commas):")
          val input = readLine().split(",")
          val newEnergyData = EnergyData(input(0), input(1).toLong, input(2), input(3).toDouble, input(4))
          val energyData = List(solarPanel, windTurbine, hydroPower).flatMap(_.getEnergyData) :+ newEnergyData
          println("Enter the filename to store the energy data:")
          val filename = readLine()
          DataStorage.storeData(energyData, filename)

        // Option 3: View energy data
        case "3" =>
          println("Enter the filename to load the energy data:")
          val filename = readLine()
          val energyData = DataStorage.loadData(filename)
          powerPlantView.showEnergyData(energyData)

        // Option 4: Analyze energy data
        case "4" =>
          var continue = true
          println("Enter the filename to load the energy data:")
          val filename = readLine()
          val energyData = DataStorage.loadData(filename)
          while (continue) {
            println("Please select an option for analyze:")
            println("1. Filter by energy type")
            println("2. Sort by date")
            println("3. Search data")
            println("4. Calculate statistical metrics")
            println("5. Show data by month")
            println("6. Show data by day")
            println("7. Show data by hour")
            println("0. Exit")
            print("Enter your option:")
            val option = readLine().toInt
            option match {
              // Filter data by energy type
              case 1 =>
                println("Enter the energy type to filter data:")
                val source = readLine()
                val filteredData = DataFilter.filterBySource(energyData, source)
                powerPlantView.showEnergyData(filteredData)

              // Sort data by date
              case 2 =>
                val sortedData = DataSorter.sortByHour(energyData)
                powerPlantView.showEnergyData(sortedData)

              // Search data
              case 3 =>
                println("Enter the data id you want to search for:")
                val searchData = readLine()
                val searchResults = DataSearcher.searchData(energyData, searchData)
                powerPlantView.showSearchResults(searchResults)

              // Calculate statistical metrics
              case 4 =>
                val meanValue = DataAnalysis.mean(energyData).getOrElse(0.0)
                //If the Option returned by the mean function is Some, the value will be extracted;
                // if it is None, the default value 0.0 will be used.
                val medianValue = DataAnalysis.median(energyData)
                val modeValue = DataAnalysis.mode(energyData)
                val rangeValue = DataAnalysis.range(energyData)
                val midrangeValue = DataAnalysis.midrange(energyData)
                println(s"The mean value is: $meanValue")
                println(s"The median value is: $medianValue")
                println(s"The mode value is: $modeValue")
                println(s"The range value is: $rangeValue")
                println(s"The midrange value is: $midrangeValue")

              // Show data by month
              case 5 =>
                println("Enter month to show (MM):")
                val month = readLine()
                val monthData = powerPlantView.filterEnergyDataByDate(energyData, 'M', month)
                powerPlantView.showEnergyData(monthData)

              // Show data by day
              case 6 =>
                println("Enter day to show (dd):")
                val day = readLine()
                val dayData = powerPlantView.filterEnergyDataByDate(energyData, 'D', day)
                powerPlantView.showEnergyData(dayData)

              // Show data by hour
              case 7 =>
                println("Enter hour to show (HH):")
                val hour = readLine()
                val hourData = powerPlantView.filterEnergyDataByDate(energyData, 'H', hour)
                powerPlantView.showEnergyData(hourData)

              // Exit the menu
              case 0 =>
                continue = false

              // Invalid option
              case _ =>
                println("Invalid option")
            }
          }

        // Option 5: Detect and handle issues
        case "5" =>
          val energyData = DataStorage.readDataFromCSV("src/main/scala/com/energy_data.csv")
          val solarData = energyData.filter(_.energyType == "solar")
          val windData = energyData.filter(_.energyType == "wind")
          val hydroData = energyData.filter(_.energyType == "hydro")
          val solarPanel = new SolarPanel(solarData)
          val windTurbine = new WindTurbine(windData)
          val hydroPower = new HydroPower(hydroData)

          // Check for any issues in the power plants
          val issues = List(solarPanel, windTurbine, hydroPower).flatMap(_.checkIssues())
          // If there are no issues, print a message
          if (issues.isEmpty) {
            println("No issues detected.")
          } else { // If there are issues, print them and generate alerts for each issue
            println("Detected issues:")
            issues.foreach(issue => {
              println(issue.description)
              // You will need to implement the alert generation logic
              println(AlertGenerator.generateAlert(issue))
            })
          }

        // Option 6: View
        case "6" =>
          val powerPlantView = new PowerPlantView()

          // Read data from CSV files
          val energyData = DataStorage.readDataFromCSV("src/main/scala/com/energy_data.csv")
          val energyDataView = DataStorage.readDataViewFromCSV("src/main/scala/com/energy_data_view.csv")
          val originalData = DataStorage.loadData("src/main/scala/com/energy_data.csv")

          // Convert data to a viewable format
          val convertedData = originalData.map(data => EnergyDataView(data.equipmentId, powerPlantView.hourToLocalDateTime(data.hour).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), data.energyType, data.energy, data.equipmentStatus))
          // Store the converted data to a CSV file
          DataStorage.writeViewDataToCSV(convertedData, "src/main/scala/com/energy_data_view.csv")

          var currentData = (energyData, energyDataView)
          var continueLoop = true
          while (continueLoop) {
            // Show the menu and handle user input
            powerPlantView.showMenu()
            currentData = powerPlantView.handleUserInput(currentData._1, currentData._2)
            // Ask the user whether they want to continue
            continueLoop = StdIn.readLine("Do you want to continue? (yes/no)").toLowerCase != "no"
          }

        // Option 0: Exit the system
        case "0" =>
          running = false

        // If the input does not match any options, print an error message
        case _ =>
          println("Invalid option. Please enter a number between 1 and 6.")
      }
    }
  }
}

