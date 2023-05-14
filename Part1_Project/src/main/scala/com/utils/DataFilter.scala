package com.utils

import com.models.EnergyData

// The DataFilter object provides functions for filtering energy data based on certain criteria.
object DataFilter {

  // filterByHour function filters a list of EnergyData to include only those data entries
  // that are within a given range of hours.
  // Parameters:
  // startHour: The beginning of the hour range. EnergyData with hour less than this will be excluded.
  // endHour: The end of the hour range. EnergyData with hour more than this will be excluded.
  def filterByHour(energyData: List[EnergyData], startHour: Long, endHour: Long): List[EnergyData] = {
    energyData.filter(data => data.hour >= startHour && data.hour <= endHour)
  }

  // filterBySource function filters a list of EnergyData to include only those data entries
  // that are generated from a specific source of energy.
  // Parameter:
  // source: The source of energy. EnergyData with energy type not equal to this source will be excluded.
  def filterBySource(energyData: List[EnergyData], source: String): List[EnergyData] = {
    energyData.filter(data => data.energyType == source)
  }
}
