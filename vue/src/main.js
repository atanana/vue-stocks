// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import VueRouter from 'vue-router'
import ProductsPage from './ProductsPage'

Vue.use(VueRouter);

const Products = {
  template: '<ProductsPage/>',
  components: {ProductsPage}
};
const Categories = {template: '<div>bar</div>'};

const routes = [
  {path: '/products', component: Products},
  {path: '/categories', component: Categories}
];

const router = new VueRouter({
  routes, // short for routes: routes,
  mode: 'history'
});

const app = new Vue({
  router
}).$mount('#app');

router.replace(routes[0].path);
