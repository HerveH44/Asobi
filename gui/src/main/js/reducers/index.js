import { combineReducers } from 'redux'
import todos from './todos'
import visibilityFilter from './visibilityFilter'
import websocket from "./websocket";

const todoApp = combineReducers({
  todos,
  visibilityFilter,
  websocket
});

export default todoApp
