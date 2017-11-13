package models.sample;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class SampleThread extends Model {
    public String description;

    public SampleThread(final String description) {
        this.description = description;
    }
}
