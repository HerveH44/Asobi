import { createStore as reduxCreateStore, applyMiddleware } from "redux";
import websocket from "redux-middleware-websocket";
import reducers from "../reducers";

const createStore = () =>
  reduxCreateStore(reducers, applyMiddleware(websocket));

export default createStore;
