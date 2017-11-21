/* exp4todo.js */
/* -*- coding: utf-8 -*- */

/**
 * 登録フォームチェック
 * @param field フィールド("email", "password"など)
 */
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

/**
 * タスクの追加
 * @param task
 * @returns
 */
function addTask() {
  const req = new XMLHttpRequest();
  
  const wizard = document.getElementById("new-task-wizard");
  
  const title = "たいとる";
  const detail = "しょうさい";
  const dueDate = new Date(Date.now() + 1145141919).toLocaleString();
  const progress = 1;
  const priority = 1;
  const values = {
		  title,
		  detail,
		  dueDate,
		  progress,
		  priority
  }

  req.onreadystatechange = () => {
    if (req.readyState !== 4) return;
    if (req.status !== 200) {
      console.log("fail to add task");
      return;
    }

    const data = JSON.parse(req.responseText);

    console.log(data);
    updateStatus();
  }

  req.open("POST", "/board/addTask");
  req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
  req.send(toQuery(values));
}

/**
 * 表示しているタスクの更新
 * @returns
 */
function updateStatus() {
  
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
