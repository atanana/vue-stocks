<template>
  <tr>
    <td>{{safeProductType.name}}</td>
    <td>{{safeCategory.name}}</td>
    <td>
      <span class="tag is-info" v-for="item in packs">
        {{item.name}} x {{item.quantity}}
        <button class="delete is-small" @click="deleteItem(item.id, item.name)"></button>
      </span>
      <AddProductPackPopup :categoryId="safeCategory.id" :productTypeId="safeProductType.id" :packs="packTypes"/>
    </td>
  </tr>
</template>

<style>
  span.tag {
    margin-right: 1em;
  }
</style>

<script>
  import AddProductPackPopup from 'components/products/AddProductPackPopup'

  export default {
    props: ['product', 'packTypes'],
    components: {
      AddProductPackPopup
    },
    computed: {//todo refactor
      safeProductType() {
        return this.product.productType || {id: 0, name: '-'};
      },
      safeCategory() {
        return this.product.category || {id: 0, name: '-'}
      },
      packs() {
        return this.product.packs.map(item => ({
          name: item.pack ? item.pack.name : '-',
          quantity: item.quantity,
          id: item.pack ? item.pack.id : 0
        }));
      }
    },
    methods: {
      deleteItem(packId, packName) {
        if (confirm(`Вы действительно хотите удалить ${this.name} ${packName}`)) {
          this.$store.dispatch('deleteProduct', {
            productTypeId: this.product.productType.id,
            categoryId: this.product.category.id,
            packId: packId,
          });
        }
      }
    }
  }
</script>
