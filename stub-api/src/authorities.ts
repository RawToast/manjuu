import { Map } from 'immutable'
import { angus } from './data/angus'
import { amberValley } from './data/amber-valley'
import { aberdeen } from './data/aberdeen'
import { aberdeenshire } from './data/aberdeenshire'
import { adur } from './data/adur'
import { anglesey } from './data/anglesey'

export const authorityData = {
  authorities: [
    {
      LocalAuthorityId: 197,
      LocalAuthorityIdCode: '760',
      Name: 'Aberdeen City',
      EstablishmentCount: 2226,
      SchemeType: 2,
      links: [
        {
          rel: 'self',
          href: 'http://api.ratings.food.gov.uk/authorities/197'
        }
      ]
    },
    {
      LocalAuthorityId: 198,
      LocalAuthorityIdCode: '761',
      Name: 'Aberdeenshire',
      EstablishmentCount: 2491,
      SchemeType: 2,
      links: [
        {
          rel: 'self',
          href: 'http://api.ratings.food.gov.uk/authorities/198'
        }
      ]
    },
    {
      LocalAuthorityId: 277,
      LocalAuthorityIdCode: '323',
      Name: 'Adur',
      EstablishmentCount: 443,
      SchemeType: 1,
      links: [
        {
          rel: 'self',
          href: 'http://api.ratings.food.gov.uk/authorities/277'
        }
      ]
    },
    {
      LocalAuthorityId: 48,
      LocalAuthorityIdCode: '062',
      Name: 'Amber Valley',
      EstablishmentCount: 1100,
      SchemeType: 1,
      links: [
        {
          rel: 'self',
          href: 'http://api.ratings.food.gov.uk/authorities/48'
        }
      ]
    },
    {
      LocalAuthorityId: 334,
      LocalAuthorityIdCode: '551',
      Name: 'Anglesey',
      EstablishmentCount: 763,
      SchemeType: 1,
      links: [
        {
          rel: 'self',
          href: 'http://api.ratings.food.gov.uk/authorities/334'
        }
      ]
    },
    {
      LocalAuthorityId: 199,
      LocalAuthorityIdCode: '762',
      Name: 'Angus',
      EstablishmentCount: 954,
      SchemeType: 2,
      links: [
        {
          rel: 'self',
          href: 'http://api.ratings.food.gov.uk/authorities/199'
        }
      ]
    }
  ]
}

export const authoritiesMap: Map<String, Object> = Map({
  '48': amberValley,
  '197': aberdeen,
  '198': aberdeenshire,
  '199': angus,
  '277': adur,
  '334': anglesey
})
