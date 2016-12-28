<template>
  <div id="app">
    <ProductsList
      :products="products"
    />
  </div>
</template>

<script>

import ProductsList from 'components/ProductsList'

export default {
  name: 'app',
  props: ['products'],
  components: {
    ProductsList
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
