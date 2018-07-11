import { CREATE_GAME } from "../reducers/game";
import { WEBSOCKET_SEND } from "redux-middleware-websocket";
import { push } from "react-router-redux";
import { GAME_ID, joinGame } from "../actions/server";

const createGame = ({ getState, dispatch }) => next => action => {
    const {
        game,
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
                    ...game,
                    sets: game.sets[game.gameType]
                }
            });
        case "PICK":
            console.log("youhou! Pick :)");
            console.log(action);
            return dispatch({
                type: WEBSOCKET_SEND,
                payload: {
                    type: "PICK",
                    gameId,
                    playerId,
                    cardIndex: action.payload
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
        default:
            return next(action);
    }

};

export default createGame;
