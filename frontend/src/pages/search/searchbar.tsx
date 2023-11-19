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
  CommandSeparator,
  CommandShortcut
} from '@/components/ui/command'
import { Authority } from '@src/lib/client'
import React from 'react'

export function SearchBar(props: { authorities: Authority[] }) {
  const [searchText, setSearchText] = React.useState('')
  console.log(searchText.length)
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
            {props.authorities.map((authority) => {
              return (
                <CommandItem key={authority.id}>
                  {authority.establishments > 1000 ? (
                    <BuildingOffice2Icon className='mr-2 h-4 w-4' />
                  ) : authority.establishments > 100 ? (
                    <BuildingOfficeIcon className='mr-2 h-4 w-4' />
                  ) : authority.establishments > 10 ? (
                    <HomeModernIcon className='mr-2 h-4 w-4' />
                  ) : (
                    <HomeIcon className='mr-2 h-4 w-4' />
                  )}
                  <span>{authority.name}</span>
                </CommandItem>
              )
            })}
          </CommandGroup>

          <CommandSeparator />
          <CommandGroup heading='Recent'>
            <CommandItem id='matlock'>
              <HomeModernIcon className='mr-2 h-4 w-4' />
              <span>Matlock</span>
              <CommandShortcut>⌘1</CommandShortcut>
            </CommandItem>
          </CommandGroup>
        </CommandList>
      )}
    </Command>
  )
}
