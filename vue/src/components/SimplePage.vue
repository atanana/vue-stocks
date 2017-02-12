<template>
  <div>
    <SimpleList
      :items="itemsData"
      :newItemLabel="newItemLabel"
    />
    <SaveButton @save="$emit('saveItems', itemsData)"/>
  </div>
</template>

<script>
  import SimpleList from 'components/SimpleList'
  import SaveButton from 'components/buttons/SaveButton'
  import {copyData} from "utility/objectUtils";

  export default {
    props: ['items', 'newItemPlaceholder', 'newItemLabel'],
    components: {
      SimpleList,
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
