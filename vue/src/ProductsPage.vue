<template>
  <ProductsList
    :products="products"
  />
</template>

<script>

  import axios from 'axios'
  import ProductsList from 'components/ProductsList'

  export default {
    name: 'app',
    components: {
      ProductsList
    },
    data() {
      return {
        products: []
      }
    },
    created () {
      axios.get('/api/products/all')
        .then((response) => {
          this.products = response.data;
        });
    }
  }
</script>

<style>
  #app {
    margin: 1em;
  }
</style>
