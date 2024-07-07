// src/pages/_app.tsx

import type { AppProps } from 'next/app'
import Head from 'next/head'
import ControlPad from '../components/ControlPad';
import '../app/globals.css'
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';



function MyApp({ Component, pageProps }: AppProps) {
  return (
    <>
      <Head>
        <link rel="icon" href="/icon.ico" />
      </Head>
      <Component {...pageProps} />
      <ControlPad />
      <ToastContainer />
    </>
  )
}

export default MyApp
