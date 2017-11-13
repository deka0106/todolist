package controllers.sample;

import java.util.HashMap;
import java.util.Map;

import play.data.validation.Required;
import play.mvc.Controller;

public class Ajax extends Controller {

    public static void index() {
        render();
    }

    public static void convert() {
        // 標準出力に入力されたパラメータの名前を出力する．
        System.out.println(params.all().keySet());

        String p = params.get("p");
        
        // pの最後の文字を最初に移動させた文字列を作成する．
        String last = p.substring(p.length() - 1, p.length());
        String rest = p.substring(0, p.length() - 1);
        String result = last + rest;

        // Map に結果を蓄え，JSON として出力する．
        Map<String, Object> map = new HashMap<>();
        map.put("status", "OK");
        map.put("result", result);
        renderJSON(map);
    }
}
