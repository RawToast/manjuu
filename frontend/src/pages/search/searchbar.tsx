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
import React from 'react'
import { useQuery } from '@tanstack/react-query'

export function SearchBar() {
  const { status, data, error } = useQuery({ queryKey: ['todos'], queryFn: fetchAuthorities })

  const [searchText, setSearchText] = React.useState('')

  if (status === 'error') {
    return <span>Error: {error.message}</span>
  }

  return (
    <Command className='rounded-lg border shadow-md'>
      <CommandInput
        placeholder='Type a command or search...'
        value={searchText}
        onValueChange={setSearchText}
      />
      {searchText.length == 0 ? (
        <></>
      ) : (
        <CommandList>
          <CommandEmpty>No results found.</CommandEmpty>

          <CommandGroup heading='Authorities'>
            {status === 'pending' ? (
              <CommandEmpty>Loading...</CommandEmpty>
            ) : (
              data.map((authority) => {
                return <AuthorityItem key={authority.id} authority={authority}></AuthorityItem>
              })
            )}
          </CommandGroup>

          <CommandSeparator />
          <CommandGroup heading='Recent'>
            <AuthorityItem
              authority={{ id: 10, name: 'Matlock', establishments: 65 }}
            ></AuthorityItem>
          </CommandGroup>
        </CommandList>
      )}
    </Command>
  )
}

function AuthorityItem({ authority }: { authority: Authority }) {
  return (
    <CommandItem key={authority.id}>
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
