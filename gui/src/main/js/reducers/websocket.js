import {WEBSOCKET_MESSAGE} from "redux-middleware-websocket";
import { createAction, handleActions } from 'redux-actions';

export const sendData = createAction("SEND_DATA");

export default handleActions({
  [sendData](state, { payload }) {
    console.log("State lors de l'action SEND DATA! ", state);
    return state;
  },
}, {});

export const getSend = state => state.send;
