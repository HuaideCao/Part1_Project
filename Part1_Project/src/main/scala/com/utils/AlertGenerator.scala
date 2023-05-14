package com.utils

import com.models.{EnergyData, Issue}

// AlertGenerator object provides a function to generate an alert based on an identified issue.
object AlertGenerator {

  // generateAlert function takes an Issue as input and prints an alert message to the console.
  // The alert message includes the description of the issue, timestamp, energy output, and status.
  def generateAlert(issue: Issue): Unit = {
    println(s"Alert: ${issue.equipmentId} -- ${issue.description} at ${issue.timestamp}(timestamp) with energy output ${issue.energy} and status ${issue.status}")
  }
}
