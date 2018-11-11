package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class CityDao implements AbstractDao {


    public City insert(City city) {
        if (city.isNew()) {
            int id = insertGeneratedId(city);
            city.setId(id);
        } else {
            InsertWithId(city);
        }
        return city;
    }

    @SqlUpdate("INSERT INTO cities (code, description) VALUES (:code, :description) ")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean City city);

    @SqlUpdate("INSERT INTO cities (id, code, description) VALUES (:id, :code, :description) ")
    abstract void InsertWithId(@BindBean City city);

    @SqlQuery("SELECT * FROM cities WHERE code= :it")
    public abstract City getByCode(@Bind String code);

    @SqlQuery("SELECT * FROM cities")
    public abstract List<City> findAll();



    //   http://stackoverflow.com/questions/13223820/postgresql-delete-all-content
    @SqlUpdate("TRUNCATE cities CASCADE")
    @Override
    public abstract void clean();

}
