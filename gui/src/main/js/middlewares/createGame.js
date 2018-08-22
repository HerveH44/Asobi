import {WEBSOCKET_SEND, WEBSOCKET_CLOSED} from "redux-middleware-websocket";
import {push} from "react-router-redux";
import {
    onKick,
    onSwap,
    onClickLog,
    AUTH_TOKEN,
    hashDeck,
    autoPick,
    joinGame,
    leaveGame,
    onSetPlayerName,
    pick,
    startGame,
    ERROR,
    createGame
} from "../actions/server";
import {MAIN, SIDE} from "../reducers/playerState";


export default ({getState, dispatch}) => next => action => {
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
        case onClickLog.toString():
            next(action);
            return dispatch({
                type: WEBSOCKET_SEND,
                payload: {
                    type: "DRAFT_LOG",
                    gameId,
                    playerId,
                }
            });
        case hashDeck.toString():
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


        case onSetPlayerName.toString():
            next(action);
            return dispatch({
                type: WEBSOCKET_SEND,
                payload: {
                    type: "SET_NAME",
                    gameId,
                    playerId,
                    name: action.payload.target.value
                }
            });

        case createGame.toString():
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
        case pick.toString():
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
        case joinGame.toString():
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
        case leaveGame.toString():
            next(action);
            return dispatch({
                type: WEBSOCKET_SEND,
                payload: {
                    type: "LEAVE_GAME",
                    gameId: action.payload,
                    playerId
                }
            });
        case startGame.toString():
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
        case autoPick.toString():
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

        case onKick.toString():
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

        case onSwap.toString():
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

        /*
            TODO: Theses cases should be handled in a separate middleware ?
            They are linked to the app receiving infos from the server
         */

        case AUTH_TOKEN:
            next(action);
            return dispatch(push("games/" + action.payload.gameId));

        case ERROR:
            next(action);
            return dispatch(push("/"));

        case WEBSOCKET_CLOSED:
            next(action);
            return dispatch(push("/"));

        default:
            return next(action);
    }

};
