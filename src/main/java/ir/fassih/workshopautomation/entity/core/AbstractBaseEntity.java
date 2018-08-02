package ir.fassih.workshopautomation.entity.core;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
public class AbstractBaseEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

}
