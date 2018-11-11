package ru.javaops.masterjava.persist.dao;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.CityTestData;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class CityDaoTest extends AbstractDaoTest<CityDao> {

    public CityDaoTest() {
        super(CityDao.class);
    }

    @BeforeClass
    public static void setUp() {
        CityTestData.init();
    }

    @Test
    public void testFindAll() {
        List<City> all = dao.findAll();
        assertThat(all, equalTo(CityTestData.CITIES));
    }

    @Test
    public void testGetByCode() {
        City asdf = dao.getByCode("asdf");
        assertThat(asdf, is(nullValue()));

        City moscow = dao.getByCode("mow");
        assertThat(moscow, equalTo(CityTestData.MOSCOW));
    }




}
