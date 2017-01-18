<template>
  <button type="button" class="btn btn-default new-item-button" @click="$emit('addNew')">
    <span class="glyphicon glyphicon-plus"></span>
    {{label}}
  </button>
</template>

<script>
  export default {
    props: ['label']
  }
</script>

<style>
  .new-item-button {
    margin-bottom: 1em;
  }
</style>
