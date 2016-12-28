// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import axios from 'axios'

/* eslint-disable no-new */
const app = new Vue({
  el: '#app',
  template: '<App :products="products"/>',
  components: { App },
  data: {
    products: []
  }
});

axios.get('/api/products/all')
  .then((response) => {
    app.products = response.data;
  });
