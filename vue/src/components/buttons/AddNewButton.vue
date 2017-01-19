<template>
  <button type="button" class="button new-item-button" @click="$emit('addNew')">
    <span class="icon">
      <i class="fa fa-plus"></i>
    </span>
    <span>{{label}}</span>
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
