<template>
  <tr>
    <td>{{name}}</td>
    <td>{{category}}</td>
    <td>
      <span class="tag is-info" v-for="item in packs">
        {{item.name}} x {{item.quantity}}
        <button class="delete is-small"></button>
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
          quantity: item.quantity
        }));
      }
    }
  }
</script>
