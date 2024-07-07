// src/pages/_app.tsx

import type { AppProps } from 'next/app'
import Head from 'next/head'
import '../app/globals.css'
import ControlPad from '../components/ControlPad';


function MyApp({ Component, pageProps }: AppProps) {
  return (
    <>
      <Head>
        <link rel="icon" href="/icon.ico" />
      </Head>
      <Component {...pageProps} />
      <ControlPad />
    </>
  )
}

export default MyApp
