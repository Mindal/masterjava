package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Group {

    private String name;

    @Column("group_type")
    private GroupType groupType;
}
