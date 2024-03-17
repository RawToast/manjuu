import { Map } from 'immutable'
import { amberValleyEstablishments } from './data/amber-valley'
import { aberdeenEstablishments } from './data/aberdeen'
import { amberdeenshireEstablishments } from './data/aberdeenshire'
import { angusEstablishments } from './data/angus'
import { adurEstablishments } from './data/adur'
import { angleseyEstablishments } from './data/anglesey'

export const establishmentsMap: Map<String, Object> = Map({
  '48': amberValleyEstablishments,
  '197': aberdeenEstablishments,
  '198': amberdeenshireEstablishments,
  '199': angusEstablishments,
  '277': adurEstablishments,
  '334': angleseyEstablishments
})
