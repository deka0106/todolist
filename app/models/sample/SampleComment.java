package models.sample;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class SampleComment extends Model {
    public String description;

    public Long thread;

    public SampleComment(final Long thread, final String description) {
        this.thread = thread;
        this.description = description;
    }
}
