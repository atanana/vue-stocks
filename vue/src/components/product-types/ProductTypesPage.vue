<template>
  <div>
    <ProductTypesList
      :items="itemsData"
      :categories="categories"
      :newItemPlaceholder="newItemPlaceholder"
      :newItemLabel="newItemLabel"
    />
    <SaveButton @save="$emit('saveItems', itemsData)"/>
  </div>
</template>

<script>
  import ProductTypesList from 'components/product-types/ProductTypesList'
  import SaveButton from 'components/buttons/SaveButton'
  import {copyData} from "utility/objectUtils";

  export default {
    props: ['items', 'categories', 'newItemPlaceholder', 'newItemLabel'],
    components: {
      ProductTypesList,
      SaveButton
    },
    data() {
      return {
        itemsData: copyData(this.items)
      }
    },
    watch: {
      items(newItems) {
        this.itemsData = copyData(newItems)
      }
    }
  }
</script>
