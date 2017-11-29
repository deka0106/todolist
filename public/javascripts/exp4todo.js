/* exp4todo.js */
/* -*- coding: utf-8 -*- */

const PROGRESS = {
  0: "todo",
  1: "doing",
  2: "done"
};

const taskMap = {
  0: [],
  1: [],
  2: []
};
const taskIdToEl = {};
const taskIdToTask = {};

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
 * タスクの追加
 * @returns
 */
function addTask() {
  const req = new XMLHttpRequest();

  let tmp = document.getElementById("new-title");
  const title = tmp ? tmp.value : null;
  tmp.value = "";
  tmp = document.getElementById("new-detail");
  const detail = tmp ? tmp.value : null;
  tmp.value = "";
  tmp = document.getElementById("new-due");
  const dueDate = tmp && tmp.value !== "" ? new Date(tmp.value).toLocaleString() : null;
  if (tmp) tmp.value = "";
  tmp = document.getElementById("new-progress");
  const progress = tmp && tmp.value ? parseInt(tmp.value) : null;
  if (tmp) tmp.value = "";
  tmp = document.getElementById("new-priority");
  const priority = tmp && tmp.value ? parseInt(tmp.value) : null;
  tmp.value = "";
  const values = {
    title,
    detail,
    dueDate,
    progress,
    priority
  };

  req.onreadystatechange = () => {
    if (req.readyState !== 4) return;
    if (req.status !== 200) {
      console.error("fail to add task");
      return;
    }

    const data = JSON.parse(req.responseText);

    if (!data.ok) {
      console.error("something wrong add task");
    } else {
      const t = data.task;
      const task = makeTaskHtml(t.id, t.title, t.detail, t.dueDate, t.priority);
      taskIdToEl[t.id] = task;
      taskIdToTask[t.id] = t;
      const list = document.getElementById(PROGRESS[t.progress]);
      const index = insertTask(t);
      const next = list.children[index];
      list.insertBefore(task, next);
    }
  }

  req.open("POST", "/board/addTask");
  req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
  req.send(toQuery(values));
}

/**
 * タスクの編集
 * @returns
 */
function editTask() {
  const req = new XMLHttpRequest();

  let tmp = document.getElementById("new-title");
  const title = tmp ? tmp.value : null;
  tmp.value = "";
  tmp = document.getElementById("new-detail");
  const detail = tmp ? tmp.value : null;
  tmp.value = "";
  tmp = document.getElementById("new-due");
  const dueDate = tmp && tmp.value !== "" ? new Date(tmp.value).toLocaleString() : null;
  tmp.value = "";
  tmp = document.getElementById("new-progress");
  const progress = tmp && tmp.value ? parseInt(tmp.value) : null;
  tmp.value = "";
  tmp = document.getElementById("new-priority");
  const priority = tmp && tmp.value ? parseInt(tmp.value) : null;
  tmp.value = "";
  const values = {
    title,
    detail,
    dueDate,
    progress,
    priority
  };

  req.onreadystatechange = () => {
    if (req.readyState !== 4) return;
    if (req.status !== 200) {
      console.error("fail to add task");
      return;
    }

    const data = JSON.parse(req.responseText);

    console.log(data);
  }

  req.open("POST", "/board/addTask");
  req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
  req.send(toQuery(values));
}

/**
 * タスクの進捗状態を変更
 * @param id タスクID
 * @param dir 方向
 * @returns
 */
function moveTask(id, dir) {
  let taskEl = taskIdToEl[id];
  let task = taskIdToTask[id];
  let list = taskEl.parentNode;

  const p = parseInt(task.progress);
  if (p <= 0 && dir <= 0) return;
  else if (p >= 2 && dir >= 1) return;
  const newp = p + (dir <= 0 ? -1 : 1);

  const values = {
    taskId: task.id,
    progress: newp
  };

  const req = new XMLHttpRequest();

  req.onreadystatechange = () => {
    if (req.readyState !== 4) return;
    if (req.status !== 200) {
      console.error("fail to move task");
      return;
    }

    const data = JSON.parse(req.responseText);

    if (!data.ok) {
      console.error("something wrong move task");
    } else {
      list.removeChild(taskEl);
      task = data.task;
      taskEl = makeTaskHtml(task.id, task.title, task.detail, task.dueDate, task.priority);
      taskIdToEl[id] = taskEl;
      taskIdToTask[id] = task;
      list = document.getElementById(PROGRESS[task.progress]);
      const index = insertTask(task);
      const next = list.children[index];
      list.insertBefore(taskEl, next);
    }

  }

  req.open("POST", "/board/editTask");
  req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
  req.send(toQuery(values));
}

