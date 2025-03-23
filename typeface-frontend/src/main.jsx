import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import FileManager from './FileManager.jsx'
import './index.css';

createRoot(document.getElementById('root')).render(
  <FileManager />
)
