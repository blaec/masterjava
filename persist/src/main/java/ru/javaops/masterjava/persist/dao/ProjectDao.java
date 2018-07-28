package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.Project;

import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class ProjectDao implements AbstractDao {
    public Project insert(Project project) {
        if (project.isNew()) {
            int id = insertGeneratedId(project);
            project.setId(id);
        } else {
            insertWithId(project);
        }
        List<Group> groups = project.getGroups();
        for (Group group : groups) {
            insertProjectGroups(project.getId(), group.getId());
        }
        return project;
    }

    @SqlUpdate("TRUNCATE project_groups, projects")
    @Override
    public void clean() {
    }

    @SqlUpdate("INSERT INTO projects (name, description) VALUES (:name, :description) ")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean Project project);

    @SqlUpdate("INSERT INTO projects (id, name, description) VALUES (:id, :name, :description) ")
    abstract void insertWithId(@BindBean Project project);

    @SqlQuery("SELECT * FROM projects LIMIT :it")
    public abstract List<Project> getWithLimit(@Bind int limit);

    @SqlUpdate("INSERT INTO project_groups (project_id, group_id) VALUES (:projectId, :groupId)")
    abstract void insertProjectGroups(@Bind("projectId") int projectId, @Bind("groupId") int getUserId);

    @SqlQuery("SELECT group_id FROM project_groups WHERE project_id=:id")
    public abstract List<Integer> getProjectGroups(@Bind("id") int id);
}
