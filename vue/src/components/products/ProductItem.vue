<template>
  <tr>
    <td>{{name}}</td>
    <td>{{category}}</td>
    <td>
      <span class="tag is-info" v-for="item in packs">
        {{item.name}} x {{item.quantity}}
        <button class="delete is-small" @click="deleteItem(item.id, item.name)"></button>
      </span>
    </td>
  </tr>
</template>

<style>
  span.tag {
    margin-right: 1em;
  }
</style>

<script>
  export default {
    props: ['product'],
    computed: {
      name() {
        if (this.product.productType) {
          return this.product.productType.name;
        } else {
          return '-';
        }
      },
      category() {
        if (this.product.category) {
          return this.product.category.name;
        } else {
          return '-';
        }
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
