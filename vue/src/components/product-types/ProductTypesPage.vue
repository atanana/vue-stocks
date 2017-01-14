<template>
  <div>
    <ProductTypesList
      :items="items"
      :categories="categories"
      :newItemPlaceholder="newItemPlaceholder"
      :newItemLabel="newItemLabel"
    />
    <SaveButton @save="$emit('saveItems')"/>
  </div>
</template>

<script>
  import ProductTypesList from 'components/product-types/ProductTypesList'
  import SaveButton from 'components/buttons/SaveButton'

  export default {
    props: ['items', 'categories', 'newItemPlaceholder', 'newItemLabel'],
    components: {
      ProductTypesList,
      SaveButton
    }
  }
</script>
