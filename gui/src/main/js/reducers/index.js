import { combineReducers } from 'redux'
import websocket from "./websocket";
import site from "./site";
import game from "./game";

const reducers = combineReducers({
  site,
  game,
  websocket
});

export default reducers
