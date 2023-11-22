import { SearchBar } from './search'

function Layout() {
  return (
    <div className='min-h-full container mx-auto'>
      <div className='grid grid-cols-6'>
        <div className='col-start-3 col-span-2 pt-32'>
          <SearchBar />
        </div>
      </div>
    </div>
  )
}

export { Layout }
