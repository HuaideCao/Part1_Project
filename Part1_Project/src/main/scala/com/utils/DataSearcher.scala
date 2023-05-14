package com.utils

import com.models.EnergyData

// The DataSearcher object provides functions for searching energy data based on certain criteria.
object DataSearcher {

  // searchData function filters a list of EnergyData to include only those data entries
  // that contain a specific keyword either in the equipmentId or the energyType fields.
  // Parameters:
  // keyword: The keyword to search for. EnergyData with equipmentId or energyType not containing this keyword will be excluded.
  def searchData(energyData: List[EnergyData], keyword: String): List[EnergyData] = {
    energyData.filter(data => data.equipmentId.contains(keyword) || data.energyType.contains(keyword))
  }
}
