package ru.javaops.masterjava.persist.dao;

import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.model.Group;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TestUtils {
    static List<Group> getGroups(List<Integer> groupIds) {
        Map<Integer, Group> groupMap = new HashMap<>();
        GroupDao groupDao = DBIProvider.getDao(GroupDao.class);
        List<Group> groups = new ArrayList<>();

        for (int groupId : groupIds) {
            Group group;
            if (groupMap.containsKey(groupId)) {
                group = groupMap.get(groupId);
            } else {
                group = groupDao.getById(groupId);
                groupMap.put(groupId, group);
            }
            groups.add(group);
        }

        return groups;
    }
}
