import type {CSSProperties} from 'react';import type {EquipmentStatus} from '../types/domain';import {statusMeta} from '../utils/status';
export function StatusBadge({status}: {status:EquipmentStatus}){const m=statusMeta[status];return <span className="status-badge" style={{'--status':m.color} as CSSProperties}><b>{m.icon}</b>{m.label}</span>}
