<template>
  <input type="text" class="form-control" placeholder="Название категории" v-model="category.name">
</template>

<script>
  export default {
    props: ['category']
  }
</script>
