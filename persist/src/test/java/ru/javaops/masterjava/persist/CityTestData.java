package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.CityDao;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;

public class CityTestData {
    public static City MOSCOW;
    public static City SPB;
    public static City KIEV;
    public static City MINSK;

    public static List<City> CITIES;

    public static void init() {
        MOSCOW = new City("mow", "Москва");
        SPB = new City("spb", "Санкт-Петербург");
        KIEV = new City("kiv", "Киев");
        MINSK = new City("mnsk", "Минск");

        CITIES = ImmutableList.of(MOSCOW, SPB, KIEV, MINSK);

        CityDao dao = DBIProvider.getDao(CityDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction(((conn, status) -> CITIES.forEach(dao::insert)));
    }
}
