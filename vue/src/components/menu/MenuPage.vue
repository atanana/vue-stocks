<template>
  <div>
    <SimpleList
      :items="itemsData"
      newItemLabel="Добавить блюдо"
      ref="items"
    >
      <template slot="item" scope="props">
        <MenuEntryItem
          :item="props.item"
          class="simple-item"
          @deleteItem="$refs.items.deleteItem(props.item)"
        />
      </template>
    </SimpleList>
    <SaveButton ref="saveButton" @save="save(itemsData)"/>
  </div>
</template>

<script>
  import SimpleList from 'components/SimpleList';
  import SaveButton from 'components/buttons/SaveButton';
  import {copyData} from 'utility/objectUtils';
  import MenuEntryItem from 'components/menu/MenuEntryItem';

  export default {
    components: {
      SimpleList,
      SaveButton,
      MenuEntryItem
    },
    computed: {
      menuItems() {
        return copyData(this.$store.state.menuItems);
      }
    },
    data() {
      return {
        itemsData: copyData(this.$store.state.menuItems)
      }
    },
    watch: {
      menuItems(newItems) {
        this.itemsData = copyData(newItems)
      }
    },
    created() {
      this.$store.dispatch('loadMenuItems');
    },
    methods: {
      save(newMenuItems) {
        this.$store.dispatch('updateMenuItems', newMenuItems);
      }
    },
  }
</script>
