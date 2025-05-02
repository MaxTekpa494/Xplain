/* @refresh reload */
import { createSignal, onMount } from "solid-js";
import { render } from "solid-js/web";
import 'bulma/css/bulma.min.css';
import App from './App'
import './index.css'


/* Organisation de <resources> décrite ici: https://docs.quarkiverse.io/quarkus-web-bundler/dev/index.html#_usage */

const root = document.getElementById('root')
render(() => <App />, root)