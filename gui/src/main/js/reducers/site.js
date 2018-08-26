import {handleActions} from 'redux-actions';
import {WEBSOCKET_CLOSED, WEBSOCKET_DISCONNECT} from "redux-middleware-websocket";
import {ERROR, LOBBY_STATS} from "../actions/server"

const initialState = {
    siteTitle: "Asobi",
    error: "",
    users: 0,
    players: 0,
    games: 0,
    roomsInfo: [],
    motd: ["test motd"],
    repo: "https://github.com/herveh44/asobi",
    version: "FixMe"
};

export default handleActions({
    [ERROR](state, {payload}) {
        return {
            ...state,
            error: payload
        }
    },
    [WEBSOCKET_CLOSED](state, {payload}) {
        console.log(payload);
        return {
            ...state,
            error: "The connection with the server is closed. Please refresh your browser.\n" + payload.event.reason
        }
    },
    [WEBSOCKET_DISCONNECT](state, {payload}) {
        console.log(payload);
        return {
            ...state,
            error: "The connection with the server has been interrupted. Please refresh your browser.\n" + payload.event.reason
        }
    },
    [LOBBY_STATS](state, {payload}) {
        return {
            ...state,
            ...payload
        }

    }
}, initialState);
