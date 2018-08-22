import {WEBSOCKET_SEND} from "redux-middleware-websocket";
import {errorMessage, pressKeyDown} from "../actions/game";

const Chat = ({getState, dispatch}) => next => action => {

    const {
        player: {
            playerId
        },
        gameState: {
            gameId
        },
    } = getState();


    if (action.type === pressKeyDown.toString()) {
        next(action);
        if (action.payload.key === "Enter") {

            const text = action.payload.target.value.trim();
            action.payload.target.value = "";

            switch (true) {
                case !text:
                    return;
                case text[0] === "/":
                    return handleCommand({dispatch, gameId, playerId, text});
                default:
                    return sendMessage({dispatch, gameId, playerId, text});
            }
        }
    }
    return next(action);
};

const handleCommand = ({dispatch, gameId, playerId, text}) => {
    const [, command, arg] = text.match(/\/(\w*)\s*(.*)/);

    switch (command) {
        case "name":
        case "nick":
            const name = arg.trim().slice(0, 15);

            if (!name) {
                return dispatch({
                    type: "ERROR_MESSAGE",
                    payload: "enter a name"
                });
            }

            dispatch({
                type: "CHANGE_NAME",
                payload: {
                    target: {
                        value: name
                    }
                }
            });

            return dispatch({
                type: WEBSOCKET_SEND,
                payload: {
                    type: "SET_NAME",
                    gameId,
                    playerId,
                    name
                }
            });
        default:
            return dispatch({
                ...errorMessage(),
                payload: `unsupported command: ${command}`
            });
    }

};

const sendMessage = ({dispatch, gameId, playerId, text}) => (
    dispatch({
        type: WEBSOCKET_SEND,
        payload: {
            type: "GAME_MESSAGE",
            gameId,
            playerId,
            message: text
        }
    })
);


export default Chat;
