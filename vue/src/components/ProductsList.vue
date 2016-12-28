<template>
  <table class="table">
    <tbody>
      <ProductItem
        v-for="product in products"
        :product="product"
      />
    </tbody>
  </table>
</template>

<script>
  import ProductItem from 'components/ProductItem'

  export default {
    props: ['products'],
    components: {
      ProductItem
    }
  }
</script>
