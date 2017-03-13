<template>
  <div>
    <SimpleList
      :items="itemsData"
      :newItemLabel="newItemLabel"
      ref="items"
    >
      <template slot="item" scope="props">
        <SimpleItem
          :item="props.item"
          :placeholder="newItemPlaceholder"
          class="simple-item"
          @deleteItem="$refs.items.deleteItem(props.item)"
        />
      </template>
    </SimpleList>
    <SaveButton ref="saveButton" @save="$emit('saveItems', itemsData)"/>
  </div>
</template>

<script>
  import SimpleItem from 'components/SimpleItem';
  import SimpleList from 'components/SimpleList';
  import SaveButton from 'components/buttons/SaveButton';
  import {copyData} from "utility/objectUtils";

  export default {
    props: ['items', 'newItemPlaceholder', 'newItemLabel'],
    components: {
      SimpleList,
      SaveButton,
      SimpleItem
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

<style>
  .simple-item {
    margin-bottom: 1em;
  }
</style>