/**
 * タスクをロードする
 * @params progress 進捗状況
 * @params num ロードする数
 */
function loadTasks(progress, num) {
  const list = document.getElementById(PROGRESS[progress]);
  const tasks = list.getElementsByClassName("task");

  const values = {
    first: tasks.length,
    count: num,
    progress
  };

  const req = new XMLHttpRequest();

  req.onreadystatechange = () => {
    if (req.readyState !== 4) return;
    if (req.status !== 200) {
      console.error("fail to load task");
      return;
    }

    const data = JSON.parse(req.responseText);

    if (!data.ok) {
      console.error("something wrong load task");
    } else {
      data.tasks.forEach(t => {
        const task = makeTaskHtml(t.id, t.title, t.detail, t.dueDate, t.priority);
        taskIdToTask[t.id] = t;
        taskIdToEl[t.id] = task;
        const index = insertTask(t);
        const next = list.children[index];
        list.insertBefore(task, next);
      });
    }

  }

  req.open("POST", "/board/getTasks");
  req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
  req.send(toQuery(values));
}

loadTasks(0, 10);
loadTasks(1, 10);
loadTasks(2, 10);

/**
 * taskMapに挿入
 * @returns インデックス
 */
function insertTask(task) {
  const p = task.progress;
  let left = 0,
    right = taskMap[p].length;
  let index = 0;
  while (left < right) {
    let mid = Math.floor((left + right) / 2);
    const t = taskMap[p][mid];
    if (compareTask(t, task)) {
      left = mid + 1;
    } else {
      right = mid;
    }
    index = left;
  }
  taskMap[p].splice(index, 0, task);
  return index;
}

/**
 * Taskを比較
 */
function compareTask(t1, t2) {
  if (t1.priority < t2.priority) return true;
  else if (t1.priority > t2.priority) return false;

  if (t1.dueDate && !t2.dueDate) return true;
  else if (!t1.dueDate && t2.dueDate) return false;

  if (t1.dueDate && t2.dueDate) {
    const d1 = new Date(t1.dueDate);
    const d2 = new Date(t2.dueDate);
    if (d1 < d2) return true;
    else if (d1 > d2) return false;
  }

  return t1.createDate < t2.createDate;
}

/**
 * 表示しているタスクの更新
 * @params progress 進捗状況
 */
function updateTasks(progress) {
  const list = document.getElementById(PROGRESS[progress]);
  const tasks = list.getElementsByClassName("task");
  const next = list.children[tasks.length];

  while (list.firstChild) list.removeChild(list.firstChild);
  list.appendChild(next);

  for (let i in taskMap[progress]) {
    const t = taskMap[progress][i];
    let task;
    if (taskIdToEl[t.id]) {
      task = taskIdToEl[t.id];
    } else {
      task = makeTaskHtml(t.id, t.title, t.detail, t.dueDate, t.priority);
      taskIdToEl[t.id] = task;
    }
    list.insertBefore(task, next);
  }
}

/**
 * タスクのHTML
 */
function makeTaskHtml(id, title, detail, dueDate, priority) {
  let str = '';
  str += '<div onclick="' + "openTask('" + escapeHtml(id) + "')" + '" class="task">';
  str += '<div onclick="' + "moveTask('" + escapeHtml(id) + "', 0)" + '" class="move-task move-task-left"></div>';
  str += '<div onclick="' + "moveTask('" + escapeHtml(id) + "', 1)" + '" class="move-task move-task-right"></div>';
  str += '<div class="task-title">' + escapeHtml(title) + '</div>';
  str += '<div class="task-detail">' + escapeHtml(detail) + '</div>';
  if (dueDate) str += '<div class="task-dueDate">' + escapeHtml(dateToStr(new Date(dueDate))) + '</div>';
  str += '<div class="task-priority">' + escapeHtml(priority) + '</div>';
  str += '</div>';
  return toElement(str);
}

/**
 * タスク編集画面
 * @param id タスクID
 */
function openTask(id) {}


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