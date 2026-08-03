import {useEffect,useState} from 'react';
export function useMockQuery<T>(loader:()=>Promise<T>){const [data,setData]=useState<T>();const [error,setError]=useState('');useEffect(()=>{loader().then(setData).catch(e=>setError(e instanceof Error?e.message:'데이터 오류'));},[]);return {data,error,loading:!data&&!error};}
