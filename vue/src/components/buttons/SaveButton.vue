<template>
  <button type="button" class="button is-primary" @click="emitSave">
    <span class="icon">
      <i class="fa fa-floppy-o"></i>
    </span>
    <span>Сохранить</span>
  </button>
</template>

<script>
  export default {
    methods: {
      emitSave() {
        this.$emit('save');
      }
    }
  }
</script>
