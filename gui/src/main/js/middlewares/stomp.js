import SockJS from "sockjs-client";
import * as StompJs from "@stomp/stompjs";
import {ERROR, LOBBY_STATS} from "../actions/server";

export const WEBSOCKET_CONNECT = "WEBSOCKET_CONNECT";
export const WEBSOCKET_CONNECTED = "WEBSOCKET_CONNECTED";
export const WEBSOCKET_CLOSED = "WEBSOCKET_CLOSED";
export const WEBSOCKET_SEND = "WEBSOCKET_SEND";

let client = new StompJs.Client({
    webSocketFactory: () => new SockJS('/stomp'),
    debug: function (str) {
        console.log(str);
    },
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
    onConnect: frame => {
        client.dispatch({
            type: WEBSOCKET_CONNECTED
        });
        client.subscribe('/user/queue/reply', ({body}) => {
            client.dispatch(JSON.parse(body));
        });
        client.subscribe('/app/sets', ({body}) => {
            client.dispatch(JSON.parse(body));
        });
        client.subscribe('/topic/lobby', ({body}) => {
            const payload = JSON.parse(body);
            client.dispatch({
                type: LOBBY_STATS,
                payload
            });
        });
    },
    onWebSocketClose: frameElement => {
        client.dispatch({
            type: WEBSOCKET_CLOSED
        })
    },
    onDisconnect: frameElement => {
        client.dispatch({
            type: WEBSOCKET_CLOSED
        })
    },
    onStompError: (frame) => {
    // Will be invoked in case of error encountered at Broker
    // Bad login/passcode typically will cause an error
    // Complaint brokers will set `message` header with a brief message. Body may contain details.
    // Compliant brokers will terminate the connection after any error
    console.log('Broker reported error: ' + frame.headers['message']);
    console.log('Additional details: ' + frame.body);
}
});

const connect = (dispatch) => {
    client.dispatch = dispatch;
    client.activate();
};

const stompMWare = ({getState, dispatch}) => next => action => {
    const {type, metadata, payload} = action;

    if (WEBSOCKET_SEND === type) {

        if (!client) {
            dispatch({
                type: ERROR,
                payload: "The socket has not been initialized. Try to connect first."
            });

        } else {
            client.publish({
                destination: metadata ? metadata : "/app/game",
                body: JSON.stringify(payload)
            });
        }
    }

    if (WEBSOCKET_CONNECT === type) {
        connect(dispatch);
    }

    next(action);

};


export default stompMWare;
