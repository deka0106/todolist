/* exp4todo.js */
/* -*- coding: utf-8 -*- */

function check(field) {
  const req = new XMLHttpRequest();

  req.onreadystatechange = () => {
    if (req.readyState !== 4) return;
    if (req.status !== 200) {
      console.log("fail to check " + field);
      return;
    }

    const data = JSON.parse(req.responseText);

    console.log(data);
    if (!data.ok) {
      document.getElementById(field + "_error").innerHTML = data.error;
    } else {
      document.getElementById(field + "_error").innerHTML = "";
    }
  }

  req.open("POST", "/validation/" + field);
  req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
  req.send("value=" + enc(document.getElementById(field).value));
}

function checkName() {
  check("name");
}

function checkEmail() {
  check("email");
}

function checkPassword() {
  check("password");
}

/**
 * 入力文字列を urlencode して返す．
 */
function enc(s) {
  return encodeURIComponent(s).replace(/%20/g, '+');
}