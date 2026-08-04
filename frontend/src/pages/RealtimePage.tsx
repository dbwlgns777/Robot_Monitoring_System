import {useState} from 'react';
import {Activity,Clock,Radio,X} from 'lucide-react';
import {Card,Loading,PageHeader,Progress} from '../components/Common';
import {StatusBadge} from '../components/StatusBadge';
import {useRealtimeEquipment} from '../hooks/useRealtimeEquipment';

function displayTagValue(value:unknown):string
{
 if(value===null)return 'null';
 if(typeof value==='object')return JSON.stringify(value);
 return String(value);
}

export function RealtimePage()
{
 const {data}=useRealtimeEquipment();
 const [selectedId,setSelectedId]=useState<string>();
 if(!data)return <Loading/>;
 const selected=data.find(equipment=>equipment.id===selectedId);
 return <><PageHeader title="공장·라인 실시간 현황" description="설비 상태를 선택하면 상세 운전 정보를 확인할 수 있습니다." actions={<div className="legend"><StatusBadge status="RUNNING"/><StatusBadge status="WAITING"/><StatusBadge status="ROBOT_FAULT"/><StatusBadge status="COMMUNICATION_LOSS"/></div>}/>{[1,2,3].map(line=><Card key={line} title={`${line}라인 · ${['프론트 패널 LH','사이드 멤버 RH','브래킷 ASSY'][line-1]}`} caption={`로봇 4대 · 프레스 P-0${line}`}><div className="flow-line"><div className="press-node"><b>P-0{line}</b><span>프레스</span><StatusBadge status={line===2?'WAITING':'RUNNING'}/></div>{data.filter(e=>e.lineId===`line-${line}`).map(e=><button className={`equipment-node state-${e.status.toLowerCase()}`} key={e.id} onClick={()=>setSelectedId(e.id)}><div><b>{e.code}</b><StatusBadge status={e.status}/></div><strong>{e.name}</strong><span>{e.product}</span><div className="equipment-numbers"><span>실적 <b>{e.actual.toLocaleString()}</b></span><span>CT <b>{e.cycleTime}초</b></span></div>{e.alarm&&<em>{e.alarm}</em>}</button>)}</div></Card>)}{selected&&<div className="drawer-overlay" onClick={()=>setSelectedId(undefined)}><aside className="detail-drawer" onClick={event=>event.stopPropagation()}><button className="drawer-close" onClick={()=>setSelectedId(undefined)}><X/></button><span className="eyebrow">EQUIPMENT DETAIL</span><h2>{selected.code} · {selected.name}</h2><StatusBadge status={selected.status}/><div className="detail-hero"><Activity/><div><span>현재 상태 지속시간</span><b>{selected.statusDuration}</b></div></div><h3>실시간 운전 정보</h3><dl className="detail-list"><div><dt>현재 제품</dt><dd>{selected.product}</dd></div><div><dt>생산 실적</dt><dd>{selected.actual.toLocaleString()} / {selected.target.toLocaleString()}개</dd></div><div><dt>최근 사이클타임</dt><dd>{selected.cycleTime}초</dd></div></dl><h3>동적 수집 태그</h3><p className="dynamic-tag-help">데이터 종류를 고정하지 않고 수집된 키와 값을 그대로 표시합니다.</p><dl className="dynamic-tag-list">{Object.entries(selected.dynamicTags).length===0?<div className="dynamic-tag-empty">수집된 동적 태그가 없습니다.</div>:Object.entries(selected.dynamicTags).sort(([left],[right])=>left.localeCompare(right)).map(([key,value])=><div key={key}><dt>{key}</dt><dd>{displayTagValue(value)}</dd></div>)}</dl><h3>통신 상태</h3><div className="comm-card"><Radio/><div><b>{selected.status==='COMMUNICATION_LOSS'?'연결 끊김':'READ 연결 정상'}</b><span>{selected.ip} · {selected.protocol}</span></div><strong>{selected.responseMs||'-'}ms</strong></div><h3>최근 1시간 사이클</h3><Progress value={82}/><div className="readonly-banner"><Clock/> 조회 전용 화면입니다. 설비 제어 기능은 제공되지 않습니다.</div></aside></div>}</>
}
