package com.prima.factory.domain;
public final class StatusClassifier {
 private StatusClassifier() {}
 public static EquipmentStatus classify(CommunicationStatus comm, boolean safety, boolean alarm, boolean manual, boolean teaching, boolean planned, boolean running, EquipmentStatus waitingOrLoss) {
  if (comm == CommunicationStatus.DISCONNECTED) return EquipmentStatus.COMMUNICATION_LOSS;
  if (safety) return EquipmentStatus.SAFETY_STOP;
  if (alarm) return EquipmentStatus.ROBOT_FAULT;
  if (teaching) return EquipmentStatus.TEACHING;
  if (manual) return EquipmentStatus.MANUAL;
  if (planned) return EquipmentStatus.PLANNED_STOP;
  if (running) return EquipmentStatus.RUNNING;
  return waitingOrLoss == null ? EquipmentStatus.UNCLASSIFIED_STOP : waitingOrLoss;
 }
}
