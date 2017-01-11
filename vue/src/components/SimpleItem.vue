<template>
  <div class="simple-item">
    <!--suppress HtmlFormInputWithoutLabel -->
    <input type="text" class="form-control" :placeholder="placeholder" v-model="item.name">
    <DeleteButton class="delete-button" @delete="$emit('deleteItem')"/>
  </div>
</template>

<script>
  import DeleteButton from 'components/buttons/DeleteButton'

  export default {
    props: ['item', 'placeholder'],
    components: {
      DeleteButton
    }
  }
</script>

<style>
  .delete-button {
    margin-left: 1em;
  }

  .simple-item {
    display: flex;
  }
</style>
