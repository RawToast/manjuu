import { RouterProvider, createBrowserRouter } from 'react-router-dom'
import './index.css'
import { Welcome } from './pages/search'

const ROOT_PATH = '/'

const router = createBrowserRouter([
  {
    id: 'root',
    path: ROOT_PATH,

    // This route could be a hero dashboard or something
    // but we'll just redirect to the login page
    Component: () => <Welcome />
  }
])

export default function App() {
  return <RouterProvider router={router} fallbackElement={<p>Initial Load...</p>} />
}
