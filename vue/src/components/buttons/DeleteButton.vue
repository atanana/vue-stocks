<template>
  <button type="button" class="button is-danger" @click="$emit('delete')">
    <span class="icon">
      <i class="fa fa-times"></i>
    </span>
    <span>Удалить</span>
  </button>
</template>

<script>
  export default {}
</script>
