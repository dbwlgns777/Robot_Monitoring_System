# Read-only equipment safety

All PLC and robot connections are monitoring-only READ connections. WRITE commands, remote start/stop, set-value endpoints and control buttons are prohibited. Device Server writes only observations to MySQL. The simulator connects to no physical controller. Communication state is stored separately from operating state.

Verification: `rg -n "1401H|remoteStart|remoteStop|writePlc" backend device-server frontend/src` must return no implementation.
