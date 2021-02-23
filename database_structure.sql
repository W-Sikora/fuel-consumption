CREATE TABLE `Users` (
  `id` INTEGER primary key autoincrement,
  `login` text,
  `password` password
);

CREATE TABLE `Engines` (
    `engine_name` text primary key,
    `engine_type` INTEGER
);

CREATE TABLE `Clients` (
  `android_id` text primary key,
  `engine_name` text,

  FOREIGN KEY (`engine_name`) REFERENCES `Engines` (engine_name)
);

CREATE TABLE `Journeys` (
  `id` INTEGER primary key autoincrement,
  `android_id` text,
  `created_at` datetime,
  `avg_predict_fuel_consumption` float,
  `avg_real_fuel_consumption` float,
  `total_route_length` float,
  `journeys_interval` long,
  `avg_speed` float,
  `std_speed` float,
  `min_speed` float,
  `max_speed` float,
  `avg_fuel_consumption` float,
  `std_fuel_consumption` float,
  `min_fuel_consumption` float,
  `max_fuel_consumption` float,

  FOREIGN KEY (`android_id`) REFERENCES `Clients` (android_id)
);

CREATE TABLE `Measurements` (
  `id` INTEGER primary key autoincrement,
  `journey_id` INTEGER,
  `measured_at` datetime,
  `speed` float,
  `fuel_consumption` float,

  FOREIGN KEY (`journey_id`) REFERENCES `Journeys` (`id`)
);

INSERT INTO Engines VALUES ('engine1', 1), ('engine2', 2), ('engine3', 3)