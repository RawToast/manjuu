import {
  BuildingOfficeIcon,
  BuildingOffice2Icon,
  HomeIcon,
  HomeModernIcon
} from '@heroicons/react/24/outline'
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
  CommandSeparator
} from '@/components/ui/command'
import { Authority, fetchAuthorities } from '@/lib/client'
import { useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import { Link } from '@tanstack/react-router'

export function SearchBar() {
  const { status, data } = useQuery({ queryKey: ['todos'], queryFn: fetchAuthorities })

  const [searchText, setSearchText] = useState('')
  const [recent, setRecent] = useState<Authority[]>([])

  function updateRecent(authority: Authority) {
    // place on head of array, remove duplicates, and limit to 5
    setRecent(
      [authority, ...recent]
      // .filter((item, index, self) => self.findIndex((i) => i.id === item.id) === index)
      // .slice(0, 5)
    )
  }

  function onAuthorityClick(_e: React.MouseEvent<'a', MouseEvent>, _authorityId: number) {
    // Could update recent here
    setSearchText('')
  }

  return (
    <Command
      id='searchbar-label'
      className='rounded-lg border shadow-md'
      label='Search for an authority'
    >
      <CommandInput
        placeholder='Search for an authority...'
        value={searchText}
        onValueChange={(s) => {
          setSearchText(s)
        }}
        data-testid='searchbar-input'
      />
      {searchText.length <= 1 ? (
        <></>
      ) : (
        <CommandList>
          <CommandEmpty data-testid='search-no-results'>No results found.</CommandEmpty>
          <CommandGroup data-testid='search-authorities-group' heading='Authorities'>
            {/* Status error will bubble up to the top level */}
            {status === 'pending' && <CommandEmpty>Loading...</CommandEmpty>}
            {status === 'success' &&
              data.map((authority) => {
                return (
                  <Link
                    data-testid={`result-${authority.name.toLowerCase().replaceAll(' ', '-')}`}
                    key={'link' + authority.id}
                    to='/authority/$authorityId'
                    params={{ authorityId: `${authority.id}` }}
                    onClick={(e) => onAuthorityClick(e, authority.id)}
                    role='link'
                    title={authority.name}
                  >
                    <AuthorityItem
                      key={'item' + authority.id}
                      authority={authority}
                      onClick={updateRecent}
                    ></AuthorityItem>
                  </Link>
                )
              })}
          </CommandGroup>
          <CommandSeparator />
          {/* <CommandGroup heading='Recent'>
            {recent.map((authority) => (
              <AuthorityItem
                key={authority.id}
                authority={authority}
                onClick={updateRecent}
              ></AuthorityItem>
            ))}
          </CommandGroup> */}
        </CommandList>
      )}
    </Command>
  )
}

function AuthorityItem({
  authority,
  onClick
}: {
  authority: Authority
  onClick: (Authority) => void
}) {
  return (
    <CommandItem
      data-testid={`result-${authority.name.toLowerCase().replaceAll(' ', '-')}`}
      value={authority.name}
      key={authority.id}
      onClick={() => onClick(authority)}
    >
      {authority.establishments > 3000 ? (
        <BuildingOffice2Icon className='mr-2 h-4 w-4' />
      ) : authority.establishments > 2000 ? (
        <BuildingOfficeIcon className='mr-2 h-4 w-4' />
      ) : authority.establishments > 1000 ? (
        <HomeModernIcon className='mr-2 h-4 w-4' />
      ) : (
        <HomeIcon className='mr-2 h-4 w-4' />
      )}
      <span>{authority.name}</span>
      {/* <CommandShortcut>⌘1</CommandShortcut> */}
    </CommandItem>
  )
}
