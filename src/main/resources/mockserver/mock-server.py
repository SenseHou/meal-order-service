import flask,json
from flask import request

server = flask.Flask(__name__)

@server.route('/wechat-pay', methods=['post'])
def pay():
    value = request.json.get('amount')
    response_data_success = {
        "code":"SUCCESS"
    }
    response_data_fail = {
        "code":"LACK_OF_BALANCE"
    }
    if value > 40:
        return json.dumps(response_data_fail, ensure_ascii=False),{'Content-Type': 'application/json'}

    else:
        return json.dumps(response_data_success, ensure_ascii=False),{'Content-Type': 'application/json'}

if __name__ == '__main__':
    server.run(debug=True, port=5000, host='127.0.0.1')
