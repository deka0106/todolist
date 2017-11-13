package controllers.sample;

import play.*;
import play.mvc.*;
import models.*;
import models.sample.SampleComment;
import models.sample.SampleThread;

import javax.persistence.*;

import java.util.*;

public class Board extends Controller {

    public static void index() {
        final List<SampleThread> threads = SampleThread.all().fetch();
        final Map<SampleThread, List<SampleComment>> commentMap = new HashMap<>();
        for (final SampleThread t : threads) {
            final List<SampleComment> comments = SampleComment.find("thread = ?1", t.id).fetch();
            commentMap.put(t, comments);
        }
        renderArgs.put("threads", threads);
        renderArgs.put("commentMap", commentMap);
        render();
    }

    public static void postThread() {
        final SampleThread thread = new SampleThread(params.get("description"));
        thread.save();
        index();
    }

    public static void postComment() {
        Long threadId;
        try {
            threadId = Long.valueOf(params.get("thread"));
        } catch (final NumberFormatException e) {
            error(403, "Format error");
            return;
        }
        final SampleThread thread = SampleThread.findById(threadId);
        if (thread == null) {
            error(403, "Thread not found");
            return;
        }
        final SampleComment comment = new SampleComment(threadId, params.get("description"));
        comment.save();
        index();
    }
}
