package com.models

sealed trait Issue1 {
  def description: String
}

case class LowEnergyOutput() extends Issue1 {
  override def description: String = "Low energy output detected"
}

case class EquipmentMalfunction() extends Issue1 {
  override def description: String = "Equipment malfunction detected"
}

