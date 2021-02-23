from flask import Flask, request, jsonify, render_template
from tensorflow.keras.models import load_model
import numpy as np
from database import Database

app = Flask(__name__)
model = load_model('network.h5')

journeys = dict()


def transform(array):
    return array.reshape(array.shape[0], 2, 1)


@app.route('/api/add', methods=['POST', 'GET'])
def add():
    data = request.json
    with Database() as db:
        client = db.get_client(data["android_id"])
        if not client:
            db.add_new_client(data["android_id"], data["engine"])
            client = db.get_client(data["android_id"])
        elif client[1] != data["engine"]:
            db.update_client_engine(data["android_id"], data["engine"])
        if client[0] not in journeys:
            journey_key = db.add_new_journey(client[0])
            journeys[client[0]] = journey_key
    return jsonify('ok')


@app.route('/api/predict', methods=['POST', 'GET'])
def predict():
    data = request.json
    try:
        journey_id = journeys[data["android_id"]]
    except KeyError:
        with Database() as db:
            journey_id = db.add_new_journey(data["android_id"])
            journeys[data["android_id"]] = journey_id
    with Database() as db:
        engine_id = db.get_client(data["android_id"])[2]
        past = db.get_past_measurements(engine_id)
        measurements = [[x[0], engine_id] for x in past]
        new_data_len = len(data["data"])
        new_measurements = [[x["speed"], engine_id] for x in data["data"]]
        measurements.extend(new_measurements)
    measurements = np.array(measurements)
    measurements = transform(measurements)

    measurements = model.predict(measurements)

    for ind, _data in enumerate(data["data"]):
        _data["predict"] = measurements[(-new_data_len) + ind, 0]

    with Database() as db:
        db.add_measurements(data["data"], journey_id)

    return jsonify(round(float(measurements[-1, 0]), 2))


@app.route('/api/end', methods=['POST', 'GET'])
def end():
    data = request.json
    with Database() as db:
        db.end_journey(journeys[data["android_id"]], data["real_fuel_consumption"])
    del journeys[data["android_id"]]
    return jsonify("ok")


@app.route('/stats', methods=['POST', 'GET'])
def statistics():
    with Database() as db:
        data = db.get_data_for_stats()
    headers = ['measured_at', 'speed', 'fuel_consumption', 'android_id', 'created_at',
               'avg_predict_fuel_consumption', 'avg_real_fuel_consumption',
               'total_route_length',
               'journeys_interval',
               'avg_speed',
               'std_speed',
               'min_speed',
               'max_speed',
               'avg_fuel_consumption',
               'std_fuel_consumption',
               'min_fuel_consumption',
               'max_fuel_consumption']
    print(data)
    return render_template('index.html',
                           headers=headers,
                           objects=data)


if __name__ == "__main__":
    app.run()
