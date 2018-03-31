import { combineReducers } from 'redux'
import site from "./site";
import game from "./game";

const reducers = combineReducers({
  site,
  game
});

export default reducers
