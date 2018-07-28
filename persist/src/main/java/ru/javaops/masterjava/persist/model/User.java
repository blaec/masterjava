package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class User extends BaseEntity {
    @Column("full_name")
    private @NonNull String fullName;
    private @NonNull String email;
    private @NonNull UserFlag flag;
    private @NonNull City city;
    private @NonNull List<Group> groups;

    public User(Integer id, String fullName, String email, UserFlag flag, City city, List<Group> groups) {
        this(fullName, email, flag, city, groups);
        this.id=id;
    }

//    @ConstructorProperties({"id", "full_name", "email", "flag", "city_id"})
    public User(Integer id, String fullName, String email, UserFlag flag, int cityId) {
        this(fullName, email, flag, new City(), new ArrayList<Group>());
        this.city.setId(cityId);
        this.id=id;
    }
}