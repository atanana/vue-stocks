<template>
  <table class="table">
    <thead>
      <tr>
        <th>Имя</th>
        <th>Место</th>
        <th>Количество</th>
      </tr>
    </thead>
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
