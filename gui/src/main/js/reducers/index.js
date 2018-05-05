import {combineReducers} from 'redux'
import site from "./site";
import game from "./game";
import gameSettings from "./gameSettings";
import player from "./player";
import websocket from "./websocket";
import {routerReducer as router} from 'react-router-redux';
import storage from 'redux-persist/lib/storage'
import {persistReducer} from 'redux-persist'

const rootPersistConfig = {
    key: 'root',
    storage,
    whitelist: ['site', 'game', 'player']
}

const reducers = combineReducers({
    site,
    game,
    gameSettings,
    router,
    player,
    websocket
});

export default persistReducer(rootPersistConfig, reducers)
