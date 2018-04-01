import {CREATE_GAME} from "../reducers/game";
import {WEBSOCKET_SEND} from "redux-middleware-websocket";
import {push} from "react-router-redux";

const createGame = ({getState, dispatch}) => next => action => {
    if (action.type == "GAME_ID") {
        return dispatch(push("games/" + action.payload));
    }

    if (action.type != CREATE_GAME) {
        return next(action);
    }

    const {
        game: {
            isPrivate,
            title,
            seats,
            gameType,
            gameMode
        }
    } = getState();
    dispatch({
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
};

export default createGame;
