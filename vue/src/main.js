// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import VueRouter from 'vue-router'
import Vuex from 'vuex'

import axios from 'axios'

import ProductsPage from 'components/products/ProductsPage'
import CategoriesPage from 'components/categories/CategoriesPage'

Vue.use(VueRouter);
Vue.use(Vuex);

const Products = {
  template: '<ProductsPage/>',
  components: {ProductsPage}
};
const Categories = {
  template: '<CategoriesPage/>',
  components: {CategoriesPage}
};

const routes = [
  {path: '/products', component: Products},
  {path: '/categories', component: Categories}
];

const router = new VueRouter({
  routes, // short for routes: routes,
  mode: 'history'
});

const store = new Vuex.Store({
  state: {
    products: [],
    categories: []
  },
  mutations: {
    loadProducts(state) {
      axios.get('/api/products/all')
        .then((response) => {
          state.products = response.data;
        });
    },
    loadCategories(state) {
      axios.get('/api/categories/all')
        .then((response) => {
          state.categories = response.data;
        });
    }
  }
});

const app = new Vue({
  router,
  store
}).$mount('#app');

if (location.pathname === '/') {
  router.replace(routes[0].path);
}
