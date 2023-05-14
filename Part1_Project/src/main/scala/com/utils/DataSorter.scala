package com.utils

import com.models.EnergyData
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// The DataSorter object provides functions for sorting energy data based on certain criteria.
object DataSorter {

  // sortByHour function sorts a list of EnergyData based on the hour field.
  // It returns a new list of EnergyData sorted in ascending order by the hour.
  def sortByHour(energyData: List[EnergyData]): List[EnergyData] = {
    energyData.sortBy(data => data.hour)
  }
}
