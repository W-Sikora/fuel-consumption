CREATE TABLE `Users` (
  `id` long,
  `login` String,
  `password` password
);

CREATE TABLE `Clients` (
  `MAC` byte,
  `engine` String
);

CREATE TABLE `Journeys` (
  `id` long,
  `MAC` byte,
  `created_at` datetime,
  `avg_preditect_fuel_consumption` float,
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
  `max_fuel_consumption` float
);

CREATE TABLE `Measurements` (
  `journey_id` long,
  `measured_at` datetime,
  `speed` float,
  `fuel_consumption` float
);

ALTER TABLE `Journeys` ADD FOREIGN KEY (`MAC`) REFERENCES `Clients` (`MAC`);

ALTER TABLE `Measurements` ADD FOREIGN KEY (`journey_id`) REFERENCES `Journeys` (`id`);
