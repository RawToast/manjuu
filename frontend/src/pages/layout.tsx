import { Outlet } from '@tanstack/react-router'
import { SearchBar } from './search'

function Layout() {
  return (
    <div className='container mx-auto min-h-full'>
      <div className='grid grid-cols-6'>
        <div className='col-span-2 col-start-3 pt-32'>
          <SearchBar />
        </div>
        <div className='col-span-2 col-start-3 pt-32'>
          <Outlet />
        </div>
      </div>
    </div>
  )
}

export { Layout }
