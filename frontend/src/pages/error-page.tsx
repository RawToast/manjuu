import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/card'
import { ExclamationTriangleIcon, ReloadIcon } from '@radix-ui/react-icons'
import { Button } from '@src/components/ui/button'

const ReloadButton = () => (
  <Button className='mx-auto' asChild>
    <a href=''>
      <ReloadIcon className='h-4 w-4 sm:mr-2' />
      <span className='hidden sm:contents md:hidden'>Reload</span>
      <span className='hidden md:contents'>Reload page</span>
    </a>
  </Button>
)

export const ErrorCard = ({ reload = true }) => (
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
      {reload && (
        <CardContent>
          <div className='flex items-center pt-6'>
            <ReloadButton />
          </div>
        </CardContent>
      )}
    </CardHeader>
  </Card>
)

export const ErrorPage = () => (
  <div className='container mx-auto min-h-full'>
    <div className='grid grid-cols-6'>
      <div className='pt-32 md:col-span-2 md:col-start-3'>
        <ErrorCard />
      </div>
    </div>
  </div>
)
