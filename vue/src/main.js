// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from "vue";
import VueRouter from "vue-router";
import ProductsPage from "components/products/ProductsPage";
import CategoriesPage from "components/CategoriesPage";
import PacksPage from "components/PacksPage";
import ProductTypesPage from "components/product-types/ProductTypesPage";
import store from "store/store";

Vue.use(VueRouter);

const routes = [
  {path: '/products', component: ProductsPage},
  {path: '/categories', component: CategoriesPage},
  {path: '/packs', component: PacksPage},
  {path: '/product-types', component: ProductTypesPage},
];

const router = new VueRouter({
  routes, // short for routes: routes,
  mode: 'history'
});

const app = new Vue({
  router,
  store
}).$mount('#app');

if (location.pathname === '/') {
  router.replace(routes[0].path);
}
