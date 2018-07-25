DROP TABLE IF EXISTS projects;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS cities;
DROP SEQUENCE IF EXISTS GLOBAL_SEQ;
DROP TYPE IF EXISTS user_flag;
DROP TYPE IF EXISTS group_type;

CREATE TYPE user_flag AS ENUM ('active', 'deleted', 'superuser');
CREATE TYPE group_type AS ENUM ('REGISTERING', 'CURRENT', 'FINISHED');

CREATE SEQUENCE global_seq START 100000;

CREATE TABLE cities (
  id          INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
  name        TEXT NOT NULL
);

CREATE TABLE groups (
  id          INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
  name        TEXT NOT NULL,
  type        group_type NOT NULL
);

CREATE TABLE users (
  id          INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
  full_name   TEXT NOT NULL,
  email       TEXT NOT NULL,
  flag        user_flag NOT NULL,
  city_id     INTEGER NOT NULL,
  group_id    INTEGER NOT NULL,
  FOREIGN KEY (city_id) REFERENCES cities(id) ON DELETE CASCADE,
  FOREIGN KEY (group_id) REFERENCES groups(id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX email_idx ON users (email);

CREATE TABLE projects (
  id          INTEGER PRIMARY KEY DEFAULT nextval('global_seq'),
  name        TEXT NOT NULL,
  desciption  TEXT NOT NULL,
  group_id    INTEGER NOT NULL,
  FOREIGN KEY (group_id) REFERENCES groups(id) ON DELETE CASCADE
);