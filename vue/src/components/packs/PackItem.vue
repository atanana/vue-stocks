<template>
  <div class="category-item">
    <input type="text" class="form-control" placeholder="Название категории" v-model="category.name">
    <DeleteButton class="delete-button" @delete="$emit('deleteCategory')"/>
  </div>
</template>

<script>
  import DeleteButton from 'components/buttons/DeleteButton'

  export default {
    props: ['category'],
    components: {
      DeleteButton
    }
  }
</script>

<style>
  .delete-button {
    margin-left: 1em;
  }

  .category-item {
    display: flex;
  }
</style>
