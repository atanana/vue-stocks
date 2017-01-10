// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from "vue";
import VueRouter from "vue-router";
import Vuex from "vuex";
import axios from "axios";
import ProductsPage from "components/products/ProductsPage";
import CategoriesPage from "components/categories/CategoriesPage";
import PacksPage from "components/packs/PacksPage";

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
const Packs = {
  template: '<PacksPage/>',
  components: {PacksPage}
};

const routes = [
  {path: '/products', component: Products},
  {path: '/categories', component: Categories},
  {path: '/packs', component: Packs},
];

const router = new VueRouter({
  routes, // short for routes: routes,
  mode: 'history'
});

const store = new Vuex.Store({
  state: {
    products: [],
    categories: [],
    packs: []
  },
  mutations: {
    setProducts(state, products) {
      state.products = products;
    },
    setCategories(state, categories) {
      state.categories = categories;
    },
    setPacks(state, categories) {
      state.packs = categories;
    },
  },
  actions: {
    loadProducts({commit}) {
      axios.get('/api/products/all')
        .then(response => {
          commit('setProducts', response.data);
        });
    },
    loadCategories({commit}) {
      axios.get('/api/categories/all')
        .then(response => {
          commit('setCategories', response.data);
        });
    },
    loadPacks({commit}) {
      axios.get('/api/packs/all')
        .then(response => {
          commit('setPacks', response.data);
        });
    },
    updateCategories({commit}, categories) {
      axios.put('/api/categories/update', categories)
        .then(response => {
          commit('setCategories', response.data);
        });
    },
    updatePacks({commit}, packs) {
      axios.put('/api/packs/update', packs)
        .then(response => {
          commit('setPacks', response.data);
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
