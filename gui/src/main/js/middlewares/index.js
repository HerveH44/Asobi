import { applyMiddleware } from 'redux'
import websocketMiddleware from './websocket'
import loggerMiddleware from './logger'
import createGameMiddleware from './createGame'
import websocket from "redux-middleware-websocket";

export default applyMiddleware(loggerMiddleware,createGameMiddleware, websocket, websocketMiddleware)
