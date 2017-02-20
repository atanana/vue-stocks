<template>
  <table class="table is-striped">
    <thead>
      <tr>
        <th width="30%">Имя</th>
        <th width="15%">Место</th>
        <th width="*">Количество</th>
      </tr>
    </thead>
    <tbody>
      <ProductItem
        v-for="product in products"
        :product="product"
        :packTypes="packs"
        ref="items"
      />
    </tbody>
  </table>
</template>

<script>
  import ProductItem from 'components/products/ProductItem'

  export default {
    props: ['products', 'packs'],
    components: {
      ProductItem
    }
  }
</script>
