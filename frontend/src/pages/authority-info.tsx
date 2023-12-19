// import React from 'react'

import { Card, CardHeader, CardTitle, CardContent, CardDescription } from '@/components/ui/card'
import { Progress } from '@/components/ui/progress'
import { Label } from '@/components/ui/label'
import { Skeleton } from '@/components/ui/skeleton'
import { authorityInfoQueryOptions } from '@/lib/router'
import { useQuery } from '@tanstack/react-query'
import { Ratings, ScottishRatings, StandardRatings } from '@src/lib/client'
import { useEffect, useState } from 'react'

export default function ProgressAnimated(value) {
  const [progress, setProgress] = useState(1)

  useEffect(() => {
    const timer = setTimeout(() => setProgress(value), 500)
    return () => clearTimeout(timer)
  }, [])

  return <Progress value={progress} className='px- w-[80%]' />
}

const LoadingCard = () => (
  <Card>
    <CardHeader>
      <Skeleton className='h-4 w-[150px] font-semibold' />
      <Skeleton className='mt-1 h-3 w-[350px]' />
      <Skeleton className='mt-1 h-3 w-[50px]' />
    </CardHeader>
    <CardContent>
      <Skeleton className='mt-1 h-3 w-[250px]' />
      <Skeleton className='mt-1 h-3 w-[50px]' />
      <Skeleton className='mt-1 h-3 w-[150px]' />
    </CardContent>
  </Card>
)

const RatingsContent = ({ ratings }: { ratings: Ratings }) => {
  if (ratings.Scottish !== undefined) {
    const rating = ratings.Scottish as ScottishRatings

    const total = rating.pass + rating.improvementRequired + rating.exempt
    const passScore = Math.round((rating.pass / total) * 100)
    const improvementRequiredScore = Math.round((rating.improvementRequired / total) * 100)
    const exemptScore = Math.round((rating.exempt / total) * 100)
    console.log(passScore, improvementRequiredScore, exemptScore)

    return (
      <div className='space-y-2'>
        <Label>Pass</Label>
        <Progress value={passScore} className='w-[90%]' />
        <Label>Improvement Required</Label>
        <Progress value={improvementRequiredScore} className='w-[90%]' />
        <Label>Exempt</Label>
        <Progress value={exemptScore} className='w-[90%]' />
      </div>
    )
  }
  const rating = ratings.Standard as StandardRatings
  return (
    <div>
      <p>Standard Ratings</p>
      <p>{rating.five}</p>
    </div>
  )
}

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
        <RatingsContent ratings={data.ratings} />
      </CardContent>
    </Card>
  )
}
