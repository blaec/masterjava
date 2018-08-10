package ru.javaops.masterjava.service.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;
import ru.javaops.masterjava.persist.model.BaseEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SentMail extends BaseEntity {
    @Column("has_attachment") private boolean hasAttachment;
    @Column("from_mail") private String fromMail;
    @Column("from_name") private String fromName;
    private String subject;
    private String body;
    @Column("to_list") private String toList;
    @Column("cc_list") private String ccList;

    public SentMail(Integer id, boolean hasAttachment, String fromMail, String fromName, String subject, String body, String to, String cc) {
        this(hasAttachment, fromMail, fromName, subject, body, to, cc);
        this.id = id;
    }
}
