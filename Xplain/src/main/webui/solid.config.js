// solid.config.js
import { defineConfig } from 'solid-start';
import solidStartNode from 'solid-start-node';

export default defineConfig({
  solid: {
    adapter: solidStartNode(),
  },
});