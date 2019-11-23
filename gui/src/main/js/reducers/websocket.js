import {handleActions} from 'redux-actions';
import {WEBSOCKET_CONNECTED, WEBSOCKET_CLOSED, WEBSOCKET_CONNECT} from "../middlewares/stomp";

const initialState = {
    connected: false
};

export default handleActions({
    [WEBSOCKET_CLOSED](state) {
        return {
            ...state,
            connected: false,
            connecting: false
        }
    },
    [WEBSOCKET_CONNECT](state) {
        return {
            ...state,
            connected: false,
            connecting: true
        }
    },
    [WEBSOCKET_CONNECTED](state) {
        return {
            ...state,
            connected: true,
            connecting: false
        };
    },
}, initialState);
