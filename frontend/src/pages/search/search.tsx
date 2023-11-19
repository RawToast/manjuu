import { Authority } from '@src/lib/client'
import { SearchBar } from './searchbar'

const DummyAuthorities: Authority[] = [
  {
    name: 'Town',
    id: '1',
    establishments: 58
  },
  {
    name: 'Major City',
    id: '2',
    establishments: 9001
  },
  {
    name: 'Village',
    id: '3',
    establishments: 1
  },
  {
    name: 'Small City',
    id: '4',
    establishments: 345
  }
]

function Welcome() {
  return (
    <>
      <div className='min-h-full'>
        <div className='container mx-auto'>
          <div className='grid grid-cols-6'>
            <div className='col-start-3 col-span-2 pt-32'>
              <SearchBar authorities={DummyAuthorities} />
            </div>
          </div>
        </div>
      </div>
    </>
  )
}

export { Welcome }
