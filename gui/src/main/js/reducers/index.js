import {combineReducers} from 'redux'
import site from "./site";
import game from "./game";
import gameSettings from "./gameSettings";
import {routerReducer as router} from 'react-router-redux';

const reducers = combineReducers({site, game, gameSettings, router});

export default reducers
