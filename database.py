import sqlite3


class Database:
    def __init__(self):
        self.conn = sqlite3.connect("database.db")
        self.cursor = self.conn.cursor()
        if not self.cursor.execute("select count(*) from sqlite_master where type='table'").fetchone()[0]:
            self.create_empty_database()
    
    def get_client(self, android_id):
        return self.cursor.execute("SELECT android_id, Clients.engine_name, engine_type FROM Clients INNER JOIN Engines WHERE android_id = ?", (android_id,)).fetchone()
    
    def get_past_measurements(self, journey_id):
        return self.cursor.execute("SELECT speed FROM Measurements WHERE journey_id = ?", (journey_id, ))
    
    def add_measurements(self, data, journey_id):
        for _data in data:
            self.cursor.execute("INSERT INTO Measurements(journey_id, measured_at, speed, fuel_consumption) VALUES (?, ?, ?, ?)", (journey_id, _data["time"], _data["speed"], _data["predict"]))
    
    def end_journey(self, id, data):
        self.cursor.execute("UPDATE Journeys SET avg_real_fuel_consumption = ? WHERE id = ?", (data, id))
    
    def get_data_for_stats(self):
        return self.cursor.execute("SELECT measured_at, speed, fuel_consumption, android_id, created_at, avg_predict_fuel_consumption, avg_real_fuel_consumption, total_route_length, journeys_interval, avg_speed, std_speed, min_speed, max_speed, avg_fuel_consumption, std_fuel_consumption, min_fuel_consumption, max_fuel_consumption FROM Measurements JOIN Journeys").fetchall()

    def add_new_client(self, android_id, engine):
        try:
            self.cursor.execute("INSERT INTO Clients (android_id, engine_name) VALUES (?, ?)", (android_id, engine))
        except sqlite3.IntegrityError:
            return 'MAC is in db'
    
    def add_new_journey(self, android_id):
        self.cursor.execute("INSERT INTO Journeys (android_id) VALUES (?)", (android_id, ))
        return self.cursor.lastrowid

    def update_client_engine(self, android_id, engine):
        self.cursor.execute("UPDATE Clients SET engine_name = ? WHERE android_id = ?", (engine, android_id))

    def add_new_measurement(self, *args):
        self.cursor.execute("INSERT INTO Measurements (journey_id, measured_at, speed, fuel_consumption)"
                            "VALUES (?, ?, ?, ?)", args)

    def get_speed_for_journey(self, journey):
        return self.cursor.execute("SELECT speed FROM Measurements where journey_id = ?", (journey, )).fetchall()

    def __enter__(self):
        return self
    
    def __exit__(self, exc_type, exc_val, exc_tb):
        self.conn.commit()
        self.conn.close()

    def create_empty_database(self):
        with open("database_structure.sql", "r") as dump:
            sql_as_string = dump.read()
            self.cursor.executescript(sql_as_string)

# with Database() as db:
#     db.create_empty_database()