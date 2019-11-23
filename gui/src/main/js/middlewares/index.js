import {applyMiddleware} from 'redux';
import game from './game';
import router from "./router";
import chat from "./chat"
import stomp from "./stomp";

export default applyMiddleware(chat, game, stomp, router);
