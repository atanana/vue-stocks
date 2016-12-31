<template>
  <ProductsList
    :products="products"
  />
</template>

<script>

  import ProductsList from 'components/ProductsList'

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
      this.$store.commit('loadProducts');
    }
  }
</script>

<style>
  #app {
    margin: 1em;
  }
</style>
