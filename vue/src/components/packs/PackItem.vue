<template>
  <div class="pack-item">
    <input type="text" class="form-control" placeholder="Название упаковки" v-model="pack.name">
    <DeleteButton class="delete-button" @delete="$emit('deletePack')"/>
  </div>
</template>

<script>
  import DeleteButton from 'components/buttons/DeleteButton'

  export default {
    props: ['pack'],
    components: {
      DeleteButton
    }
  }
</script>

<style>
  .delete-button {
    margin-left: 1em;
  }

  .pack-item {
    display: flex;
  }
</style>
