<template>
  <ul>
    <ProductItem
      v-for="product in products"
      :product="product"
    />
  </ul>
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
