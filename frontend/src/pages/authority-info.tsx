import { Card, CardHeader, CardTitle, CardContent, CardDescription } from '@/components/ui/card'
import { Progress } from '@/components/ui/progress'
import { Label } from '@/components/ui/label'
import { Skeleton } from '@/components/ui/skeleton'
import { authorityInfoQueryOptions } from '@/lib/router'
import { useQuery } from '@tanstack/react-query'
import { Ratings, ScottishRatings, StandardRatings } from '@src/lib/client'
import { ErrorCard } from './error-page'

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
        <RatingSection label='Pass' score={passScore} />
        <RatingSection label='Improvement Required' score={improvementRequiredScore} />
        <RatingSection label='Exempt' score={exemptScore} />
      </div>
    )
  }
  const rating = ratings.Standard as StandardRatings
  const total =
    rating.five + rating.four + rating.three + rating.two + rating.one + rating.zero + rating.exempt

  const fiveScore = Math.round((rating.five / total) * 100)
  const fourScore = Math.round((rating.four / total) * 100)
  const threeScore = Math.round((rating.three / total) * 100)
  const twoScore = Math.round((rating.two / total) * 100)
  const oneScore = Math.round((rating.one / total) * 100)
  const zeroScore = Math.round((rating.zero / total) * 100)
  const exemptScore = Math.round((rating.exempt / total) * 100)
  return (
    <div>
      <RatingSection label='Very Good' score={fiveScore} />
      <RatingSection label='Good' score={fourScore} />
      <RatingSection label='Satisfactory' score={threeScore} />
      <RatingSection label='Improvement necessary' score={twoScore} />
      <RatingSection label='Major improvement necessary' score={oneScore} />
      <RatingSection label='Urgent improvement necessary' score={zeroScore} />
      <RatingSection label='Exempt' score={exemptScore} />
    </div>
  )
}

const RatingSection = ({ label, score }: { label: string; score: number }) => (
  <div className='mt-2'>
    <Label>{label}</Label>
    <Progress value={score} className='w-[90%]' />
  </div>
)

export const AuthorityInfo = ({ authorityId }: { authorityId: string }) => {
  const result = useQuery(authorityInfoQueryOptions(authorityId))

  const { data, isPending, isError } = result

  if (isError) {
    return <ErrorCard reload={false} />
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
