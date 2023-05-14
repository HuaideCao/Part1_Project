package com.models

// RenewableEnergySource is a trait (interface) that any renewable energy source must implement.
// It ensures that any class that extends this trait will have getEnergyData and checkIssues methods.
trait RenewableEnergySource {
  // Method to get the energy data associated with a particular renewable energy source.
  def getEnergyData: List[EnergyData]

  // Method to check for issues with the renewable energy source.
  // For example, it can check if the energy output is too low or if the equipment is inoperational.
  def checkIssues(): List[Issue]
}


// SolarPanel is a class that represents a solar panel. It extends the RenewableEnergySource trait,
// so it must implement the getEnergyData and checkIssues methods.
class SolarPanel(energyData: List[EnergyData]) extends RenewableEnergySource {

  // Method to get the energy data associated with the solar panel.
  // It simply returns the list of energy data that was provided when the SolarPanel object was created.
  override def getEnergyData: List[EnergyData] = energyData

  // Method to check for issues with the solar panel.
  // It checks each piece of energy data for issues (like low energy output or inoperational status),
  // and returns a list of all the issues it finds.
  override def checkIssues(): List[Issue] = {
    energyData.flatMap { data =>
      var issues = List[Issue]()
      if (data.energy < 160.0) {
        issues = Issue(data.equipmentId, "Low energy output", data.hour, data.energy, data.equipmentStatus) :: issues
      }
      if (data.equipmentStatus == "inoperational") {
        issues = Issue(data.equipmentId, "Equipment is inoperational", data.hour, data.energy, data.equipmentStatus) :: issues
      }
      issues
    }
  }
}

// Similar to the SolarPanel class, but represents a wind turbine.
class WindTurbine(energyData: List[EnergyData]) extends RenewableEnergySource {

  override def getEnergyData: List[EnergyData] = energyData

  override def checkIssues(): List[Issue] = {
    energyData.flatMap { data =>
      var issues = List[Issue]()
      if (data.energy < 160.0) {
        issues = Issue(data.equipmentId, "Low energy output", data.hour, data.energy, data.equipmentStatus) :: issues
      }
      if (data.equipmentStatus == "inoperational") {
        issues = Issue(data.equipmentId, "Equipment is inoperational", data.hour, data.energy, data.equipmentStatus) :: issues
      }
      issues
    }
  }
}

// Similar to the SolarPanel and WindTurbine classes, but represents a hydropower plant.
class HydroPower(energyData: List[EnergyData]) extends RenewableEnergySource {

  override def getEnergyData: List[EnergyData] = energyData

  override def checkIssues(): List[Issue] = {
    energyData.flatMap { data =>
      var issues = List[Issue]()
      if (data.energy < 160.0) {
        issues = Issue(data.equipmentId, "Low energy output", data.hour, data.energy, data.equipmentStatus) :: issues
      }
      if (data.equipmentStatus == "inoperational") {
        issues = Issue(data.equipmentId, "Equipment is inoperational", data.hour, data.energy, data.equipmentStatus) :: issues
      }
      issues
    }
  }
}


