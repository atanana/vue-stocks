<template>
  <ProductsList
    :products="products"
  />
</template>

<script>

  import ProductsList from 'components/products/ProductsList'

  export default {
    name: 'app',
    components: {
      ProductsList
    },
    computed: {
      products() {
        return this.$store.state.products;
      }
    },
    created () {
      this.$store.dispatch('loadProducts');
    }
  }
</script>

<style>
  #app {
    margin: 1em;
  }
</style>
