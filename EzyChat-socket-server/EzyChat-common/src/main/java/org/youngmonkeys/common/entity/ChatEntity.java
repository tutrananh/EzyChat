package org.youngmonkeys.common.entity;

import com.tvd12.ezyfox.io.EzyDates;
import lombok.Data;

import java.util.Date;

@Data
public class ChatEntity {
    private Date creationDate = new Date();
    private Date lastReadDate = new Date();
    private Date lastModifiedDate = new Date();
    private int day = EzyDates.formatAsInteger(new Date());

}
