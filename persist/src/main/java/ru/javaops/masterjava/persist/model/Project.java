package ru.javaops.masterjava.persist.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Project {

    private String description;

    private List<Group> groups;

}
