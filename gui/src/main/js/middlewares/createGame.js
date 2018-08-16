import {CREATE_GAME} from "../reducers/game";
import {WEBSOCKET_SEND, WEBSOCKET_CLOSED} from "redux-middleware-websocket";
import {push} from "react-router-redux";
import {AUTH_TOKEN} from "../actions/server";
import {MAIN, SIDE} from "../reducers/playerState";

const createGame = ({getState, dispatch}) => next => action => {
    const {
        game: {
            title,
            seats,
            isPrivate,
            gameType,
            gameMode,
            sets,
            modernOnly,
            totalChaos,
            packsNumber,
            cubeList
        },
        startPanel: {
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
            authToken,
            gameId
        },
        playerState: {
            [MAIN]: main,
            [SIDE]: side
        }
    } = getState();

    switch (action.type) {
        case "HASH":
            next(action);
            return dispatch({
                type: WEBSOCKET_SEND,
                payload: {
                    type: "HASH",
                    gameId,
                    playerId,
                    main: main.map(card => card.name),
                    side: side.map(card => card.name)
                }
            });

        case "AUTH_TOKEN":
            next(action);
            return dispatch(push("games/" + action.payload.gameId));

        case "CREATE_GAME":
            next(action);
            return dispatch({
                type: WEBSOCKET_SEND,
                payload: {
                    type: "CREATE_GAME",
                    title,
                    gameType,
                    gameMode,
                    seats,
                    isPrivate,
                    sets: sets[gameType],
                    modernOnly,
                    totalChaos,
                    packsNumber,
                    cubeList
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
