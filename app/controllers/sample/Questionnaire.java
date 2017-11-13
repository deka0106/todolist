package controllers.sample;

import play.*;
import play.mvc.*;
import models.*;
import models.sample.SampleEntry;

import javax.persistence.*;

import java.util.*;

/**
 * 課題Aアンケートアプリのplay!による実装．
 */
public class Questionnaire extends Controller {

    public static void index() {
        // sample/Questionnaire/index.html に記述されたビューを描画
        render();
    }

    /**
     * 性別を入力するためのフォームを含むページを出力する．
     */
    public static void genderForm() {
        // sample/Questionnaire/genderForm.html に記述されたテンプレートを描画
        render();
    }

    /**
     * 性別データの送信を受け付ける．
     */
    public static void postGender() {
        // 入力された性別の情報をセッションに書き込む．
        // フォームのデータは params オブジェクトの中に格納されている．
        session.put("gender", params.get("gender"));

        // アクションメソッドを呼び出すことにより，該当URLにリダイレクトする．
        // ここでは sample.questionnaire/nameform にリダイレクトする．
        nameForm();
    }

    public static void nameForm() {
        render();
    }

    public static void postName() {
        session.put("name", params.get("name"));
        commentForm();
    }

    public static void commentForm() {
        render();
    }

    public static void postComment() {
        session.put("comment", params.get("comment"));
        confirm();
    }

    public static void confirm() {
        render();
    }

    /**
     * アンケートデータが送信された場合の処理．
     */
    public static void submit() {
        int gender;
        if ("男性".equals(session.get("gender"))) {
            gender = SampleEntry.MALE;
        } else {
            gender = SampleEntry.FEMALE;
        }
        String name = session.get("name");
        String comment = session.get("comment");

        // 標準出力に性別，名前，感想のデータを出力する．
        Logger.info("性別：%s", gender);
        Logger.info("名前：%s", name);
        Logger.info("感想：%s", comment);

        // アンケートデータをデータベースに書き込む
        SampleEntry entry = new SampleEntry(gender, name, comment);
        entry.save();

        // 埋め込むべき変数をテンプレートに渡す．
        // テンプレートからはキー名で参照できる．
        // テンプレートからは，${entry.id} として該当データで自動生成された
        // ID番号を取得している．
        renderArgs.put("entry", entry);

        // HTMLを出力する．
        render();
    }

    public static void list() {
        // 全エントリを取得する．
        List<SampleEntry> entries = SampleEntry.all().fetch();

        // 男性のエントリのみ取得する．
        // find メソッドにクエリ文（"gender = ?1"）と引数を指定することにより行う．
        // パラメータとして与えた SampleEntry.MALE が，?1 と指定した部分に対応する．
        // つまり，ここでは，genderの値が SampleEntry.MALE であるようなエントリのみを
        // 抽出するよう指示している．
        // ?1 の数字 1 は，1番目のパラメータであることを意味する．
        // （与える引数が2つ以上になれば，?1 ?2 と添える数字が大きくなっていく．）
        List<SampleEntry> males = SampleEntry.find("gender = ?1", SampleEntry.MALE).fetch();

        // 女性のエントリのみ取得する．
        List<SampleEntry> females = SampleEntry.find("gender = ?1", SampleEntry.FEMALE).fetch();

        // 得たエントリ群を，テンプレート側に渡す．
        renderArgs.put("entries", entries);
        renderArgs.put("males", males);
        renderArgs.put("females", females);

        render();
    }
}
