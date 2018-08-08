import {CREATE_GAME} from "../reducers/game";
import {WEBSOCKET_SEND, WEBSOCKET_CLOSED} from "redux-middleware-websocket";
import {push} from "react-router-redux";
import {GAME_ID} from "../actions/server";

const createGame = ({getState, dispatch}) => next => action => {
    const {
        game,
        gameSettings: {
            authToken,
            addBots,
            useTimer,
            shufflePlayers,
            timerLength
        },
        player: {
            playerId,
            name
        },
        gameState: {
            gameId
        }
    } = getState();

    switch (action.type) {
        case "GAME_ID":
            next(action);
            return dispatch(push("games/" + action.payload.gameId));

        case "CREATE_GAME":
            next(action);
            return dispatch({
                type: WEBSOCKET_SEND,
                payload: {
                    type: "CREATE_GAME",
                    ...game,
                    sets: game.sets[game.gameType]
                }
            });
        case "PICK":
            next(action);
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
            next(action);
            return dispatch({
                type: WEBSOCKET_SEND,
                payload: {
                    type: "JOIN_GAME",
                    gameId: action.payload,
                    playerId,
                    name
                }
            });
        case "LEAVE_GAME":
            next(action);
            return dispatch({
                type: WEBSOCKET_SEND,
                payload: {
                    type: "LEAVE_GAME",
                    gameId: action.payload,
                    playerId
                }
            });
        case "START_GAME":
            next(action);
            return dispatch({
                type: WEBSOCKET_SEND,
                payload: {
                    type: "START_GAME",
                    gameId,
                    authToken,
                    addBots,
                    useTimer,
                    shufflePlayers,
                    timerLength
                }
            });
        case "AUTOPICK":
            next(action);
            return dispatch({
                type: WEBSOCKET_SEND,
                payload: {
                    type: "AUTOPICK",
                    gameId,
                    playerId,
                    autoPickId: action.payload
                }
            });

        case "KICK":
            next(action);
            return dispatch({
                type: WEBSOCKET_SEND,
                payload: {
                    type: "KICK",
                    gameId,
                    authToken,
                    kick: action.payload
                }
            });

        case "SWAP":
            next(action);
            return dispatch({
                type: WEBSOCKET_SEND,
                payload: {
                    type: "SWAP",
                    gameId,
                    authToken,
                    swap: action.payload
                }
            });

        case "ERROR":
            next(action);
            return dispatch(push("/"));

        case WEBSOCKET_CLOSED:
            next(action);
            return dispatch(push("/"));

        default:
            return next(action);
    }

};

export default createGame;
