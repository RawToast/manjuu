import { Card, CardHeader, CardTitle, CardDescription } from '@/components/ui/card'
import { ExclamationTriangleIcon } from '@radix-ui/react-icons'

export const ErrorCard = ({ message }) => (
  <Card>
    <CardHeader>
      <CardTitle>
        <div className='grid grid-cols-6'>
          <div className='col-span-1'>
            <ExclamationTriangleIcon className='h-5 w-5' />
          </div>
          <div className='col-span-5'>Error</div>
        </div>
      </CardTitle>
      <CardDescription>Something has gone terribly wrong...</CardDescription>
    </CardHeader>
  </Card>
)

export const ErrorPage = () => (
  <div className='container mx-auto min-h-full'>
    <div className='grid grid-cols-6'>
      <div className='col-span-2 col-start-3 pt-32'>
        {/* <SearchBar />
      </div>
      <div className='col-span-2 col-start-3 pt-4'>
        <Outlet /> */}
        <ErrorCard />
      </div>
    </div>
  </div>
)
