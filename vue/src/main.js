// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import VueRouter from 'vue-router'
import App from './App'
import axios from 'axios'

Vue.use(VueRouter);

const Products = {
  template: '<App :products="products"/>',
  components: {App},
  data () {
    return {
      products: []
    }
  }
};
const Categories = {template: '<div>bar</div>'};

const routes = [
  {path: '/products', component: Products},
  {path: '/categories', component: Categories}
];

const router = new VueRouter({
  routes // short for routes: routes
});

const app = new Vue({
  router
}).$mount('#app');

// axios.get('/api/products/all')
//   .then((response) => {
//     Products.products = response.data;
//   });
