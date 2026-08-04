import type {ReactNode} from 'react';
export function PageHeader({eyebrow='PRIMA FACTORY 360',title,description,actions}:{eyebrow?:string;title:string;description:string;actions?:ReactNode}){return <header className="page-header"><div><span className="eyebrow">{eyebrow}</span><h1>{title}</h1><p>{description}</p></div>{actions&&<div className="header-actions">{actions}</div>}</header>}
export function Card({title,caption,children,className=''}:{title?:string;caption?:string;children:ReactNode;className?:string}){return <section className={`card ${className}`}>{title&&<div className="card-head"><div><h2>{title}</h2>{caption&&<p>{caption}</p>}</div></div>}{children}</section>}
export function Loading(){return <div className="state-box"><span className="spinner"/>현장 데이터를 불러오는 중입니다.</div>}
export function ErrorState({message}:{message:string}){return <div className="state-box error">! {message}</div>}
export function Progress({value,warn=false}:{value:number;warn?:boolean}){return <div className="progress"><i className={warn?'warn':''} style={{width:`${Math.min(value,100)}%`}}/></div>}
export function Metric({label,value,unit,delta,tone='default'}:{label:string;value:string|number;unit?:string;delta?:string;tone?:string}){return <div className={`metric tone-${tone}`}><span>{label}</span><strong>{value}<small>{unit}</small></strong>{delta&&<em>{delta}</em>}</div>}
