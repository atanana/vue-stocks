<template>
  <div id="app">
    <Navbar />
    <ProductsList
      :products="products"
    />
  </div>
</template>

<script>

import ProductsList from 'components/ProductsList'
import Navbar from 'components/Navbar'

export default {
  name: 'app',
  props: ['products'],
  components: {
    ProductsList,
    Navbar
  }
}
</script>

<style>
#app {
  margin: 1em;
}
</style>
