<template>
  <tr>
    <td>{{product.name}}</td>
    <td>{{product.category}}</td>
  </tr>
</template>

<script>
  export default {
    props: ['product']
  }
</script>
