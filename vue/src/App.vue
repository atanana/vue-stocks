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

}
</style>
