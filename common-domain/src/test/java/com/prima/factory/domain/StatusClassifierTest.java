package com.prima.factory.domain;
import org.junit.jupiter.api.Test; import static org.junit.jupiter.api.Assertions.*;
class StatusClassifierTest { @Test void communicationLossHasHighestPriority(){ assertEquals(EquipmentStatus.COMMUNICATION_LOSS, StatusClassifier.classify(CommunicationStatus.DISCONNECTED,true,true,true,true,true,true,EquipmentStatus.WAITING)); } @Test void safetyPrecedesAlarm(){assertEquals(EquipmentStatus.SAFETY_STOP,StatusClassifier.classify(CommunicationStatus.CONNECTED,true,true,false,false,false,true,null));}}
