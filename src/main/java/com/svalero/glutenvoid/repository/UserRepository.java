package com.svalero.glutenvoid.repository;

import com.svalero.glutenvoid.domain.GlutenCondition;
import com.svalero.glutenvoid.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {

    List<User> getAll();

    List<User> getByGlutenCondition(GlutenCondition glutenCondition);

    List<User> getByAdmin(boolean isAdmin);

}
