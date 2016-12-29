<template>
  <tr>
    <td>{{product.name}}</td>
    <td>{{product.category}}</td>
    <td>
      <span class="badge" v-for="item in productItems">{{item}}</span>
    </td>
  </tr>
</template>

<style>
  span.badge {
    margin-right: 1em;
  }
</style>

<script>
  export default {
    props: ['product'],
    computed: {
      productItems() {
        const result = [];
        for (let item of this.product.items) {
          for (let i = 0; i < item.quantity; i++) {
            result.push(item.pack);
          }
        }

        return result;
      }
    }
  }
</script>
