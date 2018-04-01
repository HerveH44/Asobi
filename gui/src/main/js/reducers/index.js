import {combineReducers} from 'redux'
import site from "./site";
import game from "./game";
import {routerReducer as router} from 'react-router-redux';

const reducers = combineReducers({site, game, router});

export default reducers
