<template>
  <button type="button" class="btn btn-primary" @click="emitSave">Сохранить</button>
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
