import {WEBSOCKET_SEND} from "redux-middleware-websocket";

export const WS_SEND = (data) => ({
  type: WEBSOCKET_SEND,
  payload: data
});
