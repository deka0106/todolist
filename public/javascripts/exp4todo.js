/* exp4todo.js */
/* -*- coding: utf-8 -*- */

/**
 * 登録フォームチェック
 * @param key "name", "email", "password"
 */
function check(key) {
  const req = new XMLHttpRequest();

  req.onreadystatechange = () => {
    if (req.readyState !== 4) return;
    if (req.status !== 200) {
      console.error("fail to check " + key);
      return;
    }

    const data = JSON.parse(req.responseText);

    if (!data.ok) {
      document.getElementById(key + "_error").innerHTML = data.error;
    } else {
      document.getElementById(key + "_error").innerHTML = "";
    }
  }

  req.open("POST", "/validate/" + key);
  req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
  req.send("value=" + enc(document.getElementById(key).value));
}

/**
 * 文字列 -> HTML
 * @param str 文字列
 */
function toElement(str) {
  const tmp = document.createElement('div');
  tmp.innerHTML = str;
  return tmp.firstElementChild;
}

/**
 * 入力文字列を urlencode して返す．
 */
function enc(s) {
  return encodeURIComponent(s).replace(/%20/g, '+');
}

/**
 * 連想配列をクエリ文字列にする
 */
function toQuery(values) {
  let query = "";
  for (let key in values) {
    query += key;
    query += "=";
    query += enc(values[key]);
    query += "&";
  }
  query = query.substr(0, query.length - 1);
  return query;
}

/**
 * HTMLエスケープ
 * @param str 文字列
 * @returns
 */
function escapeHtml(str) {
  str += "";
  return str ? str.replace(/[&'`"<>]/g, c => {
    return {
      "&": "&amp;",
      "'": "&#x27;",
      "`": "&#x60;",
      '"': "&quot;",
      "<": "&lt;",
      ">": "&gt;"
    }[c];
  }) : "";
}

/**
 * Dateをyyyy/MM/dd HH:mm形式にする
 */
function dateToStr(date) {
  let str = "";
  str += date.getFullYear() + "/";
  str += ("00" + (date.getMonth() + 1)).slice(-2) + "/";
  str += ("00" + (date.getDate())).slice(-2) + " ";
  str += ("00" + (date.getHours())).slice(-2) + ":";
  str += ("00" + (date.getMinutes())).slice(-2);
  return str;
}