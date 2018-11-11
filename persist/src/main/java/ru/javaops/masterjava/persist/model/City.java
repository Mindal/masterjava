package ru.javaops.masterjava.persist.model;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(callSuper = true)
public class City extends BaseEntity {

    private @NonNull
    String code;

    private @NonNull
    String description;

    public City(Integer id, String code, String description) {
        this(code, description);
        this.id = id;
    }
}
