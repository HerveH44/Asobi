import {applyMiddleware} from 'redux';
import websocketMiddleware from './websocket';
import loggerMiddleware from './logger';
import createGameMiddleware from './createGame';
import router from "./router";
import websocket from "redux-middleware-websocket";
import chat from "./chat"

export default applyMiddleware(/*loggerMiddleware, */chat, createGameMiddleware, websocket, websocketMiddleware, router);
