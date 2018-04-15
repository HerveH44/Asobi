import {CREATE_GAME} from "../reducers/game";
import {WEBSOCKET_SEND} from "redux-middleware-websocket";
import {push} from "react-router-redux";
import {GAME_ID, joinGame} from "../actions/server";

const createGame = ({getState, dispatch}) => next => action => {
    const {
        game: {
            isPrivate,
            title,
            seats,
            gameType,
            gameMode
        },
        gameSettings: {
            gameId
        }
    } = getState();

    switch (action.type) {
        case "GAME_ID":
            next(action);
            return dispatch(push("games/" + action.payload));
        case "CREATE_GAME":
            return dispatch({
                type: WEBSOCKET_SEND,
                payload: {
                    type: "CREATE_GAME",
                    isPrivate,
                    title,
                    seats,
                    gameType,
                    gameMode
                }
            });
        case "JOIN_GAME":
            return dispatch({
                type: WEBSOCKET_SEND,
                payload: {
                    type: "JOIN_GAME",
                    gameId: action.payload
                }
            });
        case "START_GAME":
            return dispatch({
                type: WEBSOCKET_SEND,
                payload: {
                    type: "START_GAME",
                    gameId: gameId
                }
            })
        default:
            return next(action);
    }

};

export default createGame;
