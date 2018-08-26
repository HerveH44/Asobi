import {handleActions} from 'redux-actions';
import {WEBSOCKET_CLOSED, WEBSOCKET_CONNECT, WEBSOCKET_OPEN} from 'redux-middleware-websocket';

const initialState = {
    connected: false
};

export default handleActions({
    [WEBSOCKET_OPEN](state) {
        return {
            ...state,
            connected: true,
            connecting: false
        };
    },
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
    }
}, initialState);
