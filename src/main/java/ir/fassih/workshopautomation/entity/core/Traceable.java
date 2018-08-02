package ir.fassih.workshopautomation.entity.core;

import java.util.Date;

public interface Traceable {

    Date getCreateDate();
    void setCreateDate(Date createDate);

    Date getLastModificationDate();
    void setLastModificationDate(Date lastModificationDate);


}
