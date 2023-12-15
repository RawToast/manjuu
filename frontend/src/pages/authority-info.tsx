// import React from 'react'

import {
  Card,
  CardHeader,
  CardTitle,
  CardContent,
  CardDescription,
  CardFooter
} from '@src/components/ui/card'
import { Skeleton } from '@/components/ui/skeleton'
import { authorityInfoQueryOptions } from '@src/lib/router'
import { useQuery } from '@tanstack/react-query'

const LoadingCard = () => (
  <Card>
    <CardHeader>
      <CardTitle>
        <Skeleton className='h-4 w-[150px] font-semibold' />
      </CardTitle>
      <CardDescription>
        <Skeleton className='mt-1 h-3 w-[350px]' />
        <Skeleton className='mt-1 h-3 w-[50px]' />
      </CardDescription>
    </CardHeader>
    <CardContent>
      <p>Card Content</p>
    </CardContent>
    <CardFooter>
      <p>Card Footer</p>
    </CardFooter>
  </Card>
)

export function AuthorityInfo({ authorityId }: { authorityId: string }) {
  const result = useQuery(authorityInfoQueryOptions(authorityId))

  const { data, isPending, isError } = result

  if (isError) {
    return <p>Error</p>
  }

  if (isPending) {
    return <LoadingCard />
  }
  const ariaLabel = `View detailed food hygiene ratings for ${data.name}`
  const cardDescription = `Health Authority data for the ${data.establishments} establishments in `
  return (
    <div>
      <Card>
        <CardHeader>
          <CardTitle>{data.name}</CardTitle>
          <CardDescription>
            {cardDescription}
            <a href={data.url} aria-label={ariaLabel}>
              {data.name}
            </a>
          </CardDescription>
        </CardHeader>
        <CardContent>
          <p>Card Content</p>
        </CardContent>
        <CardFooter>
          <p>Card Footer</p>
        </CardFooter>
      </Card>
    </div>
  )
}
