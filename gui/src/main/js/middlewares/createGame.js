import {CREATE_GAME} from "../reducers/game";
import {WEBSOCKET_SEND, WEBSOCKET_OPEN} from "redux-middleware-websocket";
import {push} from "react-router-redux";
import {GAME_ID, joinGame} from "../actions/server";

const createGame = ({getState, dispatch}) => next => action => {
    const {
        game: {
            isPrivate,
            title,
            seats,
            gameType,
            gameMode,
            sets
        },
        gameSettings: {
            gameId,
            authToken
        },
        player: {
            playerId,
            name
        }
    } = getState();

    switch (action.type) {
        case "GAME_ID":
            next(action);
            return dispatch(push("games/" + action.payload.gameId));
        case "CREATE_GAME":
            return dispatch({
                type: WEBSOCKET_SEND,
                payload: {
                    type: "CREATE_GAME",
                    isPrivate,
                    title,
                    seats,
                    gameType,
                    gameMode,
                    sets : ["XLN", "XLN", "XLN"] //TODO: fixme!
                }
            });
        case "JOIN_GAME":
            return dispatch({
                type: WEBSOCKET_SEND,
                payload: {
                    type: "JOIN_GAME",
                    gameId: action.payload,
                    name
                }
            });
        case "LEAVE_GAME":
            return dispatch({
                type: WEBSOCKET_SEND,
                payload: {
                    type: "LEAVE_GAME",
                    gameId: action.payload,
                    playerId
                }
            });
        case "START_GAME":
            return dispatch({
                type: WEBSOCKET_SEND,
                payload: {
                    type: "START_GAME",
                    gameId,
                    authToken
                }
            })
        // case WEBSOCKET_OPEN:
        //     return dispatch({
        //         type: WEBSOCKET_SEND,
        //         payload: {
        //             type: "ID",
        //             id,
        //             name
        //         }
        //     })
        default:
            return next(action);
    }

};

export default createGame;
