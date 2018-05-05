import {handleActions} from 'redux-actions';
import { WEBSOCKET_CLOSED, WEBSOCKET_OPEN } from 'redux-middleware-websocket';

const initialState = {
    connected: false
};

export default handleActions({
    [WEBSOCKET_OPEN](state, {payload}) {
        return {
            ...state,
            connected: true
        };
    },
    [WEBSOCKET_CLOSED](state, {payload}) {
        return {
            ...state,
            connected: false
        }
    }
}, initialState);
