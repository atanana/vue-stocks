<template>
  <tr>
    <td>
      <input type="text" class="form-control" placeholder="Название категории" v-model="category.name">
    </td>
  </tr>
</template>

<script>
  export default {
    props: ['category']
  }
</script>
