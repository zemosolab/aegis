export const csvToObject = (bufferString, failedImport) => {
  let arr;
  arr = bufferString.split("\n");
  var jsonObj = [];
  var headers = arr[0].split(",");
  for (var i = 1; i < arr.length; i++) {
    var data = arr[i].split(",");
    if (data.length < headers.length && data.length !== 1) {
      failedImport(true);
      return null;
    }
    var obj = {};
    var include = true;
    for (var j = 0; j < data.length; j++) {
      if (data[j].trim() === "GUEST") {
        include = false;
      }
      if (
        include ||
        (headers[j].trim() !== "employeeId" &&
          headers[j].trim() !== "userEmail")
      ) {
        obj[headers[j].trim()] = data[j].trim();
      }
    }
    if (data.length === headers.length) jsonObj.push(obj);
  }
  JSON.stringify(jsonObj);
  return jsonObj;
};
