import { combineReducers } from 'redux'
import websocket from "./websocket";
import def from "./default";

const reducers = combineReducers({
  def,
  websocket
});

export default reducers
