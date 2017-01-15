export const toMap = (array) => {
  if (array.length) {
    return Object.assign(...array.map(entry => ({[entry['id']]: entry})));
  } else {
    return {}
  }
};

export const copyData = (data) => {
  return JSON.parse(JSON.stringify(data));
};
