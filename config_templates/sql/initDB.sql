DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS user_seq;
DROP TYPE IF EXISTS user_flag;

DROP TABLE IF EXISTS cities;
DROP SEQUENCE IF EXISTS city_seq;

CREATE TYPE user_flag AS ENUM ('active', 'deleted', 'superuser');

CREATE SEQUENCE user_seq
  START 100000;

CREATE TABLE users (
  id        INTEGER PRIMARY KEY DEFAULT nextval('user_seq'),
  full_name TEXT      NOT NULL,
  email     TEXT      NOT NULL,
  flag      user_flag NOT NULL
);

CREATE UNIQUE INDEX email_idx
  ON users (email);

CREATE SEQUENCE city_seq
  START 1000;

CREATE TABLE cities (
  id          INTEGER PRIMARY KEY DEFAULT nextval('city_seq'),
  code        TEXT NOT NULL UNIQUE,
  description TEXT NOT NULL
);

ALTER TABLE users
  ADD city_id INTEGER,
  ADD CONSTRAINT users_cities_id_fk
FOREIGN KEY (city_id) REFERENCES cities (id);
