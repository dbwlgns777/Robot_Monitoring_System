import {describe,expect,it} from 'vitest';
import {mapEquipmentRows} from './restApiProvider';

describe('REST equipment mapping',()=>
{
 it('preserves collector-defined dynamic tags without a fixed schema',()=>
 {
  const [equipment]=mapEquipmentRows([{
   id:7,lineId:2,status:'RUNNING',type:'ROBOT',code:'R-07',name:'Robot',
   dynamicTags:{'vendor.anyNumber':12.5,'vendor.anyFlag':true,'vendor.anyText':'ok'}
  }]);
  expect(equipment.lineId).toBe('line-2');
  expect(equipment.dynamicTags).toEqual({
   'vendor.anyNumber':12.5,'vendor.anyFlag':true,'vendor.anyText':'ok'
  });
 });

 it('uses an empty map when no dynamic tags are collected',()=>
 {
  const [equipment]=mapEquipmentRows([{id:1,lineId:1,status:'RUNNING'}]);
  expect(equipment.dynamicTags).toEqual({});
 });
});
