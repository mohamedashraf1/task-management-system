CREATE TABLE lo_task_status (
    status_id INT AUTO_INCREMENT PRIMARY KEY, 
    status_name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE `user` (
    user_id bigint AUTO_INCREMENT PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
	mobile_number VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    creation_date timestamp default current_timestamp,
    UNIQUE KEY email (email)
);

CREATE TABLE task (
    task_id bigint AUTO_INCREMENT PRIMARY KEY,
    user_id bigint not null,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    priority INT NOT NULL CHECK (priority BETWEEN 1 AND 5),
    due_date DATE,
    status_id INT NOT NULL,
    FOREIGN KEY (status_id) REFERENCES lo_task_status(status_id),
	FOREIGN KEY (user_id) REFERENCES user(user_id)
);

CREATE TABLE jwt_token (
  token_id bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
  access_token varchar(500) NOT NULL,
  refresh_token varchar(500) NOT NULL,
  user_id bigint NOT NULL,
  creation_date timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  is_valid int NOT NULL DEFAULT '1',
  updated_date timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY user_id_UNIQUE (user_id),
  FOREIGN KEY (user_id) REFERENCES user (user_id) ON DELETE CASCADE
);

CREATE TABLE role (
  role_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name varchar(45) DEFAULT NULL
);

CREATE TABLE user_role (
  user_id bigint NOT NULL,
  role_id int NOT NULL,
  PRIMARY KEY (user_id,role_id),
  FOREIGN KEY (role_id) REFERENCES role (role_id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES user (user_id)
);

