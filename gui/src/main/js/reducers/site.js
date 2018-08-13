import {createAction, handleActions} from 'redux-actions';
import {WEBSOCKET_CLOSED, WEBSOCKET_DISCONNECT} from "redux-middleware-websocket";
export const editDefault = createAction("EDIT_DEFAULT");

const initialState = {
    siteTitle: "Asobi",
    error: "",
    numUsers: 0,
    numPlayers: 0,
    numActiveGames: 0,
    roomsInfo: [],
    motd: ["test motd"],
    repo: "https://github.com/herveh44/asobi",
    version: "FixMe"
};

export default handleActions({
    [editDefault](state, {payload}) {
        return {
            ...state,
            ...payload
        };
    },
    "ERROR"(state, {payload}) {
        return {
            ...state,
            error: payload
        }
    },
    [WEBSOCKET_CLOSED](state, {payload}) {
        console.log(payload);
        return {
            ...state,
            error: "The connection with the server is closed. Please refresh your browser."
        }
    },
    [WEBSOCKET_DISCONNECT](state, {payload}) {
        console.log(payload);
        return {
            ...state,
            error: "The connection with the server has been interrupted. Please refresh your browser."
        }
    }
}, initialState);
