/**
 * XMLHttpRequest のサンプル．
 */
function xhrsample() {
    var req = new XMLHttpRequest();
    // 送信先のURLを指定する．
    req.open("POST", "/sample.ajax/convert");
    // 結果が帰ってきた際に実行されるハンドラを指定する．
    req.onreadystatechange = function () {
        // readyState == 4: 修了
        if (req.readyState != 4) {
            return;
        }
        // status == 200: 成功
        if (req.status != 200) {
            // 成功しなかったため，失敗であることを表示して抜ける．
            alert("失敗しました．");
            return;
        }
        
        // body にはサーバから返却された文字列が格納される．
        var body = req.responseText;
        // デバッグ表示 
        alert('body: ' + body);

        // 戻ってきた JSON 文字列を JavaScript オブジェクトに変換
        var data = eval("(" + body + ")");

        // デバッグ表示
        alert('result: ' + data.result);

        // 入力に利用したインプットボックスの値を，
        // サーバから戻ってきた値で書き換える．
        document.getElementById("f").value = data.result;
    }
    
    // Content-Type の指定
    req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    // <input id="f"> に入力された文字列をエンコードして送信        
    req.send("p=" + enc(document.getElementById("f").value));
}

/**
 * 入力文字列を urlencode して返す．
 */
function enc(s) {
    return encodeURIComponent(s).replace(/%20/g, '+');
}

/**
 * id="list" のリストに要素を追加する．
 */
function add() {
    var li = document.createElement('li');
    var text = document.createTextNode('new item');
    li.appendChild(text);
    var list = document.getElementById('list');
    list.appendChild(li);
}

/**
 * id="list" のリストの0番目のli要素の内容を変更する．
 */
function modify1() {
    var li = document.getElementById('listitem0');
    var text = li.childNodes[0];
    var newText = document.createTextNode(text.textContent + '!');
    li.replaceChild(newText, text);
}
function modify2() {
    var li = document.getElementById('listitem0');
    li.innerHTML = li.innerHTML + '?';
}
