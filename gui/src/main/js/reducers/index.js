import {combineReducers} from 'redux'
import site from "./site";
import game from "./game";
import gameState from "./gameState";
import gameSettings from "./gameSettings";
import player from "./player";
import playerState from "./playerState";
import websocket from "./websocket";
import {routerReducer as router} from 'react-router-redux';
import storage from 'redux-persist/lib/storage'
import {persistReducer} from 'redux-persist'

const rootPersistConfig = {
    key: 'root',
    storage,
    whitelist: ['game', 'player']
};

const reducers = combineReducers({
    site,
    game,
    gameState,
    gameSettings,
    router,
    player,
    playerState,
    websocket
});

export default persistReducer(rootPersistConfig, reducers)
