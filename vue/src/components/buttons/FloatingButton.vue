<template>
  <div class="floating-button" @click="$emit('addNew')">
    <p class="plus">+</p>
  </div>
</template>

<style>
  .floating-button {
    width: 55px;
    height: 55px;
    border-radius: 50%;
    background: #00d1b2;
    position: fixed;
    bottom: 30px;
    right: 30px;
    cursor: pointer;
    box-shadow: 0 2px 5px #666;
  }

  .floating-button .plus {
    color: white;
    position: absolute;
    top: 0;
    display: block;
    bottom: 0;
    left: 0;
    right: 0;
    text-align: center;
    padding: 0;
    margin: 0;
    line-height: 55px;
    font-size: 38px;
    font-family: 'Roboto';
    font-weight: 300;
  }
</style>
